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
        holder.mTitle.setText(actionGroup.getTitle());
        // filter only visible actions
        List<Action> actions = actionGroup.getActions();
        actions = Stream.ofNullable(actions).filter(action -> Utils.isObjectNull(action.isVisible()) || action.isVisible()).toList();
        // set the adapter
        final int size = actions.size();
        holder.mGridView.setAdapter(new ActionGridViewAdapter(mActionSheet, actions, actionsClickListener));

        holder.mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // remove the callback to prevent inifnite loop.
                holder.mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewGroup.LayoutParams params = holder.mGridView.getLayoutParams();

                // get the number of rows
                final double rowDouble = size / 4.0;
                int rows = rowDouble > 1.0 && rowDouble > ((int) rowDouble) && rowDouble < ((int) rowDouble) + 1 ? (int) (rowDouble + 1) : (int) rowDouble;
                // 250 is a magic number. :-)
                params.height = 250 * rows;
                holder.mGridView.setLayoutParams(params);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Utils.isObjectNotNull(mGroupedActions) ? mGroupedActions.size() : 0;
    }

    /**
     * @param aActionGroup            {@link ActionGroup} object
     * @param aCheckForDuplicateTitle true to check for existing titles and merge their actions, otherwise add aActionGroups without any further check.
     */
    public void addItem(ActionGroup aActionGroup, boolean aCheckForDuplicateTitle) {
        if (aCheckForDuplicateTitle) {
            Stream.of(mGroupedActions).filter(value -> value.getTitle().equals(aActionGroup.getTitle())).findSingle().ifPresentOrElse(group -> {
                final int index = mGroupedActions.indexOf(group);
                group.getActions().addAll(aActionGroup.getActions());
                mGroupedActions.set(index, group);
            }, () -> mGroupedActions.add(aActionGroup));
        }
        else {
            mGroupedActions.add(aActionGroup);
        }
        notifyDataSetChanged();
    }

    /**
     * @param aActionGroups list of {@link ActionGroup} object
     * @param aCheckForDuplicateTitle true to check for existing titles and merge their actions, otherwise add aActionGroups without any further check.
     */
    public void addItems(List<ActionGroup> aActionGroups, boolean aCheckForDuplicateTitle) {
        if (aCheckForDuplicateTitle) {
            Stream.of(aActionGroups).forEach(aActionGroup -> {
                // check for existence...
                Stream.ofNullable(mGroupedActions).filter(value -> aActionGroup.getTitle().equals(value.getTitle())).findSingle().ifPresentOrElse(group -> {
                    final int index = mGroupedActions.indexOf(group);
                    group.getActions().addAll(aActionGroup.getActions());
                    mGroupedActions.set(index, group);
                }, () -> mGroupedActions.add(aActionGroup));
            });
        }
        else {
            mGroupedActions.addAll(aActionGroups);
        }
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