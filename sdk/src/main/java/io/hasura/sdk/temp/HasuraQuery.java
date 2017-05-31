package io.hasura.sdk.temp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Created by jaison on 23/05/17.
 */

public class HasuraQuery {

    enum QueryType {
        SELECT,
        INSERT,
        UPDATE,
        DELETE,
        BULK
    }

    enum BoolExp {
        EqualTo,
        NotEqualTo,
        IsNull,
        NotNull,
        GreaterThan,
        GreaterThanEqualTo
    }

    public HasuraQuery(Builder builder) {

    }

    static class Condition {

        JsonObject exp;
        String columnName;

        interface BoolExp {
            JsonObject getExpression();
        }

        static class EqualTo implements BoolExp {

            JsonObject expression;

            public EqualTo(JsonObject exp) {
                this.expression = exp;
            }

            public EqualTo(String columnName, Object val) {
                JsonObject exp = new JsonObject();
                exp.add("$eq", new Gson().toJsonTree(val));

                JsonObject expMain = new JsonObject();
                expMain.add(columnName, exp);

                this.expression = expMain;
            }

            @Override
            public JsonObject getExpression() {
                return this.expression;
            }

            public void and(BoolExp bexp) {
                JsonArray array = new JsonArray();
                JsonObject exp  = new JsonObject();
                array.add(bexp.getExpression());
                array.add(expression);
                exp.add("$and", array);

                this.expression = exp;
            }

        }

        public Condition(String columnName) {

        }

        public Condition equalTo(Object val) {
            exp = new JsonObject();
//            exp.add(columnName, new JsonPrimitive(val));
            return this;
        }

        public Condition and(Condition c) {
            JsonArray array = new JsonArray();
            return this;
        }

    }

    static class Builder {
        private QueryType queryType;
        private String table;
        private String[] columns;
        private String[] returning;
        private HashMap<String, String[]> relationships = new HashMap<>();


        public Builder queryType(QueryType type) {
            this.queryType = type;
            return this;
        }

        public Builder onTable(String tableName) {
            this.table = tableName;
            return this;
        }

        public Builder fromColumns(String... columns) {
            this.columns = columns;
            return this;
        }

        public Builder fromAllColumns() {
            return this;
        }

        public Builder fromColumn(String column) {
            this.columns = new String[]{column};
            return this;
        }

        public Builder returningValues(String... column) {
            this.returning = column;
            return this;
        }

        public Builder fromRelationship(String relName, String... columns) {
            this.relationships.put(relName, columns);
            return this;
        }

        public HasuraQuery build() {
            return new HasuraQuery(this);
        }

    }

}
