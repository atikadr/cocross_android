package com.cocross;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class ActivityCounter extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
	}
	
	public void onFinishClicked(View v){
		SubmitDialog mDialog = SubmitDialog.newInstance("Dialog Message", "AMRAP");
		mDialog.show(getSupportFragmentManager(), "qr_dialog");
	}
	

}
