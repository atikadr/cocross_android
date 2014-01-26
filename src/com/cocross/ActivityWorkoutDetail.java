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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
		setContentView(R.layout.activity_workout_detail);
		MainActivity.setActionBar(this, R.layout.actionbar_custom_view_with_back);
		((Button)getActionBar().getCustomView().findViewById(R.id.button_back))
			.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mWorkout = (ParseProxyObject) getIntent().getSerializableExtra(WorkOutList.PROXY_TAG);
		String name = mWorkout.getString("workoutName");
		TextView tv = ((TextView)findViewById(R.id.text_workout_name));
		tv.setText(name);
		((TextView)findViewById(R.id.text_workout_description)).setText(mWorkout.getString("description"));
	}
	
	public void startButtonClicked(View v){
		Log.d("started", "clicked");
		//if(mWorkout.getString("score_type").equals("time")){
		if(true){
			//workout type is time
			Intent startCounterIntent = new Intent(this, ActivityCounter.class);
			startCounterIntent.putExtra("workout", mWorkout);
			startActivity(startCounterIntent);
		}
	}
	
	public void keyInButtonClicked(View v){
		SubmitDialog mDialog = SubmitDialog.newInstance("Dialog Message", mWorkout, "00");
		mDialog.show(getSupportFragmentManager(), "qr_dialog");
	}
	
	public void onDismiss(View v){
		finish();
	}

	public ParseProxyObject getWorkout() {
		return mWorkout;
	}
	

}
