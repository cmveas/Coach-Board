package com.sportcoachhelper.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sportcoachhelper.MainActivity;
import com.sportcoachhelper.R;
import com.sportcoachhelper.database.DatabaseHelper;
import com.sportcoachhelper.fragments.ScreenSlidePageFragment.PlaysAdapter;
import com.sportcoachhelper.model.Play;
import com.sportcoachhelper.util.FontManager;

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment {

    public static final String PLAY = "play";
	private int position;
	private String label;
	private int[] fieldIndexes;
	private ImageView field_type;
	private TextView field_name;
	private ListView playList;
	private PlaysAdapter playsAdapter;
	private TextView savedPlays;
	private TextView template_list_title;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		android.util.Log.d("[OnCreateView]","[OnCreateView]");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.display_field_fragment, container, false);
        fieldIndexes = getActivity().getResources().getIntArray(R.array.fieldsIndexes);
        field_name = (TextView) rootView.findViewById(R.id.field_name);
        savedPlays = (TextView) rootView.findViewById(R.id.play_list_title);
        template_list_title = (TextView) rootView.findViewById(R.id.template_list_title);
        field_name.setText(label);
        field_name.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
        savedPlays.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
        template_list_title.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
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
        
        playList = (ListView) rootView.findViewById(R.id.playList);
        playList.setAdapter(playsAdapter = new PlaysAdapter());
        playList.setDividerHeight(0);
        playList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> paramAdapterView,
        			View paramView, int position, long paramLong) {
        		Play play = (Play) playsAdapter.getItem(position);
        		Intent intent = new Intent(getActivity(),MainActivity.class);
        		intent.putExtra(PLAY, play.getName());
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
			result = R.drawable.voley;
			break;
		case 2:
			result = R.drawable.basket;
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
	
	@Override
	public void onResume() {
		super.onResume();
		playsAdapter.reloadList(label);
		playsAdapter.notifyDataSetChanged();
		
	}
	
	public class PlaysAdapter extends BaseAdapter {

		private ArrayList<Play> plays;
		private LayoutInflater inflater;

		public PlaysAdapter(){
		
		}

		private void reloadList(String label) {
			plays = new ArrayList<Play>();
			Cursor cursor = DatabaseHelper.getInstance().getPlays(label);
			if(cursor.moveToFirst()) {
				int nameIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_NAME);
				int dateIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_DATE);
				while(!cursor.isAfterLast()){
					String name = cursor.getString(nameIndex);
					long date = cursor.getLong(dateIndex);
					Play temp = new Play();
					temp.setName(name);
					temp.setLastSaved(date);
					plays.add(temp);
					cursor.moveToNext();
				}
			}
			if(cursor!=null && !cursor.isClosed()) {
				cursor.close();
			}
			inflater = LayoutInflater.from(getActivity());
		}
		
		@Override
		public int getCount() {
			int result = 0;
			if(plays!=null) {
				result = plays.size();
			}
			return result;
		}

		@Override
		public Object getItem(int position) {
			return plays.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = (View) convertView;
			
			if(view==null) {
				view = inflater.inflate(R.layout.plays_item, null);
			}
			
			TextView name = (TextView) view.findViewById(R.id.play_name);
			name.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
			name.setTextColor(getResources().getColor(android.R.color.white));
			TextView date = (TextView) view.findViewById(R.id.play_date);
			date.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
			date.setTextColor(getResources().getColor(android.R.color.white));
			name.setText(plays.get(position).getName());
			 DateFormat format = SimpleDateFormat.getDateTimeInstance();
			date.setText(format.format(new Date(plays.get(position).getLastSaved())));
			
			return view;
		}
		
	}
	
}
