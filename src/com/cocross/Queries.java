package com.cocross;

import java.util.Date;

import android.net.ParseException;
import android.util.Log;
import android.view.View;

import com.parse.CountCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Queries {
	public void submitLog(		
			String description,
			boolean isPR,
			int score,
			String workoutName,
			Date date,
			boolean gender){

		ParseObject logs = new ParseObject("logs");
		logs.put("score", score);
		logs.put("isPR", isPR);
		logs.put("description", description);
		logs.put("workoutTime", date);
		logs.put("workoutName", workoutName);
		logs.put("gender", gender);
		logs.put("createdBy", ParseUser.getCurrentUser());
		
		logs.saveInBackground();
		
		ParseQuery<ParseObject> mainQuery = ParseQuery.getQuery("logs");
		mainQuery.whereEqualTo("workoutName", workoutName);
		mainQuery.countInBackground(new CountCallback() {			
			public void done(int count, ParseException e) {
			}

			@Override
			public void done(int count, com.parse.ParseException e) {
				// TODO Auto-generated method stub
				if (e == null){
					if (count != 0)
						getMyRanking();
				}
				else {
					Log.d("query test", "Error " + e.getMessage());
				}
			}
		});
	}
	
	public void getMyRanking(){
		
		int myScore = 5;
		String workoutName = "13.1";
		boolean gender = true;
		
		ParseQuery<ParseObject> mainQuery = ParseQuery.getQuery("logs");

		mainQuery.whereEqualTo("workoutName", workoutName);
		mainQuery.whereEqualTo("gender", gender);
		mainQuery.whereEqualTo("isPR", true); 
		mainQuery.whereGreaterThan("score", myScore);

		
		mainQuery.countInBackground(new CountCallback() {			
			public void done(int count, ParseException e) {
			}

			@Override
			public void done(int count, com.parse.ParseException e) {
				// TODO Auto-generated method stub
				if (e == null){
					Log.d("query test", "my ranking: " + (count + 1));
				}
				else {
					Log.d("query test", "Error " + e.getMessage());
				}
			}
		});
	}

}
