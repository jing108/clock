package www.theboy.wang.clock.view.wheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.util.List;

import www.theboy.wang.clock.sputils.SPValue;

/**
 * Created by wands_wang on 2017/9/26.
 */

public class WheelView extends View {

    private int c = 15;
    private int f;
    private int mWidth = 10;
    private int margin;
    private int padding = 20;
    private int e;
    private int normalItemTxtWidth;
    private int labelTxtWidth;
    private int D;

    private float i = 1.5f;

    private ValueAdapter valueAdapter;

    private StaticLayout currentSl;
    private StaticLayout normalItemSl;
    private StaticLayout prevSl;
    private StaticLayout nextSl;
    private StaticLayout labelTextSl;

    private TextPaint currentTextPaint;
    private TextPaint normalItemTxtPaint;
    private TextPaint labelTextPaint;

    private List F;

    private boolean isSliding;

    //滑动的高度
    private int scrollDistance;

    private int itemHeight = 0;

    private int currentValue;

    private int visibleItems = 5;

    //是否循环
    private boolean isCyclic;

    private Callback callback;

    private String labelTxt;
    private int textSize = 20;

    private Scroller mScroller;

    private Handler mHandler;

    private GestureDetector gestureDetector;

    public WheelView(Context context) {
        super(context);
        setDefault(context);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDefault(context);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefault(context);
    }

    private void setDefault(Context context) {
        e = textSize / 3;
        f = textSize / 2;

        initGestureDetector();

        mScroller = new Scroller(context);

        mHandler = new MyHandler(new WeakReference<>(this));
    }

    private void freeStaticLayout() {
        normalItemSl = null;
        currentSl = null;
        prevSl = null;
        nextSl = null;
        scrollDistance = 0;
    }

    private int measureItemWidth(int size, int mode) {
        if (normalItemTxtPaint == null) {
            normalItemTxtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            normalItemTxtPaint.setTypeface(SPValue.typeface);
            normalItemTxtPaint.setTextSize(textSize);
        }

        if (labelTextPaint == null) {
            labelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
            labelTextPaint.setTextSize(textSize);
            labelTextPaint.setTypeface(SPValue.typeface);
            labelTextPaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
        }

        if (currentTextPaint == null) {
            currentTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            currentTextPaint.setTypeface(SPValue.typeface);
            currentTextPaint.setTextSize(textSize);
        }

        if (getMaxTextLength() > 0) {
            normalItemTxtWidth = (int) (getMaxTextLength() * Math.ceil(Layout.getDesiredWidth("0", normalItemTxtPaint)));
        } else {
            normalItemTxtWidth = 0;
        }

        normalItemTxtWidth += mWidth;

        labelTxtWidth = 0;
        if (labelTxt != null && labelTxt.length() > 0) {
            labelTxtWidth = (int) Math.ceil(Layout.getDesiredWidth(labelTxt, labelTextPaint));
        }

        boolean remeasureTxtWidth;
        int contentWidth;
        if (mode != MeasureSpec.EXACTLY) {
            contentWidth = labelTxtWidth + normalItemTxtWidth + padding * 2;
            if (labelTxtWidth > 0) {
                contentWidth = contentWidth + margin;
            }
            contentWidth = Math.max(contentWidth, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && size < contentWidth) {
                remeasureTxtWidth = true;
            } else {
                size = contentWidth;
                remeasureTxtWidth = false;
            }
        } else {
            remeasureTxtWidth = true;
        }

        if (remeasureTxtWidth) {
            int remain = size - margin - padding * 2;
            if (remain < 0) {
                normalItemTxtWidth = 0;
                labelTxtWidth = 0;
            }

            if (labelTxtWidth <= 0) {
                normalItemTxtWidth = remain + margin;
            } else {
                normalItemTxtWidth = normalItemTxtWidth * remain / (labelTxtWidth + normalItemTxtWidth);
                labelTxtWidth = remain - normalItemTxtWidth;
            }
        }

        if (normalItemTxtWidth > 0) {
            initStaticLayout(normalItemTxtWidth, labelTxtWidth);
        }

        return size;
    }

