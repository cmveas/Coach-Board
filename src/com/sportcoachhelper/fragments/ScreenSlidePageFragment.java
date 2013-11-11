package com.sportcoachhelper.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sportcoachhelper.MainActivity;
import com.sportcoachhelper.R;
import com.sportcoachhelper.managers.PlaysManager;
import com.sportcoachhelper.model.Play;
import com.sportcoachhelper.model.Template;
import com.sportcoachhelper.managers.FontManager;
import com.sportcoachhelper.managers.TemplateManager;
import com.sportcoachhelper.util.Utility;

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment {

    public static final String PLAY = "play";
    public static final String TYPE = "type";
    public static final String TYPE_FIELD = "type field";

    private int position;
	private String label;
	private int[] fieldIndexes;
	private ImageView field_type;
	private TextView field_name;
	private ListView playList;
	private PlaysAdapter playsAdapter;
	private ListView templateList;
    private Spinner type_of_field;
    private TypesListAdapter spinnerData
            ;
    private String type;
    private RadioButton btnSaves;
    private RadioButton btnTemplates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		android.util.Log.d("[OnCreateView]","[OnCreateView]");

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.display_field_fragment, container, false);
        fieldIndexes = getActivity().getResources().getIntArray(R.array.fieldsIndexes);
        field_name = (TextView) rootView.findViewById(R.id.field_name);
        type_of_field = (Spinner) rootView.findViewById(R.id.type_of_field);
        field_name.setText(label);
        field_name.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
        field_type = (ImageView) rootView.findViewById(R.id.field_type);
        type = getString(R.string.full);
        setFieldType();
        field_type.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Intent intent = new Intent(getActivity(),MainActivity.class);
					intent.putExtra("field", label);
                     intent.putExtra(TYPE_FIELD, type);
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
        		intent.putExtra(PLAY, play.getId());
        		intent.putExtra(TYPE, "play");
                intent.putExtra(TYPE_FIELD, play.getFieldType());
        		intent.putExtra("field", label);
        		startActivity(intent);
               	}
		});
        
        templateList = (ListView) rootView.findViewById(R.id.templateList);
        templateList.setAdapter(new TemplateAdapter());
        templateList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> paramAdapterView,
        			View paramView, int position, long paramLong) {
        		TemplateAdapter templateAdapter = (TemplateAdapter) paramAdapterView.getAdapter();
        		Template template = (Template) templateAdapter.getItem(position);
        		Intent intent = new Intent(getActivity(),MainActivity.class);
        		intent.putExtra(PLAY, template.getName());
        		intent.putExtra(TYPE, "template");
                intent.putExtra(TYPE_FIELD, getString(R.string.full));
        		intent.putExtra("field", template.getField());
        		startActivity(intent);
        	}
		});
        templateList.setDividerHeight(0);


        btnSaves = (RadioButton) rootView.findViewById(R.id.btnSaves);

        btnSaves.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    enableSaves();
                }
            }
        });
        btnSaves.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));

        btnTemplates = (RadioButton) rootView.findViewById(R.id.btnTemplates);

        btnTemplates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                   disableSaves();
                }
            }
        });

        btnTemplates.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));

        String [] types_of_fields = getResources().getStringArray(R.array.types_of_fields);
        spinnerData = new TypesListAdapter(getActivity(), types_of_fields);
        type_of_field.setAdapter(spinnerData);
        type_of_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type = spinnerData.getItem(position);
                setFieldType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        enableSaves();

        return rootView;
}

    private void disableSaves() {
        templateList.setVisibility(View.VISIBLE);

        playList.setVisibility(View.GONE);
    }

    private void enableSaves() {
        playList.setVisibility(View.VISIBLE);

        templateList.setVisibility(View.GONE);
    }

    private void setFieldType() {
        int resource = getField();

        field_type.setImageBitmap(Utility.decodeBitmapHalfResFromResource(getResources(),resource));
    }


    private int getField() {
		int result = R.drawable.soccer;
		switch(position){
		case 0:
            if(type.equals(getString(R.string.full))) {
                result = R.drawable.soccer;
            } else if(type.equals(getString(R.string.attack_half))) {
                result = R.drawable.soccer_attack_half;
            } else if(type.equals(getString(R.string.defense_half))){
                result = R.drawable.soccer_half;
            }

			break;
		case 1:
            if(type.equals(getString(R.string.full))) {
                result = R.drawable.voley;
            } else if(type.equals(getString(R.string.attack_half))) {
                result = R.drawable.voley_half_inverse;
            } else if(type.equals(getString(R.string.defense_half))){
                result = R.drawable.voley_half;
            }

			break;
		case 2:
            if(type.equals(getString(R.string.full))) {
                result = R.drawable.basket;
            } else if(type.equals(getString(R.string.attack_half))) {
                result = R.drawable.basket_half;
            } else if(type.equals(getString(R.string.defense_half))){
                result = R.drawable.basket_half_inverse;
            }
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
        PlaysManager.getInstance().invalidate(label);
        playsAdapter.notifyDataSetChanged();
		
	}
	
	public class PlaysAdapter extends BaseAdapter {

        private ArrayList<Play> plays;
		private LayoutInflater inflater;

		public PlaysAdapter(){
            inflater = LayoutInflater.from(getActivity());
            initialize();
        }

        private void initialize() {
            plays = PlaysManager.getInstance().getPlaysFromField(label);
        }

        @Override
        public void notifyDataSetChanged() {
            initialize();
            super.notifyDataSetChanged();
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
            TextView type = (TextView) view.findViewById(R.id.play_type);
            type.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
            type.setTextColor(getResources().getColor(android.R.color.white));
            type.setText(plays.get(position).getFieldType());
			
			return view;
		}
		
	}
	
	
	class TemplateAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		private ArrayList<Template> fieldTemplates;

		public TemplateAdapter() {
			inflater = LayoutInflater.from(getActivity());
			ArrayList<Template> allTemplates = TemplateManager.getInstance().getTemplates();
			fieldTemplates = new ArrayList<Template>();
			for (Template template : allTemplates) {
				if(field_name.getText().toString().equals(getString(R.string.soccer))) {
					fieldTemplates.add(template);
				}
			}
		}
		
		@Override
		public int getCount() {		
			return fieldTemplates.size();
		}

		@Override
		public Template getItem(int position) {
			return fieldTemplates.get(position);
		}

		@Override
		public long getItemId(int paramInt) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView,
				ViewGroup paramViewGroup) {
			View view = (View) convertView;
			
			if(view==null) {
				view = inflater.inflate(R.layout.plays_item, null);
			}
			
			TextView name = (TextView) view.findViewById(R.id.play_name);

			name.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
			name.setTextColor(getResources().getColor(android.R.color.white));
			name.setText(getItem(position).getName());


			
			return view;
		}
		
	}


    class TypesListAdapter extends BaseAdapter{

        private final String[] fields;
        private final LayoutInflater inflater;

        public TypesListAdapter(FragmentActivity activity, String[] types_of_fields) {
            fields = types_of_fields;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        @Override
        public int getCount() {
            return fields.length;
        }

        @Override
        public String getItem(int i) {
            return fields[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView text = (TextView) view;
            if(text==null) {
                text = (TextView)  inflater.inflate(R.layout.types_layout_item,null);
            }
            text.setText(getItem(i));
            text.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
            text.setTextColor(getResources().getColor(android.R.color.white));
            return text;
        }
    }
}
