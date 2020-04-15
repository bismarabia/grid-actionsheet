package bisma.rabia.actionsheet.model;


import java.util.*;

import androidx.annotation.NonNull;

public class ActionGroup {
    private String aTitle;
    private List<Action> mActions;
    private boolean isHeightCalculated;

    public ActionGroup(String aATitle, List<Action> aActions) {
        aTitle = aATitle;
        mActions = aActions;
    }

    public String getaTitle() {
        return aTitle;
    }

    public List<Action> getActions() {
        return mActions;
    }

    public boolean isHeightCalculated() {
        return isHeightCalculated;
    }

    public void setHeightCalculated(boolean aHeightCalculated) {
        isHeightCalculated = aHeightCalculated;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (!(aO instanceof ActionGroup)) return false;
        ActionGroup that = (ActionGroup) aO;
        return Objects.equals(getaTitle(), that.getaTitle()) &&
                Objects.equals(getActions(), that.getActions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getaTitle(), getActions());
    }

    @NonNull
    @Override
    public String toString() {
        return "ActionGroup{" +
                "aTitle='" + aTitle + '\'' +
                ", mActions=" + mActions +
                '}';
    }
}