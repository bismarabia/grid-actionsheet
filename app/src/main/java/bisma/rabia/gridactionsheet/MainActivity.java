package bisma.rabia.gridactionsheet;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import bisma.rabia.actionsheet.ActionSheetBuilder;
import bisma.rabia.actionsheet.model.*;
import bisma.rabia.gridactionsheet.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false);
        setContentView(activityMainBinding.getRoot());

        ActionSheetBuilder actionSheetBuilder = new ActionSheetBuilder(this)
                // default action's icon if not defined.
                .withDefaultActionIcon(R.drawable.ico_unknown_black_24dp)

                // if you want to use group actions
                .withGroupedActions(new ArrayList<ActionGroup>() {{

                    // 1st group.
                    add(new ActionGroup("Sync Actions", new ArrayList<Action>() {{
                        add(new Action(3, getResources().getDrawable(R.drawable.ic_menu_send), "Send"));
                        add(new Action(4, getResources().getDrawable(R.drawable.ic_menu_share), "Share"));
                    }}).withEnableExpandable(true));

                    // 2nd group.
                    add(
                            new ActionGroup("General", new ArrayList<Action>() {{
                                add(new Action(0, 0, "Camera"));
                                add(new Action(1, getResources().getDrawable(R.drawable.ic_menu_gallery), "Gallery"));
                                add(new Action(2, getResources().getDrawable(R.drawable.ic_menu_manage), "Manage"));
                            }})
                                    // enable expandable
                                    .withEnableExpandable(true)

                                    // expand at start.
                                    .withExpandedOnStart(true)
                    );

                    // 3rd group.
                    add(new ActionGroup("Non-Standard", new ArrayList<Action>() {{
                        add(new Action(3, getResources().getDrawable(R.drawable.ic_menu_send), "Send"));
                        add(new Action(4, getResources().getDrawable(R.drawable.ic_menu_share), "Share"));
                    }}));
                }})

                // or use normal grid ActionSheet.
                .withActions(new ArrayList<Action>() {{
                    add(new Action(0, 0, "Camera"));
                    add(new Action(1, getResources().getDrawable(R.drawable.ic_menu_gallery), "Gallery"));
                    add(new Action(2, getResources().getDrawable(R.drawable.ic_menu_manage), "Manage"));
                    add(new Action(3, getResources().getDrawable(R.drawable.ic_menu_send), "Send"));
                    add(new Action(4, getResources().getDrawable(R.drawable.ic_menu_share), "Share"));
                }})

                // if true, place groups with expandable at the bottom.
                .putExpandableAtTheEnd(true)

                // callback that provides groupAction adapter
                .withGroupActionAdapterListener(aActionGroupAdapter -> {

                })

                // callback that provides Actions adapter
                .withActionAdapterListener(aActionAdapter -> {

                })

                // add extra layout on top of the ActionSheet.
                .withExtraView(R.layout.layout_extra, aInflate -> {

                })

                // handle click event on the actions if not specified in the actions definition.
                .withActionsClickListener(aId -> {
                    // handle item click event.
                });

        activityMainBinding.btnShowActionSheet.setOnClickListener(v -> {
            // show ActionSheet.
            actionSheetBuilder.show();
        });
    }
}