package com.boymask.alca.alcaasset.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

public class Util {
    public static void showMessage(View view, int id){
        Snackbar mySnackbar = Snackbar.make(view,
                id, Snackbar.LENGTH_LONG);
       // mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
        mySnackbar.show();
    }
    public static String getServer(Context ctx){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String server = (String) prefs.getString("hostname", "");
        return server;
    }
    public static void showAlert(Context ctx, String msg) {        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Attenzione");
        builder.setMessage(msg);        // add a button
        builder.setPositiveButton("OK", null);        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void showMessage(Context ctx, String msg) {        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("");
        builder.setMessage(msg);        // add a button
        builder.setPositiveButton("OK", null);        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
