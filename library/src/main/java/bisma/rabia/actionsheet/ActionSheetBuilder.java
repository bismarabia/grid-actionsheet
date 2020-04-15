package bisma.rabia.actionsheet;

import android.view.View;

import java.util.List;

import androidx.annotation.*;
import androidx.fragment.app.FragmentActivity;

public class ActionSheetBuilder {

    /**
     * action items click callback
     */
    public interface IActionSheetActionClickListener {
        void onActionClicked(int aId);
    }

    /**
     * extra layout binding callback.
     */
    public interface IActionSheetExtraLayout {
        void onLayoutBound(View aInflate);
    }

    private FragmentActivity mActivity;
    private List<Action> mActions;
    private int mLayout;
    private IActionSheetExtraLayout mIActionSheetExtraLayout;
    private IActionSheetActionClickListener mActionsClickListener;
    private int mDefaultActionIcon;

    public ActionSheetBuilder(FragmentActivity aActivity) {
        mActivity = aActivity;
    }

    /**
     * @param aActions list of actions
     * @return the current instance.
     */
    public ActionSheetBuilder withActions(List<Action> aActions) {
        mActions = aActions;
        return this;
    }

    /**
     * @param aActionsClickListener action item click callback
     * @return the current instance.
     */
    public ActionSheetBuilder withActionsClickListener(IActionSheetActionClickListener aActionsClickListener) {
        mActionsClickListener = aActionsClickListener;
        return this;
    }

    /**
     * @param aLayout                  layout
     * @param aIActionSheetExtraLayout layout binding callback
     * @return the current instance.
     */
    public ActionSheetBuilder withExtraView(@LayoutRes int aLayout, IActionSheetExtraLayout aIActionSheetExtraLayout) {
        mLayout = aLayout;
        mIActionSheetExtraLayout = aIActionSheetExtraLayout;
        return this;
    }

    public ActionSheetBuilder withDefaultActionIcon(@DrawableRes int aDefaultActionIcon) {
        mDefaultActionIcon = aDefaultActionIcon;
        return this;
    }

    /**
     * show the ActionSheet.
     *
     * @return the current instance.
     */
    public ActionSheetBuilder show() {
        new ActionSheet(mActivity, this);
        return this;
    }

    /**
     * @return the activity
     */
    public FragmentActivity getActivity() {
        return mActivity;
    }

    /**
     * @return list of the actions
     */
    public List<Action> getActions() {
        return mActions;
    }

    /**
     * @return the layout id to be bound in the extra view.
     */
    public int getLayout() {
        return mLayout;
    }

    /**
     * @return extra layout binding callback.
     */
    public IActionSheetExtraLayout getIActionSheetExtraLayout() {
        return mIActionSheetExtraLayout;
    }

    /**
     * @return actions item click callback.
     */
    public IActionSheetActionClickListener getActionsClickListener() {
        return mActionsClickListener;
    }

    public int getDefaultActionIcon() {
        return mDefaultActionIcon;
    }
}