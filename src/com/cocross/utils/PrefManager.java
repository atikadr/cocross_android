package com.cocross.utils;

import com.parse.ParseUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
	SharedPreferences mPref;
	PrefManager mInstance;
	public PrefManager(Context c){
		if(mPref!=null) mPref = c.getSharedPreferences("com.cocross.pref", Context.MODE_PRIVATE); 
	}
	public PrefManager getInstance(Context c){
		if (mInstance ==null){
			mInstance = new PrefManager(c);
		}
		return mInstance;
	}

	public PrefManager getInstance(){
		return mInstance;
	}
	
	public void login(ParseUser user){
		Editor editor = mPref.edit();
		editor.putString("username", user.getUsername());
		editor.putString("facebook_id", (String) user.get("facebook_id"));
		editor.putBoolean("gender", user.getBoolean("gender"));
		editor.putString("email", (String)user.getString("email"));
		editor.commit();
	}
	
	public ParseUser getCurrentUser(){
		ParseUser user = new ParseUser();
		user.put("gender", mPref.getBoolean("gender", true));
		user.setEmail(mPref.getString("email", null));
		user.setUsername(mPref.getString("username", null));
		user.put("facebook_id", mPref.getString("facebook_id", null));
		return user;
	}
}
