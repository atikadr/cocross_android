package com.cocross;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Activity to start a new log
 * @author kaka
 *
 */
public class ActivityNewLog extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_log);
	}
	
	public void startButtonClicked(View v){
		Log.d("started", "clicked");
	}
	
	public void keyInButtonClicked(View v){
		
	}
	

}
