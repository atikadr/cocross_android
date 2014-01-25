/*package com.cocross;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class FacebookInfoFragment extends Fragment {
		
	private ProfilePictureView facebookProfilePic;
	private TextView facebookUserName;
	private TextView facebookAge;
	private TextView facebookId;
	
	private UiLifecycleHelper uiLifeHelper;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    
    private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
    	if (session != null && session.isOpened()) {
            if (!state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
                makeMeRequest(session);
            }
        } else {
        	facebookId.setText("");
            facebookProfilePic.setProfileId(null);
            facebookUserName.setText("");
            facebookAge.setText("");
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiLifeHelper = new UiLifecycleHelper(getActivity(), callback);
		uiLifeHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View theView = inflater.inflate(R.layout.facebook_info, container, false);
		
		facebookId = (TextView) theView.findViewById(R.id.facebookId);
		
		facebookProfilePic = (ProfilePictureView) theView.findViewById(R.id.facebookProfilePic);
		facebookProfilePic.setCropped(true);
		
		facebookUserName = (TextView) theView.findViewById(R.id.facebookUserName);
		
		facebookAge = (TextView) theView.findViewById(R.id.facebookAge);
		
		return theView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	    uiLifeHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	    uiLifeHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	    uiLifeHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiLifeHelper.onSaveInstanceState(outState);
	}
}
*/