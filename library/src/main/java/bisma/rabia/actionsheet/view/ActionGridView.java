package bisma.rabia.actionsheet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class ActionGridView extends GridView {

    public ActionGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionGridView(Context context) {
        super(context);
    }

    public ActionGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = getLayoutParams().height == LayoutParams.WRAP_CONTENT ? MeasureSpec.makeMeasureSpec(View.MEASURED_SIZE_MASK, MeasureSpec.AT_MOST) : heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}