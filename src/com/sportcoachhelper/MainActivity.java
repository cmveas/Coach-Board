package com.sportcoachhelper;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sportcoachhelper.components.DrawingView;
import com.sportcoachhelper.database.DatabaseHelper;
import com.sportcoachhelper.dialogs.ClearDialog;
import com.sportcoachhelper.dialogs.PlaysNameDialogFragment;
import com.sportcoachhelper.interfaces.OnComponentSelectedListener;
import com.sportcoachhelper.model.Team;
import com.sportcoachhelper.paths.ColorPath;
import com.sportcoachhelper.util.TeamManager;

public class MainActivity extends GraphicsActivity implements OnComponentSelectedListener  {

	private static final int PICKFILE_RESULT_CODE = 0;
	private DrawingView drawingView;
	private ImageView playerImage;
	private ImageView triangleTool;
	private ImageView squareTool;
	private ImageView ballTool;
	private EditText playerNumber;
	private Button playerNumberButton;
	private ImageView playerImage2;
	private ImageView triangleTool2;
	private ImageView squareTool2;
	private Button playerNumberButton2;
	private EditText playerNumber2;
	private String field;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		drawingView = (DrawingView) findViewById(R.id.drawingPanel);

		/*
		 * Team A Toolbar
		 */
		
		playerImage = (ImageView) findViewById(R.id.playerTool);
		triangleTool = (ImageView) findViewById(R.id.triangleTool);
		squareTool = (ImageView) findViewById(R.id.squareTool);
		ballTool = (ImageView) findViewById(R.id.ballTool);
		playerNumberButton = (Button) findViewById(R.id.playerNumberButton);
		playerNumber = (EditText) findViewById(R.id.playerNumber);
		
		
		playerNumberButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String number = playerNumber.getText().toString();
				if(!number.trim().equals("")) {
					drawingView.setLabel(number);
				}
				
			}
		});
		
		
		
		/*
		 * Team B Toolbar
		 */
		
		playerImage2 = (ImageView) findViewById(R.id.playerTool2);
		triangleTool2 = (ImageView) findViewById(R.id.triangleTool2);
		squareTool2 = (ImageView) findViewById(R.id.squareTool2);
		playerNumber2 = (EditText) findViewById(R.id.playerNumber2);
		playerNumberButton2 = (Button) findViewById(R.id.playerNumberButton2);
		
		playerNumberButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String number = playerNumber2.getText().toString();
				if(!number.trim().equals("")) {
					drawingView.setLabel(number);
				}
				
			}
		});
		
		Intent intent = getIntent();
		field  = intent.getStringExtra("field");
		drawingView.setField(field);

		manageDrag(field);
		
		drawTheAppropiateBall(field);
		
	    drawingView.setOnComponentSelectedListener(this);	    	  
	    
	    checkForFullTools();
	}

	private void checkForFullTools() {
		boolean fullTools = fullTools(field);
	    triangleTool.setVisibility((fullTools?View.VISIBLE:View.GONE));
	    triangleTool2.setVisibility((fullTools?View.VISIBLE:View.GONE));
	    squareTool.setVisibility((fullTools?View.VISIBLE:View.GONE));
	    squareTool2.setVisibility((fullTools?View.VISIBLE:View.GONE));
	}

	private void drawTheAppropiateBall(String field) {
		int resource = giveMeFieldBall(field);
		
		ballTool.setImageResource(resource);
	}

	private int giveMeFieldBall(String field) {
		int resource = -1;
		final String volley = getString(R.string.voley);
		final String soccer = getString(R.string.soccer);
		final String basket = getString(R.string.basketball);
		if (field.equals(soccer)) {
			resource = R.drawable.soccerball;
		} else if (field.equals(basket)) {
			resource = R.drawable.basketball;
		} else if (field.equals(volley)) {
			resource = R.drawable.volleyball;
		}
		return resource;
	}
	
	private boolean fullTools(String field) {
		boolean result = false;
		final String volley = getString(R.string.voley);
		final String soccer = getString(R.string.soccer);
		final String basket = getString(R.string.basketball);
		if (field.equals(soccer)) {
			result = false;
		} else if (field.equals(basket)) {
			result = false;
		} else if (field.equals(volley)) {
			result = true;
		}
		return result;
	}
	
	

	@SuppressLint("NewApi")
	private void manageDrag(final String field) {
		OnDragListener dragListener = new OnDragListener() {

			@Override
			public boolean onDrag(View paramView, DragEvent paramDragEvent) {
				switch (paramDragEvent.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					break;
				case DragEvent.ACTION_DROP:
					// Dropped, reassign View to ViewGroup
					int x = (int) paramDragEvent.getX();
					int y = (int) paramDragEvent.getY();

					ClipData data = paramDragEvent.getClipData();

					String label = data.getDescription().getLabel().toString();
					checkDataType(x, y, label);
					paramView.setVisibility(View.VISIBLE);
					playerImage.setVisibility(View.VISIBLE);
					break;
				}
				android.util.Log.d("[DRAG ACTION]",
						"Event:" + paramDragEvent.getAction());
				return true;
			}

			private void checkDataType(int x, int y, String label) {
				String data[] = label.split(",");
				label = data[0];
				int team = Integer.parseInt(data[1]);
				if (label.equals("player")) {
					drawingView.setCirclePlayer(x, y,team);
				} else if (label.equals("triangle")) {
					drawingView.setTrianglePlayer(x, y,team);
				}else if (label.equals("square")) {
					drawingView.setSquarePlayer(x, y,team);
				} else if(label.equals("ball")){
					drawingView.setBall(x, y,team,giveMeFieldBall(field));
				}
			}
		};

		drawingView.setOnDragListener(dragListener);

		Team teamA = TeamManager.getInstance().getTeamA();
		Team teamB = TeamManager.getInstance().getTeamB();
		
		
		playerImage.setOnTouchListener(new TouchForDragListener(playerImage,
				"player",teamA));
		triangleTool.setOnTouchListener(new TouchForDragListener(triangleTool,
				"triangle",teamA));
		
		squareTool.setOnTouchListener(new TouchForDragListener(squareTool,
				"square",teamA));
		
		ballTool.setOnTouchListener(new TouchForDragListener(ballTool,
				"ball",teamA));
		
		
		playerImage2.setOnTouchListener(new TouchForDragListener(playerImage,
				"player",teamB));
		triangleTool2.setOnTouchListener(new TouchForDragListener(triangleTool,
				"triangle",teamB));
		
		squareTool2.setOnTouchListener(new TouchForDragListener(squareTool,
				"square",teamB));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = false;
		switch(item.getItemId()){
		case R.id.menu_open_document:
			openDocument();
			break;
		case R.id.menu_save_document:
			showPlayNameDialog();
			break;
		case R.id.menu_clear_document:
			showClearDialog();
			result = true;
			break;
		case R.id.menu_exit:
			finish();
			break;
		}
		
		if(!result) {
			result = super.onMenuItemSelected(featureId, item);
		}
		
		return result;
	}


	private void openDocument() {
		 Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
	        try {
	        	fileintent.setType("file/*");
	            startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
	        } catch (ActivityNotFoundException e) {
	            Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
	        }

		
	}

	public void saveDocument(String name) {
		File file = Environment.getExternalStorageDirectory();
		drawingView.saveDocument(file,name);
		
	}

	@Override
	public void onBackPressed() {
		boolean result = drawingView.undoLast();
		if(!result) {
			super.onBackPressed();
		}
	}

	class TouchForDragListener implements OnTouchListener {

		private View view;
		private String dataToInput;
		private Team team;

		public TouchForDragListener(View view, String data, Team team) {
			this.view = view;
			this.dataToInput = data;
			this.team = team;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
			switch (paramMotionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText(dataToInput+","+ team.getNumber(), dataToInput+","+ team.getNumber());
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				// view.setVisibility(View.INVISIBLE);
				break;

			case MotionEvent.ACTION_UP:
				// view.setVisibility(View.VISIBLE);
				break;
			}
			return false;
		}

	}

	private void showClearDialog() {
		FragmentManager fm = getSupportFragmentManager();
		ClearDialog clearDialog = new ClearDialog();
		clearDialog.show(fm, "fragment_clear_name");
	}
	
	private void showPlayNameDialog() {
		FragmentManager fm = getSupportFragmentManager();
		PlaysNameDialogFragment  playsNameDialog = new PlaysNameDialogFragment();
		playsNameDialog.show(fm, "fragment_clear_name");
	}



	    public void onFinishClearDialog(boolean clearData) {
	        if(clearData) {
	        	drawingView.clearBoard();
	        }
	    }

		@Override
		public void onComponentSelected(ColorPath path) {
			playerNumber.setVisibility(View.VISIBLE);
			playerNumberButton.setVisibility(View.VISIBLE);
		}

		@Override
		public void onComponentRelease() {
			playerNumber.setVisibility(View.GONE);
			playerNumberButton.setVisibility(View.GONE);
			
		}
		
		
       

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
        case PICKFILE_RESULT_CODE:
            if (resultCode == RESULT_OK) {
                String FilePath = data.getData().getPath();
                drawingView.openDocument(new File(FilePath));
            }
        }
}

}
