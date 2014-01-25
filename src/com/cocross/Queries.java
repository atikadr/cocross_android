package com.example.cocross;

import java.util.Date;
import java.util.List;

import android.net.ParseException;
import android.util.Log;
import android.view.View;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
/**
 * 
 * @author LENOVO
 *
 */
public class Queries {
	public void submitLogAndGetRanking(	
			final Date workoutTime,
			final String description,
			final boolean isPR,
			final int score,
			final ParseObject workout,
			CountCallback ccb
			){

		ParseObject logs = new ParseObject("Logs");
		logs.put("score", score);
		logs.put("description", description);
		logs.put("workoutTime", workoutTime);
		logs.put("workout", workout);
		logs.put("createdBy", ParseUser.getCurrentUser());
		
		ParseObject PR = null;
		
		ParseQuery<ParseObject> mainQuery = ParseQuery.getQuery("Logs");
		mainQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser());
		mainQuery.whereEqualTo("workout", workout);
		mainQuery.whereEqualTo("isPR", true);
		try {
			PR = mainQuery.getFirst();
		} catch (com.parse.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (PR == null || PR.getInt("score") < score){
			logs.put("isPR", true);
		}

		logs.saveInBackground();
		
		getMyRanking(logs, ccb);
		
	}
	
	/**
	 * ccb needs to implement done() where count returned is handled
	 * 
	 * 		
		 * mainQuery.countInBackground(new CountCallback() {			
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
	 * 
	 * @param myScore
	 * @param workoutName
	 * @param ccb
	 */
	public void getMyRanking(ParseObject logs, CountCallback ccb){
		
		int myRank;
		
		ParseQuery<ParseObject> mainQuery = ParseQuery.getQuery("Logs");

		mainQuery.whereEqualTo("workout", logs.get("workout"));
		mainQuery.whereEqualTo("gender", logs.get("gender"));
		mainQuery.whereEqualTo("isPR", true); 
		mainQuery.whereGreaterThan("score", logs.get("score"));

		mainQuery.countInBackground(ccb);
		

	}
	
	public void getMyLogs10(){
		ParseQuery<ParseObject> mainQuery = ParseQuery.getQuery("Logs");
		mainQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser());
		mainQuery.addDescendingOrder("workoutTime");
		mainQuery.setLimit(10);
		
		mainQuery.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> logList, ParseException e) {
		    }
		    
			@Override
			public void done(List<ParseObject> logList,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				
		        if (e == null) {
		            Log.d("query test", "Retrieved " + logList.size() + " logs");
		        } else {
		            Log.d("query test", "Error: " + e.getMessage());
		        }
		    }	
		});
	}

	public void getBoxes(){
		ParseQuery<ParseObject> mainQuery = ParseQuery.getQuery("Box");
	}

}
