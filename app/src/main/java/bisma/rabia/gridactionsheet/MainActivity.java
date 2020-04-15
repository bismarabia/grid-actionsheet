package bisma.rabia.gridactionsheet;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import bisma.rabia.gridactionsheet.databinding.ActivityMainBinding;
import bisma.rabia.library.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false);
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.btnShowActionSheet.setOnClickListener(v ->
                new ActionSheetBuilder(this)
                        .withDefaultActionIcon(R.drawable.ico_unknown_black_24dp)
                        .withActions(new ArrayList<Action>() {{
                            add(new Action(0, 0, "Camera"));
                            add(new Action(1, getResources().getDrawable(R.drawable.ic_menu_gallery), "Gallery"));
                            add(new Action(2, getResources().getDrawable(R.drawable.ic_menu_manage), "Manage"));
                            add(new Action(3, getResources().getDrawable(R.drawable.ic_menu_send), "Send"));
                            add(new Action(4, getResources().getDrawable(R.drawable.ic_menu_share), "Share"));
                        }})
                        .withActionsClickListener(aId -> {
                            // handle item click event.
                        })
                        .show());
    }
}