package io.hasura;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaison on 31/05/17.
 */

public class GsonTest {

    class User {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    public void test() throws IOException {

        List<User> userList = new ArrayList<>();
        userList.add(new User("user1",22));
        userList.add(new User("user2",23));

        String userListString = new Gson().toJson(userList);

        System.out.print(userListString);

        List<User> users = new Gson().fromJson(userListString, new TypeToken<List<User>>(){}.getType());


    }
}
