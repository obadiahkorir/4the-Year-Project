package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 7/18/2018.
 */
import android.content.Context;
import android.content.SharedPreferences;
public class AppPreferences {
    private static AppPreferences sInstance;
    private SharedPreferences mPrefs;
    private static final String PREF_APP_RATE = "pref_app_rate";
    private static final String PREF_LAUNCH_COUNT = "pref_launch_count";

    private AppPreferences(Context paramContext) {
        this.mPrefs = paramContext.getSharedPreferences("app_prefs", 0);
    }

    public static AppPreferences getInstance(Context paramContext) {
        if (sInstance == null) {
            sInstance = new AppPreferences(paramContext);
        }
        return sInstance;
    }

    public boolean getAppRate() {
        return this.mPrefs.getBoolean(PREF_APP_RATE, true);
    }

    public void setAppRate(boolean paramBoolean) {
        SharedPreferences.Editor localEditor = this.mPrefs.edit();
        localEditor.putBoolean(PREF_APP_RATE, paramBoolean);
        localEditor.commit();
    }

    public int getLaunchCount() {
        return this.mPrefs.getInt(PREF_LAUNCH_COUNT, 0);
    }

    public void incrementLaunchCount() {
        int i = getLaunchCount();
        SharedPreferences.Editor localEditor = this.mPrefs.edit();
        localEditor.putInt(PREF_LAUNCH_COUNT, i + 1);
        localEditor.commit();
    }

    public void resetLaunchCount() {
        SharedPreferences.Editor localEditor = this.mPrefs.edit();
        localEditor.remove(PREF_LAUNCH_COUNT);
        localEditor.commit();
    }
}