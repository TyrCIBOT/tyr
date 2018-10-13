package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xstefank.utils.TestUtils;
import org.xstefank.api.ContinuousIntegrationAPI;
import org.xstefank.api.GitHubAPI;

import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GitHubAPI.class)
public class AddUserCommandTest {

    private AddUserCommand addUserCommand;

    @Before
    public void before() {
        String regex = TestUtils.FORMAT_CONFIG.getFormat().getCommands().get("AddUserCommand");
        List<ContinuousIntegrationAPI> apis = new ArrayList<>();

        addUserCommand = new AddUserCommand(regex, apis);

        PowerMockito.stub(method(GitHubAPI.class, "getJsonWithPullRequest", JsonNode.class))
                .toReturn(TestUtils.TEST_PAYLOAD);

    }

    @Test
    public void testAddUserToList() {
       addUserCommand.process(TestUtils.ISSUE_PAYLOAD,
                TestUtils.ADMIN_LIST, TestUtils.USER_LIST);

        Assert.assertTrue(TestUtils.isUserListed(TestUtils.USER_LIST, "prUser"));
    }

    @Test(expected = NullPointerException.class)
    public void testProcessMethodNullParams() {
        addUserCommand.process(null, null, null);
    }

    @After
    public void after() {
        TestUtils.deleteUserListFile(TestUtils.USER_LIST);
    }
}
