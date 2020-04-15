package bisma.rabia.actionsheet.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.*;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.*;
import androidx.databinding.DataBindingUtil;
import bisma.rabia.actionsheet.*;
import bisma.rabia.actionsheet.databinding.LayoutActionSheetItemBinding;
import bisma.rabia.actionsheet.model.Action;
import bisma.rabia.actionsheet.util.Utils;

/**
 * grid adapter class.
 */
public class ActionGridViewAdapter extends ArrayAdapter<Action> {

    private final Context mContext;
    private final int mFullWidth;
    private ActionSheet mActionSheet;
    private List<Action> mActions;
    private ActionSheetBuilder.IActionSheetActionClickListener mActionsClickListener;

    public ActionGridViewAdapter(@NonNull ActionSheet aActionSheet, List<Action> actions, ActionSheetBuilder.IActionSheetActionClickListener aActionsClickListener) {
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
                    final int defaultIcon = mActionSheet.getActionSheetBuilder().getDefaultActionIcon();
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

    /**
     * @param aAction {@link Action} object
     */
    public void addItem(Action aAction) {
        mActions.add(aAction);
        notifyDataSetChanged();
    }

    /**
     * @param aActions list of {@link Action} object
     */
    public void addItems(List<Action> aActions) {
        mActions.addAll(aActions);
        notifyDataSetChanged();
    }
}