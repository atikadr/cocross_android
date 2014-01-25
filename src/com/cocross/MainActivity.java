package com.cocross;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

public class MainActivity extends Activity {
	
	private TableRow logInTableRow;
	private TableRow facebookInfoTableRow;
	
	private LoginButton facebookLoginButton;
	private ProfilePictureView facebookProfilePic;
	private TextView facebookUserName;
	private TextView facebookAge;
	private TextView facebookId;
	
	private Button workOutButton;

	private static final int LOG_IN = 0;
	private static final int FACEBOOK_INFO = 1;

	private boolean isResumed = true;

	private UiLifecycleHelper facebookUiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_fragment);

		facebookUiHelper = new UiLifecycleHelper(this, callback);
		facebookUiHelper.onCreate(savedInstanceState);
		
		logInTableRow = (TableRow) findViewById(R.id.logInTableRow);
		facebookLoginButton = (LoginButton) logInTableRow.findViewById(R.id.authButton);
		facebookLoginButton.setReadPermissions(Arrays
				.asList("user_likes", "user_status"));
		
		facebookInfoTableRow = (TableRow) findViewById(R.id.facebookInfoTableRow);
		facebookProfilePic = (ProfilePictureView) facebookInfoTableRow.findViewById(R.id.facebookProfilePic);
		facebookUserName = (TextView) facebookInfoTableRow.findViewById(R.id.facebookUserName);
		facebookAge = (TextView) facebookInfoTableRow.findViewById(R.id.facebookAge);
		facebookId = (TextView) facebookInfoTableRow.findViewById(R.id.facebookId);
		
		workOutButton = (Button) findViewById(R.id.workOutButton);
		workOutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WorkOutList.class);
				startActivity(intent);
			}
			
		});
		
		Session session = Session.getActiveSession();
		if(session.isOpened()) {
			showView(FACEBOOK_INFO);
		} else {
			showView(LOG_IN);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebookUiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		facebookUiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		facebookUiHelper.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		facebookUiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		facebookUiHelper.onSaveInstanceState(outState);
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {

		if (isResumed) {
			if (state.isOpened()) {
				showView(FACEBOOK_INFO);
			} else if (state.isClosed()) {
				showView(LOG_IN);
			}
		}
	}
	
	private void makeMeRequest(final Session session) {
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (session == Session.getActiveSession()) {
                    if (user != null) {
                    	facebookId.setText(user.getId());
                        facebookProfilePic.setProfileId(user.getId());
                        facebookUserName.setText(user.getName());
                        facebookAge.setText(user.getBirthday());
                    }
                }
                if (response.getError() != null) {
                   
                }
            }
        });
        request.executeAsync();
	}
	
	private void showView(int id) {
		if(id == LOG_IN) {
			facebookInfoTableRow.setVisibility(View.GONE);
		} else {
			logInTableRow.setVisibility(View.GONE);
			Session session = Session.getActiveSession();
			if(session.isOpened()) {
				makeMeRequest(session);
			}
		}
	}

}
