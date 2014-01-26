package com.cocross;

import java.util.Date;

import com.cocross.utils.ParseProxyObject;
import com.cocross.utils.Queries;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitDialog extends DialogFragment {
	String mMessage;
    Dialog mDialog;
    /**
     * Create a new instance of MyDialogFragment, providing "text"
     * as an argument.
     */
    static SubmitDialog newInstance(String msg, ParseProxyObject mWorkout, String score) {
    	SubmitDialog f = new SubmitDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("msg", msg);
        args.putSerializable("workout", mWorkout);
        args.putString("score", score);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString("msg");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("msg");
        mDialog = new AlertDialog.Builder(getActivity()).create();
        
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View newLogView = inflater.inflate(R.layout.dialog_new_log, null);
	    
	    //set score
	    String score = getArguments().getString("score");
	    final ParseProxyObject workout = (ParseProxyObject) getArguments().getSerializable("workout");
	    if(score!=null)
	    ((EditText)newLogView.findViewById(R.id.field_score)).setText(score);
	    //submit button
	    newLogView.findViewById(R.id.button_submit).setOnClickListener(
	    		new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Date workoutTime = new Date();
						EditText t;
						int score  = Integer.parseInt(((EditText)newLogView.findViewById(R.id.field_score)).getText().toString());
						String comment  = ((EditText)newLogView.findViewById(R.id.field_comment)).getText().toString();
						Queries.submitLogAndGetRanking(workoutTime, null, score, 
								comment, workout,
								new CountCallback() {
									@Override
									public void done(int count, com.parse.ParseException e) {
										// TODO Auto-generated method stub
										if (e == null){
											dismiss();
											Toast.makeText(getActivity(), "Ranking: "+ (count+1), Toast.LENGTH_LONG).show();
											Log.d("my rank!!!", "my ranking: " + (count + 1));
										}
										else {
											//Log.d("query test", "Error " + e.getMessage());
										}
									}
								});
					}
				});;
		//dismiss button
//		newLogView.findViewById(R.id.button_dismiss).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						dismiss();
//					}
//				});
        ((AlertDialog) mDialog).setView(newLogView);
        return mDialog;
    }
    

	@Override
	public void onDismiss(DialogInterface dialog) {
		//TODO: Add facebook share prompt on submit success
		//if(fbChecked && mType!=null)
		//	((ActivityLanding)getActivity()).publishStory(mType);
		super.onDismiss(dialog);
		
	}
    
    
}
