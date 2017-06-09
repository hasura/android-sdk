package io.hasura;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import io.hasura.sdk.core.HasuraTokenInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 31/05/17.
 */

public class RandomTests {

    HashMap<HasuraTokenInterceptor, OkHttpClient> clientHashMap = new HashMap<>();

    @Test
    public void test() throws IOException {
        HasuraTokenInterceptor hasuraTokenInterceptor = new HasuraTokenInterceptor();
        hasuraTokenInterceptor.setAuthToken("asdasdasd");
        hasuraTokenInterceptor.setRole("user");

        clientHashMap.put(hasuraTokenInterceptor, new OkHttpClient.Builder()
                .addInterceptor(hasuraTokenInterceptor).build());

        HasuraTokenInterceptor newTokenIntereceptor = new HasuraTokenInterceptor();
        newTokenIntereceptor.setAuthToken("asdasdasd");
        newTokenIntereceptor.setRole("user");



        OkHttpClient client = clientHashMap.get(newTokenIntereceptor);

        if (client != null) {
            System.out.print("Client not null");
        } else System.out.print("Client null");
    }
}