    private int getMaxTextLength() {
        if (valueAdapter == null) return 0;

        int maxLength = valueAdapter.valueMaxLength();
        if (maxLength > 0) return maxLength;

        //todo : 小于等于0的情况
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            //cond_0
            int height = 0;
            if (currentSl != null) {
                //cond_1
                height = Math.max(getItemHeight() * visibleItems - e * 2 - c, getSuggestedMinimumHeight());
            }
            //goto_2
            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = Math.min(height, heightSize);
            } else {
                heightSize = height;
            }
        }

        //goto_0
        if (heightSize == 0) {
            c = widthSize / 5;
            textSize = widthSize / 7;
        } else {
            //cond_2
            c = heightSize / 20;
            textSize = heightSize / 30;
        }
        //goto_1
        mWidth = widthSize;
        margin = widthSize / 10;
        float density = getContext().getResources().getDisplayMetrics().density;
        padding = (int) (5.0f * density + 0.5f);
        e = -margin;
        measureItemWidth(widthSize, widthMode);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (normalItemSl == null) {
            if (normalItemTxtWidth == 0) {
                measureItemWidth(getWidth(), MeasureSpec.EXACTLY);
            } else {
                initStaticLayout(normalItemTxtWidth, labelTxtWidth);
            }
        }

        //cond_0 goto_0
        if (normalItemTxtWidth > 0) {
            canvas.save();
            canvas.translate(padding, -e);

            float tmp = scrollDistance / (2.0f * itemHeight);

            canvas.save();
            int lineTop = normalItemSl.getLineTop(1);
            canvas.translate(0, scrollDistance - lineTop);
            setNormalItemTxtPaint();
            normalItemTxtPaint.drawableState = getDrawableState();
            normalItemSl.draw(canvas);


            canvas.restore();
            canvas.save();
            Rect rect = new Rect();
            normalItemSl.getLineBounds(visibleItems * 2 - 1, rect);
            setNormalItemTxtPaint();

            normalItemTxtPaint.drawableState = getDrawableState();

            if (scrollDistance > 0) {
                normalItemTxtPaint.setTextScaleX(1.0f + tmp);
                normalItemTxtPaint.setColor(Color.argb(0x4d - ((int) (Math.abs(tmp) * -178.0f)), 0xf9, 0xf9, 0xf9));
                canvas.translate(0, rect.top - scrollDistance / 4 + scrollDistance);
                canvas.scale(1.0f, 1.0f + tmp);
                prevSl.draw(canvas);
            } else {
                canvas.translate(0, rect.top + scrollDistance);
                prevSl.draw(canvas);
            }

            //goto_1
            canvas.restore();
            canvas.save();
            Rect rect1 = new Rect();
            normalItemSl.getLineBounds(visibleItems * 2 + 1, rect1);
            setNormalItemTxtPaint();
            normalItemTxtPaint.drawableState = getDrawableState();

            if (scrollDistance < 0) {
                normalItemTxtPaint.setTextScaleX(1.0f - tmp);

                normalItemTxtPaint.setColor(Color.argb(0x4d - ((int) (Math.abs(tmp) * -178.0f)), 0xf9, 0xf9, 0xf9));
                canvas.translate(0, rect1.top + scrollDistance / 4 + scrollDistance);
                canvas.scale(1.0f, 1.0f - tmp);
                nextSl.draw(canvas);
            } else {
                //cond_6
                canvas.translate(0, rect1.top + scrollDistance);
                nextSl.draw(canvas);
            }

            //goto_2
            canvas.restore();

            labelTextPaint.setColor(0xFF898C95);
            labelTextPaint.drawableState = getDrawableState();
            Rect rect2 = new Rect();
            normalItemSl.getLineBounds(visibleItems / 2, rect2);
            if (labelTextSl != null) {
                canvas.save();
                canvas.translate(normalItemSl.getWidth() - margin, rect2.top);
                labelTextSl.draw(canvas);
                canvas.restore();
            }

            //cond_1
            if (currentSl != null) {
                canvas.save();
                canvas.translate(0, (rect2.top - rect2.bottom + Math.abs(scrollDistance)) / 4 + rect2.top + scrollDistance);
                currentTextPaint.setTextScaleX(i - Math.abs(tmp));

                currentTextPaint.setColor(Color.argb((int) (0xff - Math.abs(tmp) * 178.0f), 0xf9, 0xf9, 0xf9));
                canvas.scale(1.0f, i - Math.abs(tmp));
                currentSl.draw(canvas);
                canvas.restore();
            }
            ///cond_2
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (valueAdapter != null) {
            if (!gestureDetector.onTouchEvent(event)) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    f();
                }
            }
        }

        return true;
    }

    private void initStaticLayout(int normalItemTxtWidth, int labelTxtWidth) {
        labelTextPaint.setColor(0xfff9f9f9);

        if (normalItemSl != null && normalItemSl.getWidth() <= normalItemTxtWidth) {
            normalItemSl.increaseWidthTo(normalItemTxtWidth);
        } else {
            normalItemSl = new StaticLayout(getNormalItemsFormatTxt(), normalItemTxtPaint, normalItemTxtWidth, Layout.Alignment.ALIGN_CENTER, 2, 0, false);
        }

        if (prevSl != null && prevSl.getWidth() <= normalItemTxtWidth) {
            prevSl.increaseWidthTo(normalItemTxtWidth);
        } else {
            prevSl = new StaticLayout(getItemTextByValue(currentValue - 1), normalItemTxtPaint, normalItemTxtWidth, Layout.Alignment.ALIGN_CENTER, 2, 0, false);
        }

        if (nextSl != null && nextSl.getWidth() <= normalItemTxtWidth) {
            nextSl.increaseWidthTo(normalItemTxtWidth);
        } else {
            nextSl = new StaticLayout(getItemTextByValue(currentValue + 1), normalItemTxtPaint, normalItemTxtWidth, Layout.Alignment.ALIGN_CENTER, 2, 0, false);
        }

        if (currentSl != null && currentSl.getWidth() <= normalItemTxtWidth) {
            currentSl.increaseWidthTo(normalItemTxtWidth);
        } else {
            String text = "";
            if (valueAdapter != null) {
                text = valueAdapter.formatValue(currentValue);
            }
            currentSl = new StaticLayout(text, currentTextPaint, normalItemTxtWidth, Layout.Alignment.ALIGN_CENTER, 1, c, false);
        }

        if (labelTxtWidth > 0) {
            if (labelTextSl != null && labelTextSl.getWidth() <= labelTxtWidth) {
                labelTextSl.increaseWidthTo(labelTxtWidth);
            } else {
                labelTextSl = new StaticLayout(labelTxt, labelTextPaint, labelTxtWidth, Layout.Alignment.ALIGN_NORMAL, 1, c, false);
            }
        }
    }

    private String getNormalItemsFormatTxt() {
        StringBuilder sb = new StringBuilder();
        int count = visibleItems / 2 + 1;
        for (int i = currentValue - count; i <= currentValue + count; i++) {
            if (i != currentValue - 1 && i != currentValue && i != currentValue + 1) {
                String value = getItemTextByValue(i);
                if (value != null) {
                    sb.append(value);
                }
            }

            if (i < currentValue + count) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private String getItemTextByValue(int value) {
        String result = null;

        if (valueAdapter != null) {
            int max = valueAdapter.maxItems();
            if (max != 0) {
                if ((value >= 0 && value < max) || (isCyclic && (value < 0 || value >= max))) {
                    while (value < 0) {
                        value += max;
                    }

                    value %= max;
                    result = valueAdapter.formatValue(value);
                }
            }
        }

        return result;
    }

    private void setNormalItemTxtPaint() {
        if (normalItemTxtPaint != null) {
            normalItemTxtPaint.setColor(0xFF898C95);
            normalItemTxtPaint.setTextScaleX(1.0f);
        }
    }

    private void f() {
        if (valueAdapter != null) {
            D = 0;
            int itemHeight = getItemHeight();
            int v0;
            if (scrollDistance > 0) {
                int max = valueAdapter.maxItems();
                if (currentValue >= max) {
                    v0 = 0;
                } else {
                    v0 = 1;
                }
            } else {
                if (currentValue > 0) {
                    v0 = 1;
                } else {
                    v0 = 0;
                }
            }

            if (isCyclic || v0 != 0) {
                //cond_1
                Math.abs(scrollDistance);
                if (Math.abs(scrollDistance) > itemHeight / 2.0f) {
                    if (scrollDistance < 0) {
                        scrollDistance = scrollDistance + itemHeight + 1;
                    } else {
                        scrollDistance = scrollDistance - (itemHeight + 1);
                    }
                }
            }
            //cond_2 goto_2
            if (Math.abs(scrollDistance) > 1) {
                mScroller.startScroll(0, 0, 0, scrollDistance, 400);
                setNextMessage(1);
            }
            //cond_7
            a();
        }
    }

    private void setNextMessage(int what) {
        removeAllMessages();
        mHandler.sendEmptyMessage(what);
    }

    private void a() {
        if (isSliding) {
            while (F.iterator().hasNext()) {
                F.iterator().next();
            }
            isSliding = false;
        }
        freeStaticLayout();
        invalidate();
    }

    private void setCurrentItem$2563266(int value) {
        if (valueAdapter != null) {
            if (valueAdapter.maxItems() != 0) {
                if ((value < 0) || value > valueAdapter.maxItems() && !isCyclic) return;

                value = (value + valueAdapter.maxItems()) % valueAdapter.maxItems();

                if (value != currentValue) {
                    freeStaticLayout();
                    currentValue = value;
                    invalidate();
                }
            }
        }
    }

    private int getItemHeight() {
        if (itemHeight != 0)
            return itemHeight;

        if (normalItemSl != null && normalItemSl.getLineCount() > 0) {
            itemHeight = normalItemSl.getLineTop(2) - normalItemSl.getLineTop(1);
            return itemHeight;
        }

        return getHeight() / visibleItems;
    }


    public int getCurrentItem() {
        return currentValue;
    }


    public ValueAdapter getAdapter() {
        return valueAdapter;
    }

    public void setAdapter(ValueAdapter valueAdapter) {
        this.valueAdapter = valueAdapter;
        freeStaticLayout();
        invalidate();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public void setCurrentItem(int item) {
        setCurrentItem$2563266(item);
    }

    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        invalidate();
        freeStaticLayout();
    }

    public void setInterpolator(Interpolator interpolator) {
        mScroller.forceFinished(true);
        mScroller = new Scroller(getContext(), interpolator);
    }

    public void setLabel(String label) {
        if (labelTxt == null || !labelTxt.equals(label)) {
            labelTxt = label;
            labelTextSl = null;
            invalidate();
        }
    }

    public void setVisibleItems(int items) {
        visibleItems = items;
        invalidate();
    }

    public interface Callback {

    }

    static class MyHandler extends Handler {
        private WeakReference<WheelView> wheelView;

        public MyHandler(WeakReference<WheelView> wheelViewWeakReference) {
            wheelView = wheelViewWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WheelView wv = wheelView.get();
            if (wv != null) {
                wv.mScroller.computeScrollOffset();
                int currY = wv.mScroller.getCurrY();
                int x = wv.D - currY;
                wv.D = currY;
                if (x != 0) {
                    wv.a(x);
                }

                if (Math.abs(currY - wv.mScroller.getFinalY()) == 0) {
                    wv.mScroller.forceFinished(true);
                }

                if (wv.mScroller.isFinished()) {
                    if (msg.what == 0) {
                        wv.f();
                    } else {
                        wv.a();
                    }
                } else {
                    wv.mHandler.sendEmptyMessage(msg.what);
                }
            }
        }
    }

    private void removeAllMessages() {
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
    }

    private void initGestureDetector() {
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                if (isSliding) {
                    mScroller.forceFinished(true);
                    removeAllMessages();
                }
                return isSliding;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                D = currentValue * getItemHeight() + scrollDistance;

                int minY, maxY;
                if (isCyclic) {
                    maxY = Integer.MAX_VALUE;
                } else {
                    maxY = getItemHeight() * valueAdapter.maxItems();
                }

                if (isCyclic) {
                    minY = -maxY;
                } else {
                    minY = 0;
                }

                mScroller.fling(0, D, 0, (int) -velocityY / 2, 0, 0, minY, maxY);

                setNextMessage(0);

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!isSliding) {
                    isSliding = true;
                    while (F.iterator().hasNext()) {
                        F.iterator().next();
                    }
                }

                a((int) -distanceY);

                return true;
            }
        };

        gestureDetector = new GestureDetector(getContext(), listener);
        gestureDetector.setIsLongpressEnabled(true);
    }

    private void a(int distance) {
        scrollDistance += distance;
        int count = scrollDistance / getItemHeight();
        int value = currentValue - count;

        if (isCyclic && valueAdapter.maxItems() > 0) {
            //goto_0
            while (value < 0) {
                value += valueAdapter.maxItems();
            }
            value %= valueAdapter.maxItems();
        } else {
            //cond_2
            if (isSliding) {
                if (value >= valueAdapter.maxItems()) {
                    value = valueAdapter.maxItems() - 1;
                    count = currentValue - valueAdapter.maxItems() + 1;
                } else if (value < 0) {
                    count = currentValue;
                }
            } else {
                //cond_4
                value = Math.min(Math.max(value, currentValue), valueAdapter.maxItems() - 1);
            }
        }

        //goto_1
        if (value == currentValue) {
            invalidate();
        } else {
            setCurrentItem$2563266(value);
        }

        //goto_2
        scrollDistance -= getItemHeight() * count;

        if (scrollDistance > getHeight()) {
            scrollDistance = scrollDistance % getHeight() + getHeight();
        }
    }

}
