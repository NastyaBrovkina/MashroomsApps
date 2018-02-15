package com.study.mashroomsapps.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by nvv on 16.01.2018.
 */

public class MyAlertDialog {

    AlertDialog.Builder dialog;

    public MyAlertDialog(Context context) {
        dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
    }

    public void setView(View view){
        dialog.setView(view);
    }

    public void setMessage(String message){
        dialog.setMessage(message);
    }

    public void setPositiveButton(String btnText, DialogInterface.OnClickListener onClickListener){
        dialog.setPositiveButton(btnText, onClickListener);
    }

    public void setNegativeButton(String btnText, DialogInterface.OnClickListener onClickListener){
        dialog.setNegativeButton(btnText, onClickListener);
    }

    public void show(){
        dialog.show();
    }


}
