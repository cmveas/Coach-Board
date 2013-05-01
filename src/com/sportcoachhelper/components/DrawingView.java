package com.sportcoachhelper.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sportcoachhelper.R;
import com.sportcoachhelper.interfaces.OnComponentSelectedListener;
import com.sportcoachhelper.paths.BallPath;
import com.sportcoachhelper.paths.CirclePath;
import com.sportcoachhelper.paths.ColorPath;
import com.sportcoachhelper.paths.LinePath;
import com.sportcoachhelper.paths.ShapePath;
import com.sportcoachhelper.paths.SquarePath;
import com.sportcoachhelper.paths.interfaces.Detectable;
import com.sportcoachhelper.paths.interfaces.Dibujables;
import com.sportcoachhelper.util.Utility;

public class DrawingView extends View {

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private LinePath mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private Paint playerLinePaint;
	private ColorPath mPlayerPath;
	private Paint playerPaint;
	private Paint trianglePaint;
	private Paint squarePaint;
	private Detectable mSelectedPath;
	private OnComponentSelectedListener listener;

	public void setOnComponentSelectedListener(OnComponentSelectedListener listener){
		this.listener = listener;
	}
	
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
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(4);
		mPaint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
		
		mPath = new LinePath(mPaint);
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

	private void initPlayerPath(int x, int y) {
		mPlayerPath = new CirclePath(playerPaint);	
		mPlayerPath.setX(x);
		mPlayerPath.setY(y);
	}
	
	private void initTrianglePath(int x,int y) {
		mPlayerPath = new CirclePath(trianglePaint);		
	}

	private List<Dibujables> undoablePaths = new ArrayList<Dibujables>();
	private int w;
	private int h;

	public void setDrawingPaint(Paint paint) {
		mPaint = paint;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		initializeField(w, h);
	}

	private void initializeField(int w, int h) {
		this.w=w;
		this.h=h;
		if(w!=0 && h!=0) {
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			Bitmap bitmap = getFieldFromSelection();
			Bitmap resizedBitmap = Utility.getResizedBitmap(bitmap, h, w);
			mCanvas.drawBitmap(resizedBitmap, new Matrix(), new Paint());
		}
	}

	private Bitmap getFieldFromSelection() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.soccer);
		if(field.equals("Soccer")) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.soccer);
		} else if (field.equals("Voleyball")) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.voley);
		} else if (field.equals("Basketball")) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.basket);
		}
		return bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFFFFFFF);

		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

		for (Dibujables path : undoablePaths) {
			path.draw(canvas);
			
		}

		canvas.drawPath(mPath, mPaint);
	}

	private float mX, mY;
	private Paint ballPaint;
	private Detectable movable;
	private String field;
	private static final float TOUCH_TOLERANCE = 2;

	private void touch_start(float x, float y) {
		movable = checkMovableObjectsPosition(x, y);
		setSelectedPath(movable);
		if(movable==null) {
			init();
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}
	}

	private Detectable checkMovableObjectsPosition(float x, float y) {
		Detectable result = null;
		for (Dibujables item : undoablePaths) {
			if(item instanceof Detectable) {
				Detectable moveItem = (Detectable)item;
				if(moveItem.isItIn(x, y)) {
					result = moveItem;
					break;
				}
			}
		}
		return result;
	}

	private void touch_move(float x, float y) {
		if(movable==null) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		} else {
			if(movable.canBeMoved()) {
				movable.setX((int)x);
				movable.setY((int)y);
				invalidate();
			}
		}
	}

	private void touch_up(float x, float y) {
		if(movable==null) {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			// mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			// mPath.reset();
			undoablePaths.add(mPath);
			// mPath.reset();
		}
		
		movable=null;
		invalidate();
	}

	private void setSelectedPath(Detectable movable) {
		mSelectedPath =movable;
		if(mSelectedPath instanceof ColorPath) {
			listener.onComponentSelected((ColorPath)mSelectedPath);
		}
	}

	

	public boolean undoLast() {
		boolean result = false;
		if (undoablePaths.size() > 0) {
			Dibujables removedPath = undoablePaths.remove(undoablePaths.size() - 1);
			removedPath.resetPath();
			invalidate();
			result=true;
		}
		
		return result;
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
			touch_up(x, y);
			invalidate();
			break;
		}
		return true;
	}

	public void setCirclePlayer(int x, int y) {
		initPlayerPath(x, y);
		mPlayerPath.addCircle(x, y, ColorPath.HALF_SIZE, Path.Direction.CCW);
		undoablePaths.add(mPlayerPath);
		invalidate();
	}

	public void setTrianglePlayer(int x, int y) {
		initTrianglePath(x, y);
		mPlayerPath.moveTo(x, y);
		mPlayerPath.lineTo(x-ColorPath.HALF_SIZE, y+ColorPath.SIZE);
		mPlayerPath.lineTo(x+ColorPath.HALF_SIZE, y+ColorPath.SIZE);
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
		initSquarePath(x,y);
		mPlayerPath.addRect(x,y, x+ColorPath.SIZE, y+ColorPath.SIZE, Path.Direction.CCW);	
		undoablePaths.add(mPlayerPath);
		invalidate();
		
	}

	private void initSquarePath(int x,int y) {
		mPlayerPath = new SquarePath(squarePaint);		
		mPlayerPath.setX(x);
		mPlayerPath.setY(y);
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

	public void setLabel(String number) {
		if(mSelectedPath!=null) {
			if(mSelectedPath instanceof ShapePath) {
				 ((ShapePath)mSelectedPath).setLabel(number);
				 invalidate();
			}
		}
		
	}

	public void setField(String label) {
		this.field = label;
		initializeField(w, h);
	}


}
