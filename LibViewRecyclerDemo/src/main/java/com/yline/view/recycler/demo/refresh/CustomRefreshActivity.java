package com.yline.view.recycler.demo.refresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.demo.R;

public class CustomRefreshActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_refresh);
    }
    
    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, CustomRefreshActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
    		}
    		context.startActivity(intent);
    	}
    }
}
