package com.sportcoachhelper.components;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import com.sportcoachhelper.CoachApp;
import com.sportcoachhelper.R;
import com.sportcoachhelper.database.DatabaseHelper;
import com.sportcoachhelper.interfaces.OnComponentSelectedListener;
import com.sportcoachhelper.model.Play;
import com.sportcoachhelper.model.Team;
import com.sportcoachhelper.model.Template;
import com.sportcoachhelper.model.TemplateItem;
import com.sportcoachhelper.paths.BallPath;
import com.sportcoachhelper.paths.CirclePath;
import com.sportcoachhelper.paths.ColorPath;
import com.sportcoachhelper.paths.LinePath;
import com.sportcoachhelper.paths.ShapePath;
import com.sportcoachhelper.paths.SquarePath;
import com.sportcoachhelper.paths.TrianglePath;
import com.sportcoachhelper.paths.interfaces.Detectable;
import com.sportcoachhelper.paths.interfaces.Dibujables;
import com.sportcoachhelper.managers.TeamManager;
import com.sportcoachhelper.managers.TemplateManager;
import com.sportcoachhelper.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

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
	private Play play;
    private ToggleButton playerButton;
    private int dottedLine=0xFF000000;
    private int continuousLine=0xFF000000;
    private int screenWidth;
    private int screenHeight;


    public void setOnComponentSelectedListener(OnComponentSelectedListener listener){
		this.listener = listener;
	}
	
	public DrawingView(Context c) {
		super(c);
		init();
		initializePaints();
	}

	public DrawingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		initializePaints();
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		initializePaints();
	}

	private void init() {

        if(getContext() instanceof Activity) {
            Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }
		play = new Play();
		play.setFieldType(getContext().getString(R.string.full));
	}

	private void initializePaints() {
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(getColorPerLineMode(lineMode));
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

    private int getColorPerLineMode(String mode) {
        if(mode.equals(getContext().getString(R.string.continuous_line_mode))){
            return continuousLine;
        } else {
            return dottedLine;
        }
    }


    private int w;
	private int h;

	public void setDrawingPaint(Paint paint) {
		mPaint = paint;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		initializeField(w, h);
		
		if(template!=null) {
			ArrayList<TemplateItem> templates = template.getPlayers();
			
			for (TemplateItem item : templates) {
				int x = getXValueFromPercentage(item.getxPercentage());
				int y = getYValueFromPercentage(item.getyPercentage());
				int shape = item.getShape();
				switch (shape) {
				case TemplateItem.SHAPE_CIRCLE:
					
					setCirclePlayer(x, y, TeamManager.getInstance().getTeamA().getNumber());
					break;

				case TemplateItem.SHAPE_SQUARE:
					setSquarePlayer(x, y, TeamManager.getInstance().getTeamA().getNumber());
					break;
					
				case TemplateItem.SHAPE_TRIANGLE:
					setTrianglePlayer(x, y, TeamManager.getInstance().getTeamA().getNumber());
					break;
				}
			}
			invalidate();
			template=null;
		}
	}

	private void initializeField(int w, int h) {
		this.w=w;
		this.h=h;
		if(w>0 && h>0) {
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			Bitmap bitmap = getFieldFromSelection();
            if(bitmap!=null) {
			    Bitmap resizedBitmap = Utility.getResizedBitmap(bitmap, h, w);
			    mCanvas.drawBitmap(resizedBitmap, new Matrix(), new Paint());
            }
		}
	}

	private Bitmap getFieldFromSelection() {
		Bitmap bitmap = null;
		Context context = getContext();
		final String volley = context.getString(R.string.voley);
		final String soccer = context.getString(R.string.soccer);
		final String basket = context.getString(R.string.basketball);
        int result = -1;
        String field_type = play.getFieldType();
		if(play.getField().equals(soccer)) {
            if(field_type!=null) {
                if(field_type.equals(getContext().getString(R.string.full))) {
                    result = R.drawable.soccer;
                } else if(field_type.equals(getContext().getString(R.string.attack_half))) {
                    result = R.drawable.soccer_attack_half;
                } else if(field_type.equals(getContext().getString(R.string.defense_half))){
                    result = R.drawable.soccer_half;
                }

            }
		} else if (play.getField().equals(volley)) {

            if(field_type.equals(getContext().getString(R.string.full))) {
                result = R.drawable.voley;
            } else if(field_type.equals(getContext().getString(R.string.attack_half))) {
                result = R.drawable.voley_half_inverse;
            } else if(field_type.equals(getContext().getString(R.string.defense_half))){
                result = R.drawable.voley_half;
            }
		} else if (play.getField().equals(basket)) {

            if(field_type.equals(getContext().getString(R.string.full))) {
                result = R.drawable.basket;
            } else if(field_type.equals(getContext().getString(R.string.attack_half))) {
                result = R.drawable.basket_half;
            } else if(field_type.equals(getContext().getString(R.string.defense_half))){
                result = R.drawable.basket_half_inverse;
            }
		}
        bitmap = Utility.decodeSampledBitmapFromResource(getResources(),result,screenWidth,screenHeight);
		return bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		long now = System.currentTimeMillis();
		android.util.Log.d(TAG, "onDraw Started");
		
		//canvas.drawColor(0xFFFFFFFF);

		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

		ArrayList<Dibujables> undoablePaths = play.getUndoablePaths();
		
		for (Dibujables path : undoablePaths) {
			path.draw(canvas);
			
		}

		canvas.drawPath(mPath, mPaint);
		long last = System.currentTimeMillis();
		android.util.Log.d(TAG, "onDraw Finished:" + (last - now) + "now: " + now + " last:" + last);
	}

	private float mX, mY;
	private Paint ballPaint;
	private Detectable movable;
	private String lineMode = getContext().getString(R.string.continuous_line_mode);
	private long now;
	private Template template;
	private static final float TOUCH_TOLERANCE = 2;
	private static final String TAG = "DrawingView";

	private void touch_start(float x, float y) {
		android.util.Log.d(TAG, "touch_start");
        if(playerButton==null) {
            if(isOrganizationMode()) {
                disSelectMovable();
                movable = checkMovableObjectsPosition(x, y);

                if(movable==null) {
                    movable = checkNonMovableObjectsPosition(x, y);
                }
                setSelectedPath(movable);
                selectMovable();
            } else {
                initializePaints();
                mPath.reset();
                mPath.moveTo(x, y);
                mX = x;
                mY = y;
            }
        }
	}

	private void selectMovable() {
		if(movable!=null) {
			movable.setSelected(true);
		}
	}

	private void disSelectMovable() {
		if(movable!=null) {
			movable.setSelected(false);
            movable = null;
		}
	}

	private boolean isOrganizationMode() {
		return stateMode.equals(getContext().getString(R.string.organization_mode));
	}

	private Detectable checkMovableObjectsPosition(float x, float y) {
		Detectable result = null;
		ArrayList<Dibujables> undoablePaths = play.getUndoablePaths();
		for (Dibujables item : undoablePaths) {
			if(item instanceof Detectable && ((Detectable) item).canBeMoved()) {
				Detectable moveItem = (Detectable)item;
				if(moveItem.isItIn(x, y)) {
					result = moveItem;
					break;
				}
			}
		}
		return result;
	}
	
	private Detectable checkNonMovableObjectsPosition(float x, float y) {
		Detectable result = null;
		ArrayList<Dibujables> undoablePaths = play.getUndoablePaths();
		for (Dibujables item : undoablePaths) {
			if(item instanceof Detectable && !((Detectable) item).canBeMoved()) {
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
		android.util.Log.d(TAG, "touch_move start");
		if (isOrganizationMode()) {
			if (movable != null && movable.canBeMoved() ) {
				movable.setX((int) x);
				movable.setY((int) y);
				movable.reinitialize();
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
		android.util.Log.d(TAG, "touch_up");
        if(playerButton==null) {
            if (!isOrganizationMode()) {
                mPath.lineTo(mX, mY);
                // commit the path to our offscreen
                // mCanvas.drawPath(mPath, mPaint);
                // kill this so we don't double draw
                // mPath.reset();
                mPath.setColor(getColorPerLineMode(lineMode));
                addPathsToQueue(mPath);
                // mPath.reset();
                movable=null;
            }
        } else {
            String tag = (String) playerButton.getTag();

            if(tag!=null && tag.equals(getContext().getString(R.string.localroundplayer))) {
                setCirclePlayer((int)x,(int)y,TeamManager.getInstance().getTeamA().getNumber());
            } else if (tag!=null && tag.equals(getContext().getString(R.string.localsquareplayer))) {
                setSquarePlayer((int)x,(int)y,TeamManager.getInstance().getTeamA().getNumber());
            } else if (tag!=null && tag.equals(getContext().getString(R.string.localtriangleplayer))) {
                setTrianglePlayer((int)x,(int)y,TeamManager.getInstance().getTeamA().getNumber());
            } else if (tag!=null && tag.equals(getContext().getString(R.string.visitsquareplayer))) {
                setSquarePlayer((int)x,(int)y,TeamManager.getInstance().getTeamB().getNumber());
            } else if (tag!=null && tag.equals(getContext().getString(R.string.visittriangleplayer))) {
                setTrianglePlayer((int)x,(int)y,TeamManager.getInstance().getTeamB().getNumber());
            }else if (tag!=null && tag.equals(getContext().getString(R.string.visitroundplayer))) {
                setCirclePlayer((int)x,(int)y,TeamManager.getInstance().getTeamB().getNumber());
            } else if (tag!=null && tag.equals(getContext().getString(R.string.ball))) {
                setBall((int)x-ColorPath.HALF_SIZE,(int)y-ColorPath.HALF_SIZE,TeamManager.getInstance().getTeamB().getNumber(),Utility.giveMeFieldBall(play.getField()));
            }


            post(new Runnable() {
                public void run() {
                    playerButton.setChecked(false);
                    playerButton=null;
                }
            });
        }
	}

	private void addPathsToQueue(ColorPath mPath) {
		play.addPath(mPath);
        if(listener !=null && isShape(mPath)) {
            listener.onPlayerAdded();
        }
	}

    private boolean isShape(ColorPath mPath) {
        return mPath!=null && mPath instanceof ShapePath;
    }

    private void setSelectedPath(Detectable movable) {
		mSelectedPath =movable;
        if(listener!=null) {
		    listener.onComponentSelected((ColorPath)mSelectedPath);
        }
     }

	

	public boolean undoLast() {
		boolean result = false;
		result = play.removeLast();
		if(result) {
			invalidate();
		}
		return result;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		drawDifference("onTouchEvent Started",event.getAction());
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
		
		
		drawDifference("onTouchEvent Ended",event.getAction());
		return true;
	}


	

	private void drawDifference(String event, int action) {
		long dif =  (System.currentTimeMillis()-now);
		if(dif>500) {
			android.util.Log.d(TAG, event + dif + " " + action);
		}
		now = System.currentTimeMillis();
		
	}

	public void clearBoard() {
		play.removeAllMoves();
		
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
		addPathsToQueue(mPlayerPath);
		invalidate();
	}
	
	private void initPlayerPath(int x, int y,Team team) {
		mPlayerPath = new CirclePath(team.getPaint(),x,y);	
		((CirclePath)mPlayerPath).setTeam(team.getNumber());
		setPathSelected(mPlayerPath);
	}
	
	public void setTrianglePlayer(int x, int y, int team) {
		initTrianglePath(x, y,TeamManager.getInstance().getTeam(team));
		addPathsToQueue(mPlayerPath);
		invalidate();
		
	}
	
	
	private void initTrianglePath(int x,int y, Team team) {
		mPlayerPath = new TrianglePath(team.getPaint(),x,y);	
		TemplateItem item = new TemplateItem();
		item.setShape(TemplateItem.SHAPE_TRIANGLE);
		item.setxPercentage(getPercentageWidth(x));
		item.setyPercentage(getPercentageHeight(y));
		((TrianglePath)mPlayerPath).setTeam(team.getNumber());
		setPathSelected(mPlayerPath);
	}

	private void setPathSelected(ColorPath mPlayerPath) {
		if(mPlayerPath!=null) {
			disSelectMovable();
			movable = mPlayerPath;
			selectMovable();
			setSelectedPath(movable);
		}
	}
	
	public void setSquarePlayer(int x, int y, int team) {
		initSquarePath(x,y,TeamManager.getInstance().getTeam(team));		
		addPathsToQueue(mPlayerPath);
		invalidate();		
	}

	private void initSquarePath(int x,int y, Team team) {
		mPlayerPath = new SquarePath(team.getPaint(),x,y);		
		((SquarePath)mPlayerPath).setTeam(team.getNumber());
		setPathSelected(mPlayerPath);
	}
	
	private void initBallPath(int field2) {
		mPlayerPath = new BallPath(ballPaint,field2,getResources());			
	}

	public void setBall(int x, int y, int team, int field) {
		initBallPath(field);
		((BallPath)mPlayerPath).setX(x);
		((BallPath)mPlayerPath).setY(y);
		setPathSelected(mPlayerPath);
		addPathsToQueue(mPlayerPath);
		invalidate();
	}

	private int getPercentageWidth(int x) {
		int totalWidth = getWidth();
		return (x*100)/totalWidth;
	}
	

	private int getPercentageHeight(int y) {
		int totalHeight = getHeight();
		return (y*100)/totalHeight;
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
		play.setField(label);
		initializeField(w, h);
	}

	public void saveDocument(String name) {

	try {
        play.setName(name);
		if(play.getId()!=-1) {
            DatabaseHelper.getInstance().updatePlay(play.getId(),name, play.getField(), play.getFieldType() , "", System.currentTimeMillis());
            DatabaseHelper.getInstance().deletePlayComponents(play.getId());
        } else {
            long playId= DatabaseHelper.getInstance().insertPlay(name, play.getField(),play.getFieldType() , "", System.currentTimeMillis());
            play.setId(playId);

        }

            createDBComponents(play.getId());
            createTemplate();

	} catch (Exception e) {
		e.printStackTrace();
	}
	}

    private void createDBComponents(long playId) {
        try{
        if(playId!=-1) {
            ArrayList<Dibujables> components = play.getUndoablePaths();
            int index = 0;
            for(Dibujables component : components){
                JSONObject data = new JSONObject();
                android.util.Log.d(TAG,"Saving shape: " + component.getComponentType());
                data.put("DATA",component.toJsonData());
                DatabaseHelper.getInstance().insertPlayComponent(component.getComponentType(),data.toString(),playId,index);
                index++;
            }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
	 * Creation and template saving
	 */
	private void createTemplate() {
		Template template = new Template();
		template.setField(play.getField());
		template.setName(play.getName());
		ArrayList<Dibujables> plays = play.getUndoablePaths();
		for (Dibujables dibujable : plays) {
			if(dibujable instanceof ShapePath) {
				TemplateItem item = new TemplateItem();
				int shape = -1;
				if(dibujable instanceof CirclePath) {
					shape = TemplateItem.SHAPE_CIRCLE;
				} else if(dibujable instanceof TrianglePath) {
					shape = TemplateItem.SHAPE_TRIANGLE;
				} else if(dibujable instanceof SquarePath) {
					shape = TemplateItem.SHAPE_SQUARE;
				} 
				item.setShape(shape);
				item.setxPercentage(getXPercentageOf(((ShapePath) dibujable).getX()));
				item.setyPercentage(getYPercentageOf(((ShapePath) dibujable).getY()));
				template.add(item);
			}
		}
		File toSaveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/" + play.getName() + "_template");	
		
		FileOutputStream output;
		try {
			if(!toSaveFile.exists()) {
				toSaveFile.createNewFile();
			}
			output = new FileOutputStream(toSaveFile);

			ObjectOutputStream stream = new ObjectOutputStream(output);
			stream.writeObject(template);
			stream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int getYPercentageOf(int y) {
		int totalHeight = getHeight();
		int percentage = (y * 100)/totalHeight;
		return percentage;
	}

	private int getXPercentageOf(int x) {
		int totalWidth = getWidth();
		int percentage = (x * 100)/totalWidth;
		return percentage;
	}

	public void openDocument(File file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			ObjectInputStream fin = new ObjectInputStream(stream);
			play =  (Play) fin.readObject();
			
			initializePaths();
			
			invalidate();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

    public void openDocument(Play play) {
        try {
          this.play = play;
          Cursor cursor = DatabaseHelper.getInstance().getPlayComponents(play.getId());

          if(cursor!=null && cursor.moveToFirst()  ) {
              int indexShape = cursor.getColumnIndex(DatabaseHelper.PLAYS_COMPONENT_SHAPE);
              int indexData = cursor.getColumnIndex(DatabaseHelper.PLAYS_COMPONENT_DATA);
            while(!cursor.isAfterLast()){
                String shape = cursor.getString(indexShape);
                android.util.Log.d(TAG,"Loading Shape:" + shape);
                if(shape!=null && (shape.equals(CirclePath.CIRCLE)||shape.equals(SquarePath.SQUARE)||shape.equals(TrianglePath.TRIANGLE))) {
                    processShapeData(shape,cursor.getString(indexData));
                } else if(shape!=null && shape.equals(LinePath.LINE)) {
                    processLineData(cursor.getString(indexData));
                } else if(shape!=null && shape.equals(BallPath.BALL)) {
                    processBallData(cursor.getString(indexData));
                }
                cursor.moveToNext();
            }
          }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void processBallData(String data) {
        try {
        JSONObject dataJson = new JSONObject(data);
        JSONArray array = dataJson.getJSONArray("DATA");
            int ballType = -1;

                JSONObject Data = array.getJSONObject(0);
                ballType =  Data.getInt("balltype");
                Data = array.getJSONObject(1);
                int x = Data.getInt("X");
                int y = Data.getInt("Y");
                BallPath ball = new BallPath(mPaint, ballType, CoachApp.getInstance().getResources());
                ball.setX(x);
                ball.setY(y);
                play.addPath(ball);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLineData(String data) {
        try {
            JSONObject dataJson = new JSONObject(data);
            JSONArray array = dataJson.getJSONArray("DATA");
            LinePath path = new LinePath(mPaint);

            Paint newLinePaint = new Paint(path.getPaint());
            String mode = null;
            int color = -1;
            for(int i=0;i<array.length();i++) {
                JSONObject Data = array.getJSONObject(i);
                if(i==0) {
                    mode =  Data.getString("mode");
                } else if(i==1) {
                    color =  Data.getInt("color");


                }
                else {
                    float x = (float) Data.getDouble("X");
                    float y = (float)  Data.getDouble("Y");
                    float xf = (float)  Data.getDouble("XF");
                    float yf = (float)  Data.getDouble("YF");
                    float[] pointsData = new float[] {x,y,xf,yf};
                    path.addPathPoints(pointsData);
                }

            }

            Paint paint = new Paint(mPaint);
            paint.setPathEffect(getLineMode(mode));
            paint.setColor(color);
            path.setPaint(paint);
            path.setLineMode(mode);
            path.setColor(color);
            path.loadPathPointsAsQuadTo();
            play.addPath(path);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void processShapeData(String shape,String data) {
        try {
            JSONObject dataJson = new JSONObject(data);
            JSONArray array = dataJson.getJSONArray("DATA");
            JSONObject Data = array.getJSONObject(0);
            int x = Data.getInt("X");
            int y = Data.getInt("Y");
            int team = Data.getInt("Team");
            if(shape.equals(CirclePath.CIRCLE)) {
                setCirclePlayer(x,y,team);
            } else if (shape.equals(SquarePath.SQUARE)) {
                setSquarePlayer(x,y,team);
            } else if (shape.equals(TrianglePath.TRIANGLE)){
                setTrianglePlayer(x,y,team);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public Play getPlay(){
		return play;
	}

	private void initializePaths() {
		ArrayList<Dibujables> temp = play.getUndoablePaths();
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
                paint.setColor(((LinePath)dibujable).getColor());
				((LinePath)dibujable).setPaint(paint);	
				((LinePath)dibujable).loadPathPointsAsQuadTo();
			} 
			
		}
		
	}

	public void setMode(String mode) {
		this.stateMode=mode;		
	}


	public void eraseSelected() {
		if(play.erasedSelected(movable)) {
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

	public void setModeChanged() {
		disSelectMovable();
		invalidate();
		if(listener!=null) {
			listener.onComponentRelease();
		}
	}

	public void loadFromTemplate(String play2) {
		template = TemplateManager.getInstance().getTemplateFromName(play2);
		
	}

	private int getYValueFromPercentage(int y) {
		int result = (y*getHeight())/100;
		return result;
	}

	private int getXValueFromPercentage(int x) {
		int result = (x*getWidth())/100;
		return result;
	}


    public void setLastSelectedPlayerView(ToggleButton button){
        this.playerButton = button;
    }


    public int getContinuousColor() {
        return mPaint.getColor();
    }

    public void setDottedLineColor(int color) {
       dottedLine = color;
    }

    public void setContinuousLineColor(int color) {
        continuousLine = color;
}

    public void setFieldType(String field_type) {
       play.setFieldType(field_type);
    }

    public void setSidesMargins(int margin) {

    }
}
