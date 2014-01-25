package com.cocross;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class Facebook extends FragmentActivity {

	private LogInFacebookFragment logInFragment;
	private FacebookInfoFragment facebookInfoFragment;

	private static final int LOG_IN = 0;
	private static final int FACEBOOK_INFO = 1;
	
	private boolean isResumed = true;
	
	private UiLifecycleHelper facebookUiHelper;

	//private static final String PARSE_APPLICATION_ID = "UxxptgHpkSjVgl8z9QWv6T2EoiaOzsk7sT3RNjWS";
	//private static final String PARSE_CLIENT_KEY = "PIQIEM0IGMUPEmcTQ1IYqwQqp7pNWsp9Ug5XtZkG";
	private static final String TAG = "com.cocross.FacebookFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_fragment);
		
		facebookUiHelper = new UiLifecycleHelper(this, callback);
		facebookUiHelper.onCreate(savedInstanceState);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		logInFragment = (LogInFacebookFragment) fragmentManager.findFragmentById(R.id.facebookFrameContainer);
		if(logInFragment == null) {
			logInFragment = new LogInFacebookFragment();
			fragmentManager.beginTransaction().add(R.id.facebookFrameContainer, logInFragment);
		}
		
		facebookInfoFragment = (FacebookInfoFragment) fragmentManager.findFragmentById(R.id.facebookFrameContainer);
		if(facebookInfoFragment == null) {
			facebookInfoFragment = new FacebookInfoFragment();
		}
		
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.hide(facebookInfoFragment);
		transaction.hide(logInFragment);
		
		transaction.commit();
	}
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
	    
		FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
	    
	    transaction.hide(logInFragment);
    	transaction.hide(facebookInfoFragment);
	    
	    if(fragmentIndex == LOG_IN) {
	    	transaction.show(logInFragment);
	    } else {
	    	transaction.show(facebookInfoFragment);
	    }
	    	
	    	
	    if (addToBackStack) {
	        transaction.addToBackStack(null);
	    }
	    transaction.commit();
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
	        FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        if (state.isOpened()) {
	            // If the session state is open:
	            // Show the authenticated fragment
	            showFragment(FACEBOOK_INFO, false);
	        } else if (state.isClosed()) {
	            // If the session state is closed:
	            // Show the login fragment
	            showFragment(LOG_IN, false);
	        }
	    }
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	        // if the session is already open,
	        // try to show the selection fragment
	        showFragment(FACEBOOK_INFO, false);
	    } else {
	        // otherwise present the splash screen
	        // and ask the person to login.
	        showFragment(LOG_IN, false);
	    }
	}


}
