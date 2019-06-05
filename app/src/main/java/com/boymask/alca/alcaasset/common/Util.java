package com.boymask.alca.alcaasset.common;

import android.support.design.widget.Snackbar;
import android.view.View;

public class Util {
    public static void showMessage(View view, int id){
        Snackbar mySnackbar = Snackbar.make(view,
                id, Snackbar.LENGTH_LONG);
       // mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
        mySnackbar.show();
    }
}
