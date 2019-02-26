package com.feige.iweather;


import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;

import com.feige.animatetest.MainActivity_Sp;
import com.feige.sweather.R;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;


public class Welcome extends Activity {

	private SharedPreferences preferences;  
	private Editor editor;  
	private OutputStream os;  
  
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		JPushInterface.onPause(null);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		JPushInterface.onResume(null);
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		 preferences = getSharedPreferences("phone", Context.MODE_PRIVATE); 
		
		TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);
		new Titanic().start(tv);

		Runnable run = new Runnable() {

			@Override
			public void run() {

				
				   if (preferences.getBoolean("firststart", true)) {  
					    editor = preferences.edit();  
					    editor.putBoolean("firststart", false);  
					    editor.commit();  
					    Intent intent = new Intent();
						intent.setClass(Welcome.this, MainActivity_Sp.class);
						Welcome.this.startActivity(intent);
						Welcome.this.finish();  
						} else {
							 Intent intent = new Intent();
								intent.setClass(Welcome.this, Weather.class);
								Welcome.this.startActivity(intent);
								Welcome.this.finish();  
						}
				   

				
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(run, 8500);
	}

}
