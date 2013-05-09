package com.dewald.goodBowler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeagueNightActivity extends Activity implements OnClickListener, TextWatcher{
	
	private TextView tvName;
	private TextView tvLeague;
	private TextView tvDate;
	private TextView tvGame;
	private EditText et1Score;
	private EditText et2Score;
	private EditText et3Score;
	private EditText etSeries;
	private Button btnAdd;
	private Button btnRemove;
	private Button btnAccept;
	private Button btnList;
	private Button btnFrameGameOne;
	private Button btnFrameGameTwo;
	private Button btnFrameGameThree;
	private LinearLayout llTop;
	private BowlerDatabaseAdapter mDbHelper;
	private int requestCode;
	private Bundle extras;
	private String sqlDate;
	private String regDate;
	private String bowler;
	private String league;
	private Integer series = 0;
	
	//variables for the add and remove game feature to track 1 - 20 games per date, league, bowler
	private int gameCount = 1; //this starts at zero index to match arrays
	private int btnArray[] = {R.id.btnGame1, R.id.btnGame2, R.id.btnGame3, R.id.btnGame4, R.id.btnGame5, R.id.btnGame6, R.id.btnGame7, R.id.btnGame8, R.id.btnGame9, R.id.btnGame10,
				R.id.btnGame11, R.id.btnGame12, R.id.btnGame13, R.id.btnGame14, R.id.btnGame15, R.id.btnGame16, R.id.btnGame17, R.id.btnGame18, R.id.btnGame19, R.id.btnGame20};
	private int etArray[] = {R.id.etGame1, R.id.etGame2, R.id.etGame3, R.id.etGame4, R.id.etGame5, R.id.etGame6, R.id.etGame7, R.id.etGame8, R.id.etGame9, R.id.etGame10,
				R.id.etGame11, R.id.etGame12, R.id.etGame13, R.id.etGame14, R.id.etGame15, R.id.etGame16, R.id.etGame17, R.id.etGame18, R.id.etGame19, R.id.etGame20};
	private int llArray[] = {R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7, R.id.ll8, R.id.ll9, R.id.ll10,
				R.id.ll11, R.id.ll12, R.id.ll13, R.id.ll14, R.id.ll15, R.id.ll16, R.id.ll17, R.id.ll18, R.id.ll19, R.id.ll20};
	private int tvArray[] = {R.id.tvGame1, R.id.tvGame2, R.id.tvGame3, R.id.tvGame4, R.id.tvGame5, R.id.tvGame6, R.id.tvGame7, R.id.tvGame8, R.id.tvGame9, R.id.tvGame10,
				R.id.tvGame11, R.id.tvGame12, R.id.tvGame13, R.id.tvGame14, R.id.tvGame15, R.id.tvGame16, R.id.tvGame17, R.id.tvGame18, R.id.tvGame19, R.id.tvGame20};
	//variable to hold the scores from the database or the scores to go into the database
	private int[] scoreArray = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaguenight);
        
        mDbHelper = new BowlerDatabaseAdapter(this);
        mDbHelper.open();
        
        
        extras = getIntent().getExtras();
        sqlDate = extras.getString("SQLDate");
        regDate = extras.getString("date");
        bowler = extras.getString("bowler");
        league = extras.getString("league");
        //Log.v("sqlDate", sqlDate);
        
        tvName = (TextView)findViewById(R.id.nameTV);
        tvName.setText("Bowler Name: " + bowler);
        tvLeague = (TextView)findViewById(R.id.leagueTV);
        tvLeague.setText("League Name: " + league);
        tvDate = (TextView)findViewById(R.id.dateTV);
        tvDate.setText("Date: " + regDate);
        tvGame = (TextView)findViewById(R.id.gameTV);
        et1Score = (EditText)findViewById(R.id.etGame1);
        et1Score.addTextChangedListener(this);
        etSeries = (EditText)findViewById(R.id.series);
        btnAccept = (Button)findViewById(R.id.acceptButton);
        btnAccept.setOnClickListener(this);
        btnList = (Button)findViewById(R.id.listButton);
        btnList.setOnClickListener(this);
        btnFrameGameOne = (Button)findViewById(R.id.btnGame1);
        btnFrameGameOne.setOnClickListener(this);
        btnAdd = (Button)findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);
        btnRemove = (Button)findViewById(R.id.removeButton);
        btnRemove.setOnClickListener(this);
        llTop = (LinearLayout)findViewById(R.id.llTop);
        
        getGames();
        checkLeagueNightExists();
        calculateSeries();
        
        
	}
	
	@Override
	public void onStop(){
		super.onStop();
		updateScoreArray();
		//check to see database entry already exists and delete it so multiple records do not exist
		for(int i = 0; i < gameCount; i++){
			if(scoreArray[i] > 0){
				if(mDbHelper.checkGameRecords(bowler, league, sqlDate, "" + (i + 1)).getCount() > 0){
					Cursor c = mDbHelper.checkGameRecords(bowler, league, sqlDate, "" + (i + 1));
					c.moveToFirst();
					int id = c.getInt(0);
					c.close();
					mDbHelper.updateGameScore("" + scoreArray[i], id);
				}else{
					mDbHelper.createGameScore(bowler, league, sqlDate, "" + (i+1), "" + scoreArray[i]);
				}
			}
		}
	} 
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDbHelper.close();
	}
	
	@Override
	public void finish(){
			updateScoreArray();
			if(verifyScores()){
			//check to see database entry already exists and delete it so multiple records do not exist
			for(int i = 0; i < gameCount; i++){
				if(scoreArray[i] > 0){
					if(mDbHelper.checkGameRecords(bowler, league, sqlDate, "" + (i + 1)).getCount() > 0){
						Cursor c = mDbHelper.checkGameRecords(bowler, league, sqlDate, "" + (i + 1));
						c.moveToFirst();
						int id = c.getInt(0);
						c.close();
						mDbHelper.updateGameScore("" + scoreArray[i], id);
					}else{
						mDbHelper.createGameScore(bowler, league, sqlDate, "" + (i+1), "" + scoreArray[i]);
					}
				}
			}
			super.finish();
			}else{
				Toast t = Toast.makeText(this, "Values must be between 0 and 300", Toast.LENGTH_SHORT);
				t.show();
			}
	}
	
	//TODO fix this stuff
