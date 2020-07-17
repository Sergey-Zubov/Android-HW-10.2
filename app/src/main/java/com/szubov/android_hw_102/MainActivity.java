package com.szubov.android_hw_102;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String KEY1 = "key1";
    private final String KEY2 = "key2";
    private SharedPreferences mListSharedPref;
    private final String LIST_TEXT = "list_text";
    private List<Map<String,String>> mArrayList = new ArrayList<>();
    private BaseAdapter mListContentAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        ListView mListView = findViewById(R.id.list);
        mListSharedPref = getSharedPreferences("MyText", MODE_PRIVATE);
        mSwipeRefresh = findViewById(R.id.swipeRefresh);

        if (!mListSharedPref.contains(getString(R.string.large_text))) {
            SharedPreferences.Editor myEditor = mListSharedPref.edit();
            myEditor.putString(LIST_TEXT, getString(R.string.large_text));
            myEditor.apply();
        }

        prepareContent();
        mListContentAdapter = createAdapter(mArrayList);
        mListView.setAdapter(mListContentAdapter);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareContent();
                mListContentAdapter.notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mSelectedItem = ((TextView)view.findViewById(R.id.textView1)).
                        getText().toString() + ((TextView)view.findViewById(R.id.textView2)).
                        getText().toString();
                for (int i = 0; i < mArrayList.size(); i++) {
                    if (mSelectedItem.equals(mArrayList.get(i).get(KEY1) +
                            mArrayList.get(i).get(KEY2))) {
                        mArrayList.remove(i);
                        mListContentAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
    }

    private BaseAdapter createAdapter(List<Map<String,String>> arrayList) {
        return new SimpleAdapter(this, arrayList, R.layout.list_item,
                new String[]{KEY1,KEY2}, new int[]{R.id.textView1, R.id.textView2});
    }

    private void prepareContent() {
        String[] arrayContent = mListSharedPref.getString(LIST_TEXT,"").split("\n\n");
        mArrayList.clear();
        for (String s : arrayContent) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY1, s);
            map.put(KEY2, s.length() + "");
            mArrayList.add(map);
        }
    }
}