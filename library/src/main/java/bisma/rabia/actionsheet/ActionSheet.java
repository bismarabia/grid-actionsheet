package bisma.rabia.actionsheet;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;

import com.annimon.stream.Stream;
import com.google.android.material.bottomsheet.*;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.*;
import bisma.rabia.actionsheet.adapter.*;
import bisma.rabia.actionsheet.databinding.LyoActionSheetBinding;
import bisma.rabia.actionsheet.model.*;
import bisma.rabia.actionsheet.util.Utils;
import bisma.rabia.actionsheet.view.OoExpandableLayout;

import static bisma.rabia.actionsheet.util.Utils.TAG;

public class ActionSheet extends BottomSheetDialogFragment {

    private final FragmentActivity mActivity;
    private List<Action> mActions;
    private List<ActionGroup> mGroupedActions;
    private ActionSheetBuilder.IActionSheetActionClickListener mActionsClickListener;
    private int mLayout;
    private ActionSheetBuilder mActionSheetBuilder;

    public ActionSheet(FragmentActivity aActivity, ActionSheetBuilder aActionSheetBuilder) {
        mActivity = aActivity;
        mActions = aActionSheetBuilder.getActions();
        mGroupedActions = aActionSheetBuilder.getGroupedActions();
        mActionsClickListener = aActionSheetBuilder.getActionsClickListener();
        mLayout = aActionSheetBuilder.getLayout();
        mActionSheetBuilder = aActionSheetBuilder;
        show(aActivity.getSupportFragmentManager(), TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.lyo_action_sheet, null);

        //binding views to data binding.
        LyoActionSheetBinding actionSheetBinding = DataBindingUtil.bind(view);

        //setting layout with bottom sheet
        bottomSheet.setContentView(view);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());


        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // set state as expanded on start.
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        if (Utils.isObjectNotNull(actionSheetBinding)) {
            // bind the extra layout
            ViewStub viewStub = actionSheetBinding.viewStub.getViewStub();
            if (Utils.isObjectNotNull(viewStub)) {
                if (mLayout != 0) {
                    viewStub.setLayoutResource(mLayout);
                    View inflatedView = viewStub.inflate();
                    if (Utils.isObjectNotNull(inflatedView)) {
                        mActionSheetBuilder.getIActionSheetExtraLayout().onLayoutBound(inflatedView);
                    }
                }
                else {
                    actionSheetBinding.lyoViewStubContainer.setVisibility(View.GONE);
                }
            }


            final boolean isGroupsEnabled = mGroupedActions != null && !mGroupedActions.isEmpty();
            if (isGroupsEnabled) {
                // if groups are enabled
                actionSheetBinding.rvActionSheet.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                actionSheetBinding.rvActionSheet.setLayoutManager(linearLayoutManager);
                actionSheetBinding.rvActionSheet.setHasFixedSize(true);
                if (mActionSheetBuilder.isPutExpandableAtTheEnd()) {
                    mGroupedActions = Stream.of(mGroupedActions).sortBy(ActionGroup::isEnableExpandable).toList();
                }
                final ActionGroupAdapter adapter = new ActionGroupAdapter(mActivity, this);
                actionSheetBinding.rvActionSheet.setAdapter(adapter);
                if (Utils.isObjectNotNull(mActionSheetBuilder.getGroupActionAdapterListener())) {
                    mActionSheetBuilder.getGroupActionAdapterListener().onGroupActionAdapterAdapted(adapter);
                }
                mActionSheetBuilder.setActionGroupAdapter(adapter);
            }
            else {
                // filter only visible actions
                mActions = Stream.of(mActions).filter(actions -> Utils.isObjectNull(actions.isVisible()) || actions.isVisible()).toList();

                // set the adapter
                actionSheetBinding.gridView.setVisibility(View.VISIBLE);
                final ActionGridViewAdapter actionGridViewAdapter = new ActionGridViewAdapter(this, mActions, mActionsClickListener);
                actionSheetBinding.gridView.setAdapter(actionGridViewAdapter);
                mActionSheetBuilder.setActionGridViewAdapter(actionGridViewAdapter);
                if (Utils.isObjectNotNull(mActionSheetBuilder.getActionAdapterListener())) {
                    mActionSheetBuilder.getActionAdapterListener().onActionAdapterAdapted(actionGridViewAdapter);
                }
                new OoExpandableLayout(actionSheetBinding.exlActionSheetGridView);
            }

            // cancel action sheet.
            actionSheetBinding.lyoActionSheetCancel.setOnClickListener(v -> dismiss());

            // handle the action sheet height and set dragging state accordingly.
            actionSheetBinding.lyoActionSheetMainContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final int fullScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
                    final int bottomSheetHeight = actionSheetBinding.lyoActionSheetMainContainer.getMeasuredHeight();
                    int resource = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
                    if (resource > 0) {
                        int screenHeightWithoutStatusBar = fullScreenHeight - Resources.getSystem().getDimensionPixelSize(resource);
                        // if the actionSheet height is bigger than screen height disable actionSheet's dragging feature
                        if (bottomSheetHeight >= screenHeightWithoutStatusBar) {
                            // disable dragging.
                            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                                @Override
                                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    }
                                }

                                @Override
                                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                                }
                            });

                            // decrease the height of gridView so that we can fit the cancel button.
                            final int measuredHeight = actionSheetBinding.nsvActionSheetGridView.getMeasuredHeight();
                            final ViewGroup.LayoutParams layoutParams = actionSheetBinding.nsvActionSheetGridView.getLayoutParams();
                            layoutParams.height = measuredHeight - actionSheetBinding.lyoActionSheetCancel.getMeasuredHeight() * 4 / 3;
                            actionSheetBinding.nsvActionSheetGridView.setLayoutParams(layoutParams);
                            actionSheetBinding.lyoActionSheetMainContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }

        return bottomSheet;
    }

    @Override
    public int getTheme() {
        return R.style.ActionSheetTheme;
    }

    public List<Action> getActions() {
        return mActions;
    }

    public List<ActionGroup> getGroupedActions() {
        return mGroupedActions;
    }

    public ActionSheetBuilder.IActionSheetActionClickListener getActionsClickListener() {
        return mActionsClickListener;
    }

    public int getLayout() {
        return mLayout;
    }

    public ActionSheetBuilder getActionSheetBuilder() {
        return mActionSheetBuilder;
    }
}