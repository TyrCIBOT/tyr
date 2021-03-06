package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.xstefank.api.ContinuousIntegrationAPI;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.UserList;
import org.xstefank.model.Utils;

public class AddUserCommand implements Command {

    private String commandRegex;
    private List<ContinuousIntegrationAPI> apis;

    public AddUserCommand(String commandRegex, List<ContinuousIntegrationAPI> apis) {
        this.commandRegex = commandRegex;
        this.apis = apis;
    }

    @Override
    public void process(JsonNode payload, UserList adminList, UserList userList) {
        String authorUsername = payload.get(Utils.ISSUE).get(Utils.USER).get(Utils.LOGIN).asText();

        if (isCommandAuthorAdmin(payload, adminList) &&
                payload.get(Utils.ISSUE).has(Utils.PULL_REQUEST) &&
                !userList.hasUsername(authorUsername)) {

            userList.addUser(authorUsername);
            JsonNode prJson = GitHubAPI.getJsonWithPullRequest(payload);

            for (ContinuousIntegrationAPI api : apis) {
                api.triggerBuild(prJson);
            }
        }
    }

    @Override
    public String getCommandRegex() {
        return commandRegex;
    }
}