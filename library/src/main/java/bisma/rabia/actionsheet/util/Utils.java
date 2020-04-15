package bisma.rabia.actionsheet.util;

import android.app.Activity;
import android.content.Context;
import android.util.*;

import java.lang.reflect.Field;

public class Utils {

    public static final String TAG = "TAG";

    public static boolean isObjectNull(Object aO) {
        return aO == null;
    }

    public static boolean isObjectNotNull(Object aO) {
        return !isObjectNull(aO);
    }

    public static Activity getActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                if (isObjectNotNull(activityRecord)) {
                    Class activityRecordClass = activityRecord.getClass();
                    Field pausedField = activityRecordClass.getDeclaredField("paused");
                    pausedField.setAccessible(true);
                    if (!pausedField.getBoolean(activityRecord)) {
                        Field activityField = activityRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        return (Activity) activityField.get(activityRecord);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in getting Activity", e);
        }
        return null;
    }

    public static Context getSafeContext(Context aContext) {
        return isObjectNotNull(aContext) ? aContext : getActivity();
    }
}