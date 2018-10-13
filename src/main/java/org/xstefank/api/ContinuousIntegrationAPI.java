package org.xstefank.api;

import com.fasterxml.jackson.databind.JsonNode;

public interface ContinuousIntegrationAPI {

    void triggerBuild(JsonNode payload);
}