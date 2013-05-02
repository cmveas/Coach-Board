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
import com.sportcoachhelper.dialogs.ClearDialog;
import com.sportcoachhelper.interfaces.OnComponentSelectedListener;
import com.sportcoachhelper.paths.ColorPath;

public class MainActivity extends GraphicsActivity implements OnComponentSelectedListener  {

	private static final int PICKFILE_RESULT_CODE = 0;
	private DrawingView drawingView;
	private ImageView playerImage;
	private ImageView triangleTool;
	private ImageView squareTool;
	private ImageView ballTool;
	private EditText playerNumber;
	private Button playerNumberButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		drawingView = (DrawingView) findViewById(R.id.drawingPanel);

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
		

		manageDrag();
		
	    drawingView.setOnComponentSelectedListener(this);
	    
	    Intent intent = getIntent();
	    String label  = intent.getStringExtra("field");
	    drawingView.setField(label);
	}

	@SuppressLint("NewApi")
	private void manageDrag() {
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
				if (label.equals("player")) {
					drawingView.setCirclePlayer(x, y);
				} else if (label.equals("triangle")) {
					drawingView.setTrianglePlayer(x, y);
				}else if (label.equals("square")) {
					drawingView.setSquarePlayer(x, y);
				} else if(label.equals("ball")){
					drawingView.setBall(x, y);
				}
			}
		};

		drawingView.setOnDragListener(dragListener);

		playerImage.setOnTouchListener(new TouchForDragListener(playerImage,
				"player"));
		triangleTool.setOnTouchListener(new TouchForDragListener(triangleTool,
				"triangle"));
		
		squareTool.setOnTouchListener(new TouchForDragListener(squareTool,
				"square"));
		
		ballTool.setOnTouchListener(new TouchForDragListener(ballTool,
				"ball"));

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
			saveDocument();
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

	private void saveDocument() {
		File file = Environment.getExternalStorageDirectory();
		drawingView.saveDocument(file);
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

		public TouchForDragListener(View view, String data) {
			this.view = view;
			this.dataToInput = data;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
			switch (paramMotionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText(dataToInput, dataToInput);
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
