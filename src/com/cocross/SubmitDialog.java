package com.cocross;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class SubmitDialog extends DialogFragment {
	String mMessage;
    Dialog mDialog;
    /**
     * Create a new instance of MyDialogFragment, providing "text"
     * as an argument.
     */
    static SubmitDialog newInstance(String msg, String type) {
    	SubmitDialog f = new SubmitDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("msg", msg);
        args.putString("score_type", type);
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
	    View newLogView = inflater.inflate(R.layout.dialog_new_log, null);
		//dismiss button
		newLogView.findViewById(R.id.button_dismiss).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
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
