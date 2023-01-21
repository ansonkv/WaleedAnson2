package com.waleed.app.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.StringDef;


import com.waleed.app.Waleed;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;


public class LangUtils {

    @StringDef({LANG_EN, LANG_AR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LangValue {
    }

    public static final String LANG_AR = "ar";
    public static final String LANG_EN = "en";

    private static LangUtils sInstance;

    public static LangUtils get() {
        if (sInstance == null)
            sInstance = new LangUtils();
        return sInstance;
    }

    private static String mCurrentLang;

    public @LangValue
    static String getCurrentAppLang() {
        mCurrentLang = Waleed.Companion.getInstance().getAppPref().getString(AppConstants.PREF_KEY_SELECTED_LANG);
        if (mCurrentLang == null)
            mCurrentLang = getDefaultLanguageFromDeviceLocale();
        return mCurrentLang;
    }

    public static String getCurrentAppLangInt() {

        if (getCurrentAppLang().equals(LANG_EN))

            return "1";

        else

            return "2";

    }


    private static String getDefaultLanguageFromDeviceLocale() {
        String lang = Locale.getDefault().getLanguage();
        return lang.toLowerCase().contains(LANG_AR) ? LANG_AR : LANG_EN;
    }

    public static void changeCurrentLocale(Context cxt, @LangUtils.LangValue String newLang) {

        switchAppLanguage(newLang);

        Configuration oldConfig = cxt.getResources().getConfiguration();

        Log.e("Locale",newLang);

        Locale oldLocale = Locale.getDefault();
        try {
            Resources resources = cxt.getResources();
            Locale newLocale = new Locale(newLang);
            Locale.setDefault(newLocale);

            Configuration newConfig = new Configuration();
            newConfig.locale = newLocale;
            resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
        } catch (Exception e) {
            Log.e("Language Exception",e.toString());
            Locale.setDefault(oldLocale);
            cxt.getResources().updateConfiguration(oldConfig, cxt.getResources().getDisplayMetrics());
        }
    }


    public static boolean isCurrentLangArabic() {
        return LANG_AR.equals(getCurrentAppLang());
    }

    public static void switchAppLanguage(@LangValue String lang) {
        mCurrentLang = lang;
        Waleed.Companion.getInstance().getAppPref().saveString(AppConstants.PREF_KEY_SELECTED_LANG, mCurrentLang);
    }

    public static String getLanguageString(String englishString, String arabicString) {
        if (isCurrentLangArabic())
            return arabicString;
        else
            return englishString;
    }
}
