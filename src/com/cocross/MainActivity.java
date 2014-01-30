package com.cocross;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
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
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends Activity {
	
	private TableRow logInTableRow;
	private TableRow welcomeViewGroup;
	
	private LoginButton facebookLoginButton;
	private ProfilePictureView facebookProfilePic;
	private TextView facebookUserName;
	
	private Button workOutButton;

	private static final int LOG_IN = 0;
	private static final int FACEBOOK_INFO = 1;

	private boolean isResumed = true;

	private UiLifecycleHelper facebookUiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, "tIV7myvZXkFyeUe4uP6vUg889npZQX2es8LO7AKv", "rPAcfcivzZJjw0nlcPGske1oG4EDraPP11cngjr7");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		final ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_view_home);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.hide();
		facebookUiHelper = new UiLifecycleHelper(this, callback);
		facebookUiHelper.onCreate(savedInstanceState);
		
		logInTableRow = (TableRow) findViewById(R.id.logInTableRow);
		facebookLoginButton = (LoginButton) logInTableRow.findViewById(R.id.authButton);
		facebookLoginButton.setReadPermissions(Arrays
				.asList("user_likes", "user_status", "email"));
		
		welcomeViewGroup = (TableRow) findViewById(R.id.welcome_row);
		facebookProfilePic = (ProfilePictureView) actionBar.getCustomView().findViewById(R.id.facebookProfilePic);
		facebookUserName = (TextView) actionBar.getCustomView().findViewById(R.id.facebookUserName);
		
		final PopupMenu menu = new PopupMenu(this, facebookProfilePic);
		menu.inflate(R.menu.facebook);
		
		facebookProfilePic.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				menu.show();	
			}
			
		});
		
		menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Session session = Session.getActiveSession();
				if(session.isOpened()) {
					session.closeAndClearTokenInformation();
				}
				return false;
			}
		});
		
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
			populateWorkoutListView();
		} else {
			showView(LOG_IN);
		}

	}

	private void populateWorkoutListView() {
		
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
				getActionBar().show();
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
                        facebookProfilePic.setProfileId(user.getId());
                        facebookUserName.setText(user.getName());
                        if(ParseUser.getCurrentUser()==null){
	                        final ParseUser pUser = new ParseUser();
	                        pUser.setUsername(user.getName());
	                        pUser.setPassword("password");
	                        pUser.setEmail(user.getProperty("email").toString());
	                        pUser.put("facebook_id", Integer.parseInt(user.getId()));
	                        pUser.put("gender", user.getProperty("gender").toString().equals("male"));
	                        if(user.getBirthday()!=null)
	                        	pUser.put("age", getAge(user.getBirthday()));
	                        
	                        SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
							sp.edit().putString("username", user.getName()).commit();
							sp.edit().putString("facebook_id", user.getId()).commit();
	                        
	                        pUser.signUpInBackground(new SignUpCallback() {
								@Override
								public void done(com.parse.ParseException e) {
									if(e!=null) Log.d("error logging in", e.getLocalizedMessage());
									else {Log.d("Yay", "Signed-up!");
										ParseUser.logInInBackground(pUser.getUsername(), "password", new LogInCallback() {
											@Override
											public void done(final ParseUser user, com.parse.ParseException e) {
												if(e!=null) Log.d("error logging in", e.getLocalizedMessage());
												else{
													Log.d("Parse login", "YAY" + user.getUsername());
													if (!ParseFacebookUtils.isLinked(user)) {
														  ParseFacebookUtils.link(user, MainActivity.this, new SaveCallback() {
														    @Override
														    public void done(com.parse.ParseException ex) {
														      if (ParseFacebookUtils.isLinked(user)) {
														        Log.d("MyApp", "Woohoo, user logged in with Facebook!");
														      }
														    }
														  });
														}
												}
											}
										});
									}
								}
							});
                        }
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
			welcomeViewGroup.setVisibility(View.GONE);
			logInTableRow.setVisibility(View.VISIBLE);
		} else {
			logInTableRow.setVisibility(View.GONE);
			welcomeViewGroup.setVisibility(View.VISIBLE);
			Session session = Session.getActiveSession();
			if(session.isOpened()) {
				makeMeRequest(session);
			}
		}
	}
	
	private int getAge(String birthDateString) {
		
		Date birthDate = null;
		
		try {
			birthDate = new SimpleDateFormat("MM/DD/yyyy", Locale.ENGLISH).parse(birthDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar dob = Calendar.getInstance();  
		dob.setTime(birthDate);  
		Calendar today = Calendar.getInstance();  
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
		  age--;  
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
		    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
		  age--;  
		}
		
		return age;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    //MenuInflater inflater = getMenuInflater();
	    //inflater.inflate(R.menu.action_bar, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Helper function to setup actionbar
	 * need to be logged-in
	 * @param activity
	 * @param actionbarLayoutID
	 */
	public static void setActionBar(final Activity activity, final int actionbarLayoutID) {
		//set actionbar
		final ActionBar actionBar = activity.getActionBar();
        actionBar.setCustomView(actionbarLayoutID);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        if(actionbarLayoutID ==  R.layout.actionbar_custom_view_with_back){
        	((Button)actionBar.getCustomView().findViewById(R.id.button_back))
        	.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.finish();
				}
			});
        }
        SharedPreferences sp = activity.getSharedPreferences("credentials", MODE_PRIVATE);
       // if(ParseUser.getCurrentUser()!=null){
	        ProfilePictureView facebookProfilePic = (ProfilePictureView) actionBar.getCustomView().findViewById(R.id.facebookProfilePic);
	        if(facebookProfilePic!=null)
	        	facebookProfilePic.setProfileId(sp.getString("facebook_id", null));
	        TextView facebookUserName = (TextView) actionBar.getCustomView().findViewById(R.id.facebookUserName);
	        if(facebookUserName!=null)
	        	facebookUserName.setText(sp.getString("username", "CoCross"));
       // }
	}

}
