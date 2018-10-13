package org.xstefank.api;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.DatatypeConverter;
import org.jboss.logging.Logger;
import org.xstefank.model.Utils;
import org.xstefank.model.json.triggerbuild.BuildJson;

public class TeamCityAPI implements ContinuousIntegrationAPI {

    private static final Logger log = Logger.getLogger(TeamCityAPI.class);

    private final String baseUrl;
    private final String encryptedCredentials;
    private final String buildId;

    public TeamCityAPI() {
        this.baseUrl = getBaseUrl(Utils.getTyrProperty(Utils.TEAMCITY_HOST_PROPERTY),
                Integer.parseInt(Utils.getTyrProperty(Utils.TEAMCITY_PORT_PROPERTY)));

        this.encryptedCredentials = encryptCredentials(Utils.getTyrProperty(Utils.TEAMCITY_USER_PROPERTY),
                Utils.getTyrProperty(Utils.TEAMCITY_PASS_PROPERTY));

        this.buildId = Utils.getTyrProperty(Utils.TEAMCITY_BUILD_CONFIG);
    }

    TeamCityAPI(String host, int port, String username, String password, String buildId) {
        this.baseUrl = getBaseUrl(host, port);
        this.encryptedCredentials = encryptCredentials(username, password);
        this.buildId = buildId;
    }

    public void triggerBuild(JsonNode payload) {
        String pull = payload.get("number").asText();
        String sha = payload.get("head").get("sha").asText();

        Client resteasyClient = ClientBuilder.newClient();
        URI statusUri = UriBuilder
                .fromUri(baseUrl)
                .path("/app/rest/buildQueue")
                .build();

        WebTarget target = resteasyClient.target(statusUri);

        Entity<BuildJson> json = Entity.json(new BuildJson("pull/" + pull, buildId,
                sha, pull, "master"));

        Response response = target.request()
                .header(HttpHeaders.ACCEPT_ENCODING, "UTF-8")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encryptedCredentials)
                .post(json);

        log.info("Teamcity status update: " + response.getStatus());
        response.close();
    }

    private String encryptCredentials(String username, String password) {
        String authStr = username + ":" + password;
        try {
            return DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getBaseUrl(String host, int port) {
        return port == 443 ? "https://" + host + "/httpAuth" : "http://" + host + ":" + port + "/httpAuth";
    }
}
