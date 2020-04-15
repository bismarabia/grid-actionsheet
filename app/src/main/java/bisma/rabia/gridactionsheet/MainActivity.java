package bisma.rabia.gridactionsheet;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import bisma.rabia.gridactionsheet.databinding.ActivityMainBinding;
import bisma.rabia.library.ActionSheetBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false);
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.btnShowActionSheet.setOnClickListener(v ->
                new ActionSheetBuilder(this)
                        .withActions(new ArrayList<ActionSheetBuilder.Action>() {{
                            add(new ActionSheetBuilder.Action(0, getResources().getDrawable(R.drawable.ic_menu_camera), "Camera"));
                            add(new ActionSheetBuilder.Action(1, getResources().getDrawable(R.drawable.ic_menu_gallery), "Gallery"));
                            add(new ActionSheetBuilder.Action(2, getResources().getDrawable(R.drawable.ic_menu_manage), "Manage"));
                            add(new ActionSheetBuilder.Action(3, getResources().getDrawable(R.drawable.ic_menu_send), "Send"));
                            add(new ActionSheetBuilder.Action(4, getResources().getDrawable(R.drawable.ic_menu_share), "Share"));
                        }})
                        .withActionsClickListener(aId -> {
                            // handle item click event.
                        })
                        .show());
    }
}