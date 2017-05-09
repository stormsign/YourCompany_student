package com.miuhouse.yourcompany.student.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.miuhouse.yourcompany.student.R;

/**
 * Created by kings on 2/20/2017.
 */
public class DialogHelp {
    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message,
        DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder dialog = getDialog(context);
        dialog.setMessage(message);
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", onClickListener);
        return dialog;
    }
}
