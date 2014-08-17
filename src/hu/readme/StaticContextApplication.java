
package hu.readme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

public class StaticContextApplication extends Application {

    private static Application INSTANCE;

    private static List<Activity> ACTIVITY_CONTEXT_LIST = new ArrayList<Activity>();

    public static Context getAppContext() {
        return INSTANCE;
    }

    public static Resources getStaticResources() {
        return INSTANCE.getResources();
    }

    public static boolean getStaticBoolean(int resId) {
        return getStaticResources().getBoolean(resId);
    }

    public static float getStaticDimension(int resId) {
        return getStaticResources().getDimension(resId);
    }

    public static String getStaticString(int resId) {
        return getStaticResources().getString(resId);
    }

    public static String[] getStaticArray(int resId) {
        return getStaticResources().getStringArray(resId);
    }

    public static int getStaticColor(int resId) {
        return getStaticResources().getColor(resId);
    }

    public static Drawable getStaticDrawable(int resId) {
        return getStaticResources().getDrawable(resId);
    }

    public static String getStaticPackageName() {
        return INSTANCE.getPackageName();
    }

    public static Activity getCurrentActivity() {
        if (ACTIVITY_CONTEXT_LIST.isEmpty()) {
            return null;
        }

        return ACTIVITY_CONTEXT_LIST.get(ACTIVITY_CONTEXT_LIST.size() - 1);
    }
    
    public static ContentResolver getStaticContentResolver() {
        final Activity activity = getCurrentActivity();
        if(null == activity) {
            final Context context = getAppContext();
            if(null == context) {
                return null;
            } else {
                return context.getContentResolver();
            }
        } else {
            return activity.getContentResolver();
        }
    }

    public static void registerActivity(Activity activity) {
        ACTIVITY_CONTEXT_LIST.add(activity);
    }

    public static void unregisterActivity(Activity activity) {
        ACTIVITY_CONTEXT_LIST.remove(activity);
    }

    public StaticContextApplication() {
        INSTANCE = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();   
    }

    public static LayoutInflater getLayoutInflater() {
        final Activity activity = StaticContextApplication.getCurrentActivity();
        if(null == activity) {
            return null;
        } else {
            return activity.getLayoutInflater();
        }
    }
    
}
