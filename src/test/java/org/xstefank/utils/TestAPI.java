package org.xstefank.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.api.ContinuousIntegrationAPI;

public class TestAPI implements ContinuousIntegrationAPI {

    @Override
    public void triggerBuild(JsonNode payload) {
        return;
    }
}
