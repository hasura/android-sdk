package io.hasura.sdk.temp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_USER = "user";
    public static final String ID = "_id";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String MOBILE = "mobile";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ENABLE_MOBILE_OTP_LOGIN = "enable_mobile_otp_login";

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_USER + "( "
                    + ID + " integer primary key, "
                    + EMAIL + " text, "
                    + USERNAME + " text, "
                    + MOBILE + " text, "
                    + AUTH_TOKEN + " text, "
                    + ACCESS_TOKEN + " text, "
                    + ENABLE_MOBILE_OTP_LOGIN + " integer"
                    + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

}