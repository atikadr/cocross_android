package com.cocross;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WorkOutList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_out_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.work_out_list, menu);
		return true;
	}

}