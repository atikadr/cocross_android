package com.cocross;

import com.cocross.utils.ParseProxyObject;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

/**
 * Activity to show details of a workout, and start a new log for that
 * @author kaka
 *
 */
public class ActivityWorkoutDetail extends FragmentActivity{
	public final static String KEY_WORKOUT = "workout";
	private ParseProxyObject mWorkout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWorkout = (ParseProxyObject) getIntent().getSerializableExtra(KEY_WORKOUT);
		setContentView(R.layout.activity_workout_detail);
	}
	
	public void startButtonClicked(View v){
		Log.d("started", "clicked");
		//if(mWorkout.getString("score_type").equals("time")){
		if(true){
			//workout type is time
			Intent startCounterIntent = new Intent(this, ActivityCounter.class);
			startActivity(startCounterIntent);
		}
	}
	
	public void keyInButtonClicked(View v){
		SubmitDialog mDialog = SubmitDialog.newInstance("Dialog Message", "AMRAP");
		mDialog.show(getSupportFragmentManager(), "qr_dialog");
	}

	public ParseProxyObject getWorkout() {
		return mWorkout;
	}
	

}
