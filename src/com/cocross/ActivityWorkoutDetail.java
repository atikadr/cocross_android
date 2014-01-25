package com.cocross;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

/**
 * Activity to start a new log
 * @author kaka
 *
 */
public class ActivityWorkoutDetail extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout_detail);
	}
	
	public void startButtonClicked(View v){
		Log.d("started", "clicked");
	}
	
	public void keyInButtonClicked(View v){
		SubmitDialog mDialog = SubmitDialog.newInstance("Dialog Message", "AMRAP");
		mDialog.show(getSupportFragmentManager(), "qr_dialog");
		
	}
	

}
