package bisma.rabia.actionsheet.view;

import android.view.View;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import androidx.annotation.StringRes;
import androidx.collection.SparseArrayCompat;
import bisma.rabia.actionsheet.databinding.LayoutExpandableLayoutHeaderBinding;
import bisma.rabia.actionsheet.util.Utils;

/**
 * Created by mgur on 8/9/18.
 */

public class OoExpandableLayout {

    public interface IOoExpandableLayout {
        void onLoadOnce(int aExpandableID);
    }

    public interface IOoExpandableLayoutExpansionState extends IOoExpandableLayout {
        void onExpanded(int aExpandableID);
    }

    private boolean mCallOnce = true;
    private LayoutExpandableLayoutHeaderBinding mLayoutExpandableLayoutBinding = null;
    private ExpandableLayout mTargetLayout = null;
    private int mExpandableID = 0;
    private IOoExpandableLayout mIOoExpandableLayout = null;

    public OoExpandableLayout(LayoutExpandableLayoutHeaderBinding aLayoutExpandableLayoutBinding, ExpandableLayout aTargetLayout, int aId, String aDefaultTitle, IOoExpandableLayout aIOoExpandableLayout) {
        if (validateExpandable(aLayoutExpandableLayoutBinding) && Utils.isObjectNotNull(aTargetLayout)) {
            mLayoutExpandableLayoutBinding = aLayoutExpandableLayoutBinding;
            mTargetLayout = aTargetLayout;
            mExpandableID = aId;
            mIOoExpandableLayout = aIOoExpandableLayout;
            setTitle(aDefaultTitle);
            aLayoutExpandableLayoutBinding.txvExpandableLayoutText.setOnClickListener(v -> directClick());
            aLayoutExpandableLayoutBinding.imvExpandableAction.setOnClickListener(v -> directClick());
            aLayoutExpandableLayoutBinding.lyoExpandableLayout.setOnClickListener(v -> onAction());
            refreshTargetLayout();
            mTargetLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
                if (mIOoExpandableLayout instanceof IOoExpandableLayoutExpansionState && (state == ExpandableLayout.State.EXPANDED || state == ExpandableLayout.State.EXPANDING)) {
                    ((IOoExpandableLayoutExpansionState) mIOoExpandableLayout).onExpanded(aId);
                }
            });
        }
    }

    public void hideExpandableLayout() {
        if (Utils.isObjectNotNull(mLayoutExpandableLayoutBinding)) {
            mLayoutExpandableLayoutBinding.getRoot().setVisibility(View.GONE);
        }
        if (Utils.isObjectNotNull(mTargetLayout)) {
            mTargetLayout.setVisibility(View.GONE);
        }
    }

    //Solution for 'half-checked' checkbox rare bug.
    private void refreshTargetLayout() {
        if (mTargetLayout.isExpanded()) {
            return; //No need to take action for already expanded target layouts.
        }
        mTargetLayout.setExpanded(true, false);
        mTargetLayout.setExpanded(false, false);
    }

    public OoExpandableLayout directClick() {
        mLayoutExpandableLayoutBinding.lyoExpandableLayout.performClick();
        return this;
    }

    private void onAction() {
        if (mTargetLayout.isExpanded()) {
            collapse();
        }
        else {
            if (mCallOnce) {
                if (Utils.isObjectNotNull(mIOoExpandableLayout)) {
                    mIOoExpandableLayout.onLoadOnce(mExpandableID);
                }
                mCallOnce = false;
            }
            mLayoutExpandableLayoutBinding.imvExpandableAction.animate().rotation(180F);
            mTargetLayout.expand(true);
        }
    }

    public void collapse() {
        mLayoutExpandableLayoutBinding.imvExpandableAction.animate().rotation(0F);
        mTargetLayout.collapse(true);
    }

    public void setTitle(String aRes) {
        mLayoutExpandableLayoutBinding.getRoot().post(() -> mLayoutExpandableLayoutBinding.txvExpandableLayoutText.setText(aRes));
    }

    private static boolean validateExpandable(LayoutExpandableLayoutHeaderBinding aLayoutExpandableLayoutBinding) {
        return aLayoutExpandableLayoutBinding != null;
    }

    public static ArrayList<View> getViewList(View aView, @StringRes int aContentDescription) {
        ArrayList<View> views = new ArrayList<>();
        aView.findViewsWithText(views, aView.getContext().getResources().getString(aContentDescription), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        return views;
    }

    public static void collapseOtherExpandables(SparseArrayCompat<OoExpandableLayout> aOoExpandableMap, int aExpandableID) {
        if (Utils.isObjectNull(aOoExpandableMap)) {
            return;
        }
        for (int i = 0; i < aOoExpandableMap.size(); i++) {
            if (aOoExpandableMap.keyAt(i) != aExpandableID) {
                aOoExpandableMap.valueAt(i).collapse();
            }
        }
    }
}