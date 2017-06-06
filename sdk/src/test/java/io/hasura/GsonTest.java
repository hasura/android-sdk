package io.hasura;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.temp.HasuraQuery;

/**
 * Created by jaison on 31/05/17.
 */

public class GsonTest {

    @Test
    public void test() throws IOException {
        HasuraQuery q = HasuraQuery.SELECT("tableName")
                .columns("column1", "column2")
                .relationship("relationship1").columns("r1c1", "r1c2", "r1c3")
                .relationship("relationship2").columns("r2c1", "r2c2", "r2c3")
                .orderBy("col").nullsFirst().ascending()
                .limit(1)
                .offset(0)
                .where("user1").equalTo("1")
                .where("user2").equalTo("2")
                .beginGroup()
                    .where("checkin").equalTo("accepted")
                    .or()
                    .where("checkin").equalTo("null")
                .endGroup()
                .build();

//                .where("column1").equalTo("someString")
//                .and()
//                .where("column2").equalTo("someotherstring")
//                .or()
//                .where("column3").equalTo("blah")
//                .beginGroup()
//                    .where("column4").equalTo("blach")
//                    .or()
//                    .where("column5").equalTo("sadsad")
//                .endGroup()
//                .build();

        q.printRequestAsString();


    }
}
