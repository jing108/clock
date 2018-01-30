package www.theboy.wang.clock.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import www.theboy.wang.clock.R;

/**
 *
 * @author wands_wang
 * @date 2017/9/22
 */

public class CircularSeekBar extends View {

    private int ac = 0;
    private float ah = 0;
    private float ai = 0;
    private boolean P = true;
    private boolean Q;
    private RectF aaf;
    private RectF T;
    private RectF V;
    private int ae;
    private RectF aa;

    private int pointBorder;

    private boolean isShowLabelText = false;

    private Paint r;

    private boolean touchAble;

    private String topText = "";
    private TextPaint topTextPaint;

    private TextPaint indicateTextPaint;

    private Paint pointPaint;
    private RectF pointRect;
    private float pointX;
    private float pointY;

    private int minPadding;
    private boolean isPointPressed;

    private float moveRadio = 2.5f;
    private float maxProgress = 100.0f;
    private int strokeWidth = 0xa;
    private float progressAngle;
    private float progressToMaxProgress;
    private boolean hasSetAngle = false;

    private float centerPointX;
    private float centerPointY;
    private float circleWidth;

    private Bitmap indicate3Bmp;
    private Bitmap pointPressBmp;
    private Bitmap pointPressBmp1;
    private Bitmap pointWhiteBmp;

    private RectF oval;

    private int whichQuadrant;

    private String labelText2 = "";

    private String labelText1 = "";
    private TextPaint labelTextPaint;

    private boolean isShowIndicate;
    private String indicateTopText;
    private String indicateBottomText;
    private String centerText = "00:00";
    private TextPaint centerTextPaint;
    private RectF centerTextRect;


    private int[] location = new int[2];

    private float radius;

    private PorterDuffXfermode porterDuffXfermode;

    private SeekBarChangeListener seekBarChangeListener;
    private OnViewClickListener viewClickListener;
    private OnShowBarListener showBarListener;
    private OnFinishDrawListener finishDrawListener;
    private IndicateControl indicateControl;
    private CircleChangeListener circleChangeListener;
    private AnimFinishedListener animFinishedListener;

    private Paint progressPaint = new Paint();
    private Paint circlePaint;

    public CircularSeekBar(Context context) {
        super(context);
        init();
    }

