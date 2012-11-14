
package com.hal.android.alphabetindexerlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * section listview
 * 
 * @version 2012-3-23下午5:18:27
 * @author 张汉国
 */
public class SectionIndexerListView extends ListView {

    private View mPinnedHeaderView;

    private AlphabetIndexScrollerView mAlphabetIndexerScroller;

    private int mAlphabetViewHeight;

    private int mAlphabetViewWidth;

    private boolean mIsFastScrollEnabled = false;

    private boolean mPinnedHeaderViewVisible;

    private int mPinnedHeaderViewHeight;

    private int mPinnedHeaderViewWidth;

    private Rect mAlphabetRect = new Rect();

    private int[] mAlphabetLocal = new int[2];

    private SectionIndexerListAdapter mSectionIndexerListAdapter;

    public SectionIndexerListView(Context context) {
        super(context);
    }

    public SectionIndexerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionIndexerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPinnedHeaderView != null) {
            measureChild(mPinnedHeaderView, widthMeasureSpec, heightMeasureSpec);
            mPinnedHeaderViewHeight = mPinnedHeaderView.getMeasuredHeight();
            mPinnedHeaderViewWidth = mPinnedHeaderView.getMeasuredWidth();
        }
        if (mAlphabetIndexerScroller != null) {
            measureChild(mAlphabetIndexerScroller, widthMeasureSpec, heightMeasureSpec);
            mAlphabetViewHeight = mAlphabetIndexerScroller.getMeasuredHeight();
            mAlphabetViewWidth = mAlphabetIndexerScroller.getMeasuredWidth();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPinnedHeaderView != null) {
            mPinnedHeaderView.layout(0, 0, mPinnedHeaderViewWidth, mPinnedHeaderViewHeight);
            configurePinnedHeaderView(getFirstVisiblePosition());
        }
        if (mAlphabetIndexerScroller != null) {
            mAlphabetIndexerScroller.layout(getMeasuredWidth() - mAlphabetViewWidth, 0,
                    getMeasuredWidth(), mAlphabetViewHeight);
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mPinnedHeaderViewVisible) {
            drawChild(canvas, mPinnedHeaderView, getDrawingTime());
        }
        if (mAlphabetIndexerScroller != null) {
            drawChild(canvas, mAlphabetIndexerScroller, getDrawingTime());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // if (mAlphabetIndexerScroller != null) {
        // mAlphabetIndexerScroller.draw(canvas);
        // }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!(adapter instanceof SectionIndexerListAdapter)) {
            throw new IllegalArgumentException(SectionIndexerListView.class.getSimpleName()
                    + " must use adapter of type "
                    + SectionIndexerListAdapter.class.getSimpleName());
        }
        if (mSectionIndexerListAdapter != null) {
            this.setOnScrollListener(null);
        }
        mSectionIndexerListAdapter = (SectionIndexerListAdapter) adapter;
        this.setOnScrollListener(mSectionIndexerListAdapter);
        super.setAdapter(adapter);
        if (mAlphabetIndexerScroller != null) {
            mAlphabetIndexerScroller.setAdapter(mSectionIndexerListAdapter);
        }
    }

    @Override
    public SectionIndexerListAdapter getAdapter() {
        return mSectionIndexerListAdapter;
    }

    public void setPinnedHeaderView(View view) {
        mPinnedHeaderView = view;
        if (mPinnedHeaderView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    public void setAlphabetIndexerView(AlphabetIndexScrollerView view) {
        mAlphabetIndexerScroller = view;
        if (mAlphabetIndexerScroller != null) {
            mAlphabetIndexerScroller.setFadingEdgeLength(0);
            mAlphabetIndexerScroller.setListView(this);
        }
        requestLayout();
    }

    /**
     * 控制pinnedheaderview显示
     * 
     * @param position
     */
    public void configurePinnedHeaderView(final int position) {
        if (mPinnedHeaderView == null) {
            return;
        }

        int state = mSectionIndexerListAdapter.getPinnedHeaderState(position);
        switch (state) {
            case SectionIndexerListAdapter.PINNED_HEADER_GONE: {
                mPinnedHeaderViewVisible = false;
                break;
            }

            case SectionIndexerListAdapter.PINNED_HEADER_VISIBLE: {
                mSectionIndexerListAdapter.configurePinnedHeader(mPinnedHeaderView, position, 255);
                if (mPinnedHeaderView.getTop() != 0) {
                    mPinnedHeaderView.layout(0, 0, mPinnedHeaderViewWidth, mPinnedHeaderViewHeight);
                }
                mPinnedHeaderViewVisible = true;
                break;
            }

            case SectionIndexerListAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                if (firstView != null) {
                    int bottom = firstView.getBottom();
                    int headerHeight = mPinnedHeaderView.getHeight();
                    int y;
                    int alpha;
                    if (bottom < headerHeight) {
                        y = (bottom - headerHeight);
                        alpha = 255 * (headerHeight + y) / headerHeight;
                    } else {
                        y = 0;
                        alpha = 255;
                    }
                    mSectionIndexerListAdapter.configurePinnedHeader(mPinnedHeaderView, position,
                            alpha);
                    if (mPinnedHeaderView.getTop() != y) {
                        mPinnedHeaderView.layout(0, y, mPinnedHeaderViewWidth,
                                mPinnedHeaderViewHeight + y);
                    }
                    mPinnedHeaderViewVisible = true;
                }
                break;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // if (mAlphabetIndexerScroller != null) {
        // mAlphabetIndexerScroller.onSizeChanged(w, h, oldw, oldh);
        // }
        // invalidate();
    }

    @Override
    public boolean isFastScrollEnabled() {
        return mIsFastScrollEnabled;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        // mIsFastScrollEnabled = enabled;
        // if (mIsFastScrollEnabled) {
        // if (mAlphabetIndexerScroller == null) {
        // mAlphabetIndexerScroller = new AlphabetIndexScroller(getContext(),
        // this);
        // mAlphabetIndexerScroller.show();
        // }
        // } else {
        // if (mAlphabetIndexerScroller != null) {
        // mAlphabetIndexerScroller.hide();
        // mAlphabetIndexerScroller = null;
        // }
        // }
        super.setFastScrollEnabled(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mAlphabetIndexerScroller != null) {
            mAlphabetIndexerScroller.getLocationInWindow(mAlphabetLocal);
            mAlphabetRect.left = mAlphabetLocal[0];
            mAlphabetRect.top = mAlphabetLocal[1];
            mAlphabetRect.right = mAlphabetLocal[0] + mAlphabetViewWidth;
            mAlphabetRect.bottom = mAlphabetLocal[1] + mAlphabetViewHeight;
            if (mAlphabetRect.contains((int) ev.getX(), (int) ev.getY())
                    && mAlphabetIndexerScroller.onTouchEvent(ev)) {
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

}
