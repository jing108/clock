package www.theboy.wang.clock.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by wands_wang on 2017/9/21.
 */

public class AutofitTextView extends AppCompatTextView {

    private Paint paint;

    private static int defaultMinSize = 0x14;

    private float precision;
    private float minTextSize;
    private float maxTextSize;

    public AutofitTextView(Context context) {
        super(context);
        fitDefaultMinSize();
        init();
    }

    public AutofitTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        fitDefaultMinSize();
        init();
    }

    private static void fitDefaultMinSize() {
        if (defaultMinSize > 0x3c) {
            defaultMinSize = 0x28;
        } else {
            defaultMinSize = 0x14;
        }
    }

    private void setAutoFitTextSize(String txt, int width) {
        if (width > 0) {
            int targetWidth = width - getPaddingStart() - getPaddingEnd();

            Resources resources;
            if (getContext() != null) {
                resources = getContext().getResources();
            } else {
                resources = Resources.getSystem();
            }

            getPaint().set(paint);

            float size = autoFitTextSize(txt, targetWidth, 0, maxTextSize, resources.getDisplayMetrics());
            if (size < minTextSize) {
                size = minTextSize;
            }

            setTextSize(size);
        }
    }

    private float autoFitTextSize(String text, float targetWidth, float low, float high, DisplayMetrics metrics) {
        float mid = (low + high) / 2.0f;
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mid, metrics));
        if (high - low < precision) {
            return low;
        }
        float size = paint.measureText(text);
        if (size < targetWidth) {
            return autoFitTextSize(text, targetWidth, mid, high, metrics);
        } else if (size > targetWidth) {
            return autoFitTextSize(text, targetWidth, low, mid, metrics);
        } else {
            return mid;
        }
    }

    private void init() {
        minTextSize = defaultMinSize;
        maxTextSize = getTextSize();
        precision = 0.5f;
        paint = new Paint();
    }

    public float getMaxTextSize() {
        return maxTextSize;
    }

    public float getMinTextSize() {
        return minTextSize;
    }

    public float getPrecision() {
        return precision;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        String text = getText().toString();
        setAutoFitTextSize(text, width);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setAutoFitTextSize(getText().toString(), w);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        setAutoFitTextSize(text.toString(), getWidth());
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    public void setPrecision(Float precision) {
        this.precision = precision;
    }

    public void setMinTextSize(int minTextSize) {
        this.minTextSize = (float) minTextSize;
    }

    public void setMaxTextSize(int maxTextSize) {
        this.maxTextSize = (float) maxTextSize;
    }
}
