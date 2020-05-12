package bisma.rabia.gridactionsheet;

import android.app.Application;

import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import bisma.rabia.actionsheet.util.FontUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // register any ITypeface fonts
        FontUtils.registerFont(this, FontAwesome.INSTANCE);
    }
}