package com.wsl.library.password;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wsl on 15-8-28.
 */
public class PwdDisplayView extends View {

    public static final int FLAG_TEXT = 0;
    public static final int FLAG_ASTERISK = 1;
    public static final int FLAG_CIRCLE = 2;

    private int defaultHeight;

    private Path path;
    private RectF rectF;

    private Paint borderPaint;
    private Paint innerPaint;
    private Paint textPaint;

    private float textSize;
    private float radius;

    private int flag;

    private String passwordMark = "*";
    private String[] textArray = {
            "",
            "",
            "",
            "",
            "",
            ""
    };

    public void setFlag(int flag) {
        this.flag = flag;
        invalidate();
    }

    public PwdDisplayView(Context context) {
        this(context, null);
    }

    public PwdDisplayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        defaultHeight = resources.getDimensionPixelOffset(R.dimen.default_pwd_view_height);
        textSize = resources.getDimension(R.dimen.default_pwd_view_text_size);
        radius = resources.getDimension(R.dimen.default_pwd_view_radius);

        path = new Path();

        rectF = new RectF();

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(0xffdfdfdf);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(Color.WHITE);
        innerPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xff333333);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PwdDisplayView);
        flag = a.getInt(R.styleable.PwdDisplayView_flag, FLAG_TEXT);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = Math.max(heightSize, defaultHeight);
        } else {
            heightSize = defaultHeight;
        }
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    private void setup() {
        int width = getWidth();
        int height = getHeight();
        rectF.set(getPaddingLeft(), getPaddingTop(),
                width - getPaddingRight(), height - getPaddingBottom());
        path.addRoundRect(rectF, 10, 10, Path.Direction.CW);
        lineDivider();
    }

    private void lineDivider() {
        float unitWidth = rectF.width() / 6;
        float moveDimen;
        for (int i = 1; i < 6; i++) {
            moveDimen = rectF.left + unitWidth * i;
            path.moveTo(moveDimen, rectF.top);
            path.lineTo(moveDimen, rectF.bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw inner color
        canvas.drawPath(path, innerPaint);

        //draw border
        canvas.drawPath(path, borderPaint);

        //draw text
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        float unitWidth = rectF.width() / 6;
        float translateX;

        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();
        for (int i = 0; i < textArray.length; i++) {
            translateX = rectF.left + unitWidth * i;
            canvas.save();
            canvas.translate(translateX, rectF.top);
            textPaint.setAlpha(TextUtils.isEmpty(textArray[i]) ? 0 : 255);
            switch (flag) {
                case FLAG_TEXT:
                    canvas.drawText(textArray[i], unitWidth / 2, rectF.centerY() + verticalTextOffset,
                            textPaint);
                    break;
                case FLAG_ASTERISK:
                    canvas.drawText(passwordMark, unitWidth / 2, rectF.centerY() + verticalTextOffset * 3 / 2,
                            textPaint);
                    break;
                case FLAG_CIRCLE:
                    canvas.drawCircle(unitWidth / 2, rectF.centerY(), radius, textPaint);
                    break;
            }
            canvas.restore();
        }
    }

    public void addPassword(String text) {
        int len = textArray.length;
        for(int i=0; i<len; i++) {
            if(TextUtils.isEmpty(textArray[i])) {
                textArray[i] = text;
                break;
            }
            if(i == (len -1) && !TextUtils.isEmpty(textArray[i])) {
                textArray[i] = text;
            }
        }
        invalidate();
    }

    public void deletePassword() {
        int len = textArray.length;
        for(int i = (len - 1); i >= 0; i--) {
            if(!TextUtils.isEmpty(textArray[i])) {
                textArray[i] = "";
                break;
            }
        }
        invalidate();
    }

    public void resetPassword() {
        int len = textArray.length;
        for(int i = (len - 1); i >= 0; i--) {
            textArray[i] = "";
        }
        invalidate();
    }

    public String[] getPassword() {
        return textArray.clone();
    }

    public boolean hasFullPassword() {
        int len = textArray.length;
        if(!TextUtils.isEmpty(textArray[len - 1])) {
            return true;
        }
        return false;
    }
}
