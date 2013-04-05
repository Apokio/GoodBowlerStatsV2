package com.dewald.goodBowler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BowlerDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "bowlerdata";
	private static final int DATABASE_VERSION = 2; //changed version number for v2.0
	
	// Database creation sql statement
	private static final String BOWLER_DATABASE_CREATE = "CREATE TABLE bowler (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT NOT NULL, average TEXT NOT NULL);";
	private static final String LEAGUE_DATABASE_CREATE = "CREATE TABLE league (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "leaguename TEXT NOT NULL, house TEXT NOT NULL, bowlername TEXT NOT NULL);";
	//Old Table used in v1.0 left here for historical reference
	/*private static final String LEAGUE_NIGHT_DATABASE_CREATE = "CREATE TABLE leaguenight (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "bowlername TEXT NOT NULL, leaguename TEXT NOT NULL, date TEXT NOT NULL, gameonescore INTEGER NOT NULL, " 
					+ "gametwoscore INTEGER NOT NULL, gamethreescore INTEGER NOT NULL, seriesscore INTEGER NOT NULL);"; */
	//
	private static final String BOWLING_GAME_DATABASE_CREATE = "CREATE TABLE game (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "bowlername TEXT NOT NULL, leaguename TEXT NOT NULL, date TEXT NOT NULL, gamenumber TEXT NOT NULL, "
					+ "f1b1pins TEXT NOT NULL, f1b1ball TEXT NOT NULL, f1b1mark TEXT NOT NULL, f1b1feet TEXT NOT NULL, " 
					+ "f1b2pins TEXT NOT NULL, f1b2ball TEXT NOT NULL, f1b2mark TEXT NOT NULL, f1b2feet TEXT NOT NULL, "
					+ "f2b1pins TEXT NOT NULL, f2b1ball TEXT NOT NULL, f2b1mark TEXT NOT NULL, f2b1feet TEXT NOT NULL, "
					+ "f2b2pins TEXT NOT NULL, f2b2ball TEXT NOT NULL, f2b2mark TEXT NOT NULL, f2b2feet TEXT NOT NULL, "
					+ "f3b1pins TEXT NOT NULL, f3b1ball TEXT NOT NULL, f3b1mark TEXT NOT NULL, f3b1feet TEXT NOT NULL, "
					+ "f3b2pins TEXT NOT NULL, f3b2ball TEXT NOT NULL, f3b2mark TEXT NOT NULL, f3b2feet TEXT NOT NULL, "
					+ "f4b1pins TEXT NOT NULL, f4b1ball TEXT NOT NULL, f4b1mark TEXT NOT NULL, f4b1feet TEXT NOT NULL, "
					+ "f4b2pins TEXT NOT NULL, f4b2ball TEXT NOT NULL, f4b2mark TEXT NOT NULL, f4b2feet TEXT NOT NULL, "
					+ "f5b1pins TEXT NOT NULL, f5b1ball TEXT NOT NULL, f5b1mark TEXT NOT NULL, f5b1feet TEXT NOT NULL, "
					+ "f5b2pins TEXT NOT NULL, f5b2ball TEXT NOT NULL, f5b2mark TEXT NOT NULL, f5b2feet TEXT NOT NULL, "
					+ "f6b1pins TEXT NOT NULL, f6b1ball TEXT NOT NULL, f6b1mark TEXT NOT NULL, f6b1feet TEXT NOT NULL, "
					+ "f6b2pins TEXT NOT NULL, f6b2ball TEXT NOT NULL, f6b2mark TEXT NOT NULL, f6b2feet TEXT NOT NULL, "
					+ "f7b1pins TEXT NOT NULL, f7b1ball TEXT NOT NULL, f7b1mark TEXT NOT NULL, f7b1feet TEXT NOT NULL, "
					+ "f7b2pins TEXT NOT NULL, f7b2ball TEXT NOT NULL, f7b2mark TEXT NOT NULL, f7b2feet TEXT NOT NULL, "
					+ "f8b1pins TEXT NOT NULL, f8b1ball TEXT NOT NULL, f8b1mark TEXT NOT NULL, f8b1feet TEXT NOT NULL, "
					+ "f8b2pins TEXT NOT NULL, f8b2ball TEXT NOT NULL, f8b2mark TEXT NOT NULL, f8b2feet TEXT NOT NULL, "
					+ "f9b1pins TEXT NOT NULL, f9b1ball TEXT NOT NULL, f9b1mark TEXT NOT NULL, f9b1feet TEXT NOT NULL, "
					+ "f9b2pins TEXT NOT NULL, f9b2ball TEXT NOT NULL, f9b2mark TEXT NOT NULL, f9b2feet TEXT NOT NULL, "
					+ "f10b1pins TEXT NOT NULL, f10b1ball TEXT NOT NULL, f10b1mark TEXT NOT NULL, f10b1feet TEXT NOT NULL, "
					+ "f10b2pins TEXT NOT NULL, f10b2ball TEXT NOT NULL, f10b2mark TEXT NOT NULL, f10b2feet TEXT NOT NULL, "
					+ "f10b3pins TEXT NOT NULL, f10b3ball TEXT NOT NULL, f10b3mark TEXT NOT NULL, f10b3feet TEXT NOT NULL, "
					+ "score INTEGER NOT NULL);"; //added this line for version 2.0)";
	
	private static final String BALL_DATABASE_CREATE = "CREATE TABLE ball (_id INTEGER PRIMARY KEY AUTOINCREMENT, bowlername TEXT NOT NULL, bowlingball TEXT NOT NULL)"; 
	
	public BowlerDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	//Method called when the database is created
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(BOWLER_DATABASE_CREATE);
		database.execSQL(LEAGUE_DATABASE_CREATE);
		//database.execSQL(LEAGUE_NIGHT_DATABASE_CREATE); taken out for v2.0 data is stored in game table
		database.execSQL(BOWLING_GAME_DATABASE_CREATE);
		database.execSQL(BALL_DATABASE_CREATE);
	}

	@Override
	//Method used to update the version of the database
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Cursor cursor;
		Log.w(BowlerDatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + "!!!!!!");
		//Version 2.0 database upgrades
		//alter game table to add score row
		database.execSQL("ALTER TABLE game ADD COLUMN score INTEGER '0'");
		//write game scores from league night table to new table
		//get a cursor for all the data in the leaguenight table
		cursor = database.query("leaguenight", null, null, null, null, null, null);
		//write all the game data to the proper row in the new table according to the date, leaguename, and bowlername
		cursor.moveToFirst();
		for(int r = 0; r < cursor.getCount(); r++){
			String bowler = cursor.getString(1);
			String league = cursor.getString(2);
			String date = cursor.getString(3);
			String score1 = cursor.getString(4);
			String score2 = cursor.getString(5);
			String score3 = cursor.getString(6);
			database.update("game", createGame(score1), "bowlername = '" + bowler + "' AND leaguename = '" + league + "' AND date = '" + date + "' AND gamenumber = '1'", null);
			database.update("game", createGame(score2), "bowlername = '" + bowler + "' AND leaguename = '" + league + "' AND date = '" + date + "' AND gamenumber = '2'", null);
			database.update("game", createGame(score3), "bowlername = '" + bowler + "' AND leaguename = '" + league + "' AND date = '" + date + "' AND gamenumber = '3'", null);
			cursor.moveToNext();
		}
		//drop league night table
		database.execSQL("DROP TABLE IF EXISTS leaguenight");
	}
	
	private ContentValues createGame(String score){
		ContentValues values = new ContentValues();
		values.put("score", score);
		return values;
	}

}
