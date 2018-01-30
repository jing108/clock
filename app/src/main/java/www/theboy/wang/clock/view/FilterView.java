package www.theboy.wang.clock.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wands_wang on 2017/9/20.
 */

public class FilterView extends LinearLayout {

    private GestureDetector gestureDetector;

    private boolean intercept;

    private HorizontalScrollListener listener;

    private ScrollControlCallback callback;

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intercept = false;
        gestureDetector = new GestureDetector(context, new MyOnGestureListener(false));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        intercept = gestureDetector.onTouchEvent(ev);
        ev.getAction();
        return super.dispatchTouchEvent(ev);
    }

    public void setOnHorizontalScrollListener(HorizontalScrollListener listener) {
        if (listener != null) {
            if (!listener.equals(this.listener)) {
                this.listener = listener;
            }
        } else {
            this.listener = null;
        }
    }

    public void setScrollControlCallback(ScrollControlCallback controlCallback) {
        if (controlCallback != null) {
            if (!controlCallback.equals(callback)) {
                callback = controlCallback;
            }
        } else {
            callback = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return intercept;
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        public MyOnGestureListener(boolean is) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    interface HorizontalScrollListener {

    }

    interface ScrollControlCallback {
        boolean a();
        boolean b();
    }
}
