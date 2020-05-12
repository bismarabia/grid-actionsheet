package bisma.rabia.actionsheet.util;

import android.content.Context;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.typeface.ITypeface;

public class FontUtils {

    /**
     * register fonts with {@link Iconics}
     *
     * @param aContext   context
     * @param aITypeface {@link ITypeface} instance
     */
    public static void registerFont(Context aContext, ITypeface aITypeface) {
        Iconics.init(aContext);
        Iconics.registerFont(aITypeface);
    }
}