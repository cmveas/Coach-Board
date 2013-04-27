package com.sportcoachhelper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.sportcoachhelper.components.DrawingView;
import com.sportcoachhelper.dialogs.ClearDialog;

public class MainActivity extends GraphicsActivity implements
		ColorPickerDialog.OnColorChangedListener {

	private DrawingView drawingView;
	private ImageView playerImage;
	private ImageView triangleTool;
	private ImageView squareTool;

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

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(4);
		mPaint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
		drawingView.setDrawingPaint(mPaint);

		manageDrag();
	}

	@SuppressLint("NewApi")
	private void manageDrag() {
		OnDragListener dragListener = new OnDragListener() {

			@Override
			public boolean onDrag(View paramView, DragEvent paramDragEvent) {
				int action = paramDragEvent.getAction();
				switch (paramDragEvent.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					break;
				case DragEvent.ACTION_DROP:
					// Dropped, reassign View to ViewGroup
					int x = (int) paramDragEvent.getX();
					int y = (int) paramDragEvent.getY();

					ClipData data = paramDragEvent.getClipData();

					String label = data.getDescription().getLabel().toString();
					if (label.equals("player")) {
						drawingView.setCirclePlayer(x, y);
					} else if (label.equals("triangle")) {
						drawingView.setTrianglePlayer(x, y);
					}else if (label.equals("square")) {
						drawingView.setSquarePlayer(x, y);
					}
					paramView.setVisibility(View.VISIBLE);
					playerImage.setVisibility(View.VISIBLE);
					break;
				}
				android.util.Log.d("[DRAG ACTION]",
						"Event:" + paramDragEvent.getAction());
				return true;
			}
		};

		drawingView.setOnDragListener(dragListener);

		playerImage.setOnTouchListener(new TouchForDragListener(playerImage,
				"player"));
		triangleTool.setOnTouchListener(new TouchForDragListener(triangleTool,
				"triangle"));
		
		squareTool.setOnTouchListener(new TouchForDragListener(squareTool,
				"square"));

	}

	private Paint mPaint;

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
		case R.id.menu_clear_document:
			showClearDialog();
			result = true;
			break;
		}
		
		if(!result) {
			result = super.onMenuItemSelected(featureId, item);
		}
		
		return result;
	}

	public void colorChanged(int color) {
		mPaint.setColor(color);
	}

	@Override
	public void onBackPressed() {
		drawingView.undoLast();
		// super.onBackPressed();
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
}
