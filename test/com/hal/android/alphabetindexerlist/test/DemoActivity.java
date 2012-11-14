
package com.hal.android.alphabetindexerlist.test;

import com.hal.android.alphabetindexerlist.AlphabetIndexScrollerView;
import com.hal.android.alphabetindexerlist.SectionIndexerListAdapter;
import com.hal.android.alphabetindexerlist.SectionIndexerListView;
import com.hal.android.alphabetindexlist.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 测试
 * 
 * @author <a href="http://weibo.com/halzhang">Hal</a>
 */
public class DemoActivity extends Activity implements OnItemClickListener {

    private SectionIndexerListView mIndexerListView;

    private SectionIndexerListAdapter mIndexerListAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mIndexerListView = (SectionIndexerListView) findViewById(R.id.list);
        mIndexerListView.setPinnedHeaderView(getLayoutInflater().inflate(R.layout.list_section,
                mIndexerListView, false));
        mIndexerListView.setAlphabetIndexerView((AlphabetIndexScrollerView) getLayoutInflater()
                .inflate(R.layout.alphabet, mIndexerListView, false));
        ArrayList<String> aList = new ArrayList<String>(3);
        aList.add("Adfsfdsf");
        aList.add("Addddd");
        aList.add("Addddd");
        aList.add("Addddd");
        aList.add("Adfdsfsdf");
        aList.add("Adfdsfsdf");
        aList.add("Adfdsfsdf");
        aList.add("Adfdsfsdf");
        ArrayList<String> bList = new ArrayList<String>(3);
        bList.add("Bdfsfdsf");
        bList.add("Bddddd");
        bList.add("Bddddd");
        bList.add("Bddddd");
        bList.add("Bddddd");
        bList.add("Bdfdsfsdf");
        ArrayList<String> cList = new ArrayList<String>(3);
        cList.add("Cdfsfdsf");
        cList.add("Cddddd");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        cList.add("cdfdsfsdf");
        Pair<String, ArrayList<String>> aPair = new Pair<String, ArrayList<String>>("A", aList);
        Pair<String, ArrayList<String>> bPair = new Pair<String, ArrayList<String>>("B", bList);
        Pair<String, ArrayList<String>> cPair = new Pair<String, ArrayList<String>>("C", cList);
        ArrayList<Pair<String, ArrayList<String>>> pairs = new ArrayList<Pair<String, ArrayList<String>>>(
                3);
        pairs.add(aPair);
        pairs.add(bPair);
        pairs.add(cPair);
        mIndexerListAdapter = new SectionIndexerListAdapter(getLayoutInflater(), pairs);
        mIndexerListView.setAdapter(mIndexerListAdapter);
        // mIndexerListView.setFastScrollEnabled(true);
        mIndexerListView.setOnItemClickListener(this);
        // AlphabetIndexerView alphabetIndexerView = (AlphabetIndexerView)
        // getLayoutInflater().inflate(R.layout.alphabet, mIndexerListView,
        // false);
        // alphabetIndexerView.setListView(mIndexerListView);
        // mIndexerListView.setAlphabetIndexerView(alphabetIndexerView);
    }

    private Toast mToast;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String msg = (String) mIndexerListAdapter.getItem(position);
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, 1000);
        } else {
            mToast.setText(msg);
        }

        mToast.show();
    }

}
