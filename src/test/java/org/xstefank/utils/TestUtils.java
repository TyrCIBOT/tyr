package org.xstefank.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import org.xstefank.model.ConfigTest;
import org.xstefank.model.UserList;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;

import java.io.File;
import java.io.IOException;

public class TestUtils {

    public static final String YAML_DIR = "yaml";
    public static final String JSON_DIR = "json";
    public static final String RESOURCES_PATH = "src/test/resources";

    public static final UserList ADMIN_LIST = new UserList(RESOURCES_PATH, Utils.ADMINLIST_FILE_NAME);
    public static final UserList USER_LIST = new UserList(RESOURCES_PATH, Utils.USERLIST_FILE_NAME);

    public static final JsonNode TEST_PAYLOAD = loadJson(JSON_DIR + "/testPayload.json");
    public static final JsonNode BAD_TEST_PAYLOAD = loadJson(JSON_DIR + "/badTestPayload.json");
    public static final JsonNode ISSUE_PAYLOAD = loadJson(JSON_DIR + "/issuePayload.json");
    public static final JsonNode EMPTY_PAYLOAD = createEmptyJsonPayload();
    public static final FormatConfig FORMAT_CONFIG = loadFormatFromYamlFile(YAML_DIR + "/testTemplate.yaml");
    public static final String TEST_CONFIG_PATH = ConfigTest.class.getClassLoader().getResource("testConfig.properties").getPath();

    public static FormatConfig loadFormatFromYamlFile(String fileName) {
        try {
            File file = new File(TestUtils.class.getClassLoader().getResource(fileName).getFile());
            return new ObjectMapper(new YAMLFactory()).readValue(file, FormatConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load file " + fileName);
        }
    }

    public static boolean isUserListed(UserList list, String username) {
        try (FileReader fileReader = new FileReader(new File(list.getFilePath()));
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.contains(username) && list.hasUsername(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read file located in " + list.getFilePath(), e);
        }
        return false;
    }

    public static void deleteUserListFile(UserList list) {
        File file = new File(list.getFilePath());
        if (file.exists()) {
            file.delete();
        }
    }

    private static JsonNode loadJson(String fileName) {
        try {
            File file = new File(TestUtils.class.getClassLoader().getResource(fileName).getFile());
            JsonNode jNode = new ObjectMapper().readTree(file);
            if (jNode.has(Utils.PULL_REQUEST)) {
                return trimJsonPullRequestElement(jNode);
            } else {
                return jNode;
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load file "+ fileName);
        }
    }

    private static JsonNode trimJsonPullRequestElement(JsonNode payload) {
        return payload.get(Utils.PULL_REQUEST);
    }

    private static JsonNode createEmptyJsonPayload() {
        try {
            return new ObjectMapper().readTree("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}