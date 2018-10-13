package org.xstefank.whitelist;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.xstefank.api.ContinuousIntegrationAPI;
import org.xstefank.utils.TestUtils;

public class RetestCommandTest {

    private RetestCommand retestCommand;

    @Before
    public void before() {
        List<ContinuousIntegrationAPI> apis = new ArrayList<>();
        String regex = TestUtils.FORMAT_CONFIG.getFormat().getCommands().get("RetestCommand");
        retestCommand = new RetestCommand(regex, apis);
    }

    @Test(expected = NullPointerException.class)
    public void testProcessMethodNullParams() {
        retestCommand.process(null, null, null);
    }
}
