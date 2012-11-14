
package com.hal.android.alphabetindexerlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * 字母BAR视图<br/>
 * 
 * @author Hal
 * @version Mar 24, 2012 11:09:41 AM
 */
public class AlphabetIndexScrollerView extends View {

    private float mIndexbarWidth;

    private float mIndexbarMargin;

    private float mPreviewPadding;

    private float mDensity;

    private float mScaledDensity;

    private float mAlphaRate;

    private int mListViewWidth;

    private int mListViewHeight;

    private int mCurrentSection = -1;

    private boolean mIsIndexing = false;

    private ListView mListView = null;

    private SectionIndexer mIndexer = null;

    private String[] mSections = new String[] {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    };

    private RectF mIndexbarRect;

    public AlphabetIndexScrollerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AlphabetIndexScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = context.getResources().getDisplayMetrics().density;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mIndexbarWidth = 20 * mDensity;
        mIndexbarMargin = 10 * mDensity;
        mPreviewPadding = 5 * mDensity;
        mAlphaRate = 1.0f;
    }

    public AlphabetIndexScrollerView(Context context) {
        super(context);
    }

    public void setListView(ListView lv) {
        mListView = lv;
        setAdapter(mListView.getAdapter());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) (mIndexbarWidth * mDensity),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public void onDraw(Canvas canvas) {
        // mAlphaRate determines the rate of opacity
        Paint indexbarPaint = new Paint();
        indexbarPaint.setColor(Color.RED);
        indexbarPaint.setAlpha((int) (64 * mAlphaRate));
        indexbarPaint.setAntiAlias(true);
        canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);

        if (mSections != null && mSections.length > 0) {
            // Preview is shown when mCurrentSection is set
            if (mCurrentSection >= 0) {
                // 显示浮动提示
                // Paint previewPaint = new Paint();
                // previewPaint.setColor(Color.BLACK);
                // previewPaint.setAlpha(96);
                // previewPaint.setAntiAlias(true);
                // previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0,
                // 0));
                //
                // Paint previewTextPaint = new Paint();
                // previewTextPaint.setColor(Color.WHITE);
                // previewTextPaint.setAntiAlias(true);
                // previewTextPaint.setTextSize(50 * mScaledDensity);
                //
                // float previewTextWidth =
                // previewTextPaint.measureText(mSections[mCurrentSection]);
                // float previewSize = 2 * mPreviewPadding +
                // previewTextPaint.descent()
                // - previewTextPaint.ascent();
                // RectF previewRect = new RectF((mListViewWidth - previewSize)
                // / 2,
                // (mListViewHeight - previewSize) / 2, (mListViewWidth -
                // previewSize) / 2
                // + previewSize, (mListViewHeight - previewSize) / 2 +
                // previewSize);
                //
                // canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity,
                // previewPaint);
                // canvas.drawText(mSections[mCurrentSection], previewRect.left
                // + (previewSize - previewTextWidth) / 2 - 1, previewRect.top
                // + mPreviewPadding - previewTextPaint.ascent() + 1,
                // previewTextPaint);
            }

            Paint indexPaint = new Paint();
            indexPaint.setColor(Color.WHITE);
            indexPaint.setAlpha((int) (255 * mAlphaRate));
            indexPaint.setAntiAlias(true);
            indexPaint.setTextSize(12 * mScaledDensity);

            float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
            float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
            for (int i = 0; i < mSections.length; i++) {
                float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
                canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft, mIndexbarRect.top
                        + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(),
                        indexPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // If down event occurs inside index bar region, start indexing
                if (contains(ev.getX(), ev.getY())) {
                    // It demonstrates that the motion event started from index
                    // bar
                    mIsIndexing = true;
                    // Determine which section the point is in, and move the
                    // list to that section
                    mCurrentSection = getSectionByPoint(ev.getY());
                    if (mListView != null && mIndexer != null) {
                        mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing) {
                    // If this event moves inside index bar
                    if (contains(ev.getX(), ev.getY())) {
                        // Determine which section the point is in, and move the
                        // list to that section
                        mCurrentSection = getSectionByPoint(ev.getY());
                        if (mListView != null && mIndexer != null) {
                            mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                        }
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsIndexing) {
                    mIsIndexing = false;
                    mCurrentSection = -1;
                }
                break;
        }
        return false;
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mListViewWidth = w;
        mListViewHeight = h;
        mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth, mIndexbarMargin, w
                - mIndexbarMargin, h - mIndexbarMargin);
    }

    public void setAdapter(Adapter adapter) {
        if (adapter instanceof SectionIndexer) {
            mIndexer = (SectionIndexer) adapter;
            mSections = (String[]) mIndexer.getSections();
            invalidate();
        }
    }

    private boolean contains(float x, float y) {
        // Determine if the point is in index bar region, which includes the
        // right margin of the bar
        return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top
                + mIndexbarRect.height());
    }

    private int getSectionByPoint(float y) {
        if (mSections == null || mSections.length == 0)
            return 0;
        if (y < mIndexbarRect.top + mIndexbarMargin)
            return 0;
        if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
            return mSections.length - 1;
        return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
    }

}
