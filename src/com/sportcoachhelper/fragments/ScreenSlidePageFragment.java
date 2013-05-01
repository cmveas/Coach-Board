package com.sportcoachhelper.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportcoachhelper.MainActivity;
import com.sportcoachhelper.R;
import com.sportcoachhelper.util.FontManager;

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment {

    private int position;
	private String label;
	private int[] fieldIndexes;
	private ImageView field_type;
	private TextView field_name;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		android.util.Log.d("[OnCreateView]","[OnCreateView]");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.display_field_fragment, container, false);
        fieldIndexes = getActivity().getResources().getIntArray(R.array.fieldsIndexes);
        field_name = (TextView) rootView.findViewById(R.id.field_name);
        field_name.setText(label);
        field_name.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
        field_type = (ImageView) rootView.findViewById(R.id.field_type);
        field_type.setImageResource(getField());
        field_type.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Intent intent = new Intent(getActivity(),MainActivity.class);
					intent.putExtra("field", label);
					startActivity(intent);
			}
		});
        return rootView;
    }

	private int getField() {
		int result = R.drawable.soccer;
		switch(position){
		case 0:
			result = R.drawable.soccer;
			break;
		case 1:
			result = R.drawable.basket;
			break;
		case 2:
			result = R.drawable.voley;
			break;
		}
		return result;
	}

	public void setPosition(int position) {
		this.position=position;	
	}

	public void setLabel(String label) {
		this.label=label;
		
	}
}
