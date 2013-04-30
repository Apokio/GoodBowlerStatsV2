package com.dewald.goodBowler;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.util.Log;

public class ScoreCardActivity extends Activity implements OnClickListener, OnItemSelectedListener, OnSeekBarChangeListener {
	
	private CheckBox pin1;
	private CheckBox pin2;
	private CheckBox pin3;
	private CheckBox pin4;
	private CheckBox pin5;
	private CheckBox pin6;
	private CheckBox pin7;
	private CheckBox pin8;
	private CheckBox pin9;
	private CheckBox pin10;
	
	private RadioButton rb_Ball1;
	private RadioButton rb_Ball2;
	private RadioButton rb_Ball3;
	
	private Spinner frameSpinner;
	private Spinner ballSpinner;
	
	private TextView scoreTV;
	private TextView markTV;
	private TextView feetTV;
	
	private Button btnAccept;
	private Button btnReset;
	private Button btnFinal;
	private Button btnStrike;
	private Button btnSpare;
	private Button btnf1;
	private Button btnf2;
	private Button btnf3;
	private Button btnf4;
	private Button btnf5;
	private Button btnf6;
	private Button btnf7;
	private Button btnf8;
	private Button btnf9;
	private Button btnf10;
	private Button btnResetFrame;
	
	private CustomSeekBar markSeekBar;
	private CustomSeekBar feetSeekBar;
	
	private String pins;
	private String[] scoreArray = {"-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-"};
	private String[] ballArray = {"-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-"};
	private String[] markArray = {"-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-"};
	private String[] feetArray = {"-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-"};
	private Integer totalScore;
	private String gameNumber = "none";
	private String date = "";
	private String ball = "";
	private Bundle extras;
	private String regDate;
	private String bowler;
	private String league;
	private String mark;
	private String feet;
	
	private BowlerDatabaseAdapter mDbHelper;
	private ScoreCalculator calculator = new ScoreCalculator();
	private Cursor cursor;
	
