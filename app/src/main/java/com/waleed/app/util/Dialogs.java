package com.waleed.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.lang.reflect.Field;

import com.waleed.app.R;


public class Dialogs {

    private static android.app.AlertDialog dialog;


    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongtToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * @param context
     * @param layoutId
     * @param animationStyleId
     * @return
     */
    public static Dialog createCustomDialog(Context context, int layoutId, int animationStyleId, boolean isBlureBehind
            , Boolean cancelable , Boolean isBottom) {

        final Dialog dialog = new Dialog(context,
                0
        );

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(layoutId);

        dialog.getWindow().getAttributes().windowAnimations = animationStyleId;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (isBottom){

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.gravity = Gravity.BOTTOM;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.dimAmount = 0.7f;
            dialog.getWindow().setAttributes(lp);

        }



        if (isBlureBehind)
            dialog.getWindow().getAttributes().dimAmount = 0.4f;

        dialog.setCancelable(cancelable);

        return dialog;

    }


    public static void alertDialog(Context mContext, String message, String btnMsg, DialogInterface.OnClickListener onClickListener) {


        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(mContext);

        dialogBuilder.setMessage(message);

        if (onClickListener != null) {

            dialogBuilder.setPositiveButton(btnMsg, onClickListener);

        } else {

            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                    Dialogs.dialog = null;
                }
            });

        }

        dialog = dialogBuilder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimary);
            }
        });
        if (!dialog.isShowing())
            dialog.show();


    }

    public static android.app.AlertDialog alertDialogWithTwoButton(Context mContext, String message, String positiveBtnMsg,
                                                                   DialogInterface.OnClickListener onClickListener,
                                                                   String negativeBtnMsg, DialogInterface.OnClickListener negativeClickListener) {

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(mContext);

        dialogBuilder.setMessage(message);

        dialogBuilder.setPositiveButton(positiveBtnMsg, onClickListener);

        if (negativeClickListener != null) {

            dialogBuilder.setNegativeButton(negativeBtnMsg, onClickListener);

        } else {

            dialogBuilder.setNegativeButton(negativeBtnMsg, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                    Dialogs.dialog = null;
                }
            });
        }
        return dialogBuilder.create();
    }

    public static ProgressDialog getProgressDialog(Context context,
                                                   String message, boolean cancelable) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setCanceledOnTouchOutside(cancelable);
        pDialog.setCancelable(cancelable);
        return pDialog;
    }


    public static void showProgressDialogWithTwoButtons(
            Activity context,
            String title,
            String message,
            boolean cancelable,
            String okText,
            String cancelText,
            final Runnable okMethod,
            final Runnable cancelMethod) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes response_message
        alertDialogBuilder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (okMethod != null)
                    okMethod.run();
            }
        });
        // set negative button: No response_message
        alertDialogBuilder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (cancelMethod != null)
                    cancelMethod.run();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.setCancelable(cancelable).create();
        alertDialog.setCanceledOnTouchOutside(cancelable);
        alertDialog.show();
    }

    public static ArrayAdapter<String> showProgressDialogWithList(
            Activity context,
            String title,
            String[] list,
            boolean cancelable,
            DialogInterface.OnClickListener listener) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title)
                .setAdapter(adapter, listener)
                .setCancelable(cancelable);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(cancelable);
        alertDialog.show();
        return adapter;
    }


    private static Object getValueFromObject(Object obj, String propertyName) {

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields)
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                if (key.equalsIgnoreCase(propertyName)) {
                    return value;
                }
            } catch (IllegalAccessException e) {
            }
        return null;
    }
}
