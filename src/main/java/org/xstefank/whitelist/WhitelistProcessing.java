package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xstefank.api.ContinuousIntegrationAPI;
import org.xstefank.api.TeamCityAPI;
import org.xstefank.model.UserList;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;

public class WhitelistProcessing {

    private final UserList userList;
    private final UserList adminList;

    private final List<Command> commands;

    public WhitelistProcessing(FormatConfig config) {
        String dirName = System.getProperty(Utils.JBOSS_CONFIG_DIR);
        userList = new UserList(dirName, Utils.USERLIST_FILE_NAME);
        adminList = new UserList(dirName, Utils.ADMINLIST_FILE_NAME);
        this.commands = getCommands(config);
    }

    public void process(JsonNode payload) {
        if (payload.get(Utils.ISSUE).has(Utils.PULL_REQUEST) &&
                payload.get(Utils.ACTION).asText().matches("created") &&
                    !commands.isEmpty()) {
            String message = payload.get(Utils.COMMENT).get(Utils.BODY).asText();
            for (Command command : commands) {
                if (message.matches(command.getCommandRegex())) {
                    command.process(payload, adminList, userList);
                }
            }
        }
    }

    private List<Command> getCommands(FormatConfig config) {
        List<Command> commands = new ArrayList<>();
        List<ContinuousIntegrationAPI> apis = loadApis();

        if (config.getFormat().getCommands() != null ||
                !config.getFormat().getCommands().isEmpty()) {

            Map<String, String> regexMap = config.getFormat().getCommands();

            String addUserCommandRegex = regexMap.get("AddUserCommand");
            if (addUserCommandRegex != null) {
                commands.add(new AddUserCommand(addUserCommandRegex, apis));
            }

            String retestCommandRegex = regexMap.get("RetestCommand");
            if (retestCommandRegex != null) {
                commands.add(new RetestCommand(retestCommandRegex, apis));
            }

            String removeUserCommandRegex = regexMap.get("RemoveUserCommand");
            if (removeUserCommandRegex != null) {
                commands.add(new RemoveUserCommand(removeUserCommandRegex));
            }
        }
        return commands;
    }

    private static List<ContinuousIntegrationAPI> loadApis() {
        List<ContinuousIntegrationAPI> tempApis = new ArrayList<>();

        tempApis.add(new TeamCityAPI());

        return tempApis;
    }
}
