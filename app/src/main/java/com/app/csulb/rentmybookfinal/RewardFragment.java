package com.app.csulb.rentmybookfinal;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.csulb.rentmybookfinal.login.AuthenticationManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Fragment for reward
 */
public class RewardFragment extends Fragment implements AdapterView.OnItemClickListener{
    View view;
    Button back;
    android.support.v4.app.Fragment fragment;

    TextView text1, point, text2;

    String[] names;
    String[] points;
    String[] infos;
    TypedArray pictures;

    List<RowItem> rowItems;
    ListView mylistview;

    FirebaseDatabase firebase;
    DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reward, container, false);
        return view;
    }

    public void startReward(int i){
        ArrayList<RowItem> reward = new ArrayList<RowItem>();

        RowItem addItem = new RowItem(names[i], points[i], infos[i], pictures.getResourceId(i, -1));

        reward.add(addItem);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Reward",reward);
        Intent getReward = new Intent(getActivity(), RewardDetail.class);
        getReward.putExtras(bundle);
        getReward.putExtra("Point", point.getText().toString());
        getActivity().startActivity(getReward);

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Reward");
        text1 = (TextView)view.findViewById(R.id.textView1);
        firebase = FirebaseDatabase.getInstance();
        database = firebase.getReference("Users");
        database.child(Application.User.getDisplayName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> temp = (HashMap<String, String>)dataSnapshot.getValue();
                String point_val = temp.get("Point");
                setData(point_val);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        text2 = (TextView)view.findViewById(R.id.textView2);


        rowItems = new ArrayList<RowItem>();

        names = getResources().getStringArray(R.array.Item_names);

        points = getResources().getStringArray(R.array.Required_point);

        pictures = getResources().obtainTypedArray(R.array.Item_images);

        infos = getResources().getStringArray(R.array.Item_info);

        for (int i = 0; i < names.length; i++){
            RowItem item = new RowItem(names[i], points[i], infos[i], pictures.getResourceId(i, -1));
            rowItems.add(item);
        }

        mylistview = (ListView)view.findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter(getActivity(), rowItems);
        mylistview.setAdapter(adapter);

        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startReward(i);
            }
        });
    }

    // Set field data
    private void setData(String point_val){
        point = view.findViewById(R.id.text_point);
        point.setText(point_val);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