//	@Override
//	public void onSaveInstanceState(Bundle savedInstanceState){
//		savedInstanceState.putString("SQLDate", sqlDate);
//		savedInstanceState.putString("RegDate", regDate);
//		savedInstanceState.putString("Bowler", bowler);
//		savedInstanceState.putString("League", league);
//		savedInstanceState.putString("GameOne", et1Score.getText().toString());
//		savedInstanceState.putString("GameTwo", et2Score.getText().toString());
//		savedInstanceState.putString("GameThree", et3Score.getText().toString());
//		savedInstanceState.putString("Series", etSeries.getText().toString());
//		super.onSaveInstanceState(savedInstanceState);
//	}
//	
//	@Override
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		sqlDate = savedInstanceState.getString("SQLDate");
//		regDate = savedInstanceState.getString("RegDate");
//		bowler = savedInstanceState.getString("Bowler");
//		league = savedInstanceState.getString("League");
//		et1Score.setText(savedInstanceState.getString("GameOne"));
//		et2Score.setText(savedInstanceState.getString("GameTwo"));
//		et3Score.setText(savedInstanceState.getString("GameThree"));
//		etSeries.setText(savedInstanceState.getString("Series"));
//	}
	
	//this will query the database to get the number of games if data is entered for that date, bowler, and league
	//if it is a new entry to the database it will send 3 to the loadGames so the default of 3 games will show up
	private void getGames(){
		int numGames = 0;
		if(mDbHelper.fetchScoresForBowlerLeagueDate(bowler, league, sqlDate).getCount() > 0){
			numGames = mDbHelper.fetchScoresForBowlerLeagueDate(bowler, league, sqlDate).getCount();
		}else{
			numGames = 3;
		}
		loadGames(numGames);
		Log.v("GameCountgetGames", "" + numGames);
	}
	
	//this loads the games in the layout the first game input is added in the layout and the rest are set by what is in the database
	private void loadGames(int numOfGames){
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout parent = llTop;
		
		for(int i = 1; i < numOfGames; i++){
			TextView  tv = new TextView(this);
			tv.setText("Enter Game " + (gameCount + 1) + " Score");
			tv.setTextAppearance(this, R.style.MediumText);
			parent.addView(tv);
			View gameView = inflater.inflate(R.layout.leaguenightgame, null);
			EditText et = (EditText)gameView.findViewById(R.id.et);
			Button btn = (Button)gameView.findViewById(R.id.btn);
			LinearLayout ll = (LinearLayout)gameView.findViewById(R.id.ll);
			et.setId(etArray[gameCount]);
			et.addTextChangedListener(this);
			btn.setId(btnArray[gameCount]);
			btn.setOnClickListener(this);
			ll.setId(llArray[gameCount]);
			tv.setId(tvArray[gameCount]);
			parent.addView(gameView);
			gameCount++;
		}
		Cursor c = mDbHelper.fetchScoresForBowlerLeagueDate(bowler, league, sqlDate);
		if(c.getCount() > 0){
			c.moveToFirst();
			for(int i = 0; i < c.getCount(); i++){
				int gameNum = c.getInt(1);
				EditText et = (EditText)findViewById(etArray[gameNum - 1]);
				et.setText(c.getString(0));
				c.moveToNext();
			}
		}
	}
	
	//this adds the game after the add game button is pressed
	private void addGame(){
		if(gameCount == 20){
			Toast t = Toast.makeText(this, "Cannot add more than 20 Games", Toast.LENGTH_LONG);
			t.show();
		}else{
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TextView  tv = new TextView(this);
			tv.setText("Enter Game " + (gameCount + 1) + " Score");
			tv.setTextAppearance(this, R.style.MediumText);
			llTop.addView(tv);
			View gameView = inflater.inflate(R.layout.leaguenightgame, null);
			EditText et = (EditText)gameView.findViewById(R.id.et);
			Button btn = (Button)gameView.findViewById(R.id.btn);
			LinearLayout ll = (LinearLayout)gameView.findViewById(R.id.ll);
			tv.setId(tvArray[gameCount]);
			et.setId(etArray[gameCount]);
			et.addTextChangedListener(this);
			btn.setId(btnArray[gameCount]);
			btn.setOnClickListener(this);
			ll.setId(llArray[gameCount]);
			llTop.addView(gameView);
			gameCount++;
			Log.v("GameCount", "" + gameCount);
		}
	}
	
	//this removes the game after the remove button is pressed
	private void removeGame(){
		if(gameCount == 1){
			Toast t = Toast.makeText(this, "Cannot have less than 1 Game", Toast.LENGTH_LONG);
			t.show();
		}else{
			gameCount--;
			llTop.removeView(llTop.findViewById(tvArray[gameCount]));
			llTop.removeView(llTop.findViewById(etArray[gameCount]));
			llTop.removeView(llTop.findViewById(btnArray[gameCount]));
			llTop.removeView(llTop.findViewById(llArray[gameCount]));
		}
	}
	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch(v.getId()) {
		case R.id.acceptButton:
			updateScoreArray();
			if(verifyScores()){
			//check to see database entry already exists and delete it so multiple records do not exist
			for(int i = 0; i < gameCount; i++){
				if(scoreArray[i] > 0){
					if(mDbHelper.checkGameRecords(bowler, league, sqlDate, "" + (i + 1)).getCount() > 0){
						Cursor c = mDbHelper.checkGameRecords(bowler, league, sqlDate, "" + (i + 1));
						c.moveToFirst();
						int id = c.getInt(0);
						c.close();
						mDbHelper.updateGameScore("" + scoreArray[i], id);
					}else{
						mDbHelper.createGameScore(bowler, league, sqlDate, "" + (i+1), "" + scoreArray[i]);
					}
				}
			}
			super.finish();
			}else{
				Toast t = Toast.makeText(this, "Values must be between 0 and 300", Toast.LENGTH_SHORT);
				t.show();
			}
			break;
		case R.id.listButton:
			Intent i = new Intent(LeagueNightActivity.this, ListLeagueNight.class);
			i.putExtra("sqlDate", sqlDate);
			i.putExtra("date", regDate);
			i.putExtra("bowler", bowler);
			i.putExtra("league", league);
			startActivity(i);
			break;
		case R.id.addButton:
			addGame();
			break;
		case R.id.removeButton:
			removeGame();
			break;
		case R.id.btnGame1:
		case R.id.btnGame2:
		case R.id.btnGame3:
		case R.id.btnGame4:
		case R.id.btnGame5:
		case R.id.btnGame6:
		case R.id.btnGame7:
		case R.id.btnGame8:
		case R.id.btnGame9:
		case R.id.btnGame10:
		case R.id.btnGame11:
		case R.id.btnGame12:
		case R.id.btnGame13:
		case R.id.btnGame14:
		case R.id.btnGame15:
		case R.id.btnGame16:
		case R.id.btnGame17:
		case R.id.btnGame18:
		case R.id.btnGame19:
		case R.id.btnGame20:
			i = new Intent(LeagueNightActivity.this, ScoreCardActivity.class);
			i.putExtra("gameNumber", "" + getGameNumberFromView(viewId));
			i.putExtra("sqlDate", sqlDate);
			i.putExtra("date", regDate);
			i.putExtra("bowler", bowler);
			i.putExtra("league", league);
			startActivityForResult(i, getGameNumberFromView(viewId));
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		try{
			for(int i = 0; i < gameCount; i++){
				EditText et = (EditText)findViewById(etArray[i]);
				if(!et.getText().toString().equals("")){
					scoreArray[i] = Integer.parseInt(et.getText().toString());
				Log.v("TCCount", "" + i);
				Log.v("TCGameCount", "" + gameCount);
				}
			}
		}catch(NumberFormatException nfe){
			Toast t = Toast.makeText(this, "Values must be a number", Toast.LENGTH_SHORT);
			t.show();	
		}
		calculateSeries();
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 1){
//			switch(requestCode){
//			case 1:
//				Bundle bundle1 = data.getExtras();
//				String totalScore1 = bundle1.getString("totalScore");
//				et1Score.setText(totalScore1);
//				break;
//			case 2:
//				Bundle bundle2 = data.getExtras();
//				String totalScore2 = bundle2.getString("totalScore");
//				et2Score.setText(totalScore2);
//				break;
//			case 3:
//				Bundle bundle3 = data.getExtras();
//				String totalScore3 = bundle3.getString("totalScore");
//				et3Score.setText(totalScore3);
//				break;
//			}
			Bundle bundle = data.getExtras();
			String totalScore = bundle.getString("totalScore");
			EditText et = (EditText)findViewById(etArray[requestCode -1]);
			et.setText(totalScore);
		}
	}
	
	private void calculateSeries() {
		series = 0;
			for(int i = 0; i < scoreArray.length; i++){
				if(scoreArray[i] != -1){
					series = series + scoreArray[i];
				}
			}
		etSeries.setText("" + series);
	}
	
	
	private void checkLeagueNightExists(){
		if(mDbHelper.checkLeagueNightRecords(bowler, league, sqlDate)){
			Cursor c = mDbHelper.fetchScoresForBowlerLeagueDate(bowler, league, sqlDate);
			c.moveToFirst();
			for(int i = 0; i < c.getCount(); i++){
				scoreArray[c.getInt(1) - 1] = c.getInt(0);
				Log.v("Score", "" + scoreArray[c.getInt(1) - 1]);
				Log.v("Game #", "" + c.getInt(1));
				c.moveToNext();
			}
		}
	}
	
	private int getGameNumberFromView(int v){
		int gameNumber = 0;
			for(int i = 0; i < btnArray.length; i++){
				if(v == btnArray[i]){
					gameNumber =  i + 1;
					Log.v("GameNumber", "" + gameNumber);
				}
			}
		return gameNumber;
	}
	
	private boolean verifyScores(){
		boolean valid = false;
			for(int i = 0; i < gameCount; i++){
				valid = (scoreArray[i] >= 0 && scoreArray[i] <= 300);
				if(!valid){
					break;
				}
			}
		return valid;
	}
	
	private void updateScoreArray(){
		for(int i = 0; i < gameCount; i++){
			EditText et = (EditText)findViewById(etArray[i]);
			scoreArray[i] = Integer.parseInt(et.getText().toString());
		}
	}
}
