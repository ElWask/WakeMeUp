package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Application;

/**
 * Created by SCHMID_CHARLESA-ESIG on 08.02.2017.
 */
// class prototype
public class GlobalVar extends Application {

    private static String varNameUser = "Lustucru";

    public static String getVarNameUser() {
        return varNameUser;
    }

    public static void setVarNameUser(String someVariable) {
        GlobalVar.varNameUser = someVariable;
    }

}
