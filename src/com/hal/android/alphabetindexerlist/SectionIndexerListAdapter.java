
package com.hal.android.alphabetindexerlist;

import com.hal.android.alphabetindexlist.R;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * section适配器
 * 
 * @version 2012-3-23下午3:02:33
 * @author 张汉国
 */
public class SectionIndexerListAdapter extends BaseAdapter implements SectionIndexer,
        OnScrollListener {

    private LayoutInflater mInflater;

    private ArrayList<Pair<String, ArrayList<String>>> mPairs = new ArrayList<Pair<String, ArrayList<String>>>(
            0);

    public SectionIndexerListAdapter(LayoutInflater inflater,
            ArrayList<Pair<String, ArrayList<String>>> pairs) {
        mInflater = inflater;
        if (pairs != null) {
            mPairs.addAll(pairs);
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        for (int i = 0; i < mPairs.size(); i++) {
            count += mPairs.get(i).second.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        int c = 0;
        for (int i = 0; i < mPairs.size(); i++) {
            if (position >= c && position < c + mPairs.get(i).second.size()) {
                return mPairs.get(i).second.get(position - c);
            }
            c += mPairs.get(i).second.size();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemCache itemCache;
        if (convertView == null) {
            itemCache = new ItemCache();
            convertView = mInflater.inflate(R.layout.list_item, null);
            itemCache.section = convertView.findViewById(R.id.header);
            itemCache.textView = (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(itemCache);
        } else {
            itemCache = (ItemCache) convertView.getTag();
        }
        itemCache.textView.setText((String) getItem(position));
        final int section = getSectionForPosition(position);
        boolean isShowSection = getPositionForSection(section) == position;
        if (isShowSection) {
            itemCache.section.setVisibility(View.VISIBLE);
            ((TextView) itemCache.section.findViewById(R.id.header_text))
                    .setText((String) getSections()[getSectionForPosition(position)]);
        } else {
            itemCache.section.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ItemCache {
        View section;

        TextView textView;
    }

    // sectionIndexer

    @Override
    public Object[] getSections() {
        String[] sections = new String[mPairs.size()];
        for (int i = 0; i < mPairs.size(); i++) {
            sections[i] = mPairs.get(i).first;
        }
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {
        if (section < 0) {
            section = 0;
        }
        if (section > mPairs.size() - 1) {
            section = mPairs.size() - 1;
        }
        if (section == 0) {
            return 0;
        }
        int pos = 0;
        for (int i = 0; i < section; i++) {
            pos += mPairs.get(i).second.size();
        }
        return pos;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position == 0) {
            return 0;
        }
        int secpos = 0;
        for (int i = 0; i < mPairs.size(); i++) {
            if (position >= secpos && position < secpos + mPairs.get(i).second.size()) {
                return i;
            }
            secpos += mPairs.get(i).second.size();
        }
        return -1;
    }

    /**
     * 配置浮动section
     * 
     * @param header
     * @param position
     * @param alpha
     */
    public void configurePinnedHeader(View header, int position, int alpha) {
        TextView lSectionHeader = (TextView) header.findViewById(R.id.header_text);
        lSectionHeader.setText((String) getSections()[getSectionForPosition(position)]);
    }

    // scroll

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // on action
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        if(view instanceof SectionIndexerListView){
            ((SectionIndexerListView) view).configurePinnedHeaderView(firstVisibleItem);
        }
    }
    
    /**
     * Pinned header state: don't show the header.
     */
    public static final int PINNED_HEADER_GONE = 0;

    /**
     * Pinned header state: show the header at the top of the list.
     */
    public static final int PINNED_HEADER_VISIBLE = 1;

    /**
     * Pinned header state: show the header. If the header extends beyond
     * the bottom of the first shown element, push it up and clip.
     */
    public static final int PINNED_HEADER_PUSHED_UP = 2;

    /**
     * Computes the desired state of the pinned header for the given
     * position of the first visible list item. Allowed return values are
     * {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or
     * {@link #PINNED_HEADER_PUSHED_UP}.
     */
    public int getPinnedHeaderState(int position) {
        if (position < 0 || getCount() == 0) {
            return PINNED_HEADER_GONE;
        }
        // The header should get pushed up if the top item shown
        // is the last item in a section for a particular letter.
        int section = getSectionForPosition(position);
        int nextSectionPosition = getPositionForSection(section + 1);
        if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }
        
        return PINNED_HEADER_VISIBLE;
    }

}
