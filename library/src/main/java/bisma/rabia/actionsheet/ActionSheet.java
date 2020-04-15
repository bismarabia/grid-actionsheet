package bisma.rabia.actionsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;

import com.annimon.stream.Stream;
import com.google.android.material.bottomsheet.*;

import java.util.List;

import androidx.annotation.*;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import bisma.rabia.actionsheet.databinding.*;
import bisma.rabia.actionsheet.util.Utils;

import static bisma.rabia.actionsheet.util.Utils.TAG;

public class ActionSheet extends BottomSheetDialogFragment {

    private List<Action> mActions;
    private ActionSheetBuilder.IActionSheetActionClickListener mActionsClickListener;
    private int mLayout;
    private ActionSheetBuilder mActionSheetBuilder;

    public ActionSheet(FragmentActivity aActivity, ActionSheetBuilder aActionSheetBuilder) {
        mActions = aActionSheetBuilder.getActions();
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

            // filter only visible actions
            mActions = Stream.of(mActions).filter(actions -> Utils.isObjectNull(actions.isVisible()) || actions.isVisible()).toList();

            // set the adapter
            actionSheetBinding.gridView.setAdapter(new ActionAdapter(this, mActions, mActionsClickListener));

            // cancel action sheet.
            actionSheetBinding.lyoActionSheetCancel.setOnClickListener(v -> dismiss());

            // handle the action sheet height and set dragging state accordingly.
            actionSheetBinding.lyoActionSheetMainContainer.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
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
                        final int measuredHeight = actionSheetBinding.lyoActionSheetGridView.getMeasuredHeight();
                        final ViewGroup.LayoutParams layoutParams = actionSheetBinding.lyoActionSheetGridView.getLayoutParams();
                        layoutParams.height = measuredHeight - actionSheetBinding.lyoActionSheetCancel.getMeasuredHeight() * 2 / 3;
                        actionSheetBinding.lyoActionSheetGridView.setLayoutParams(layoutParams);
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

    /**
     * grid adapter class.
     */
    static class ActionAdapter extends ArrayAdapter<Action> {

        private final Context mContext;
        private final int mFullWidth;
        private ActionSheet mActionSheet;
        private List<Action> mActions;
        private ActionSheetBuilder.IActionSheetActionClickListener mActionsClickListener;

        public ActionAdapter(@NonNull ActionSheet aActionSheet, List<Action> actions, ActionSheetBuilder.IActionSheetActionClickListener aActionsClickListener) {
            super(Utils.getSafeContext(aActionSheet.getContext()), 0, actions);
            mContext = aActionSheet.getContext();
            mActionSheet = aActionSheet;
            mActions = actions;
            mActionsClickListener = aActionsClickListener;
            mFullWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View contentView, @NonNull ViewGroup parent) {
            LayoutActionSheetItemBinding actionListItemBinding;
            if (contentView == null) {
                actionListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_action_sheet_item, parent, false);
                contentView = actionListItemBinding.getRoot();
            }
            else {
                actionListItemBinding = DataBindingUtil.bind(contentView);
            }
            if (Utils.isObjectNotNull(actionListItemBinding)) {
                final Action action = mActions.get(position);

                // set action variable to the layout
                if (Utils.isObjectNull(action.getDrawable())) {
                    if (action.getIconDrwInt() != 0) {
                        action.setIconDrw(mContext.getResources().getDrawable(action.getIconDrwInt()));
                    }
                    else {
                        final int defaultIcon = mActionSheet.mActionSheetBuilder.getDefaultActionIcon();
                        action.setIconDrw(mContext.getResources().getDrawable(defaultIcon != 0 ? defaultIcon : R.drawable.ico_unknown_black_24dp));
                    }
                }
                actionListItemBinding.setAction(action);

                // set action click listener.
                actionListItemBinding.lyoActionItem.setOnClickListener(v -> {
                    if (Utils.isObjectNotNull(action.getOnClickListener())) {
                        action.getOnClickListener().onClick(v);
                    }
                    else {
                        mActionsClickListener.onActionClicked(action.getId());
                    }
                    mActionSheet.dismiss();
                });

                // set the action width and height.
                final ViewGroup.LayoutParams layoutParams = actionListItemBinding.imvActionItemIcon.getLayoutParams();
                final int size = mFullWidth / (4 * 3);
                layoutParams.width = size;
                layoutParams.height = size;
            }
            return contentView;
        }
    }
}