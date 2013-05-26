package com.dewald.goodBowler;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class Stats extends Activity {
	
	private BowlerDatabaseAdapter dbHelper;
	private Cursor cursor;
	private Bundle extras;
	private ScoreCalculator calculator;
	
	private String bowler;
	private String league;
	private String sqlDate1;
	private String sqlDate2;
	
	private TextView bowlerLeagueName;
	private TextView dateRange;
	private TextView tableAverage;
	private TextView tableTotalGames;
	private TextView tableHighGame;
	private TextView tableHighSeries;
	private TextView tableNumStrikes;
	private TextView tableStrikeChances;
	private TextView tableStrikePercent;
	private TextView tableStrikesRow;
	private TextView tableNumSpares;
	private TextView tableSpareChances;
	private TextView tableSparePercent;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		
		dbHelper = new BowlerDatabaseAdapter(this);
	    dbHelper.open();
	    
	    calculator = new ScoreCalculator();
		
		extras = getIntent().getExtras();
		bowler = extras.getString("bowler");
		league = extras.getString("league");
		sqlDate1 = extras.getString("sqlDate1");
		sqlDate2 = extras.getString("sqlDate2");
		//Log.v("bowler", bowler);
		//Log.v("league", league);
		//Log.v("sqlDate1", sqlDate1);
		//Log.v("sqlDate2", sqlDate2);
		
		bowlerLeagueName = (TextView)findViewById(R.id.bowlerLeagueName);
		bowlerLeagueName.setText(bowler + "'s " + league + " Stats");
		dateRange = (TextView)findViewById(R.id.dateRange);
		dateRange.setText("Date(s): " + sqlDate1 + " - " + sqlDate2);
		tableAverage = (TextView)findViewById(R.id.tableAverage);
		tableTotalGames = (TextView)findViewById(R.id.tableTotalGames);
		tableHighGame = (TextView)findViewById(R.id.tableHighGame);
		tableHighSeries = (TextView)findViewById(R.id.tableHighSeries);
		tableNumStrikes = (TextView)findViewById(R.id.tableNumStrikes);
		tableStrikeChances = (TextView)findViewById(R.id.tableStrikeChances);
		tableStrikePercent = (TextView)findViewById(R.id.tableStrikePercent);
		tableStrikesRow = (TextView)findViewById(R.id.tableStrikesRow);
		tableNumSpares = (TextView)findViewById(R.id.tableNumSpares);
		tableSpareChances = (TextView)findViewById(R.id.tableSpareChances);
		tableSparePercent = (TextView)findViewById(R.id.tableSparePercent);
		
		loadGameTable();
		loadStrikeSpareTable();
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		cursor.close();
		dbHelper.close();
	}
	
	private void loadGameTable(){
		List<Integer> scores = new ArrayList<Integer>();
		List<Integer> series = new ArrayList<Integer>();
		cursor = dbHelper.fetchGameData(bowler, league, sqlDate1, sqlDate2);
		//Log.v("cursor", cursor.getColumnName(0));
		//for loop to load the gameScores
		//for(int i = 0; i < 3; i++){
			cursor.moveToFirst();
			//Log.v("cursor", cursor.getColumnName(i));
			for(int r = 0; r < cursor.getCount(); r++){
				scores.add(cursor.getInt(0));
				//Log.v("values", cursor.getString(i));
				cursor.moveToNext();
			}
		//}
		cursor.close();
		
		cursor = dbHelper.fetchSeriesData(bowler, league, sqlDate1, sqlDate2);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			series.add(cursor.getInt(0));
			//Log.v("Series", cursor.getString(0));
			cursor.moveToNext();
		}
		//writes the arraylist to an Array
		Integer[] intScores = new Integer[scores.size()]; 
		for(int i = 0; i < scores.size(); i++){
			intScores[i] = scores.get(i);
		}
		Integer[] intSeries = new Integer[series.size()]; 
		for(int i = 0; i < series.size(); i++){
			intSeries[i] = series.get(i);
		}
		cursor.close();
		
		tableAverage.setText(calculator.calculateAverage(intScores));
		tableTotalGames.setText("" + scores.size());
		tableHighGame.setText(calculator.highScore(intScores));
		tableHighSeries.setText(calculator.highScore(intSeries));
		
	}
	
	private void loadStrikeSpareTable(){
		
		cursor = dbHelper.fetchStrikeData(bowler, league, sqlDate1, sqlDate2);
		String[][] scoresArray = new String[cursor.getCount()][cursor.getColumnCount()];
		DecimalFormat df = new DecimalFormat("##.##");
		
		cursor.moveToFirst();
		for(int r = 0; r < cursor.getCount(); r++){
			for(int c = 0; c < cursor.getColumnCount(); c++){
				scoresArray[r][c] = cursor.getString(c);
				//Log.v("ScoreArray", scoresArray[r][c]);
			}
			cursor.moveToNext();
		}
		Integer strikeCount = calculator.numStrikes(scoresArray);
		Integer strikeChances = cursor.getCount() * 12;
		float strikePercent = ((float)strikeCount / (float)strikeChances) * 100;
		Integer strikesRow = calculator.strikesRow(scoresArray);
		cursor.close();
		
		cursor = dbHelper.fetchSpareData(bowler, league, sqlDate1, sqlDate2);
		scoresArray = new String[cursor.getCount()][cursor.getColumnCount()];
		
		cursor.moveToFirst();
		for(int r = 0; r < cursor.getCount(); r++){
			for(int c = 0; c < cursor.getColumnCount(); c++){
				scoresArray[r][c] = cursor.getString(c);
				//Log.v("ScoreArray", scoresArray[r][c]);
			}
			cursor.moveToNext();
		}
		
		Integer spareCount = calculator.numSpares(scoresArray);
		Integer spareChances = calculator.spareChances(scoresArray);
		float sparePercent = ((float)spareCount / (float)spareChances) * 100;
		cursor.close();
		
		tableNumStrikes.setText(strikeCount.toString());
		tableStrikeChances.setText(strikeChances.toString());
		tableStrikePercent.setText(df.format(strikePercent) + "%");
		tableStrikesRow.setText(strikesRow.toString());
		tableNumSpares.setText(spareCount.toString());
		tableSpareChances.setText(spareChances.toString());
		tableSparePercent.setText(df.format(sparePercent) + "%");
	}

}
