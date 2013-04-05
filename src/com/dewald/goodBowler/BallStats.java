package com.dewald.goodBowler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BallStats extends Activity{

	private BowlerDatabaseAdapter dbHelper;
	private Cursor cursor;
	private Bundle extras;
	private ScoreCalculator calculator;
	
	private String bowler;
	private String league;
	private String sqlDate1;
	private String sqlDate2;
	private String ball;
	
	private TextView bowlerLeagueBallName;
	private TextView dateRange;
	private TextView tableNumStrikes;
	private TextView tableStrikeChances;
	private TextView tableStrikePercent;
	private TextView tableNumSpares;
	private TextView tableSpareChances;
	private TextView tableSparePercent;
	private TextView tableSinglePinConversion;
	private TextView tableSinglePinChances;
	private TextView tableSinglePinPercent;
	private TextView tableMultiPinConversion;
	private TextView tableMultiPinChances;
	private TextView tableMultiPinPercent;
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ballstats);
		
		dbHelper = new BowlerDatabaseAdapter(this);
	    dbHelper.open();
	    
	    calculator = new ScoreCalculator();
		
		extras = getIntent().getExtras();
		bowler = extras.getString("bowler");
		league = extras.getString("league");
		sqlDate1 = extras.getString("sqlDate1");
		sqlDate2 = extras.getString("sqlDate2");
		ball = extras.getString("ball");
		Log.v("bowler", bowler);
		Log.v("league", league);
		Log.v("sqlDate1", sqlDate1);
		Log.v("sqlDate2", sqlDate2);
		Log.v("ball", ball);
		
		bowlerLeagueBallName = (TextView)findViewById(R.id.bowlerLeagueBallName);
		bowlerLeagueBallName.setText(bowler + "'s " + ball + " Stats for " + league);
		dateRange = (TextView)findViewById(R.id.dateRange);
		dateRange.setText("Date(s): " + sqlDate1 + " - " + sqlDate2);
		tableNumStrikes = (TextView)findViewById(R.id.tableNumStrikes);
		tableStrikeChances = (TextView)findViewById(R.id.tableStrikeChances);
		tableStrikePercent = (TextView)findViewById(R.id.tableStrikePercent);
		tableNumSpares = (TextView)findViewById(R.id.tableNumSpares);
		tableSpareChances = (TextView)findViewById(R.id.tableSpareChances);
		tableSparePercent = (TextView)findViewById(R.id.tableSparePercent);
		tableSinglePinConversion = (TextView)findViewById(R.id.tableSinglePinConversion);
		tableSinglePinChances = (TextView)findViewById(R.id.tableSinglePinChances);
		tableSinglePinPercent = (TextView)findViewById(R.id.tableSinglePinPercent);
		tableMultiPinConversion = (TextView)findViewById(R.id.tableMultiPinConversion);
		tableMultiPinChances = (TextView)findViewById(R.id.tableMultiPinChances);
		tableMultiPinPercent = (TextView)findViewById(R.id.tableMultiPinPercent);
		
		loadStrikeTable();
		loadSpareTables();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		dbHelper.close();
	}
	
	private void loadStrikeTable(){
		Integer numStrikes = 0;
		Integer strikeChances = 0;
		float strikePercent = 0.0f;
		DecimalFormat df = new DecimalFormat("##.##");
		
		cursor = dbHelper.fetchBallStrikeData(bowler, league, sqlDate1, sqlDate2);
		cursor.moveToFirst();
		for(int r = 0; r < cursor.getCount(); r++){
			for(int c = 1; c < cursor.getColumnCount(); c+=2){
				if(ball.equals(cursor.getString(c))){
					if(cursor.getString(c - 1).length() == 10){
						numStrikes += 1;
					}
					strikeChances += 1;
				}
			}
			cursor.moveToNext();
		}
		
		strikePercent = (float)numStrikes / (float)strikeChances * 100;
		cursor.close();
		
		
		tableNumStrikes.setText(numStrikes.toString());
		tableStrikeChances.setText(strikeChances.toString());
		tableStrikePercent.setText(df.format(strikePercent) + "%");	
	}
	
	private void loadSpareTables(){
		Integer numSpares = 0;
		Integer spareChances = 0;
		float sparePercent = 0.0f;
		Integer singleSpares = 0;
		Integer singleSpareChances = 0;
		float singleSparePercent = 0.0f;
		Integer multiSpares = 0;
		Integer multiSpareChances = 0;
		float multiSparePercent = 0.0f;
		DecimalFormat df = new DecimalFormat("##.##");
		
		cursor = dbHelper.fetchBallSpareData(bowler, league, sqlDate1, sqlDate2);
		cursor.moveToFirst();
		
		//this loop calculates the spare totals for the 1st - 10th frame up to the last ball
		for(int r = 0; r < cursor.getCount(); r++){
			for(int c = 2; c < 30; c+=3){
				if(ball.equals(cursor.getString(c))){
					if((cursor.getString(c - 2).length() != 10 && cursor.getString(c - 2).length() + cursor.getString(c - 1).length() == 10) && !cursor.getString(c - 1).equals("-")){
						numSpares += 1;
					}
					if((cursor.getString(c - 2).length() == 9 && cursor.getString(c - 1).length() == 1) && !cursor.getString(c - 1).equals("-")){
						singleSpares += 1;
					}
					if(cursor.getString(c - 2).length() != 10){
						spareChances += 1;
					}
					if(cursor.getString(c - 2).length() == 9){
						singleSpareChances += 1;
					}
				}
			}
			//this does the ball 3 in the last frame if the possibility of a spare exists there
			if(ball.equals(cursor.getString(31))){
				if((cursor.getString(27).length() == 10 && cursor.getString(28).length() != 10) && 
				   (cursor.getString(28).length() + cursor.getString(30).length() == 10 && cursor.getString(30).equals("-"))){
					numSpares += 1;
				}
				if((cursor.getString(27).length() == 10 && cursor.getString(28).length() == 9) && 
						   (cursor.getString(28).length() + cursor.getString(30).length() == 10 && cursor.getString(30).equals("-"))){
							singleSpares += 1;
						}
				if(cursor.getString(27).length() == 10 && cursor.getString(28).length() != 10){
					spareChances += 1;
				}
				if(cursor.getString(27).length() == 10 && cursor.getString(28).length() == 9){
					spareChances += 1;
				}
			}
			cursor.moveToNext();
		}
		
		multiSpares = numSpares - singleSpares;
		multiSpareChances = spareChances - singleSpareChances;
		
		sparePercent = (float)numSpares / (float)spareChances * 100;
		singleSparePercent = (float)singleSpares / (float)singleSpareChances * 100;
		multiSparePercent = (float)multiSpares / (float)multiSpareChances * 100;
		
		tableNumSpares.setText(numSpares.toString());
		tableSpareChances.setText(spareChances.toString());
		tableSparePercent.setText(df.format(sparePercent) + "%");
		tableSinglePinConversion.setText(singleSpares.toString());
		tableSinglePinChances.setText(singleSpareChances.toString());
		tableSinglePinPercent.setText(df.format(singleSparePercent));
		tableMultiPinConversion.setText(multiSpares.toString());
		tableMultiPinChances.setText(multiSpareChances.toString());
		tableMultiPinPercent.setText(df.format(multiSparePercent));
	}
}
