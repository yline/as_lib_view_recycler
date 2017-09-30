package com.yline.view.recycler.demo.refresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.demo.R;

public class CircleProgressActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
    }
    
    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, CircleProgressActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
    		}
    		context.startActivity(intent);
    	}
    }
}
