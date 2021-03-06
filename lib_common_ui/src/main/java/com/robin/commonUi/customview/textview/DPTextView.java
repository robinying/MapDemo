package com.robin.commonUi.customview.textview;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.NoCopySpan;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ConvertUtils;
import com.robin.commonUi.R;
import com.robin.commonUi.customview.Type;
import com.robin.commonUi.util.ColorUtil;


import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class DPTextView extends androidx.appcompat.widget.AppCompatTextView implements Type {
    private static final String TAG = "DPTextView";

    int normalTextColor = Color.WHITE;
    int pressedTextColor = normalTextColor;
    int disableTextColor = Color.GRAY;

    int strokeColor = Color.TRANSPARENT;
    int normalBackgroundColor = Color.TRANSPARENT;
    int pressedBackgroundColor;
    int disableBackgroundColor;

    boolean strokeMode;
    boolean pressWithStrokeMode = true;
    int strokeWidth = ConvertUtils.dp2px(0.5f);

    int radius = 0;
    int topLeftRadius = 0;
    int topRightRadius = 0;
    int bottomLeftRadius = 0;
    int bottomRightRadius = 0;

    int drawableWidth;
    int drawableHeight;
    float drawableScale;

    private int type = Type.RECTANGE;
    private boolean urlRegion;
    private OnUrlClickListener mOnUrlClickListener;


    private Runnable applyRunnable = new Runnable() {
        @Override
        public void run() {
            apply();
        }
    };

    /*
     * ????????????
     * ((http|ftp|https)://)(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\&%_\./-~-]*)?|(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\&%_\./-~-]*)?
     * */
    private String pattern =
            "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?|(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    // ?????? Pattern ??????
    Pattern r = Pattern.compile(pattern);
    // ???????????? matcher ??????
    Matcher m;
    //???????????????list
    LinkedList<String> mStringList;
    //??????????????????????????????list
    LinkedList<UrlInfo> mUrlInfos;
    int flag = Spanned.SPAN_POINT_MARK;

    public DPTextView(Context context) {
        this(context, null);
    }

    public DPTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public DPTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DPTextView);
        Drawable background = null;
        background = a.getDrawable(R.styleable.DPTextView_android_background);
        if (background != null) {
            type = Type.RECTANGE;
            setBackground(this, background);
            a.recycle();
            return;
        } else {
            applyAttrs(context, a);
        }
        a.recycle();

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getLineCount() > 1) {
                    if (getLineSpacingExtra() == 0 && getLineSpacingMultiplier() == 1) {
                        setLineSpacing(ConvertUtils.dp2px(1.2f), 1.2f);
                    }
                }
                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

    }

    void applyAttrs(Context context, TypedArray a) {
        type = a.getInt(R.styleable.DPTextView_type, Type.RECTANGE);
        normalTextColor = a.getColor(R.styleable.DPTextView_android_textColor, normalTextColor);
        pressedTextColor = a.getColor(R.styleable.DPTextView_textPressedColor, normalTextColor);
        disableTextColor = a.getColor(R.styleable.DPTextView_textDisableColor, ColorUtil.alphaColor(0.3f, normalTextColor));

        strokeColor = a.getColor(R.styleable.DPTextView_strokeColor, strokeColor);
        normalBackgroundColor = a.getColor(R.styleable.DPTextView_backgroundColor, normalBackgroundColor);
        pressedBackgroundColor = a.getColor(R.styleable.DPTextView_backgroundPressedColor, ColorUtil.brightnessColor(normalBackgroundColor, ColorUtil.DEFAULT_BRIGHTNESS));
        disableBackgroundColor = a.getColor(R.styleable.DPTextView_backgroundDisableColor, ColorUtil.alphaColor(0.3f, normalBackgroundColor));

        strokeMode = a.getBoolean(R.styleable.DPTextView_strokeMode, false);
        pressWithStrokeMode = a.getBoolean(R.styleable.DPTextView_pressWithStrokeMode, pressWithStrokeMode);
        strokeWidth = a.getDimensionPixelSize(R.styleable.DPTextView_stroke_Width, strokeWidth);

        radius = a.getDimensionPixelSize(R.styleable.DPTextView_corner_radius, radius);
        topLeftRadius = a.getDimensionPixelSize(R.styleable.DPTextView_corner_topLeftRadius, topLeftRadius);
        topRightRadius = a.getDimensionPixelSize(R.styleable.DPTextView_corner_topRightRadius, topRightRadius);
        bottomLeftRadius = a.getDimensionPixelSize(R.styleable.DPTextView_corner_bottomLeftRadius, bottomLeftRadius);
        bottomRightRadius = a.getDimensionPixelSize(R.styleable.DPTextView_corner_bottomRightRadius, bottomRightRadius);

        drawableWidth = a.getDimensionPixelSize(R.styleable.DPTextView_drawableWidth, drawableWidth);
        drawableHeight = a.getDimensionPixelSize(R.styleable.DPTextView_drawableHeight, drawableHeight);
        drawableScale = a.getFloat(R.styleable.DPTextView_drawableScale, drawableScale);

        urlRegion = a.getBoolean(R.styleable.DPTextView_urlRegion, false);

        if (drawableWidth > 0 || drawableHeight > 0 || drawableScale > 0) {
            Drawable[] drawables = getCompoundDrawables();
            if (drawables.length == 4) {
                boolean hasDrawable = false;
                for (Drawable drawable : drawables) {
                    if (drawable != null) {
                        hasDrawable = true;
                        if (drawableWidth > 0 || drawableHeight > 0) {
                            drawable.setBounds(0, 0, drawableWidth > 0 ? drawableWidth : drawable.getIntrinsicWidth(), drawableHeight > 0 ? drawableHeight : drawable.getIntrinsicHeight());
                        } else if (drawableScale > 0) {
                            drawable.setBounds(0, 0, Math.round(drawable.getIntrinsicWidth() * drawableScale), Math.round(drawable.getIntrinsicHeight() * drawableScale));
                        }
                    }
                }
                if (hasDrawable) {
                    setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
                }
            }
        }
        apply();

    }

    //???????????????set?????????????????????????????????10????????????????????????16ms???????????????.
    private void applyValue() {
        removeCallbacks(applyRunnable);
        postDelayed(applyRunnable, 10);
    }


    public void apply() {
        int shape = GradientDrawable.RECTANGLE;
        switch (type) {
            case Type.RECTANGE:
                shape = GradientDrawable.RECTANGLE;
                break;
            case Type.OVAL:
                shape = GradientDrawable.OVAL;
                break;
        }

        GradientDrawable normalDrawable = generateDrawable(shape, normalBackgroundColor);
        GradientDrawable pressedDrawable = generateDrawable(shape, pressedBackgroundColor);
        if (strokeMode && !pressWithStrokeMode) {
            pressedDrawable.setColor(pressedBackgroundColor);
        }
        GradientDrawable disableDrawable = generateDrawable(shape, disableBackgroundColor);


        StateListDrawable backgroundStateListDrawable = new StateListDrawable();

        backgroundStateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        backgroundStateListDrawable.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        backgroundStateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        backgroundStateListDrawable.addState(new int[]{}, normalDrawable);

        int[][] textColorState = new int[4][];
        textColorState[0] = new int[]{android.R.attr.state_pressed};
        textColorState[1] = new int[]{android.R.attr.state_focused};
        textColorState[2] = new int[]{-android.R.attr.state_enabled};
        textColorState[3] = new int[]{};

        int[] textColors = {pressedTextColor, pressedTextColor, disableTextColor, normalTextColor};

        setBackground(this, backgroundStateListDrawable);
        setTextColor(new ColorStateList(textColorState, textColors));
    }


    private GradientDrawable generateDrawable(int shape, int color) {
        GradientDrawable result = new GradientDrawable();
        result.setShape(shape);
        if (strokeMode) {
            result.setColor(Color.TRANSPARENT);
        } else {
            result.setColor(color);
        }
        result.setStroke(strokeWidth, strokeColor == Color.TRANSPARENT ? color : strokeColor);

        if (type == RECTANGE) {
            if (radius > 0) {
                result.setCornerRadius(radius);
            } else if (topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {
                result.setCornerRadii(new float[]{topLeftRadius,
                        topLeftRadius,
                        topRightRadius,
                        topRightRadius,
                        bottomRightRadius,
                        bottomRightRadius,
                        bottomLeftRadius,
                        bottomLeftRadius
                });
            }
        }
        return result;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        clearPointSpan();


        if (urlRegion) {
            text = recognUrl(text);
            this.setMovementMethod(LinkMovementMethod.getInstance());
        }
        super.setText(text, type);
    }

    private SpannableStringBuilderCompat recognUrl(CharSequence text) {
        mStringList.clear();
        mUrlInfos.clear();

        CharSequence contextText;
        CharSequence clickText;
        text = text == null ? "" : text;
        //?????????????????????????????????spanText
        SpannableStringBuilderCompat span = new SpannableStringBuilderCompat(text);
        ClickableSpan[] clickableSpans = span.getSpans(0, text.length(), ClickableSpan.class);
        if (clickableSpans.length > 0) {
            int start = 0;
            int end = 0;
            for (int i = 0; i < clickableSpans.length; i++) {
                start = span.getSpanStart(clickableSpans[0]);
                end = span.getSpanEnd(clickableSpans[i]);
            }
            //?????????????????????????????????
            contextText = text.subSequence(end, text.length());
            //???????????????
            clickText = text.subSequence(start,
                    end);
        } else {
            contextText = text;
            clickText = null;
        }
        m = r.matcher(contextText);
        //????????????
        while (m.find()) {
            //???????????????
            UrlInfo info = new UrlInfo();
            info.start = m.start();
            info.end = m.end();
            mStringList.add(m.group());
            mUrlInfos.add(info);
        }
        return jointText(clickText, contextText);
    }

    /**
     * ????????????
     */
    private SpannableStringBuilderCompat jointText(CharSequence clickSpanText,
                                                   CharSequence contentText) {
        SpannableStringBuilderCompat spanBuilder;
        if (clickSpanText != null) {
            spanBuilder = new SpannableStringBuilderCompat(clickSpanText);
        } else {
            spanBuilder = new SpannableStringBuilderCompat();
        }
        if (mStringList.size() > 0) {
            //??????????????????
            if (mStringList.size() == 1) {
                String preStr = contentText.toString().substring(0, mUrlInfos.get(0).start);
                spanBuilder.append(preStr);
                String url = mStringList.get(0);
                spanBuilder.append(url, new URLClick(url), flag);
                String nextStr = contentText.toString().substring(mUrlInfos.get(0).end);
                spanBuilder.append(nextStr);
            } else {
                //???????????????
                for (int i = 0; i < mStringList.size(); i++) {
                    if (i == 0) {
                        //?????????1???span???????????????
                        String headStr =
                                contentText.toString().substring(0, mUrlInfos.get(0).start);
                        spanBuilder.append(headStr);
                    }
                    if (i == mStringList.size() - 1) {
                        //??????????????????span??????????????????
                        spanBuilder.append(mStringList.get(i), new URLClick(mStringList.get(i)),
                                flag);
                        String footStr = contentText.toString().substring(mUrlInfos.get(i).end);
                        spanBuilder.append(footStr);
                    }
                    if (i != mStringList.size() - 1) {
                        //????????????span???????????????
                        spanBuilder.append(mStringList.get(i), new URLClick(mStringList.get(i)), flag);
                        String betweenStr = contentText.toString()
                                .substring(mUrlInfos.get(i).end,
                                        mUrlInfos.get(i + 1).start);
                        spanBuilder.append(betweenStr);
                    }
                }
            }
        } else {
            spanBuilder.append(contentText);
        }

        return spanBuilder;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public DPTextView setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        applyValue();
        return this;
    }

    public int getPressedTextColor() {
        return pressedTextColor;
    }

    public DPTextView setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
        applyValue();
        return this;
    }

    public int getDisableTextColor() {
        return disableTextColor;
    }

    public DPTextView setDisableTextColor(int disableTextColor) {
        this.disableTextColor = disableTextColor;
        applyValue();
        return this;
    }

    public int getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public DPTextView setNormalBackgroundColor(int normalBackgroundColor) {
        this.normalBackgroundColor = normalBackgroundColor;
        pressedBackgroundColor = ColorUtil.brightnessColor(normalBackgroundColor, ColorUtil.DEFAULT_BRIGHTNESS);
        applyValue();
        return this;
    }

    public int getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public DPTextView setPressedBackgroundColor(int pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        applyValue();
        return this;
    }

    public int getDisableBackgroundColor() {
        return disableBackgroundColor;
    }

    public DPTextView setDisableBackgroundColor(int disableBackgroundColor) {
        this.disableBackgroundColor = disableBackgroundColor;
        applyValue();
        return this;
    }

    public boolean isStrokeMode() {
        return strokeMode;
    }

    public DPTextView setStrokeMode(boolean strokeMode) {
        this.strokeMode = strokeMode;
        applyValue();
        return this;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public DPTextView setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        applyValue();
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public DPTextView setRadius(int radius) {
        this.radius = radius;
        applyValue();
        return this;
    }

    public int getTopLeftRadius() {
        return topLeftRadius;
    }

    public DPTextView setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        applyValue();
        return this;
    }

    public int getTopRightRadius() {
        return topRightRadius;
    }

    public DPTextView setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
        applyValue();
        return this;
    }

    public int getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    public DPTextView setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        applyValue();
        return this;
    }

    public int getBottomRightRadius() {
        return bottomRightRadius;
    }

    public DPTextView setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        applyValue();
        return this;
    }

    public int getType() {
        return type;
    }

    public DPTextView setType(int type) {
        this.type = type;
        applyValue();
        return this;
    }

    public boolean isUrlRegion() {
        return urlRegion;
    }

    public void setUrlRegion(boolean urlRegion) {
        this.urlRegion = urlRegion;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public DPTextView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        applyValue();
        return this;
    }

    @Override
    public void setTextColor(int color) {
        setNormalTextColor(color);
        super.setTextColor(color);
    }

    private static class UrlInfo {
        public int start;
        public int end;
    }

    private class URLClick extends ClickableSpan {
        private String text;

        public URLClick(String text) {
            this.text = text;
        }

        @Override
        public void onClick(View widget) {
            if (mOnUrlClickListener != null) {
                if (mOnUrlClickListener.onUrlClickListener(text)) return;
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(text);
            intent.setData(content_url);
            getContext().startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(0xff517fae);
            ds.setUnderlineText(false);
        }
    }

    public interface OnUrlClickListener {
        boolean onUrlClickListener(String url);
    }


    public void setLoadingText() {
        setLoadingText("...");
    }

    public void setLoadingText(CharSequence text) {
        setLoadingText(text, 1500);
    }

    public void setLoadingText(CharSequence text, int loopDuration) {
        clearPointSpan();
        SpannableStringBuilderCompat builderCompat = new SpannableStringBuilderCompat(text);
        final int length = builderCompat.length();
        //??????
        int delay = (int) ((loopDuration / length) * 0.5);
        for (int i = 0; i < length; i++) {
            LoadingPointSpan span = new LoadingPointSpan(delay * i, loopDuration, 0.4f);
            builderCompat.setSpan(span, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setText(builderCompat);
    }

    @Override
    protected void onDetachedFromWindow() {
        clearPointSpan();
        super.onDetachedFromWindow();
    }

    private void stopPointSpan() {
        if (getText() instanceof Spanned) {
            LoadingPointSpan[] span = ((Spanned) getText()).getSpans(0, getText().length(), LoadingPointSpan.class);
            if (span != null) {
                for (LoadingPointSpan loadingPointSpan : span) {
                    loadingPointSpan.stop();
                }
            }
        }
    }

    private void startPointSpan() {
        if (getText() instanceof Spanned) {
            LoadingPointSpan[] span = ((Spanned) getText()).getSpans(0, getText().length(), LoadingPointSpan.class);
            if (span != null) {
                for (LoadingPointSpan loadingPointSpan : span) {
                    loadingPointSpan.start();
                }
            }
        }
    }

    private void clearPointSpan() {
        if (getText() instanceof Spanned) {
            LoadingPointSpan[] span = ((Spanned) getText()).getSpans(0, getText().length(), LoadingPointSpan.class);
            if (span != null) {
                for (LoadingPointSpan loadingPointSpan : span) {
                    loadingPointSpan.clear();
                }
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startPointSpan();
        } else {
            stopPointSpan();
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        startPointSpan();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        stopPointSpan();
        return super.onSaveInstanceState();
    }

    /**
     * textview?????????span???????????????????????????????????????
     */
    class LoadingPointSpan extends SuperscriptSpan implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener, NoCopySpan {
        private int delay;
        private int duration;
        private float maxOffsetRatio;//???????????????????????????
        private ValueAnimator mValueAnimator;
        private int curOffset;

        LoadingPointSpan(int delay, int duration, float maxOffsetRatio) {
            this.delay = delay;
            this.duration = duration;
            this.maxOffsetRatio = maxOffsetRatio;
        }

        /**
         * ??????baseline,.???????????????
         *
         * @param tp
         */
        @Override
        public void updateDrawState(TextPaint tp) {
            initAnimate(tp.ascent());
            tp.baselineShift = curOffset;
        }


        /**
         * @param textAscent ????????????
         */
        private void initAnimate(float textAscent) {
            if (mValueAnimator != null) {
                return;
            }
            Log.i(TAG, "animate create");
            curOffset = 0;
            maxOffsetRatio = Math.max(0, Math.min(1.0f, maxOffsetRatio));
            int maxOffset = (int) (textAscent * maxOffsetRatio);
            mValueAnimator = ValueAnimator.ofInt(0, maxOffset);
            mValueAnimator.setDuration(duration);
            mValueAnimator.setStartDelay(delay);
            mValueAnimator.setInterpolator(new PointInterpolator(maxOffsetRatio));
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.addUpdateListener(this);
            mValueAnimator.addListener(this);
            mValueAnimator.start();
        }

        void stop() {
            if (mValueAnimator != null) {
                Log.i(TAG, "span stop: "+mValueAnimator);
                mValueAnimator.cancel();
            }
        }

        void start() {
            if (mValueAnimator != null) {
                Log.i(TAG, "span start: "+mValueAnimator);
                mValueAnimator.start();
            }
        }

        void clear() {
            if (mValueAnimator != null) {
                Log.i(TAG, "span clear: "+mValueAnimator);
                mValueAnimator.removeAllUpdateListeners();
                mValueAnimator.removeAllListeners();
                mValueAnimator.cancel();
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            curOffset = (int) animation.getAnimatedValue();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    invalidate();
                }
            } else {
                if (getParent() != null) {
                    invalidate();
                }
            }
            Log.e("canim", "onAnimationUpdate"+getText().toString()+curOffset);
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            curOffset = 0;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        /**
         * ???????????????0~maxOffsetRatio
         */
        private class PointInterpolator implements TimeInterpolator {

            private final float maxOffsetRatio;

            public PointInterpolator(float animatedRange) {
                maxOffsetRatio = Math.abs(animatedRange);
            }

            @Override
            public float getInterpolation(float input) {
                if (input > maxOffsetRatio) {
                    return 0f;
                }
                double radians = (input / maxOffsetRatio) * Math.PI;
                return (float) Math.sin(radians);
            }

        }
    }

    public  void setBackground(View v, Drawable background) {
        if (v == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(background);
        } else {
            v.setBackgroundDrawable(background);
        }
    }

}