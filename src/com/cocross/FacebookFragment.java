package com.cocross;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class FacebookFragment extends Fragment {

	private static final String TAG = "com.cocross.FacebookFragment";
	
	private UiLifecycleHelper facebookUiHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		facebookUiHelper = new UiLifecycleHelper(getActivity(), callback);
		facebookUiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View theView = inflater.inflate(R.layout.facebook, container, false);
		
		LoginButton authButton = (LoginButton) theView.findViewById(R.id.authButton);
		authButton.setFragment(this);
		
		return theView;
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    Session session = Session.getActiveSession();
	    if(session != null && 
	    		(session.isOpened() || session.isClosed())) {
	    	onSessionStateChange(session, session.getState(), null);
	    }
	    
	    facebookUiHelper.onResume();   
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
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    facebookUiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    facebookUiHelper.onSaveInstanceState(outState);
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	
}
