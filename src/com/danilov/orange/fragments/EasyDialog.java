package com.danilov.orange.fragments;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.danilov.orange.R;

public class EasyDialog extends DialogFragment {
	
	private static final String MESSAGE = "MESSAGE";
	private static final String CURRENT_MSG = "CURRENT_MSG";
	private static final String MESSAGE_LIST = "MESSAGE_LIST";
	
	private List<String> messageList = null;
	private String message = null;
	private int currentMsg = 0;
	private TextView messageView;
	private Button buttonClose;
	private Button buttonNext;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(savedInstanceState != null){
			restoreSavedInstanceState(savedInstanceState);
		}
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.custom_dialog, null);
	    builder.setView(v);
	    messageView = (TextView) v.findViewById(R.id.message);
	    buttonClose = (Button) v.findViewById(R.id.buttonClose);
	    buttonNext = (Button) v.findViewById(R.id.buttonNext);
	    buttonClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	    if (message != null) {
	    	processSingleMessageDialog();
	    } else {
	    	processMultiMessageDialog();
	    }
	    return builder.create();
	}
	
	public void setMessageList(final List<String> messageList) {
		this.messageList = messageList;
	}
	
	public void setMessage(final String message) {
		this.message = message;
	}
	
	private void processSingleMessageDialog() {
		messageView.setText(message);
		buttonNext.setVisibility(View.GONE);
	}
	
	private void processMultiMessageDialog() {
		int msgListSize = messageList.size();
		if (msgListSize > currentMsg) {
			buttonNext.setVisibility(View.VISIBLE);
			buttonNext.setOnClickListener(new OnButtonNextClickListener());
			String msg = messageList.get(currentMsg);
			messageView.setText(msg);
			onMessageChanged();
		}
	}
	
	private void onMessageChanged() {
		int msgListSize = messageList.size();
		if (currentMsg >= msgListSize - 1) {
			buttonNext.setVisibility(View.GONE);
		}
	}
	
	private class OnButtonNextClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			currentMsg++;
			if (currentMsg < messageList.size()) {
				String msg = messageList.get(currentMsg);
				messageView.setText(msg);
				onMessageChanged();
			} else {
				buttonNext.setVisibility(View.GONE);
			}
		}
		
	}
	
	private void restoreSavedInstanceState(Bundle savedInstanceState){
		message = savedInstanceState.getString(MESSAGE);
		String[] array = savedInstanceState.getStringArray(MESSAGE_LIST);
		messageList = Arrays.asList(array);
		currentMsg = savedInstanceState.getInt(CURRENT_MSG, 0);
	}
	
	@Override
	public void onSaveInstanceState(Bundle saved){
		saved.putString(MESSAGE, message);
		if (messageList != null) {
			String[] array = messageList.toArray(new String[messageList.size()]);
			saved.putStringArray(MESSAGE_LIST, array);
			saved.putInt(CURRENT_MSG, currentMsg);
		}
		if (message != null) {
			saved.putString(MESSAGE, message);
		}
		super.onSaveInstanceState(saved);
	}

}
