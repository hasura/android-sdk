package io.hasura.sdk;

import android.content.Context;
import android.content.SharedPreferences;

class SharedPrefManager {

    private static SharedPrefManager instance;
    private Context context;

    static SharedPrefManager getInstance() {
        if (instance == null) {
            instance = new SharedPrefManager();
        }

        return instance;
    }

    private SharedPrefManager() {}

    void initialize(Context context) {
        this.context = context;
    }


    SharedPreferences getPref(String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
}