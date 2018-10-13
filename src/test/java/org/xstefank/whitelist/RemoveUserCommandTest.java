package org.xstefank.whitelist;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xstefank.utils.TestUtils;

public class RemoveUserCommandTest {

    private RemoveUserCommand removeUserCommand;

    @Before
    public void before() {
        TestUtils.USER_LIST.addUser("prUser");
        String regex = TestUtils.FORMAT_CONFIG.getFormat().getCommands().get("RemoveUserCommand");
        removeUserCommand = new RemoveUserCommand(regex);
    }

    @Test
    public void testRemoveUserCommand() {
        removeUserCommand.process(TestUtils.ISSUE_PAYLOAD,
                TestUtils.ADMIN_LIST, TestUtils.USER_LIST);

        Assert.assertFalse(TestUtils.isUserListed(TestUtils.USER_LIST, "prUser"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullParamsInProcessMethod() {
        removeUserCommand.process(null, null, null);
    }

    @After
    public void after() {
        TestUtils.deleteUserListFile(TestUtils.USER_LIST);
    }
}
