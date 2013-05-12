package com.sportcoachhelper.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sportcoachhelper.R;
import com.sportcoachhelper.database.DatabaseHelper;
import com.sportcoachhelper.interfaces.OnComponentSelectedListener;
import com.sportcoachhelper.model.Team;
import com.sportcoachhelper.paths.BallPath;
import com.sportcoachhelper.paths.CirclePath;
import com.sportcoachhelper.paths.ColorPath;
import com.sportcoachhelper.paths.LinePath;
import com.sportcoachhelper.paths.ShapePath;
import com.sportcoachhelper.paths.SquarePath;
import com.sportcoachhelper.paths.TrianglePath;
import com.sportcoachhelper.paths.interfaces.Detectable;
import com.sportcoachhelper.paths.interfaces.Dibujables;
import com.sportcoachhelper.util.TeamManager;
import com.sportcoachhelper.util.Utility;

public class DrawingView extends View {

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private LinePath mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private ColorPath mPlayerPath;
	private Paint playerPaint;
	private Detectable mSelectedPath;
	private OnComponentSelectedListener listener;
	private String stateMode = getContext().getString(R.string.organization_mode);
	

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
		mPaint.setPathEffect(getLineMode(lineMode));
		
		mPath = new LinePath(mPaint);
		mPath.setLineMode(lineMode);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);		
		
		playerPaint = new Paint();
		playerPaint.setAntiAlias(true);
		playerPaint.setDither(true);
		playerPaint.setColor(0xFF000000);
		playerPaint.setStyle(Paint.Style.FILL);
		playerPaint.setStrokeJoin(Paint.Join.ROUND);
		playerPaint.setStrokeCap(Paint.Cap.ROUND);
		playerPaint.setStrokeWidth(4);
		
		ballPaint = new Paint();
		ballPaint.setAntiAlias(true);
		ballPaint.setDither(true);
		ballPaint.setColor(0xFF000000);
		ballPaint.setStyle(Paint.Style.FILL);
		ballPaint.setStrokeJoin(Paint.Join.ROUND);
		ballPaint.setStrokeCap(Paint.Cap.ROUND);
		ballPaint.setStrokeWidth(4);


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
		Context context = getContext();
		final String volley = context.getString(R.string.voley);
		final String soccer = context.getString(R.string.soccer);
		final String basket = context.getString(R.string.basketball);
		if(field.equals(soccer)) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.soccer);
		} else if (field.equals(volley)) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.voley);
		} else if (field.equals(basket)) {
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
	private List<Dibujables> temp;
	private String lineMode = getContext().getString(R.string.continuous_line_mode);
	private static final float TOUCH_TOLERANCE = 2;

	private void touch_start(float x, float y) {
		if(isOrganizationMode()) {
			movable = checkMovableObjectsPosition(x, y);
			setSelectedPath(movable);
		} else {
			init();
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}				
	}

	private boolean isOrganizationMode() {
		return stateMode.equals(getContext().getString(R.string.organization_mode));
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
		if (isOrganizationMode()) {
			if (movable != null && movable.canBeMoved() ) {
				movable.setX((int) x);
				movable.setY((int) y);
				movable.reinitialize();
				invalidate();
			}
		} else {

			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mPath.addPathPoints(new float[] { mX, mY, (x + mX) / 2,
						(y + mY) / 2 });
				mX = x;
				mY = y;
			}
		}
	}

	private void touch_up(float x, float y) {
		if (!isOrganizationMode()) {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			// mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			// mPath.reset();
			undoablePaths.add(mPath);
			// mPath.reset();
			movable=null;
		}
		
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
	
	public void setCirclePlayer(int x, int y, int team) {
		initPlayerPath(x, y,TeamManager.getInstance().getTeam(team));	
		undoablePaths.add(mPlayerPath);
		invalidate();
	}
	
	private void initPlayerPath(int x, int y,Team team) {
		mPlayerPath = new CirclePath(team.getPaint(),x-ColorPath.HALF_SIZE,y-ColorPath.HALF_SIZE);	
		((CirclePath)mPlayerPath).setTeam(team.getNumber());
	}
	
	public void setTrianglePlayer(int x, int y, int team) {
		initTrianglePath(x, y,TeamManager.getInstance().getTeam(team));
		undoablePaths.add(mPlayerPath);
		invalidate();
		
	}
	
	
	private void initTrianglePath(int x,int y, Team team) {
		mPlayerPath = new TrianglePath(team.getPaint(),x,y);	
		((TrianglePath)mPlayerPath).setTeam(team.getNumber());
	}

	public void setSquarePlayer(int x, int y, int team) {
		initSquarePath(x,y,TeamManager.getInstance().getTeam(team));		
		undoablePaths.add(mPlayerPath);
		invalidate();		
	}

	private void initSquarePath(int x,int y, Team team) {
		mPlayerPath = new SquarePath(team.getPaint(),x-ColorPath.HALF_SIZE,y-ColorPath.HALF_SIZE);		
		((SquarePath)mPlayerPath).setTeam(team.getNumber());
	}
	
	private void initBallPath(int field2) {
		mPlayerPath = new BallPath(ballPaint,field2,getResources());			
	}

	public void setBall(int x, int y, int team, int field) {
		initBallPath(field);
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

	public void saveDocument(File file, String name) {
	File toSaveFile = new File(file.getAbsoluteFile() + "/" + name);
	try {
	
	if(!toSaveFile.exists()) {
		toSaveFile.createNewFile();
	}
	
		FileOutputStream output = new FileOutputStream(toSaveFile);
		ObjectOutputStream stream = new ObjectOutputStream(output);
		stream.writeObject(field);
		stream.writeObject(undoablePaths);
		stream.flush();
		
		
		DatabaseHelper.getInstance().insertPlay(name, toSaveFile.getAbsolutePath(), System.currentTimeMillis());
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	public void openDocument(File file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			ObjectInputStream fin = new ObjectInputStream(stream);
			field = (String) fin.readObject();
			temp = (List<Dibujables>) fin.readObject();
			
			initializePaths();
			
			undoablePaths=temp;
			
			invalidate();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void initializePaths() {
		for (Dibujables dibujable : temp) {
			if(dibujable instanceof CirclePath) {	
				((CirclePath)dibujable).loadCirclePathPointsAsQuadTo();
			} else if(dibujable instanceof SquarePath) {
				((SquarePath)dibujable).loadSquarePathPointsAsQuadTo();
			} else if(dibujable instanceof TrianglePath) {				
				((TrianglePath)dibujable).loadTrianglePathPointsAsQuadTo();		
			} else if(dibujable instanceof BallPath) {
				((BallPath)dibujable).setPaint(ballPaint);				
			}  else if (dibujable instanceof LinePath) {
				Paint paint = new Paint(mPaint);
				paint.setPathEffect(getLineMode(((LinePath)dibujable).getLineMode()));
				((LinePath)dibujable).setPaint(paint);	
				((LinePath)dibujable).loadPathPointsAsQuadTo();
			} 
			
		}
		
	}

	public void setMode(String mode) {
		this.stateMode=mode;		
	}


	public void eraseSelected() {
		if(movable!=null && undoablePaths.contains(movable)) {
			undoablePaths.remove(movable);
			listener.onComponentRelease();
			invalidate();
		}
	}

	public void setLineMode(String mode) {
		this.lineMode = mode;
	}
	
	public DashPathEffect getLineMode(String mode) {
		DashPathEffect result = null;
		if(!mode.equals(getContext().getString(R.string.continuous_line_mode))){
			result = new DashPathEffect(new float[] { 10, 20 }, 0);
		}
		return result;
	}


}
