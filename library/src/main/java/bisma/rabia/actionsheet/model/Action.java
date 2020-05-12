package bisma.rabia.actionsheet.model;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.mikepenz.iconics.typeface.IIcon;

import androidx.annotation.DrawableRes;

public class Action {
    private int mId;
    private int mIconDrwInt;
    private Drawable mIconDrw;
    private IIcon mIIcon;
    private String mTitle;
    private View.OnClickListener mOnClickListener;
    private Boolean mIsVisible;

    private Action(int aId, String aTitle) {
        mId = aId;
        mTitle = aTitle;
    }

    public Action(int aId, Drawable aIconDrw, String aTitle) {
        this(aId, aTitle);
        mIconDrw = aIconDrw;
    }

    public Action(int aId, IIcon aIIcon, String aTitle) {
        this(aId, aTitle);
        mIIcon = aIIcon;
    }

    public Action(int aId, int aIcon, String aTitle) {
        this(aId, aTitle);
        mIconDrwInt = aIcon;
    }

    public Action withIcon(@DrawableRes int aIcon) {
        mIconDrwInt = aIcon;
        return this;
    }

    public Action withOnClickListener(View.OnClickListener aOnClickListener) {
        mOnClickListener = aOnClickListener;
        return this;
    }

    public Action withIsVisible(boolean aIsVisible) {
        mIsVisible = aIsVisible;
        return this;
    }

    public int getId() {
        return mId;
    }

    public Drawable getDrawable() {
        return mIconDrw;
    }

    public IIcon getIIcon() {
        return mIIcon;
    }

    public void setIconDrw(Drawable aIconDrw) {
        mIconDrw = aIconDrw;
    }

    public int getIconDrwInt() {
        return mIconDrwInt;
    }

    public String getTitle() {
        return mTitle;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public Boolean isVisible() {
        return mIsVisible;
    }
}