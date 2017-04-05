package com.example.akash.geofencingyou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class sampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        final ImageView tvCheck= (ImageView) findViewById(R.id.tvCheck);
        tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCheck.setImageDrawable(getResources().getDrawable(R.drawable.exa_drawable));
            }
        });

    }
    public int getJump(int height,int jump, int slip)
    {
        int count=0;
        if(height%jump==0)
        {
            return ++count;
        }
        else
        {
            int remainingHeight=(height%jump)+slip;
            int effectivejumpHeight= jump-slip;
            if(remainingHeight/jump==0)
            {
                return ++count;
            }
            else
            {
                remainingHeight =remainingHeight-slip;
            }

        }
        return 0;
    }

    public void getTotalJump()
    {
        int input1=5,input2=1;
        int[] input3= {5};
        int totalJump=0;
        for(int i=0;i<input3.length;i++)
        {
            totalJump=totalJump+getJump(input3[i],input1,input2);
            Toast.makeText(this,""+totalJump,Toast.LENGTH_SHORT).show();
        }
    }
}
