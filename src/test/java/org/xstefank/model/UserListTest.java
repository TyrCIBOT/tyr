package org.xstefank.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xstefank.utils.TestUtils;

public class UserListTest {

    private static final String USERNAME = "testUser";

    private UserList testUserList;

    @Before
    public void before() {
        testUserList = new UserList(TestUtils.RESOURCES_PATH, "test.list");
        testUserList.addUser(USERNAME);
    }

    @Test
    public void testAddUserToList() {
        Assert.assertTrue(TestUtils.isUserListed(testUserList, USERNAME));
    }

    @Test
    public void testRemoveUserFromList() {
        testUserList.removeUser(USERNAME);
        Assert.assertFalse(TestUtils.isUserListed(testUserList, USERNAME));
    }

    @After
    public void after() {
        TestUtils.deleteUserListFile(testUserList);
    }
}