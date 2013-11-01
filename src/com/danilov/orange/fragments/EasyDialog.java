package com.danilov.orange.fragments;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danilov.orange.R;

public class EasyDialog extends DialogFragment {
	
	private static final String MESSAGE = "MESSAGE";
	private static final String CURRENT_MSG = "CURRENT_MSG";
	private static final String MESSAGE_LIST = "MESSAGE_LIST";
	
	private List<String> messageList;
	private String message;
	private int currentMsg = 0;
	private TextView messageView;
	private Button buttonClose;
	private Button buttonNext;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.custom_dialog, null);
	    builder.setView(v);
	    messageView = (TextView) v.findViewById(R.id.message);
	    buttonClose = (Button) v.findViewById(R.id.buttonClose);
	    buttonNext = (Button) v.findViewById(R.id.buttonNext);
	    return builder.create();
	}
	
	

}
