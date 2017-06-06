package io.hasura.sdk.temp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedOutputStream;
import java.sql.BatchUpdateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jaison on 23/05/17.
 */

public class HasuraQuery {

    private HasuraQuery() {

    }

    Builder builder;

    private HasuraQuery(Builder builder) {
        this.builder = builder;
    }

    public HasuraQuery executeAsync() {
        return this;
    }

    public static Builder SELECT(String tableName) {
        return new Builder("select", tableName);
    }

    public void printRequestAsString() {
        System.out.println(builder.getAsJson().toString());
    }

    public static class Builder {

        public class Relationship {

            String relationshipName;

            Relationship(String relationshipName) {
                this.relationshipName = relationshipName;
            }

            public Builder columns(String... columns) {
                relationships.put(relationshipName, columns);
                return Builder.this;
            }
        }

        public class Where {

            public class Condition {

                String columnName;

                public Condition(String columnName) {
                    this.columnName = columnName;
                }

                public Where equalTo(String value) {
                    if (groupWhere == null) {
                        JsonObject expression = new JsonObject();
                        expression.addProperty("$eq", value);
                        newCondition.add(columnName, expression);
                        if (currentCondition == null) {
                            currentCondition = newCondition;
                        } else {
                            JsonObject temp = new JsonObject();
                            JsonArray array = new JsonArray();
                            array.add(currentCondition);
                            array.add(newCondition);
                            temp.add(nextOperator, array);
                            currentCondition = temp;
                            nextOperator = "$and";
                        }
                    } else {
                        JsonObject expression = new JsonObject();
                        expression.addProperty("$eq", value);
                        groupWhere.newCondition.add(columnName, expression);
                        if (groupWhere.currentCondition == null) {
                            groupWhere.currentCondition = groupWhere.newCondition;
                        } else {
                            JsonObject temp = new JsonObject();
                            JsonArray array = new JsonArray();
                            array.add(groupWhere.currentCondition);
                            array.add(groupWhere.newCondition);
                            temp.add(groupWhere.nextOperator, array);
                            groupWhere.currentCondition = temp;
                            groupWhere.nextOperator = "$and";
                        }
                    }

                    return Where.this;
                }


            }

            JsonObject currentCondition = null;
            JsonObject newCondition;
            Where groupWhere;
            String nextOperator = "$and";

            public Condition where(String columnName) {
                if (groupWhere == null)
                    newCondition = new JsonObject();
                else groupWhere.newCondition = new JsonObject();
                return new Condition(columnName);
            }

            public Where beginGroup() {
                groupWhere = new Where();
                return this;
            }

            public Where endGroup() {
                if (currentCondition == null) {
                    currentCondition = groupWhere.currentCondition;
                } else {
                    JsonObject temp = new JsonObject();
                    JsonArray array = new JsonArray();
                    array.add(currentCondition);
                    array.add(groupWhere.currentCondition);
                    temp.add("$and", array);
                    currentCondition = temp;
                }
                groupWhere = null;
                return this;
            }

            public Where or() {
                if (groupWhere == null)
                    this.nextOperator = "$or";
                else groupWhere.nextOperator = "$or";
                return this;
            }

            public Where and() {
                if (groupWhere == null)
                    this.nextOperator = "$and";
                else groupWhere.nextOperator = "$and";
                return this;
            }

            public HasuraQuery build() {
                where = currentCondition;
                return Builder.this.build();
            }
        }

        public class OrderBy {
            String columnName;
            String order; //asc or desc
            String nullsOrder; //first or last

            public OrderBy(String columnName) {
                this.columnName = columnName;
            }

            public OrderBy nullsFirst() {
                this.nullsOrder = "first";
                return this;
            }

            public OrderBy nullsLast() {
                this.nullsOrder = "last";
                return this;
            }

            public Builder ascending() {
                this.order = "asc";
                return Builder.this;
            }

            public Builder descending() {
                this.order = "desc";
                return Builder.this;
            }

            public JsonObject getAsJson() {
                JsonObject orderJson = new JsonObject();
                orderJson.addProperty("order", order);
                orderJson.addProperty("column", columnName);
                orderJson.addProperty("nulls", nullsOrder);
                return orderJson;
            }
        }

        private String queryType;
        private String table;
        private String[] columns;
        private String[] returning;
        private HashMap<String, String[]> relationships = new HashMap<>();
        private JsonObject where;
        private JsonObject orderBy;
        private Integer limit;
        private Integer offset;

        public Builder(String queryType, String tableName) {
            this.queryType = queryType;
            this.table = tableName;
        }

        public Builder columns(String... columns) {
            this.columns = columns;
            return this;
        }

        public Builder returningValues(String... column) {
            this.returning = column;
            return this;
        }

        public Relationship relationship(String relName) {
            return new Relationship(relName);
        }

        private Builder setOrder(OrderBy order) {
            orderBy = order.getAsJson();
            return this;
        }

        public OrderBy orderBy(String columnName) {
            return new OrderBy(columnName);
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Where.Condition where(String columnName) {
            return new Where().where(columnName);
        }

        public HasuraQuery build() {
            return new HasuraQuery(this);
        }

        public JsonObject getAsJson() {
            JsonObject args = new JsonObject();
            args.addProperty("table", table);
            args.add("columns", getColumnAsArray());
            if (where != null) {
                args.add("where", where);
            }
            if (limit != null) {
                args.addProperty("limit", limit);
            }
            if (offset != null) {
                args.addProperty("offset", offset);
            }
            if (orderBy != null) {
                args.add("order_by", orderBy);
            }

            JsonObject query = new JsonObject();
            query.addProperty("type", queryType);
            query.add("args", args);

            return query;
        }

        private JsonArray getColumnAsArray() {
            JsonArray columnArray = new JsonArray();
            for (String column : columns) {
                columnArray.add(column);
            }

            for (Map.Entry<String, String[]> relationship : relationships.entrySet()) {
                JsonArray relationshipColumns = new JsonArray();
                for (String column : relationship.getValue()) {
                    relationshipColumns.add(column);
                }

                JsonObject relationshipJson = new JsonObject();
                relationshipJson.addProperty("name", relationship.getKey());
                relationshipJson.add("columns", relationshipColumns);

                columnArray.add(relationshipJson);
            }

            return columnArray;
        }

    }

}
