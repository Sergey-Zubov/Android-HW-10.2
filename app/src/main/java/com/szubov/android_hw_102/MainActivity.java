package com.szubov.android_hw_102;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String KEY1 = "key1";
    private final String KEY2 = "key2";
    private SharedPreferences mListSharedPref;
    private final String LIST_TEXT = "list_text";
    private List<Map<String,String>> mArrayList;
    private BaseAdapter mListContentAdapter;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mListView = findViewById(R.id.list);
        mListSharedPref = getSharedPreferences("MyText", MODE_PRIVATE);

        if (!mListSharedPref.contains(getString(R.string.large_text))) {
            SharedPreferences.Editor myEditor = mListSharedPref.edit();
            myEditor.putString(LIST_TEXT, getString(R.string.large_text));
            myEditor.apply();
        }

        mArrayList = prepareContent();
        mListContentAdapter = createAdapter(mArrayList);
        mListView.setAdapter(mListContentAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();

                /*for (Map<String,String> list : mArrayList) {
                    Iterator<Map.Entry<String,String>> maps = list.entrySet().iterator();
                    StringBuilder selectedItem = new StringBuilder();
                    while (maps.hasNext()) {
                        Map.Entry<String, String> entry = maps.next();
                        selectedItem.append(entry.getValue());
                    }
                    if (selectedItem.toString().equals(selectedString)) {
                        mArrayList.remove(list);
                    }
                }*/
                /*for (Map<String,String> list : mArrayList) {
                    StringBuilder selectedItem = new StringBuilder();
                    for (Map.Entry<String,String> map : list.entrySet()) {
                        selectedItem.append(map.getValue());
                    }
                    if (selectedItem.toString().equals(selectedString)) {
                        mArrayList.remove(list);
                    }
                }*/
                /*for (Map<String,String> i : mArrayList) {
                    if (i.containsValue(selectedString)) {
                        mArrayList.remove(i);
                    }
                }*/
                mListContentAdapter.notifyDataSetChanged();
            }
        });
    }


    private BaseAdapter createAdapter(List<Map<String,String>> arrayList) {
        return new SimpleAdapter(this, arrayList, R.layout.list_item,
                new String[]{KEY1,KEY2}, new int[]{R.id.textView1, R.id.textView2});
    }

    private List<Map<String,String>> prepareContent() {
        String[] arrayContent = mListSharedPref.getString(LIST_TEXT,"").split("\n\n");
        List<Map<String,String>> list = new ArrayList<>();
        for (String s : arrayContent) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY1, s);
            map.put(KEY2, s.length() + "");
            list.add(map);
        }
        return list;
    }
}