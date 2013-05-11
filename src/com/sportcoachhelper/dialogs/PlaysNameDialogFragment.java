package com.sportcoachhelper.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sportcoachhelper.MainActivity;
import com.sportcoachhelper.R;

public class PlaysNameDialogFragment extends DialogFragment {

	private EditText edv ;
	private Button btnName;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		android.util.Log.d("[OnCreateView]","[OnCreateView]");
		View view  =  inflater.inflate(
                R.layout.plays_name_fragment, container, false);
		
		getDialog().setTitle(R.string.play_name);
		
        edv = (EditText) view.findViewById(R.id.edtPlayName);
        
        btnName = (Button) view.findViewById(R.id.btnName);
        
        btnName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = edv.getText().toString();
				if(name!=null && !name.trim().equals("")) {
					MainActivity activity = (MainActivity)getActivity();
					activity.saveDocument(name);
					dismiss();
				}
			}
		});
        
      return view;
        }
	
}
