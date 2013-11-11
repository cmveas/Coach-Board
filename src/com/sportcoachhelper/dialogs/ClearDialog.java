package com.sportcoachhelper.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.sportcoachhelper.MainActivity;
import com.sportcoachhelper.R;

public class ClearDialog extends DialogFragment {

	private Button btnYes;
	private Button btnNo;

	public ClearDialog() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_clear_board, container);
	
		getDialog().setTitle(R.string.sure_want_clear);
		
		btnYes = (Button) view.findViewById(R.id.btn_yes);
		btnNo = (Button) view.findViewById(R.id.btn_no);
		
		btnYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity activity = (MainActivity)getActivity();
				activity.onFinishClearDialog(true);
				dismiss();
			}
		});
		
		btnNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity activity = (MainActivity)getActivity();
				activity.onFinishClearDialog(false);
				dismiss();
			}
		});
		
		return view;
	}

}