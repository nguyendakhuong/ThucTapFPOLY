package khuonndph14998.fpoly.thuctapfpoly.model;

import java.util.ArrayList;

public class UserDirectory {
    private static ArrayList<User> userList;

    public static ArrayList<User> getUserList() {
        return userList;
    }

    public static void setUserList(ArrayList<User> userList) {
        UserDirectory.userList = userList;
    }
}
