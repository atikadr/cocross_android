package com.cocross;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class Facebook extends FragmentActivity {

	ParseUser facebookUser;

	private Fragment facebookFragment;

	private static final String PARSE_APPLICATION_ID = "UxxptgHpkSjVgl8z9QWv6T2EoiaOzsk7sT3RNjWS";
	private static final String PARSE_CLIENT_KEY = "PIQIEM0IGMUPEmcTQ1IYqwQqp7pNWsp9Ug5XtZkG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

		facebookFragment = getSupportFragmentManager().findFragmentById(
				android.R.id.content);

		if (facebookFragment == null) {
			facebookFragment = new FacebookFragment();

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, facebookFragment).commit();
			
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facebook, menu);
		return true;
	}

}
