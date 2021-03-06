package org.xstefank.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.net.URL;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.check.SkipCheck;
import org.xstefank.check.TemplateChecker;
import org.xstefank.whitelist.WhitelistProcessing;
import org.xstefank.verification.InvalidConfigurationException;
import org.xstefank.verification.VerificationHandler;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

import static org.xstefank.check.TemplateChecker.TEMPLATE_FORMAT_FILE;

@Path("/")
public class WebHookEndpoint {

    private static final Logger log = Logger.getLogger(WebHookEndpoint.class);
    private static final FormatConfig config = readConfig();
    private static WhitelistProcessing whitelistProcessing = null;
    private TemplateChecker templateChecker = new TemplateChecker(config);

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processPullRequest(JsonNode payload) {
        if (Utils.IS_WHITELISTING_ENABLED) {
            processWithWhitelisting(payload);
        } else {
            processWithoutWhitelisting(payload);
        }
    }

    private static FormatConfig readConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        FormatConfig formatConfig;
        try {
            URL formatFileUrl = Utils.getFormatUrl();
            if (formatFileUrl != null)
                formatConfig = mapper.readValue(formatFileUrl.openStream(), FormatConfig.class);
            else {
                String configFileName = System.getProperty(TEMPLATE_FORMAT_FILE);
                if (configFileName == null) {
                    configFileName = System.getProperty(Utils.JBOSS_CONFIG_DIR) + "/format.yaml";
                }
                log.info(configFileName);
                File configFile = new File(configFileName);
                formatConfig = mapper.readValue(configFile, FormatConfig.class);
            }
            VerificationHandler.verifyConfiguration(formatConfig);
            return formatConfig;
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }
    }

    private void processWithWhitelisting(JsonNode payload) {
        if (whitelistProcessing == null) {
            whitelistProcessing = new WhitelistProcessing(config);
        }

        if (payload.has(Utils.PULL_REQUEST)) {
            triggerTyr(payload);
        } else if (payload.has(Utils.ISSUE)) {
            whitelistProcessing.process(payload);
        }
    }

    private void processWithoutWhitelisting(JsonNode payload) {
        if (payload.has(Utils.PULL_REQUEST)) {
            triggerTyr(payload);
        }
    }

    private String runChecksOnPullRequestPayload(JsonNode payload) {
        if (!SkipCheck.shouldSkip(payload, config)) {
            return templateChecker.checkPR(payload);
        }
        return null;
    }

    private void sendResponse(String errorMessage, JsonNode payload) {
        if (errorMessage != null) {
            log.info("updating status");

            GitHubAPI.updateCommitStatus(config.getRepository(),
                    payload.get(Utils.HEAD).get(Utils.SHA).asText(),
                    errorMessage.isEmpty() ? CommitStatus.SUCCESS : CommitStatus.ERROR,
                    config.getStatusUrl(),
                    errorMessage.isEmpty() ? "valid" : errorMessage, "PR format check");
        }
    }

    private void triggerTyr(JsonNode payload) {
        payload = Utils.removePullRequestElement(payload);
        String errorMessage = runChecksOnPullRequestPayload(payload);
        sendResponse(errorMessage, payload);
    }
}