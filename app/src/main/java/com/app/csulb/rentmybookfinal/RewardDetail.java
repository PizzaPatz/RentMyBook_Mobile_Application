package com.app.csulb.rentmybookfinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class RewardDetail extends AppCompatActivity {

    TextView text1, point, text2;
    TextView reward_name, reward_point, reward_info;
    ImageView reward_image;
    Button claim;

    ArrayList<RowItem> rewardItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);
        text1 = (TextView) findViewById(R.id.textView1);
        point = (TextView) findViewById(R.id.text_point);
        text2 = (TextView) findViewById(R.id.textView2);

        reward_name = (TextView) findViewById(R.id.reward_name);
        reward_point = (TextView) findViewById(R.id.reward_point);
        reward_image = (ImageView) findViewById(R.id.reward_image);
        reward_info = (TextView) findViewById(R.id.description);

        point.setText(getIntent().getStringExtra("Point"));

        Bundle bundleObject = getIntent().getExtras();
        rewardItems = (ArrayList<RowItem>) bundleObject.getSerializable("Reward");

        showReward();

        claim = (Button)findViewById(R.id.button);
        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemItem();
            }
        });


    }

    public void redeemItem(){
        int original , valued, total;
        try {
            original = Integer.parseInt(point.getText().toString());
            valued = Integer.parseInt(rewardItems.get(0).getPoint());
            if (original < valued){
                Toast.makeText(this,"Not enough points to redeem", Toast.LENGTH_LONG).show();
            }
            else {
                total = original - valued;
                point.setText(Integer.toString(total));
                Toast.makeText(this,"Item redeemed", Toast.LENGTH_LONG).show();
            }
        } catch(NumberFormatException nfe) {
            Toast.makeText(this,"Could not parse " + nfe, Toast.LENGTH_LONG).show();
        }

    }

    public void showReward() {
        reward_name.setText(rewardItems.get(0).getName());
        reward_point.setText(rewardItems.get(0).getPoint());
        reward_info.setText(rewardItems.get(0).getInfo());
        reward_image.setImageResource(rewardItems.get(0).getImage());
    }
}