	private int w;
	private int h;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorerecord);
        
        mDbHelper = new BowlerDatabaseAdapter(this);
        mDbHelper.open();
        
        extras = getIntent().getExtras();
        gameNumber = extras.getString("gameNumber");
        date = extras.getString("sqlDate");
        regDate = extras.getString("date");
        bowler = extras.getString("bowler");
        league = extras.getString("league");
        Log.v("gameNumber", gameNumber);
        
        pin1 = (CheckBox)findViewById(R.id.pin1);
        pin2 = (CheckBox)findViewById(R.id.pin2);
        pin3 = (CheckBox)findViewById(R.id.pin3);
        pin4 = (CheckBox)findViewById(R.id.pin4);
        pin5 = (CheckBox)findViewById(R.id.pin5);
        pin6 = (CheckBox)findViewById(R.id.pin6);
        pin7 = (CheckBox)findViewById(R.id.pin7);
        pin8 = (CheckBox)findViewById(R.id.pin8);
        pin9 = (CheckBox)findViewById(R.id.pin9);
        pin10 = (CheckBox)findViewById(R.id.pin10);
        
        rb_Ball1 = (RadioButton)findViewById(R.id.ball1);
        rb_Ball2 = (RadioButton)findViewById(R.id.ball2);
        rb_Ball3 = (RadioButton)findViewById(R.id.ball3);
        
        frameSpinner = (Spinner)findViewById(R.id.frameSpinner);
        frameSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.frames_array , R.layout.spinnertext); 
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ballSpinner = (Spinner)findViewById(R.id.ballSpinner);
        ballSpinner.setOnItemSelectedListener(this);
        
        frameSpinner.setAdapter(adapter);
        
        scoreTV = (TextView)findViewById(R.id.scoreTV);
        markTV = (TextView)findViewById(R.id.markTV);
        markTV.setText("Touch Screen and slide to select mark thrown");
        feetTV = (TextView)findViewById(R.id.feetTV);
        feetTV.setText("Touch Screen and slide to select feet position");
        
        btnAccept = (Button)findViewById(R.id.acceptButton);
        btnAccept.setOnClickListener(this);
        btnReset = (Button)findViewById(R.id.resetButton);
        btnReset.setOnClickListener(this);
        btnFinal = (Button)findViewById(R.id.FinalButton);
        btnFinal.setOnClickListener(this);
        btnStrike = (Button)findViewById(R.id.strikeButton);
        btnStrike.setOnClickListener(this);
        btnSpare = (Button)findViewById(R.id.spareButton);
        btnSpare.setOnClickListener(this);
        btnf1 = (Button)findViewById(R.id.frame1button);
        btnf1.setOnClickListener(this);
        btnf2 = (Button)findViewById(R.id.frame2button);
        btnf2.setOnClickListener(this);
        btnf3 = (Button)findViewById(R.id.frame3button);
        btnf3.setOnClickListener(this);
        btnf4 = (Button)findViewById(R.id.frame4button);
        btnf4.setOnClickListener(this);
        btnf5 = (Button)findViewById(R.id.frame5button);
        btnf5.setOnClickListener(this);
        btnf6 = (Button)findViewById(R.id.frame6button);
        btnf6.setOnClickListener(this);
        btnf7 = (Button)findViewById(R.id.frame7button);
        btnf7.setOnClickListener(this);
        btnf8 = (Button)findViewById(R.id.frame8button);
        btnf8.setOnClickListener(this);
        btnf9 = (Button)findViewById(R.id.frame9button);
        btnf9.setOnClickListener(this);
        btnf10 = (Button)findViewById(R.id.frame10button);
        btnf10.setOnClickListener(this);
        btnResetFrame = (Button)findViewById(R.id.resetFrame);
        btnResetFrame.setOnClickListener(this);
        
        MyOnResizeListener orlResized = new MyOnResizeListener(){
        	public void OnResize(int id, int xNew, int yNew, int xOld, int yOld){
        		Bitmap bg;
        		switch(id){
        		case R.id.markSeekBar:
        			Log.v("width + height", xNew + " " + yNew);
        			bg = BitmapFactory.decodeResource(getResources(), R.drawable.arrows);
        			bg = Bitmap.createScaledBitmap(bg, xNew, yNew, false);
        			markSeekBar.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
        		break;
        		case R.id.feetSeekBar:
        			Log.v("width + height", xNew + " " + yNew);
        			bg = BitmapFactory.decodeResource(getResources(), R.drawable.feet);
        			bg = Bitmap.createScaledBitmap(bg, xNew, yNew, false);
        			feetSeekBar.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
        		}
        	}
        };
        
        try{
        	markSeekBar = (CustomSeekBar)findViewById(R.id.markSeekBar);
	        markSeekBar.setOnSeekBarChangeListener(this);
	        markSeekBar.SetOnResizeListener(orlResized);
	        markSeekBar.setMax(39);
	        markSeekBar.setThumbOffset(0);
	        Drawable myThumb = getResources().getDrawable(R.drawable.blank);
	        myThumb.setBounds(new Rect(0, 0, myThumb.getIntrinsicWidth(),myThumb.getIntrinsicHeight()));
	        markSeekBar.setProgressDrawable(null);
	        markSeekBar.setThumb(myThumb);
	        
	        feetSeekBar = (CustomSeekBar)findViewById(R.id.feetSeekBar);
	        feetSeekBar.setOnSeekBarChangeListener(this);
	        feetSeekBar.SetOnResizeListener(orlResized);
	        feetSeekBar.setMax(39);
	        feetSeekBar.setThumbOffset(0);
	        feetSeekBar.setProgressDrawable(null);
	        feetSeekBar.setThumb(myThumb);
        }catch(NullPointerException e){
        	
        }
        checkGameExists();
        updateScoreButtons();
        loadBallSpinner();
        totalScore = calculator.getTotalScore(scoreArray);    
        }
	
	@Override
	public void finish(){
		if(checkDataEntered()){
			if(mDbHelper.checkGameRecords(bowler, league, date, gameNumber).getCount() > 0){
				Cursor c = mDbHelper.checkGameRecords(bowler, league, date, gameNumber);
				c.moveToFirst();
				int id = c.getInt(0);
				c.close();
				mDbHelper.updateGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString(), id);
			}else{
			mDbHelper.createGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString());
			}
			Intent in = new Intent();
			in.putExtra("totalScore", totalScore.toString());
			setResult(1, in);
			super.finish();
		}else{
			super.finish();
		}
			
	}
	
	@Override
	public void onStop(){
		super.onStop();
		if(checkDataEntered()){
			if(checkDataEntered()){
				if(mDbHelper.checkGameRecords(bowler, league, date, gameNumber).getCount() > 0){
					Cursor c = mDbHelper.checkGameRecords(bowler, league, date, gameNumber);
					c.moveToFirst();
					int id = c.getInt(0);
					c.close();
					mDbHelper.updateGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString(), id);
				}else{
				mDbHelper.createGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString());
				}
			}
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
		cursor.close();
		mDbHelper.close();
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putStringArray("ScoreArray", scoreArray);
		savedInstanceState.putStringArray("BallArray", ballArray);
		savedInstanceState.putStringArray("FeetArray", feetArray);
		savedInstanceState.putStringArray("MarkArray", markArray);
		savedInstanceState.putString("GameNumber", gameNumber);
		savedInstanceState.putString("Date", date);
		savedInstanceState.putString("Bowler", bowler);
		savedInstanceState.putString("League", league);
		savedInstanceState.putInt("Frame", frameSpinner.getSelectedItemPosition());
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		scoreArray = savedInstanceState.getStringArray("ScoreArray");
		ballArray = savedInstanceState.getStringArray("BallArray");
		feetArray = savedInstanceState.getStringArray("FeetArray");
		markArray = savedInstanceState.getStringArray("MarkArray");
		gameNumber = savedInstanceState.getString("GameNumber");
		date = savedInstanceState.getString("Date");
		bowler = savedInstanceState.getString("Bowler");
		league = savedInstanceState.getString("League");
		frameSpinner.setSelection(savedInstanceState.getInt("Frame"));
		totalScore = calculator.getTotalScore(scoreArray);
		scoreTV.setText(totalScore.toString());
		updateScoreButtons();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.acceptButton:
			pins = pinsClicked();
			//Log.v(pins, pins);
			addScoreToArray(pins);
			totalScore = calculator.getTotalScore(scoreArray);
			//scoreTV.setText(totalScore.toString());
			updateScoreButtons();
			if(rb_Ball2.isChecked() && frameSpinner.getSelectedItemPosition() != 9){
				nextFrame();
			}else if(rb_Ball3.isChecked()){
				Toast toast = Toast.makeText(this, "Game Completed Verify Score and Finalize Game", Toast.LENGTH_SHORT);
				toast.show();
			}else if(frameSpinner.getSelectedItemPosition() == 9 && rb_Ball2.isChecked() && scoreArray[18].length() == 10 && scoreArray[19].length() != 10){
				rb_Ball3.setChecked(true);
				disableClickedPins();
				clearPins();
			}else if(frameSpinner.getSelectedItemPosition() == 9 && rb_Ball2.isChecked() && ((scoreArray[18].length() + scoreArray[19].length() < 10) || scoreArray[19].equals("-"))){
				Toast toast = Toast.makeText(this, "Game Completed Verify Score and Finalize Game", Toast.LENGTH_SHORT);
				toast.show();
				rb_Ball3.setVisibility(View.GONE);
			}else if(frameSpinner.getSelectedItemPosition() == 9 && rb_Ball2.isChecked()){
				rb_Ball3.setChecked(true);
				rb_Ball3.setVisibility(View.VISIBLE);
				enablePins();
				clearPins();
			}else if (frameSpinner.getSelectedItemPosition() == 9 && pins.length() != 10){
				rb_Ball2.setChecked(true);
				disableClickedPins();
				clearPins();
			}else if (frameSpinner.getSelectedItemPosition() == 9 && pins.length() == 10){
				rb_Ball2.setChecked(true);
				clearPins();
			}else if (pins.length() == 10){
				nextFrame();
			}else{
				nextBall();
			}
			if(checkDataEntered()){
				if(mDbHelper.checkGameRecords(bowler, league, date, gameNumber).getCount() > 0){
					Cursor c = mDbHelper.checkGameRecords(bowler, league, date, gameNumber);
					c.moveToFirst();
					int id = c.getInt(0);
					c.close();
					mDbHelper.updateGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString(), id);
				}else{
				mDbHelper.createGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString());
				}
			}
		break;
		case R.id.FinalButton:
			if(checkDataEntered()){
				if(mDbHelper.checkGameRecords(bowler, league, date, gameNumber).getCount() > 0){
					Cursor c = mDbHelper.checkGameRecords(bowler, league, date, gameNumber);
					c.moveToFirst();
					int id = c.getInt(0);
					c.close();
					mDbHelper.updateGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString(), id);
				}else{
				mDbHelper.createGame(bowler, league, date, gameNumber, scoreArray, ballArray, markArray, feetArray, totalScore.toString());
				}
			}
			Intent in = new Intent();
			in.putExtra("totalScore", totalScore.toString());
			setResult(1, in);
			finish();
			break;
		case R.id.resetButton:
			enablePins();
			clearPins();
			break;
		case R.id.strikeButton:
			strikeBall();
			break;
		case R.id.spareButton:
			spareBall();
			break;
		case R.id.resetFrame:
			resetFrame();	
			break;
		case R.id.frame1button:
			frameButtonPressed(1);
			break;
		case R.id.frame2button:
			frameButtonPressed(2);
			break;
		case R.id.frame3button:
			frameButtonPressed(3);
			break;
		case R.id.frame4button:
			frameButtonPressed(4);
			break;
		case R.id.frame5button:
			frameButtonPressed(5);
			break;
		case R.id.frame6button:
			frameButtonPressed(6);
			break;
		case R.id.frame7button:
			frameButtonPressed(7);
			break;
		case R.id.frame8button:
			frameButtonPressed(8);
			break;
		case R.id.frame9button:
			frameButtonPressed(9);
			break;
		case R.id.frame10button:
			frameButtonPressed(10);
			break;
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		switch(parent.getId()){
		case R.id.frameSpinner:
			if(frameSpinner.getSelectedItemPosition() == 9) {
				rb_Ball3.setVisibility(View.VISIBLE);
			} else {
				rb_Ball3.setVisibility(View.GONE);
			}
		break;
		case R.id.ballSpinner:
			cursor = mDbHelper.fetchBalls(bowler);
			cursor.moveToPosition(pos);
			ball = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_BALL));
			Log.v("Ball Cursor", ball);
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch(seekBar.getId()){
		case R.id.markSeekBar:
			Log.v("Mark Bar Pos", "" + progress);
			markTV.setText("Mark Selected: " + (progress + 1) + " board");
			mark = "" + (progress + 1);
			break;
		case R.id.feetSeekBar:
			Log.v("Feet Bar Pos", "" + progress);
			feetTV.setText("Feet Position: " + (progress + 1) + " board");
			feet = "" + (progress + 1);
			break;
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	public String pinsClicked() {
		String score = "";
		if(!pin1.isChecked() && !pin2.isChecked() && !pin3.isChecked() && !pin4.isChecked() && !pin5.isChecked() && !pin6.isChecked()
			&& !pin7.isChecked() && !pin8.isChecked() && !pin9.isChecked() && !pin10.isChecked()){
			score = "-";
		} else if(pin1.isChecked())
				score = score + "1";
				if(pin2.isChecked())
					score = score + "2";
					if(pin3.isChecked())
						score = score + "3";
						if(pin4.isChecked())
							score = score + "4";
							if(pin5.isChecked())
								score = score + "5";
								if(pin6.isChecked())
									score = score + "6";
									if(pin7.isChecked())
										score = score + "7";
										if(pin8.isChecked())
											score = score + "8";
											if(pin9.isChecked())
												score = score + "9";
												if(pin10.isChecked())
													score = score + "0";
	
	
		return score;
	}

	public void addScoreToArray(String pins){
		int frameNumber = frameSpinner.getSelectedItemPosition(); //gets frame number 0-9 one less
		int arrayPos = 0;
		//gets which ball the score is for if the 3rd ball only in tenth frame we know it is position 20
		if(rb_Ball1.isChecked()){
			arrayPos = frameNumber * 2;
		}else if (rb_Ball2.isChecked()){
			arrayPos = frameNumber * 2 + 1;
		}else if (rb_Ball3.isChecked()){
			arrayPos = 20;
		}
		scoreArray[arrayPos] = pins;
		ballArray[arrayPos] = ball;
		markArray[arrayPos] = mark;
		feetArray[arrayPos] = feet;
		Log.v("Ball", ballArray[arrayPos]);
	}
	
	public void clearPins(){
		pin1.setChecked(false);
		pin2.setChecked(false);
		pin3.setChecked(false);
		pin4.setChecked(false);
		pin5.setChecked(false);
		pin6.setChecked(false);
		pin7.setChecked(false);
		pin8.setChecked(false);
		pin9.setChecked(false);
		pin10.setChecked(false);
	}
	
	public void nextFrame() {
		frameSpinner.setSelection(frameSpinner.getSelectedItemPosition() + 1);
		rb_Ball1.setChecked(true);
		clearPins();
		enablePins();
	}
	
	public void nextBall() {
		rb_Ball2.setChecked(true);
		disableClickedPins();
		clearPins();
	}
	
	public void enablePins(){
		pin1.setEnabled(true);
		pin2.setEnabled(true);
		pin3.setEnabled(true);
		pin4.setEnabled(true);
		pin5.setEnabled(true);
		pin6.setEnabled(true);
		pin7.setEnabled(true);
		pin8.setEnabled(true);
		pin9.setEnabled(true);
		pin10.setEnabled(true);
		pin1.setVisibility(View.VISIBLE);
		pin2.setVisibility(View.VISIBLE);
		pin3.setVisibility(View.VISIBLE);
		pin4.setVisibility(View.VISIBLE);
		pin5.setVisibility(View.VISIBLE);
		pin6.setVisibility(View.VISIBLE);
		pin7.setVisibility(View.VISIBLE);
		pin8.setVisibility(View.VISIBLE);
		pin9.setVisibility(View.VISIBLE);
		pin10.setVisibility(View.VISIBLE);
		
	}
	
	public void disableClickedPins(){
		if(pin1.isChecked()){
			pin1.setVisibility(View.INVISIBLE);
			pin1.setEnabled(false);
		}
		if(pin2.isChecked()){
			pin2.setEnabled(false);
			pin2.setVisibility(View.INVISIBLE);
		}
		if(pin3.isChecked()){
			pin3.setEnabled(false);
			pin3.setVisibility(View.INVISIBLE);
		}
		if(pin4.isChecked()){
			pin4.setEnabled(false);
			pin4.setVisibility(View.INVISIBLE);
		}
		if(pin5.isChecked()){
			pin5.setEnabled(false);
			pin5.setVisibility(View.INVISIBLE);
		}
		if(pin6.isChecked()){
			pin6.setEnabled(false);
			pin6.setVisibility(View.INVISIBLE);
		}
		if(pin7.isChecked()){
			pin7.setEnabled(false);
			pin7.setVisibility(View.INVISIBLE);
		}
		if(pin8.isChecked()){
			pin8.setEnabled(false);
			pin8.setVisibility(View.INVISIBLE);
		}
		if(pin9.isChecked()){
			pin9.setEnabled(false);
			pin9.setVisibility(View.INVISIBLE);
		}
		if(pin10.isChecked()){
			pin10.setEnabled(false);
			pin10.setVisibility(View.INVISIBLE);
		}
	}
	
	private void checkPins(String pins){
		if(pins.contains("1")){
			pin1.setChecked(true);
		}
		if(pins.contains("2")){
			pin2.setChecked(true);
		}
		if(pins.contains("3")){
			pin3.setChecked(true);
		}
		if(pins.contains("4")){
			pin4.setChecked(true);
		}
		if(pins.contains("5")){
			pin5.setChecked(true);
		}
		if(pins.contains("6")){
			pin6.setChecked(true);
		}
		if(pins.contains("7")){
			pin7.setChecked(true);
		}
		if(pins.contains("8")){
			pin8.setChecked(true);
		}
		if(pins.contains("9")){
			pin9.setChecked(true);
		}
		if(pins.contains("0")){
			pin10.setChecked(true);
		}
	}
	
	public void strikeBall(){
		pin1.setChecked(true);
		pin2.setChecked(true);
		pin3.setChecked(true);
		pin4.setChecked(true);
		pin5.setChecked(true);
		pin6.setChecked(true);
		pin7.setChecked(true);
		pin8.setChecked(true);
		pin9.setChecked(true);
		pin10.setChecked(true);
	}
	
	public void spareBall() {
		if(pin1.isEnabled()) pin1.setChecked(true);
		if(pin2.isEnabled()) pin2.setChecked(true);
		if(pin3.isEnabled()) pin3.setChecked(true);
		if(pin4.isEnabled()) pin4.setChecked(true);
		if(pin5.isEnabled()) pin5.setChecked(true);
		if(pin6.isEnabled()) pin6.setChecked(true);
		if(pin7.isEnabled()) pin7.setChecked(true);
		if(pin8.isEnabled()) pin8.setChecked(true);
		if(pin9.isEnabled()) pin9.setChecked(true);
		if(pin10.isEnabled()) pin10.setChecked(true);
		
	}
	
	private void resetFrame(){
		enablePins();
		clearPins();
		rb_Ball1.setChecked(true);
		int frame = frameSpinner.getSelectedItemPosition();
			scoreArray[frame * 2] = "-";
			scoreArray[(frame * 2) + 1] = "-";
		if(frame == 9){ scoreArray[20] = "-"; }
		updateScoreButtons();
        totalScore = calculator.getTotalScore(scoreArray);
	}
	
	public void updateScoreButtons(){
		Integer[] frameArray = {0,0,0,0,0,0,0,0,0,0};//Array for the score per frame
		Integer[] pinsArray = calculator.createArrayScores(scoreArray);//Array to write the number visual score ex: X, 9/, 9-, -9
		String[] scoreDisplay = {"","","","","","","","","","","","","","","","","","","","",""};;//Stores what will be written to the button
		
		for(int i = 0; i < 10; i++){
			frameArray[i] = calculator.scorePerFrame(scoreArray, i+1);
		}
		//calculate the running score per frame
		for(int i = 1; i < 10; i++){
			frameArray[i] = frameArray[i-1] + frameArray[i];
		}
		
		//loop that writes the display values to the String Array for the 1st - 9th frames
		for(int i = 0; i <= 16; i+=2){
			if(pinsArray[i] == 10){
				scoreDisplay[i] = "X";
			}else if(pinsArray[i] + pinsArray[i+1] == 10){
				if(pinsArray[i] == 0){
					scoreDisplay[i] = "-";
				}else scoreDisplay[i] = pinsArray[i].toString();
				scoreDisplay[i+1] = "/"; 
			}else if(pinsArray[i] == 0 && pinsArray[i+1] == 0){
				scoreDisplay[i] = "-";
				scoreDisplay[i+1] = "-";
			}else if(pinsArray[i] < 10 & pinsArray[i+1] == 0){
				scoreDisplay[i] = pinsArray[i].toString();
				scoreDisplay[i+1] = "-";
			}else if(pinsArray[i] == 0 & pinsArray[i+1] != 0){
				scoreDisplay[i] = "-";
				scoreDisplay[i+1] = pinsArray[i+1].toString();
			}else{
				scoreDisplay[i] = pinsArray[i].toString();
				scoreDisplay[i+1] = pinsArray[i+1].toString();
			}
		}
		
		//Writes the values for the stringArray for the 10th frame
		if(pinsArray[18] == 10 && pinsArray[19] == 10 && pinsArray[20] == 10){
			scoreDisplay[18] = "X";
			scoreDisplay[19] = "X";
			scoreDisplay[20] = "X";
		}else if(pinsArray[18] == 10 && pinsArray[19] == 10 && pinsArray[20] != 10){
			scoreDisplay[18] = "X";
			scoreDisplay[19] = "X";
			if(pinsArray[20] == 0){
				scoreDisplay[20] = "-";
			}else scoreDisplay[20] = pinsArray[20].toString();
		}else if(pinsArray[18] == 10 && pinsArray[19] != 10){
			scoreDisplay[18] = "X";
			if(pinsArray[19] == 0){
				scoreDisplay[19] = "-";
			}else scoreDisplay[19] = pinsArray[19].toString();
			if(pinsArray[19] + pinsArray[20] == 10){
				scoreDisplay[20] = "/";
			}else if(pinsArray[20] == 0){
				scoreDisplay[20] = "-";
			}else scoreDisplay[20] = pinsArray[20].toString();
		}else if(pinsArray[18] + pinsArray[19] == 10){
			if(pinsArray[18] == 0){
				scoreDisplay[18] = "-";
			}else scoreDisplay[18] = pinsArray[18].toString();
			scoreDisplay[19] = "/";
			if(pinsArray[20] == 10){
				scoreDisplay[20] = "X";
			}else if(pinsArray[20] == 0){
				scoreDisplay[20] = "-";
			}else scoreDisplay[20] = pinsArray[20].toString();
		}else if(pinsArray[18] != 10){
			if(pinsArray[18] == 0){
				scoreDisplay[18] = "-";
			}else scoreDisplay[18] = pinsArray[18].toString();
			if(pinsArray[19] == 0){
				scoreDisplay[19] = "-";
			}else scoreDisplay[19] = pinsArray[19].toString();
		}
		
		btnf1.setText(scoreDisplay[0] + scoreDisplay[1] + "\n" + frameArray[0].toString() + "\n1");
		btnf2.setText(scoreDisplay[2] + scoreDisplay[3] + "\n" + frameArray[1].toString() + "\n2");
		btnf3.setText(scoreDisplay[4] + scoreDisplay[5] + "\n" + frameArray[2].toString() + "\n3");
		btnf4.setText(scoreDisplay[6] + scoreDisplay[7] + "\n" + frameArray[3].toString() + "\n4");
		btnf5.setText(scoreDisplay[8] + scoreDisplay[9] + "\n" + frameArray[4].toString() + "\n5");
		btnf6.setText(scoreDisplay[10] + scoreDisplay[11] + "\n" + frameArray[5].toString() + "\n6");
		btnf7.setText(scoreDisplay[12] + scoreDisplay[13] + "\n" + frameArray[6].toString() + "\n7");
		btnf8.setText(scoreDisplay[14] + scoreDisplay[15] + "\n" + frameArray[7].toString() + "\n8");
		btnf9.setText(scoreDisplay[16] + scoreDisplay[17] + "\n" + frameArray[8].toString() + "\n9");
		btnf10.setText(scoreDisplay[18] + scoreDisplay[19] + scoreDisplay[20] + "\n" + frameArray[9].toString() + "\n10");
		//Log.e("101", scoreDisplay[18]);
		//Log.e("102", scoreDisplay[19]);
		//Log.e("103", scoreDisplay[20]);
	}
	
	private void frameButtonPressed(int frame){
		switch(frame){
		case 1:
			frameSpinner.setSelection(0);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[0].equals("-") && scoreArray[1].equals("-")){
				resetFrame();
			}else if(scoreArray[0].length() == 10){
				strikeBall();
			}else if(scoreArray[0].length() + scoreArray[1].length() > 9 && !scoreArray[1].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[0]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[0]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[1]);
			}
			break;
		case 2:
			frameSpinner.setSelection(1);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[2].equals("-") && scoreArray[3].equals("-")){
				resetFrame();
			}else if(scoreArray[2].length() == 10){
				strikeBall();
			}else if(scoreArray[2].length() + scoreArray[3].length() > 9 && !scoreArray[3].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[2]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[2]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[3]);
			}
			break;
		case 3:
			frameSpinner.setSelection(2);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[4].equals("-") && scoreArray[5].equals("-")){
				resetFrame();
			}else if(scoreArray[4].length() == 10){
				strikeBall();
			}else if(scoreArray[4].length() + scoreArray[5].length() > 9 && !scoreArray[5].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[4]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[4]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[5]);
			}
			break;
		case 4:
			frameSpinner.setSelection(3);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[6].equals("-") && scoreArray[7].equals("-")){
				resetFrame();
			}else if(scoreArray[6].length() == 10){
				strikeBall();
			}else if(scoreArray[6].length() + scoreArray[7].length() > 9 && !scoreArray[7].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[6]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[6]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[7]);
			}
			break;
		case 5:
			frameSpinner.setSelection(4);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[8].equals("-") && scoreArray[9].equals("-")){
				resetFrame();
			}else if(scoreArray[8].length() == 10){
				strikeBall();
			}else if(scoreArray[8].length() + scoreArray[9].length() > 9 && !scoreArray[9].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[8]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[8]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[9]);
			}
			break;
		case 6:
			frameSpinner.setSelection(5);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[10].equals("-") && scoreArray[11].equals("-")){
				resetFrame();
			}else if(scoreArray[10].length() == 10){
				strikeBall();
			}else if(scoreArray[10].length() + scoreArray[11].length() > 9 && !scoreArray[11].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[10]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[10]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[11]);
			}
			break;
		case 7:
			frameSpinner.setSelection(6);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[12].equals("-") && scoreArray[13].equals("-")){
				resetFrame();
			}else if(scoreArray[12].length() == 10){
				strikeBall();
			}else if(scoreArray[12].length() + scoreArray[13].length() > 9 && !scoreArray[13].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[12]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[12]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[13]);
			}
			break;
		case 8:
			frameSpinner.setSelection(7);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[14].equals("-") && scoreArray[15].equals("-")){
				resetFrame();
			}else if(scoreArray[14].length() == 10){
				strikeBall();
			}else if(scoreArray[14].length() + scoreArray[15].length() > 9 && !scoreArray[15].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[14]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[14]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[15]);
			}
			break;
		case 9:
			frameSpinner.setSelection(8);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[16].equals("-") && scoreArray[17].equals("-")){
				resetFrame();
			}else if(scoreArray[16].length() == 10){
				strikeBall();
			}else if(scoreArray[16].length() + scoreArray[17].length() > 9 && !scoreArray[17].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[16]);
				disableClickedPins();
				spareBall();
			}else {
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[16]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[17]);
			}
			break;
		case 10:
			frameSpinner.setSelection(9);
			rb_Ball1.setChecked(true);
			clearPins();
			enablePins();
			if(scoreArray[18].equals("-") && scoreArray[19].equals("-")){
				resetFrame();
			}else if(scoreArray[20].length() == 10){
				rb_Ball3.setVisibility(View.VISIBLE);
				rb_Ball3.setChecked(true);
				strikeBall();
			}else if(scoreArray[18].length() == 10 && scoreArray[19].length() < 10){
				rb_Ball3.setVisibility(View.VISIBLE);
				rb_Ball3.setChecked(true);
				checkPins(scoreArray[19]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[20]);
			}else if(scoreArray[18].length() == 10 && scoreArray[19].length() == 10){
				rb_Ball3.setVisibility(View.VISIBLE);
				rb_Ball3.setChecked(true);
				checkPins(scoreArray[20]);
			}else if(scoreArray[18].length() + scoreArray[19].length() < 10 || scoreArray[19].equals("-")){
				rb_Ball2.setChecked(true);
				checkPins(scoreArray[18]);
				disableClickedPins();
				clearPins();
				checkPins(scoreArray[19]);
			}else if(scoreArray[18].length() + scoreArray[19].length() > 9 && !scoreArray[19].equals("-")){
				rb_Ball3.setChecked(true);
				checkPins(scoreArray[20]);
			}else {
				rb_Ball1.setChecked(true);
				checkPins(scoreArray[18]);
			}
			break;
		}
	}
	
	private void checkGameExists(){
		if(mDbHelper.checkGameRecords(bowler, league, date, gameNumber).getCount() > 0){
			Cursor c = mDbHelper.fetchPinsData(bowler, league, date, gameNumber);
			c.moveToFirst();
			for(int i = 0; i < 21; i++){
				scoreArray[i] = c.getString(i);
			}
			c.close();
			c = mDbHelper.fetchBallData(bowler, league, date, gameNumber);
			c.moveToFirst();
			for(int i = 0; i < 21; i++){
				ballArray[i] = c.getString(i);
			}
			c.close();
			c = mDbHelper.fetchFeetData(bowler, league, date, gameNumber);
			c.moveToFirst();
			for(int i = 0; i < 21; i++){
				feetArray[i] = c.getString(i);
			}
			c.close();
			c = mDbHelper.fetchMarkData(bowler, league, date, gameNumber);
			c.moveToFirst();
			for(int i = 0; i < 21; i++){
				markArray[i] = c.getString(i);
			}
			c.close();
		}
	}
	
	private boolean checkDataEntered(){
		boolean data = false;
		for(int i = 0; i < scoreArray.length; i++){
			if(!scoreArray[i].equals("-")){
				data = true;
			}
		}
		return data;
	}
	
	private void loadBallSpinner(){
		cursor = mDbHelper.fetchBalls(bowler);
		startManagingCursor(cursor);
		
		String[] from = new String[] {BowlerDatabaseAdapter.KEY_BALL};
		int[] to = new int[] {android.R.id.text1};
		
		SimpleCursorAdapter balls = new SimpleCursorAdapter(this, R.layout.spinnertext, cursor, from, to);
		balls.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		ballSpinner.setAdapter(balls);
	}
}
