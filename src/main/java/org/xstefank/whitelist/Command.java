package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.model.UserList;
import org.xstefank.model.Utils;

public interface Command {

    void process(JsonNode payload, UserList adminList,
                 UserList userList);

    String getCommandRegex();

    default boolean isCommandAuthorAdmin(JsonNode payload, UserList adminList) {
        String username = payload.get(Utils.COMMENT).get(Utils.USER).get(Utils.LOGIN).asText();
        return adminList.hasUsername(username);
    }

}