    public CircularSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        progressPaint.setColor(Color.parseColor("#ffffff"));
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);

        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor("#7fffffff"));
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(strokeWidth);

        initPointBitmap();

    }

    private void initPointBitmap() {
        Resources resources = getContext().getResources();
        pointPressBmp = BitmapFactory.decodeResource(resources, R.drawable.point_press);
        pointPressBmp1 = pointPressBmp;
        pointWhiteBmp = BitmapFactory.decodeResource(resources, R.drawable.point_white);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerPointX, centerPointY, radius, circlePaint);
        canvas.drawArc(oval, 270, progressAngle, false, progressPaint);

        if (TextUtils.isEmpty(topText)) {
            canvas.drawText(centerText, centerTextRect.left - ac / 4, centerTextRect.bottom - ac / 0xa, centerTextPaint);
        } else {
            canvas.drawText(topText, centerPointX - ah / 2, centerPointY - ac / 3, topTextPaint);
            canvas.drawText(centerText, centerPointX - ai / 2, centerPointY + ac * 2 / 3, centerTextPaint);
        }

        if (isShowLabelText) {
            float y1 = centerPointY + (ac + labelTextPaint.getTextSize()) / 2;
            canvas.drawText(labelText1, centerPointX - ac / 5, y1, labelTextPaint);

            float x = centerPointX + ai / 2 - ac / 10;
            float y2 = centerPointY + (ac + labelTextPaint.getTextSize()) / 2;
            canvas.drawText(labelText2, x, y2, labelTextPaint);
        }

        if (P) {
            pointPaint.setAntiAlias(true);

            float pointRadius = pointWidth / 2;
            //这里Y轴朝上 需要做减法
            pointRect.set(pointX - pointRadius, pointY - pointRadius, pointX + pointRadius, pointY + pointRadius);

            if (isPointPressed) {
                canvas.drawBitmap(pointPressBmp, null, pointRect, pointPaint);
                if (showBarListener != null) {
                    showBarListener.showBar(true);
                }
            } else {
                if (Q) {
                    canvas.drawBitmap(pointWhiteBmp, null, pointRect, pointPaint);
                } else {
                    canvas.drawBitmap(pointPressBmp1, null, pointRect, pointPaint);
                }
                if (showBarListener != null) {
                    showBarListener.showBar(false);
                }
            }
        }

        //cond_1 goto_1
        if (isShowIndicate) {
            canvas.saveLayer(0f, 0f, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.drawColor(Color.argb(0x99, 0, 0, 0));

            r.setColor(Color.parseColor("#7fffffff"));
            r.setXfermode(porterDuffXfermode);
            canvas.drawRoundRect(aa, ac / 2, ac / 2, r);
            canvas.drawCircle(T.centerX(), T.centerY(), T.height() * 3 / 4.0f + 2.0f, r);

            r.setColor(Color.parseColor("#00ffffff"));
            r.setXfermode(porterDuffXfermode);
            canvas.drawRoundRect(centerTextRect, ac / 2, ac / 2, r);
            canvas.drawCircle(T.centerX(), T.centerY(), T.height() * 3 / 4, r);

            indicate3Bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.indicate_3);
            canvas.drawBitmap(indicate3Bmp, null, V, null);

            if (!(TextUtils.isEmpty(indicateTopText) || TextUtils.isEmpty(indicateBottomText))) {
                indicateTextPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(indicateTopText, 0, indicateTopText.length(), centerPointX, centerPointY - radius / 2, indicateTextPaint);
                canvas.drawText(indicateBottomText, 0, indicateBottomText.length(), centerPointX, centerPointY - radius / 2 + ac / 4, indicateTextPaint);
            }

            canvas.restore();
        }

        super.onDraw(canvas);

        if (finishDrawListener != null) {
            finishDrawListener.onFinishDraw();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        centerPointX = width / 2.0f;
        int height = MeasureSpec.getSize(heightMeasureSpec);
        centerPointY = height / 2.0f;

        getLocationOnScreen(location);

        int min = width < height ? width : height;

        minPadding = min / 10;
        radius = min / 2 - circleWidth;

        strokeWidth = min / 0x50;

        pointBorder = (int) (radius / 0xa);
        int g = (int) (radius / 0xa);

        calculatePointXYByAngle((double) progressAngle);


        oval.set(centerPointX - radius, centerPointY - radius, centerPointX + radius, centerPointY + radius);

        circlePaint.setStrokeWidth(strokeWidth);
        progressPaint.setStrokeWidth(strokeWidth);

        float measure = centerTextPaint.measureText("00:00");
        ac = (int) (radius * 2 / measure * centerTextPaint.getTextSize() * 0.8);
        centerTextPaint.setTextSize(ac);
        topTextPaint.setTextSize(ac * 2 / 3);
        labelTextPaint.setTextSize(ac / 0x5);
        ah = topTextPaint.measureText(topText);
        ai = centerTextPaint.measureText(centerText);
        indicateTextPaint.setTextSize(ac / 0x4);


        centerTextRect.left = centerPointX - ai / 2 - ac / 4;
        centerTextRect.top = centerPointY - ac / 2;
        centerTextRect.right = centerPointX + ai / 2 + ac / 4;
        centerTextRect.bottom = centerPointY + ac / 2;

        aa.left = centerTextRect.left - 2.0f;
        aa.top = centerTextRect.top - 2.0f;
        aa.right = centerTextRect.right + 2.0f;
        aa.bottom = centerTextRect.bottom + 2.0f;

        float left = centerPointX - radius - strokeWidth / 2;
        float top = centerPointY - radius - strokeWidth / 2;
        float right = centerPointX + radius + strokeWidth / 2;
        float bottom = centerPointY + radius + strokeWidth / 2;
        V.set(left, top, right, bottom);
    }

    private void calculatePointXYByAngle(Double degrees) {
        double angle = degrees * Math.PI / 180.0;
        double sin = Math.sin(angle);
        pointX = (float) (centerPointX + radius * sin);
        double cos = Math.cos(angle);
        pointY = (float) (centerPointY - radius * cos);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchAble) {

            if (isShowIndicate) {
                if (indicateControl != null) {
                    indicateControl.indicateControl();
                }
            }

            float x = event.getX();
            float y = event.getY();

            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN: //_0
                    if (x > pointX - pointBorder && x < pointX + pointBorder && y > pointY + pointBorder && y < pointY - pointBorder) {
                        if (touchAble) {
                            a(x, y, false, true);
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        return true;
                    }

                    if (centerTextRect.contains(x, y)) {
                        if (viewClickListener != null) {
                            viewClickListener.onViewClick();
                        }
                    }

                    return false;

                case MotionEvent.ACTION_UP: //_2
                    a(x, y, true, false);
                    return true;

                case MotionEvent.ACTION_MOVE: //_1
                    a(x, y, false, false);
                    return true;
            }
        }

        //cond_0 goto_0
        return false;
    }

    private void a(float x, float y, boolean isActionUp, boolean isActionDown) {
        if (!isActionUp) {
            isPointPressed = true;

            double atan2 = Math.atan2(x - centerPointX, y - centerPointY);
            double degrees = Math.toDegrees(atan2);
            degrees += 360.0;
            degrees = degrees % 360.0;
            calculatePointXYByAngle(degrees - degrees % moveRadio);

            if (isActionDown) {
                whichQuadrant = getQuadrant(progressAngle);
            }

            int quadrant = getQuadrant((float) (degrees - degrees % moveRadio));

            if (whichQuadrant != quadrant) {


                if (whichQuadrant == 1) {
                    if (quadrant == 4) {
                        if (circleChangeListener != null) {
                            circleChangeListener.circleChange();
                            ae = ae - 1;
                            ae = (ae + 2) % 2;
                        }
                    }
                    whichQuadrant = quadrant;
                } else if (whichQuadrant == 4) {
                    if (quadrant == 1) {
                        if (circleChangeListener != null) {
                            circleChangeListener.circleChange();
                            ae = ae + 1;
                            ae = (ae + 2) % 2;
                        }
                    }
                    whichQuadrant = quadrant;
                } else {
                    whichQuadrant = quadrant;
                }
            }

            float wands = (float) (degrees - degrees % moveRadio);

            if (wands < 0) {
                wands = (float) (wands + 6.283185307179586);
            }
            setAngle(wands);
        } else {
            isPointPressed = false;
        }

        invalidate();
    }

    //获取象限
    private static int getQuadrant(float degrees) {
        if (degrees >= 0 && degrees < 90) return 1;

        if (degrees >= 90 && degrees < 180) return 2;

        if (degrees >= 180 && degrees < 270) return 3;

        return 4;
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public float getProgressToMaxProgress() {
        return progressToMaxProgress;
    }

    public void setAngle(float angle) {
        this.progressAngle = angle / 360.0f * 100.0f / 100.0f;
        hasSetAngle = true;
        setProgressToMaxProgress(maxProgress / 360.0f * angle);
    }

    public void setAnimFinishedListener(AnimFinishedListener animFinishedListener) {
        this.animFinishedListener = animFinishedListener;
    }

    public void setBackgroundColor(int color) {
        circlePaint.setColor(color);
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public String getCenterText() {
        return centerText;
    }

    public void setCircleChangeListener(CircleChangeListener circleChangeListener) {
        this.circleChangeListener = circleChangeListener;
    }

    public void setIndicateBottomText(String indicateBottomText) {
        this.indicateBottomText = indicateBottomText;
    }

    public void setIndicateControl(IndicateControl indicateControl) {
        this.indicateControl = indicateControl;
    }

    public void setIndicateTopText(String indicateTopText) {
        this.indicateTopText = indicateTopText;
    }

    public void setIsShowIndicate(boolean isShowIndicate) {
        this.isShowIndicate = isShowIndicate;
        postInvalidate();
    }

    public void setLabelText1(String labelText1) {
        this.labelText1 = labelText1;
    }

    public void setLabelText2(String labelText2) {
        this.labelText2 = labelText2;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setMoveRadio(float moveRadio) {
        this.moveRadio = moveRadio;
    }

    public void setOnFinishDrawListener(OnFinishDrawListener finishDrawListener) {
        this.finishDrawListener = finishDrawListener;
    }

    public void setOnShowBarListener(OnShowBarListener showBarListener) {
        this.showBarListener = showBarListener;
    }

    public void setOnViewClickListener(OnViewClickListener viewClickListener) {
        this.viewClickListener = viewClickListener;
    }

    public void setProgressToMaxProgress(float progressToMaxProgress) {
        updateProgress(progressToMaxProgress, false);
    }

    public void setProgressColor(int color) {
        progressPaint.setColor(color);
    }

    public void setSeekBarChangeListener(SeekBarChangeListener listener) {
        seekBarChangeListener = listener;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public void setTouchAble(boolean touchAble) {
        this.touchAble = touchAble;
        if (!touchAble) {
            isPointPressed = false;
        }
        postInvalidate();
    }


    private void updateProgress(float progressToMaxProgress, boolean hasSetAngle) {
        if (!hasSetAngle) {
            if (!this.hasSetAngle) {
                float angle = progressToMaxProgress / maxProgress * 360.0f;
                setAngle(angle);
                calculatePointXYByAngle((double) angle);
            }

            if (seekBarChangeListener != null) {
                seekBarChangeListener.seekBarChanged(progressToMaxProgress);
            }
            this.progressToMaxProgress = progressToMaxProgress;
            this.hasSetAngle = false;
        } else {
            progressAngle = this.progressToMaxProgress;
            calculatePointXYByAngle((double) this.progressToMaxProgress);
        }

        postInvalidate();
    }

    public void showLabelText() {
        isShowLabelText = true;
    }


    public interface CircleChangeListener {
        void circleChange();
    }

    public interface IndicateControl {
        void indicateControl();
    }

    public interface OnShowBarListener {
        void showBar(boolean show);
    }

    public interface SeekBarChangeListener {
        void seekBarChanged(float progressToMaxProgress);
    }

    public interface OnViewClickListener {
        void onViewClick();
    }

    public interface OnFinishDrawListener {
        void onFinishDraw();
    }

    public interface AnimFinishedListener {
        void animFinish();
    }
}
