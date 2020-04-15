package bisma.rabia.actionsheet.adapter;

import android.app.Activity;
import android.view.*;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bisma.rabia.actionsheet.*;
import bisma.rabia.actionsheet.databinding.LayoutGroupGridviewItemBinding;
import bisma.rabia.actionsheet.model.*;
import bisma.rabia.actionsheet.util.Utils;
import bisma.rabia.actionsheet.view.ActionGridView;

public class ActionGroupAdapter extends RecyclerView.Adapter<ActionGroupAdapter.ActionGroupHolder> {

    private Activity mActivity;
    private ActionSheet mActionSheet;
    private List<ActionGroup> mGroupedActions;
    private ActionSheetBuilder.IActionSheetActionClickListener actionsClickListener;

    public ActionGroupAdapter(Activity aActivity, ActionSheet aActionSheet) {
        mActivity = aActivity;
        mActionSheet = aActionSheet;
        mGroupedActions = mActionSheet.getGroupedActions();
        actionsClickListener = mActionSheet.getActionsClickListener();
    }

    @NonNull
    @Override
    public ActionGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutGroupGridviewItemBinding layoutGroupGridviewItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.layout_group_gridview_item, parent, false);
        return new ActionGroupHolder(layoutGroupGridviewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionGroupHolder holder, int position) {
        final ActionGroup actionGroup = mGroupedActions.get(position);
        holder.mTitle.setText(actionGroup.getaTitle());
        // filter only visible actions
        List<Action> actions = actionGroup.getActions();
        actions = Stream.ofNullable(actions).filter(action -> Utils.isObjectNull(action.isVisible()) || action.isVisible()).toList();
        // set the adapter
        final int size = actions.size();
        holder.mGridView.setAdapter(new ActionGridViewAdapter(mActionSheet, actions, actionsClickListener));

    }

    @Override
    public int getItemCount() {
        return Utils.isObjectNotNull(mGroupedActions) ? mGroupedActions.size() : 0;
    }

    /**
     * @param aActionGroup {@link ActionGroup} object
     */
    public void addItem(ActionGroup aActionGroup) {
        mGroupedActions.add(aActionGroup);
        notifyDataSetChanged();
    }

    /**
     * @param aActionGroups list of {@link ActionGroup} object
     */
    public void addItems(List<ActionGroup> aActionGroups) {
        mGroupedActions.addAll(aActionGroups);
        notifyDataSetChanged();
    }

    public static class ActionGroupHolder extends RecyclerView.ViewHolder {

        public final TextView mTitle;
        public final ActionGridView mGridView;
        public final ConstraintLayout mLyoGroupedGridViewItem;
        public boolean isHeightCalculated;

        public ActionGroupHolder(@NonNull LayoutGroupGridviewItemBinding aItemBinding) {
            super(aItemBinding.getRoot());
            mLyoGroupedGridViewItem = aItemBinding.lyoGroupedGridviewItem;
            mTitle = aItemBinding.txvActionGroupTitle;
            mGridView = aItemBinding.gvActionGroup;
            isHeightCalculated = false;
        }
    }
}