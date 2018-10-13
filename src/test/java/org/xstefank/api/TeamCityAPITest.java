package org.xstefank.api;

import org.junit.Test;

public class TeamCityAPITest {

    @Test(expected = NullPointerException.class)
    public void testTriggerBuildNullParams() {
        TeamCityAPI teamCityAPI = new TeamCityAPI(null, 0, null, null, null);
        teamCityAPI.triggerBuild(null);
    }
}
