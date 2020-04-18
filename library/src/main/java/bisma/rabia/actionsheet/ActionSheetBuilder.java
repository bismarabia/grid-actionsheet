package bisma.rabia.actionsheet;

import android.view.View;

import java.util.List;

import androidx.annotation.*;
import androidx.fragment.app.FragmentActivity;
import bisma.rabia.actionsheet.adapter.*;
import bisma.rabia.actionsheet.model.*;

public class ActionSheetBuilder {

    private boolean mPutExpandableAtTheEnd;

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

    /**
     * adapter callback
     */
    public interface IGroupActionAdapterCallBack {
        void onGroupActionAdapterAdapted(ActionGroupAdapter aActionGroupAdapter);
    }

    /**
     * group adapter callback
     */
    public interface IActionAdapterCallBack {
        void onActionAdapterAdapted(ActionGridViewAdapter aActionGroupAdapter);
    }

    private FragmentActivity mActivity;
    private List<Action> mActions;
    private List<ActionGroup> mGroupedActions;
    private int mLayout;
    private IActionSheetExtraLayout mIActionSheetExtraLayout;
    private IActionSheetActionClickListener mActionsClickListener;

    private IGroupActionAdapterCallBack mGroupActionAdapterListener;
    private ActionGroupAdapter mActionGroupAdapter;

    private IActionAdapterCallBack mIActionAdapterCallBack;
    private ActionGridViewAdapter mActionGridViewAdapter;

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
     * seting this will ignore mActions
     *
     * @param aActions map of group title to list of actions inside.
     * @return the current instance.
     */
    public ActionSheetBuilder withGroupedActions(List<ActionGroup> aActions) {
        mGroupedActions = aActions;
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

    public ActionSheetBuilder withGroupActionAdapterListener(IGroupActionAdapterCallBack aAdapterListener) {
        mGroupActionAdapterListener = aAdapterListener;
        return this;
    }

    public ActionSheetBuilder withActionAdapterListener(IActionAdapterCallBack aIActionAdapterCallBack) {
        mIActionAdapterCallBack = aIActionAdapterCallBack;
        return this;
    }

    public ActionSheetBuilder putExpandableAtTheEnd(boolean aPutExpandableAtTheEnd) {
        mPutExpandableAtTheEnd = aPutExpandableAtTheEnd;
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
     * @return map of groups and its actions
     */
    public List<ActionGroup> getGroupedActions() {
        return mGroupedActions;
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

    public IGroupActionAdapterCallBack getGroupActionAdapterListener() {
        return mGroupActionAdapterListener;
    }

    public IActionAdapterCallBack getActionAdapterListener() {
        return mIActionAdapterCallBack;
    }

    public void setActionGroupAdapter(ActionGroupAdapter aActionGroupAdapter) {
        mActionGroupAdapter = aActionGroupAdapter;
    }

    public ActionGroupAdapter getGroupAdapter() {
        return mActionGroupAdapter;
    }

    public void setActionGridViewAdapter(ActionGridViewAdapter aActionGridViewAdapter) {
        mActionGridViewAdapter = aActionGridViewAdapter;
    }

    public ActionGridViewAdapter getActionGridViewAdapter() {
        return mActionGridViewAdapter;
    }

    public boolean isPutExpandableAtTheEnd() {
        return mPutExpandableAtTheEnd;
    }
}