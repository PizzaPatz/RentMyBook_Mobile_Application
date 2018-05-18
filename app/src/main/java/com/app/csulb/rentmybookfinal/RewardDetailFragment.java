package com.app.csulb.rentmybookfinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
    Shows reward detail in depth
 */
public class RewardDetailFragment extends Fragment implements View.OnClickListener{

    View view;
    Button back;
    Fragment fragment;

    TextView text1, point, text2;
    TextView reward_name, reward_point, reward_info;
    ImageView reward_image;
    Button claim;

    ArrayList<RowItem> rewardItems;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reward_detail, container, false);

        text1 = (TextView)view.findViewById(R.id.textView1);
        point = (TextView)view.findViewById(R.id.text_point);
        text2 = (TextView)view.findViewById(R.id.textView2);

        reward_name = (TextView)view.findViewById(R.id.reward_name);
        reward_point = (TextView)view.findViewById(R.id.reward_point);
        reward_image = (ImageView)view.findViewById(R.id.reward_image);
        reward_info = (TextView)view.findViewById(R.id.description);

        point.setText(getActivity().getIntent().getStringExtra("Point"));

        Bundle bundleObject = getActivity().getIntent().getExtras();
        rewardItems = (ArrayList<RowItem>) bundleObject.getSerializable("Reward");

        showReward();

        claim = (Button)view.findViewById(R.id.button);
        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemItem();
            }
        });

        back.setOnClickListener(this);
        return view;
    }

    public void redeemItem(){
        int original , valued, total;
        try {
            original = Integer.parseInt(point.getText().toString());
            valued = Integer.parseInt(rewardItems.get(0).getPoint());
            if (original < valued){
                Toast.makeText(getActivity(),"Not enough points to redeem", Toast.LENGTH_LONG).show();
            }
            else {
                total = original - valued;
                point.setText(Integer.toString(total));
                Toast.makeText(getActivity(),"Item redeemed", Toast.LENGTH_LONG).show();
            }
        } catch(NumberFormatException nfe) {
            Toast.makeText(getActivity(),"Could not parse " + nfe, Toast.LENGTH_LONG).show();
        }

    }

    public void showReward() {
        reward_name.setText(rewardItems.get(0).getName());
        reward_point.setText(rewardItems.get(0).getPoint());
        reward_info.setText(rewardItems.get(0).getInfo());
        reward_image.setImageResource(rewardItems.get(0).getImage());
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");
    }


    @Override
    public void onClick(View view) {
        fragment = null;
        switch (view.getId()) {

        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}