package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.model.UserList;
import org.xstefank.model.Utils;

public class RemoveUserCommand implements Command {

    private String commandRegex;

    public RemoveUserCommand(String commandRegex) {
        this.commandRegex = commandRegex;
    }

    @Override
    public void process(JsonNode payload, UserList adminList, UserList userList) {
        String authorUsername = payload.get(Utils.ISSUE).get(Utils.USER).get(Utils.LOGIN).asText();
        if (isCommandAuthorAdmin(payload, adminList)) {
            userList.removeUser(authorUsername);
        }
    }

    @Override
    public String getCommandRegex() {
        return commandRegex;
    }
}
