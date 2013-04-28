package com.sportcoachhelper.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sportcoachhelper.R;
import com.sportcoachhelper.paths.BallPath;
import com.sportcoachhelper.paths.CirclePath;
import com.sportcoachhelper.paths.ColorPath;
import com.sportcoachhelper.paths.LinePath;
import com.sportcoachhelper.paths.ShapePath;
import com.sportcoachhelper.paths.SquarePath;
import com.sportcoachhelper.util.Utility;

public class DrawingView extends View {

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private Paint playerLinePaint;
	private ColorPath mPlayerPath;
	private Paint playerPaint;
	private Paint trianglePaint;
	private Paint squarePaint;

	public DrawingView(Context c) {
		super(c);

		init();
	}

	public DrawingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPath = new LinePath();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		playerLinePaint = new Paint();
		playerLinePaint.setAntiAlias(true);
		playerLinePaint.setDither(true);
		playerLinePaint.setColor(0xFF000000);
		playerLinePaint.setStyle(Paint.Style.FILL);
		playerLinePaint.setStrokeJoin(Paint.Join.ROUND);
		playerLinePaint.setStrokeCap(Paint.Cap.ROUND);
		playerLinePaint.setStrokeWidth(4);
		
		playerPaint = new Paint();
		playerPaint.setAntiAlias(true);
		playerPaint.setDither(true);
		playerPaint.setColor(0xFF000000);
		playerPaint.setStyle(Paint.Style.FILL);
		playerPaint.setStrokeJoin(Paint.Join.ROUND);
		playerPaint.setStrokeCap(Paint.Cap.ROUND);
		playerPaint.setStrokeWidth(4);
		
		trianglePaint = new Paint();
		trianglePaint.setAntiAlias(true);
		trianglePaint.setDither(true);
		trianglePaint.setColor(0xFFFF0000);
		trianglePaint.setStyle(Paint.Style.FILL);
		trianglePaint.setStrokeJoin(Paint.Join.ROUND);
		trianglePaint.setStrokeCap(Paint.Cap.ROUND);
		trianglePaint.setStrokeWidth(4);
		
		squarePaint = new Paint();
		squarePaint.setAntiAlias(true);
		squarePaint.setDither(true);
		squarePaint.setColor(0xFF00FF00);
		squarePaint.setStyle(Paint.Style.FILL);
		squarePaint.setStrokeJoin(Paint.Join.ROUND);
		squarePaint.setStrokeCap(Paint.Cap.ROUND);
		squarePaint.setStrokeWidth(4);
		
		ballPaint = new Paint();
		ballPaint.setAntiAlias(true);
		ballPaint.setDither(true);
		ballPaint.setColor(0xFF000000);
		ballPaint.setStyle(Paint.Style.FILL);
		ballPaint.setStrokeJoin(Paint.Join.ROUND);
		ballPaint.setStrokeCap(Paint.Cap.ROUND);
		ballPaint.setStrokeWidth(4);

	}

	private void initPlayerPath() {
		mPlayerPath = new CirclePath(playerPaint);		
	}
	
	private void initTrianglePath() {
		mPlayerPath = new CirclePath(trianglePaint);		
	}

	private List<Path> undoablePaths = new ArrayList<Path>();

	public void setDrawingPaint(Paint paint) {
		mPaint = paint;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.cancha_voley);
		Bitmap resizedBitmap = Utility.getResizedBitmap(bitmap, h, w);
		mCanvas.drawBitmap(resizedBitmap, new Matrix(), new Paint());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFFFFFFF);

		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

		for (Path path : undoablePaths) {
			if(path instanceof ShapePath) {
				canvas.drawPath(path,((ShapePath)path).getPaint());
			} else if(path instanceof BallPath) {
				canvas.drawBitmap(((BallPath)path).getBitmap(),((BallPath)path).getX(),((BallPath)path).getY(),ballPaint);
			} else {
				canvas.drawPath(path, mPaint);
			}
			
		}

		canvas.drawPath(mPath, mPaint);
	}

	private float mX, mY;
	private Paint ballPaint;
	private static final float TOUCH_TOLERANCE = 2;

	private void touch_start(float x, float y) {
		init();
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		// mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		// mPath.reset();
		undoablePaths.add(mPath);
		// mPath.reset();
		invalidate();
	}

	

	public void undoLast() {
		if (undoablePaths.size() > 0) {
			Path removedPath = undoablePaths.remove(undoablePaths.size() - 1);
			removedPath.reset();
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	public void setCirclePlayer(int x, int y) {
		initPlayerPath();
		mPlayerPath.addCircle(x, y, 20, Path.Direction.CCW);
		undoablePaths.add(mPlayerPath);
		invalidate();
	}

	public void setTrianglePlayer(int x, int y) {
		initTrianglePath();
		mPlayerPath.moveTo(x, y);
		mPlayerPath.lineTo(x-25, y+40);
		mPlayerPath.lineTo(x+25, y+40);
		mPlayerPath.lineTo(x, y);		
		undoablePaths.add(mPlayerPath);
		invalidate();
		
	}

	public void clearBoard() {
		undoablePaths.removeAll(undoablePaths);
		
		if(mPlayerPath!=null) {
			mPlayerPath.reset();
		}
		if(mPath!=null) {
			mPath.reset();
		}
		invalidate();
	}

	public void setSquarePlayer(int x, int y) {
		initSquarePath();
		mPlayerPath.addRect(x,y, x+35, y+35, Path.Direction.CCW);	
		undoablePaths.add(mPlayerPath);
		invalidate();
		
	}

	private void initSquarePath() {
		mPlayerPath = new SquarePath(squarePaint);				
	}
	
	private void initBallPath() {
		mPlayerPath = new BallPath(ballPaint,R.drawable.volleyball,getResources());			
	}

	public void setBall(int x, int y) {
		initBallPath();
		((BallPath)mPlayerPath).setX(x);
		((BallPath)mPlayerPath).setY(y);
		undoablePaths.add(mPlayerPath);
		invalidate();
	}


}
