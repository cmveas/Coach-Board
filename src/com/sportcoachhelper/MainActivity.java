package com.sportcoachhelper;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.*;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.sportcoachhelper.components.DrawingView;
import com.sportcoachhelper.dialogs.ClearDialog;
import com.sportcoachhelper.dialogs.PlaysNameDialogFragment;
import com.sportcoachhelper.fragments.ScreenSlidePageFragment;
import com.sportcoachhelper.interfaces.OnComponentSelectedListener;
import com.sportcoachhelper.managers.PlaysManager;
import com.sportcoachhelper.model.Play;
import com.sportcoachhelper.model.Team;
import com.sportcoachhelper.paths.BallPath;
import com.sportcoachhelper.paths.ColorPath;
import com.sportcoachhelper.managers.TeamManager;
import com.sportcoachhelper.util.Utility;

public class MainActivity extends GraphicsActivity implements
		OnComponentSelectedListener, ColorPickerDialog.OnColorChangedListener {

	private static final int PICKFILE_RESULT_CODE = 0;
    private static final int DOTTED_LINE_ID = 1;
    private static final int CONTINUOUS_LINE_ID = 2;
    private static final int WARN_NOT_SAVED_EXIT = 3;
    private DrawingView drawingView;
	private ToggleButton playerImage;
	private ToggleButton triangleTool;
	private ToggleButton squareTool;
	private ToggleButton ballTool;
	private EditText playerNumber;
	private Button playerNumberButton;
	private ToggleButton playerImage2;
	private ToggleButton triangleTool2;
	private ToggleButton squareTool2;
	private String field;
	private ImageView trash;
	private String mode;
	private ToggleButton organizationMode;
	private ToggleButton continuousLineMode;
	private ToggleButton dottedLineMode;
    private LinearLayout layoutToolBar2;
    private int id;
    private View continuousBackground;
    private View dottedBackground;
    private View rightEmptySpace;
    private View leftEmptySpace;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utility.setHoloTheme(this);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		drawingView = (DrawingView) findViewById(R.id.drawingPanel);

		/*
		 * Team A Toolbar
		 */

		playerImage = (ToggleButton) findViewById(R.id.playerTool);
		triangleTool = (ToggleButton) findViewById(R.id.triangleTool);
		squareTool = (ToggleButton) findViewById(R.id.squareTool);
        continuousBackground = findViewById(R.id.continuousBackground);
        dottedBackground = findViewById(R.id.dottedBackground);

		playerNumberButton = (Button) findViewById(R.id.playerNumberButton);
		playerNumber = (EditText) findViewById(R.id.playerNumber);
		trash = (ImageView) findViewById(R.id.trash);

		playerNumberButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = playerNumber.getText().toString();
				if (!number.trim().equals("")) {
					drawingView.setLabel(number);
				}

			}
		});

		/*
		 * Team B Toolbar
		 */

		playerImage2 = (ToggleButton) findViewById(R.id.playerTool2);
		triangleTool2 = (ToggleButton) findViewById(R.id.triangleTool2);
		squareTool2 = (ToggleButton) findViewById(R.id.squareTool2);
		organizationMode = (ToggleButton) findViewById(R.id.organization_mode);
		continuousLineMode = (ToggleButton) findViewById(R.id.continuous_line_mode);
		dottedLineMode = (ToggleButton) findViewById(R.id.dotted_line_mode);
        layoutToolBar2 = (LinearLayout) findViewById(R.id.layoutToolBar2);
        leftEmptySpace = findViewById(R.id.leftEmptySpace);
        rightEmptySpace= findViewById(R.id.rightEmptySpace);

		
		organizationMode.setChecked(true);

		Intent intent = getIntent();
		field = intent.getStringExtra("field");
		drawingView.setField(field);

		String type = intent.getStringExtra(ScreenSlidePageFragment.TYPE);
		if (type != null && type.equals("play")) {
			long playId = intent.getLongExtra(ScreenSlidePageFragment.PLAY,-1);
            Play play = PlaysManager.getInstance().getPlay(field, playId);
            if ( playId!=-1) {
				drawingView.openDocument(play);
			}
		} else if (type != null && type.equals("template")) {
			String play = intent.getStringExtra(ScreenSlidePageFragment.PLAY);
			if (play != null && !play.trim().equals("")) {
				drawingView.loadFromTemplate(play);
			}
		}

        String field_type = intent.getStringExtra(ScreenSlidePageFragment.TYPE_FIELD);
        drawingView.setFieldType(field_type);

        checkBorders(field_type);
		
		mode = getString(R.string.organization_mode);

        drawTheAppropiateBall(field);

        ballTool = (ToggleButton) findViewById(R.id.ballTool);

        setOrganizationModeButton();
		
		manageDrag(field);

		drawingView.setOnComponentSelectedListener(this);

		checkForFullTools();

        setInitialLineColors();

	}

    private void checkBorders(String field_type) {

        if(field_type.equals(getString(R.string.attack_half)) || field_type.equals(getString(R.string.defense_half))) {
            displaySideBorders();
        }

    }


    public boolean checkIfSaved() {
        boolean result = false;
        if(drawingView.getPlay().getId()!=-1) {
            result = true;
        }
        return result;
    }

    private void displaySideBorders() {
        leftEmptySpace.setVisibility(View.VISIBLE);
        rightEmptySpace.setVisibility(View.VISIBLE);
    }


    private void setInitialLineColors() {
        dottedBackground.setBackgroundColor(getResources().getColor(android.R.color.black));
        continuousBackground.setBackgroundColor(getResources().getColor(android.R.color.black));
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Dialog dialog = null;
        switch(id){
            case DOTTED_LINE_ID:
                int color = args.getInt("color");
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this,this,color);
                dialog = colorPickerDialog;
                break;
            case CONTINUOUS_LINE_ID:
                color = args.getInt("color");
                colorPickerDialog = new ColorPickerDialog(this,this,color);
                dialog = colorPickerDialog;
                break;
            case WARN_NOT_SAVED_EXIT:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage(getString(R.string.no_save_are_you_sure_exit))
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
        return dialog;
    }

    public void setOrganizationModeButton(){
		organizationMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {				
					setOrganizationMode();
					modeChanged();
				} else {
					checkAButtonIsPressed(organizationMode);
				}		
			}
		})	;
		
		continuousLineMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {					
					setContinuousLine();
					modeChanged();
				} 	else {
					checkAButtonIsPressed(continuousLineMode);
				}		
			}
		})	;

        continuousLineMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("color",drawingView.getContinuousColor());
                showDialog(CONTINUOUS_LINE_ID, bundle);
                id=CONTINUOUS_LINE_ID;
                return true;
            }
        });
		
		dottedLineMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {					
					setDottedLine();
					modeChanged();
				} 	else {
					checkAButtonIsPressed(dottedLineMode);
				}		
			}
		})	;

        dottedLineMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("color",drawingView.getContinuousColor());
                showDialog(DOTTED_LINE_ID,bundle);
                id=DOTTED_LINE_ID;
                return true;
            }
        });

        loadPlayerListeners();
	}

    private void loadPlayerListeners() {
        playerImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setChoosingLocalRoundPlayer();
                    modeChanged();
                } 	else {
                    checkAButtonIsPressed(playerImage);
                }
            }
        })	;

        triangleTool.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setChoosingLocalTriangePlayer();
                    modeChanged();
                } 	else {
                    checkAButtonIsPressed(triangleTool);
                }
            }
        })	;

        squareTool.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setChoosingLocalSquarePlayer();
                    modeChanged();
                } 	else {
                    checkAButtonIsPressed(squareTool);
                }
            }
        })	;

        playerImage2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setChoosingVisitRoundPlayer();
                    modeChanged();
                } 	else {
                    checkAButtonIsPressed(playerImage2);
                }
            }
        })	;

        triangleTool2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setChoosingVisitTriangePlayer();
                    modeChanged();
                } 	else {
                    checkAButtonIsPressed(triangleTool2);
                }
            }
        })	;

        squareTool2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setChoosingVisitSquarePlayer();
                    modeChanged();
                } 	else {
                    checkAButtonIsPressed(squareTool2);
                }
            }
        })	;

        ballTool.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setChoosingBall();
                    modeChanged();
                } 	else {
                    checkAButtonIsPressed(ballTool);
                }
            }
        })	;

    }

    private void setChoosingBall() {
        organizationMode.setChecked(false);
        continuousLineMode.setChecked(false);
        playerImage2.setChecked(false);
        playerImage.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
        drawingView.setLastSelectedPlayerView(ballTool);
    }


    protected void modeChanged() {
		drawingView.setModeChanged();
		
	}

	protected void setContinuousLine() {
		organizationMode.setChecked(false);
		dottedLineMode.setChecked(false);
        playerImage.setChecked(false);
        playerImage2.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
        ballTool.setChecked(false);
        drawingView.setMode(getString(R.string.drawing_mode));
		drawingView.setLineMode(getString(R.string.continuous_line_mode));
		
	}

    protected void setChoosingLocalTriangePlayer() {
        organizationMode.setChecked(false);
        continuousLineMode.setChecked(false);
        playerImage2.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        playerImage.setChecked(false);
        triangleTool2.setChecked(false);
        ballTool.setChecked(false);
        drawingView.setLastSelectedPlayerView(triangleTool);
    }

    protected void setChoosingLocalSquarePlayer() {
        organizationMode.setChecked(false);
        continuousLineMode.setChecked(false);
        playerImage2.setChecked(false);
        squareTool2.setChecked(false);
        playerImage.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
        ballTool.setChecked(false);
        drawingView.setLastSelectedPlayerView(squareTool);
    }

    protected void setChoosingLocalRoundPlayer(){
        organizationMode.setChecked(false);
        continuousLineMode.setChecked(false);
        playerImage2.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
        ballTool.setChecked(false);
        drawingView.setLastSelectedPlayerView(playerImage);
    }


    protected void setChoosingVisitTriangePlayer() {
        organizationMode.setChecked(false);
        continuousLineMode.setChecked(false);
        playerImage2.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        playerImage.setChecked(false);
        triangleTool.setChecked(false);
        ballTool.setChecked(false);
        drawingView.setLastSelectedPlayerView(triangleTool2);
    }

    protected void setChoosingVisitSquarePlayer() {
        organizationMode.setChecked(false);
        continuousLineMode.setChecked(false);
        playerImage2.setChecked(false);
        squareTool.setChecked(false);
        playerImage.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
        ballTool.setChecked(false);
        drawingView.setLastSelectedPlayerView(squareTool2);
    }



    protected void setChoosingVisitRoundPlayer(){
        organizationMode.setChecked(false);
        continuousLineMode.setChecked(false);
        playerImage.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
        ballTool.setChecked(false);
        drawingView.setLastSelectedPlayerView(playerImage2);
    }
	
	protected void setDottedLine() {
		organizationMode.setChecked(false);
		continuousLineMode.setChecked(false);
        playerImage.setChecked(false);
        playerImage2.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
        ballTool.setChecked(false);
		drawingView.setMode(getString(R.string.drawing_mode));
		drawingView.setLineMode(getString(R.string.dotted_line_mode));
		
	}

	protected void checkAButtonIsPressed(ToggleButton selectedButton) {
		if(!organizationMode.isChecked() && !continuousLineMode.isChecked() && !dottedLineMode.isChecked()) {
			selectedButton.setChecked(true);
		}
		
	}

	private void checkForFullTools() {
		boolean fullTools = fullTools(field);
		triangleTool.setVisibility((fullTools ? View.VISIBLE : View.GONE));
		triangleTool2.setVisibility((fullTools ? View.VISIBLE : View.GONE));
		squareTool.setVisibility((fullTools ? View.VISIBLE : View.GONE));
		squareTool2.setVisibility((fullTools ? View.VISIBLE : View.GONE));
	}

	private void drawTheAppropiateBall(String field) {
		int type = Utility.giveMeFieldBall(field);
		int resource = BallPath.getLayoutFromType(type);
		View view = getLayoutInflater().inflate(resource,null);
        layoutToolBar2.addView(view, layoutToolBar2.getChildCount()-1);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.toolbar_button_height);
        view.setLayoutParams(params);
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
		if(supportsDragAndDrop()) {
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
					drawingView.setCirclePlayer(x, y, team);
				} else if (label.equals("triangle")) {
					drawingView.setTrianglePlayer(x, y, team);
				} else if (label.equals("square")) {
					drawingView.setSquarePlayer(x, y, team);
				} else if (label.equals("ball")) {
					drawingView.setBall(x, y, team, Utility.giveMeFieldBall(field));
				}
			}
		};

		drawingView.setOnDragListener(dragListener);

		Team teamA = TeamManager.getInstance().getTeamA();
		Team teamB = TeamManager.getInstance().getTeamB();

		playerImage.setOnTouchListener(new TouchForDragListener(playerImage,
				"player", teamA));
		triangleTool.setOnTouchListener(new TouchForDragListener(triangleTool,
				"triangle", teamA));

		squareTool.setOnTouchListener(new TouchForDragListener(squareTool,
				"square", teamA));

		ballTool.setOnTouchListener(new TouchForDragListener(ballTool, "ball",
				teamA));

		playerImage2.setOnTouchListener(new TouchForDragListener(playerImage,
				"player", teamB));
		triangleTool2.setOnTouchListener(new TouchForDragListener(triangleTool,
				"triangle", teamB));

		squareTool2.setOnTouchListener(new TouchForDragListener(squareTool,
				"square", teamB));
		}
	}


	public void eraseSelected(View view) {
		drawingView.eraseSelected();
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
		switch (item.getItemId()) {
		case R.id.menu_save_document:
            String playName = drawingView.getPlay().getName();
            if(playName!=null) {
                drawingView.saveDocument(playName);
            } else {
                showPlayNameDialog();
            }
			break;
            case R.id.menu_help_document:
                openHelp();
                break;
		case R.id.menu_clear_document:
			showClearDialog();
			result = true;
			break;
		case R.id.menu_exit:
                warnAboutNotSaved();
			break;
		}

		if (!result) {
			result = super.onMenuItemSelected(featureId, item);
		}

		return result;
	}

    private void warnAboutNotSaved() {
        showDialog(WARN_NOT_SAVED_EXIT);
    }


    private void openHelp() {
        Intent intent = new Intent(this,ActHelpPage.class);
        startActivity(intent);
    }

    private void openDocument() {
		Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
		try {
			fileintent.setType("file/*");
			startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
		} catch (ActivityNotFoundException e) {
			Log.e("tag",
					"No activity can handle picking a file. Showing alternatives.");
		}

	}

	public void saveDocument(String name) {
		drawingView.saveDocument( name);

	}

	@Override
	public void onBackPressed() {
		boolean result  = false;/*drawingView.undoLast();*/
		if (!result) {
			super.onBackPressed();
		}
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!supportsDragAndDrop()){
            for(int i=0;i<menu.size();i++){
                MenuItem entry = menu.getItem(i);
                entry.setIcon(null);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void colorChanged(int color) {
        switch (id) {
            case DOTTED_LINE_ID:
                drawingView.setDottedLineColor(color);
                dottedBackground.setBackgroundColor(color);
                dottedLineMode.setChecked(true);
                break;
            case CONTINUOUS_LINE_ID:
                drawingView.setContinuousLineColor(color);
                continuousLineMode.setChecked(true);
                continuousBackground.setBackgroundColor(color);
                break;
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
				ClipData data = ClipData.newPlainText(
						dataToInput + "," + team.getNumber(), dataToInput + ","
								+ team.getNumber());
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
		Play play = drawingView.getPlay();
		String name = "";
		if(play!=null && play.getName()!=null) {
			name = play.getName();
		}
		PlaysNameDialogFragment playsNameDialog = new PlaysNameDialogFragment();
		playsNameDialog.setName(name);
		playsNameDialog.show(fm, "fragment_clear_name");
	}

	public void onFinishClearDialog(boolean clearData) {
		if (clearData) {
			drawingView.clearBoard();
		}
	}

	@Override
	public void onComponentSelected(ColorPath path) {
		playerNumber.setVisibility(View.VISIBLE);
		playerNumberButton.setVisibility(View.VISIBLE);

		trash.setVisibility(View.VISIBLE);
	}

    @Override
    public void onPlayerAdded() {
        organizationMode.setChecked(true);
    }

    @Override
	public void onComponentRelease() {
		playerNumber.setVisibility(View.GONE);
		playerNumberButton.setVisibility(View.GONE);
		trash.setVisibility(View.GONE);

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

	public boolean supportsDragAndDrop() {
		return  (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB);
	}

	private void setOrganizationMode() {
		continuousLineMode.setChecked(false);
		dottedLineMode.setChecked(false);
        playerImage.setChecked(false);
        playerImage2.setChecked(false);
        squareTool.setChecked(false);
        squareTool2.setChecked(false);
        triangleTool.setChecked(false);
        triangleTool2.setChecked(false);
		drawingView.setMode(getString(R.string.organization_mode));
	}
	
	

}
