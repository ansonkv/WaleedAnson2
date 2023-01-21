package com.waleed.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.waleed.app.ui.base.CommonActivity;
import com.waleed.app.ui.home.HomeActivity;


public class ActivitiesManager {
    public static void openActivity(Context c, Class<?> clas) {
        Intent i = new Intent(c, clas);
        c.startActivity(i);

    }

    @SuppressLint("WrongConstant")
    public static void goTOAnotherActivityAndFinish(Activity c, Class<?> clas) {
        Intent i = new Intent(c, clas);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        c.startActivity(i);
        c.finish();
    }

    @SuppressLint("WrongConstant")
    public static void goTOAnotherActivityWithBundleAndFinish(Activity c, Class<?> clas, Bundle bundle) {
        Intent i = new Intent(c, clas);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtras(bundle);
        c.startActivity(i);
        c.finish();
    }

    public static void goTOAnotherActivityWithBundle(Context c, Class<?> clas, Bundle bundle) {
        Intent i = new Intent(c, clas);
        i.putExtras(bundle);
        c.startActivity(i);

    }


    public static void openURl(Context c, String Url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));

        c.startActivity(browserIntent);

    }


    public static void startActivityForResult(Context context, Class<?> clas, Bundle bundle, int resultCode) {
        Intent i = new Intent(context, clas);

        if (bundle != null)
            i.putExtras(bundle);
        ((Activity) context).startActivityForResult(i, resultCode);

    }

    public static void startActivityForResult(Context context, Intent i, int resultCode) {

        ((Activity) context).startActivityForResult(i, resultCode);

    }

    public static void startSelectPhotoFromMediaActivity(Activity activity, int requestCode) {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), requestCode);

    }

    public static void startCommonActivity(Context context, String fragName, Bundle bundle) {

        Intent i = new Intent(context, HomeActivity.class);

        if (bundle == null)
            bundle = new Bundle();

        bundle.putString(HomeActivity.Companion.getFRAGMENT_NAME_TAG(), fragName);
        i.putExtras(bundle);
        context.startActivity(i);

    }


   /* public static void startLoginActivity(Context context, String fragName, Bundle bundle) {

        Intent i = new Intent(context, AccountsBaseActivity.class);

        if (bundle == null)
            bundle = new Bundle();

        bundle.putString(AccountsBaseActivity.Companion.getFRAGMENT_NAME_TAG(), fragName);
        i.putExtras(bundle);
        context.startActivity(i);

    }

    public static void startLoginActivityForResult(Context context, String fragName, String title, Bundle bundle, int requestCode) {

        Intent i = new Intent(context, AccountsBaseActivity.class);

        if (bundle == null)
            bundle = new Bundle();


        bundle.putString(AppConstants.KEY_SCREEN_NAME, title);
        bundle.putString(AccountsBaseActivity.Companion.getFRAGMENT_NAME_TAG(), fragName);
        i.putExtras(bundle);
        ((Activity) context).startActivityForResult(i, requestCode);

    }*/

    public static void startCommonActivity(Context context, String fragName, String title, Bundle bundle) {

        Intent i = new Intent(context, CommonActivity.class);

        if (bundle == null)
            bundle = new Bundle();


        bundle.putString(AppConstants.KEY_SCREEN_NAME, title);
        bundle.putString(CommonActivity.Companion.getFRAGMENT_NAME_TAG(), fragName);
        i.putExtras(bundle);
        context.startActivity(i);

    }


    public static void startCommonActivityForResult(Context context, String fragName, String title, Bundle bundle, int requestCode) {

        Intent i = new Intent(context, CommonActivity.class);

        if (bundle == null)
            bundle = new Bundle();


        bundle.putString(AppConstants.KEY_SCREEN_NAME, title);
        bundle.putString(CommonActivity.Companion.getFRAGMENT_NAME_TAG(), fragName);
        i.putExtras(bundle);
        ((Activity) context).startActivityForResult(i, requestCode);

    }

    public static void openUrl(Context context, String url) {


        if (url != null && !url.isEmpty()) {

            final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));

            context.startActivity(intent);

        }


    }

}
