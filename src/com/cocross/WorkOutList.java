package com.cocross;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cocross.utils.ParseProxyObject;
import com.cocross.utils.Queries;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class WorkOutList extends ListActivity {

	WorkOutAdapter adapter;
	ArrayList<ParseObject> mWorkOutList;
	
	public final static String PROXY_TAG = "com.cocross.PARSE_PROXY_OBJECT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_out_list);
		
		MainActivity.setActionBar(this, R.layout.actionbar_custom_view_home);
        

		//TODO : change workouts to workOutList ParseObject
		List<ParseObject> workOutList = Queries.getWorkoutList();
		mWorkOutList = (ArrayList<ParseObject>) workOutList;
		adapter = new WorkOutAdapter(mWorkOutList);
		
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent theIntent = new Intent(getApplication(), ActivityWorkoutDetail.class);
		
		ParseObject clickedObject = ((WorkOutAdapter) getListAdapter()).getItem(position);
		ParseProxyObject ppo = new ParseProxyObject(clickedObject);
		theIntent.putExtra(PROXY_TAG, ppo);
		
		startActivityForResult(theIntent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.action_bar, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    int id = item.getItemId();
	    
	    if(id == R.id.action_search) {
	    	return true;
	    } else if(id == R.id.action_settings) {
	        return true;
	    } else {
	        return super.onOptionsItemSelected(item);
	    }
	}


	private class WorkOutAdapter extends ArrayAdapter<ParseObject> {

		private static final int DESC_MAX_LENGTH = 45;

		public WorkOutAdapter(ArrayList<ParseObject> workOutList) {
			super(getApplication(), android.R.layout.simple_list_item_1, workOutList);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.work_out_entry, null);
			}
			
			ParseObject object = ((WorkOutAdapter) getListAdapter()).getItem(position);
			
			TextView workOutNameTextView = (TextView) convertView.findViewById(R.id.workOutNameTextView);
			workOutNameTextView.setText(object.getString("workoutName"));
			
			TextView workOutDescriptionTextView = (TextView) convertView.findViewById(R.id.workOutDescriptionTextView);
			String shortcutText = getShortcut(object.getString("description"));
			workOutDescriptionTextView.setText(shortcutText);
			
			TextView workOutScoreTextView = (TextView) convertView.findViewById(R.id.workOutScoreTextView);
			workOutScoreTextView.setText(object.getString("scoreType"));
			
			return convertView;
		}

		private String getShortcut(String string) {
			if(string.length() <= DESC_MAX_LENGTH)
				return string;
			else
				return string.substring(0, DESC_MAX_LENGTH-3) + "...";
		}
	}

}
