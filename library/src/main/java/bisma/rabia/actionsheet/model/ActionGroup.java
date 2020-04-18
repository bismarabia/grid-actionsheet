package bisma.rabia.actionsheet.model;

import java.util.*;

import androidx.annotation.NonNull;

public class ActionGroup {
    private String mTitle;
    private List<Action> mActions;
    private boolean isHeightCalculated;
    private boolean mEnableExpandable;
    private boolean mExpandedOnStart;

    public ActionGroup(String aTitle, List<Action> aActions) {
        mTitle = aTitle;
        mActions = aActions;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String aTitle) {
        mTitle = aTitle;
    }

    public List<Action> getActions() {
        return mActions;
    }

    public void setActions(List<Action> aActions) {
        mActions = aActions;
    }

    public boolean isHeightCalculated() {
        return isHeightCalculated;
    }

    public void setHeightCalculated(boolean aHeightCalculated) {
        isHeightCalculated = aHeightCalculated;
    }

    public boolean isEnableExpandable() {
        return mEnableExpandable;
    }

    public ActionGroup withEnableExpandable(boolean aEnableExpandable) {
        mEnableExpandable = aEnableExpandable;
        return this;
    }

    public ActionGroup withExpandedOnStart(boolean aExpandedOnStart) {
        mExpandedOnStart = aExpandedOnStart;
        return this;
    }

    public boolean isExpandedOnStart() {
        return mExpandedOnStart;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (!(aO instanceof ActionGroup)) return false;
        ActionGroup that = (ActionGroup) aO;
        return isHeightCalculated() == that.isHeightCalculated() &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getActions(), that.getActions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getActions(), isHeightCalculated());
    }

    @NonNull
    @Override
    public String toString() {
        return "ActionGroup{" +
                "mTitle='" + mTitle + '\'' +
                ", mActions=" + mActions +
                ", isHeightCalculated=" + isHeightCalculated +
                '}';
    }
}