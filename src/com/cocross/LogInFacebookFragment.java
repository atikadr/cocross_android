/*package com.cocross;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.widget.LoginButton;

public class LogInFacebookFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View theView = inflater.inflate(R.layout.login_facebook, container, false);

		LoginButton authButton = (LoginButton) theView
				.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays
				.asList("user_likes", "user_status"));
		
		return theView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session session = Session.getActiveSession();
		if(session.isOpened()) {
			Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
		}
	}
	
	
}
*/