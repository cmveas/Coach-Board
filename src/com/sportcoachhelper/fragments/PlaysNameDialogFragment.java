package com.sportcoachhelper.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sportcoachhelper.R;

public class PlaysNameDialogFragment extends DialogFragment {

	private EditText edv ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		android.util.Log.d("[OnCreateView]","[OnCreateView]");
		edv = (EditText) inflater.inflate(
                R.layout.plays_name_fragment, container, false);
        
      return edv;
        }
	
}
