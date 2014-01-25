package com.cocross;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class Facebook extends Activity {

	ParseUser facebookUser;
	
	private static final String FACEBOOK_APP_ID = "252904941538409";
	private static final String PARSE_APPLICATION_ID = "UxxptgHpkSjVgl8z9QWv6T2EoiaOzsk7sT3RNjWS";
	private static final String PARSE_CLIENT_KEY = "PIQIEM0IGMUPEmcTQ1IYqwQqp7pNWsp9Ug5XtZkG";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook);
		
		 Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
		
		ParseFacebookUtils.initialize(FACEBOOK_APP_ID);
		
		ParseFacebookUtils.logIn(this, new LogInCallback(){

			@Override
			public void done(ParseUser user, ParseException e) {
				if(user == null) {
					
				} else if(user.isNew()) {
					
				} else {
					
				}
			}
			
		});
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
