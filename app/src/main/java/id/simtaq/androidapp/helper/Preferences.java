package id.simtaq.androidapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    static final String KEY_STATUS_SEDANG_LOGIN = "Status_logged_in";
    static final String KEY_TOKEN = "key_token";
    static final String KEY_ID = "key_id";
    static final String KEY_NAMA = "key_nama";
    static final String KEY_EMAIL = "key_email";
    static final String KEY_LEVEL = "key_level";

    //level user
    //1. Jama'ah masjid/user biasa
    //2. Bendahara Takmir
    //3. Humas Takmir
    //4. Superadmin

    public static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /** Deklarasi Edit Preferences dan mengubah data
     *  yang memiliki key KEY_STATUS_SEDANG_LOGIN dengan parameter status */
    public static void setLoggedInStatus(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_SEDANG_LOGIN,status);
        editor.apply();
    }
    /** Mengembalikan nilai dari key KEY_STATUS_SEDANG_LOGIN berupa boolean */
    public static boolean getLoggedInStatus(Context context){
        return getSharedPreference(context).getBoolean(KEY_STATUS_SEDANG_LOGIN,false);
    }

    public static String getKeyToken(Context context) {
        return getSharedPreference(context).getString(KEY_TOKEN,"");
    }

    public static void setKeyToken(Context context, String keyToken) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_TOKEN, keyToken);
        editor.apply();
    }

    public static String getKeyId(Context context) {
        return getSharedPreference(context).getString(KEY_ID,"");
    }

    public static void setKeyId(Context context, String keyId) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_ID, keyId);
        editor.apply();
    }

    public static String getKeyNama(Context context) {
        return getSharedPreference(context).getString(KEY_NAMA,"");
    }

    public static void setKeyNama(Context context, String keyNama) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_NAMA, keyNama);
        editor.apply();
    }

    public static String getKeyEmail(Context context) {
        return getSharedPreference(context).getString(KEY_EMAIL,"");
    }

    public static void setKeyEmail(Context context, String keyEmail) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_EMAIL, keyEmail);
        editor.apply();
    }

    public static String getKeyLevel(Context context) {
        return getSharedPreference(context).getString(KEY_LEVEL,"");
    }

    public static void setKeyLevel(Context context, String keyLevel) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_LEVEL, keyLevel);
        editor.apply();
    }
}
