package com.app.csulb.rentmybookfinal;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Reward extends AppCompatActivity implements AdapterView.OnItemClickListener{

    TextView text1, point, text2;

    String[] names;
    String[] points;
    String[] infos;
    TypedArray pictures;

    List<RowItem> rowItems;
    ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        text1 = (TextView) findViewById(R.id.textView1);
        point = (TextView) findViewById(R.id.text_point);
        text2 = (TextView) findViewById(R.id.textView2);


        rowItems = new ArrayList<RowItem>();

        names = getResources().getStringArray(R.array.Item_names);

        points = getResources().getStringArray(R.array.Required_point);

        pictures = getResources().obtainTypedArray(R.array.Item_images);

        infos = getResources().getStringArray(R.array.Item_info);

        for (int i = 0; i < names.length; i++){
            RowItem item = new RowItem(names[i], points[i], infos[i], pictures.getResourceId(i, -1));
            rowItems.add(item);
        }

        mylistview = (ListView)findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);

        mylistview.setOnItemClickListener(this);
    }


    @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            startReward(i);
        }

    public void startReward(int i){
        ArrayList<RowItem> reward = new ArrayList<RowItem>();

        RowItem addItem = new RowItem(names[i], points[i], infos[i], pictures.getResourceId(i, -1));

        reward.add(addItem);

        Intent getReward = new Intent(this, RewardDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Reward",reward);
        getReward.putExtras(bundle);
        getReward.putExtra("Point", point.getText().toString());
        startActivity(getReward);

    }
}
