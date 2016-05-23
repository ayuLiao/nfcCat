package com.liao.nfcclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class FirstActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.first_activity);
		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(FirstActivity.this,Login.class));
				finish();
			}
		};
		handler.postDelayed(runnable, 2000);
	}
}
