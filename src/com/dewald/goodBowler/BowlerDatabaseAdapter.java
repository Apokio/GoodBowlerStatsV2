package com.dewald.goodBowler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BowlerDatabaseAdapter {
	
	//Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_AVERAGE = "average";
	public static final String BOWLER_TABLE = "bowler";
	public static final String KEY_LEAGUE_NAME = "leaguename";
	public static final String KEY_HOUSE = "house";
	public static final String KEY_BOWLER_NAME = "bowlername";
	public static final String KEY_BALL = "bowlingball";
	public static final String BALL_TABLE = "ball";
	public static final String LEAGUE_TABLE = "league";
	//public static final String LEAGUE_NIGHT_TABLE = "leaguenight"; not used in version 2.0
	public static final String GAME_TABLE = "game";
	public static final String KEY_DATE = "date";
	//public static final String KEY_GAME_ONE_SCORE = "gameonescore";
	//public static final String KEY_GAME_TWO_SCORE = "gametwoscore";
	//public static final String KEY_GAME_THREE_SCORE = "gamethreescore";
	//public static final String KEY_SERIES_SCORE = "seriesscore";
	public static final String KEY_GAME_NUMBER = "gamenumber";
	public static final String KEY_SCORE = "score";
	public static final String[] PINS_KEY = {"f1b1pins", "f1b2pins", "f2b1pins", "f2b2pins", 
											 "f3b1pins", "f3b2pins", "f4b1pins", "f4b2pins",
											 "f5b1pins", "f5b2pins", "f6b1pins", "f6b2pins",
											 "f7b1pins", "f7b2pins", "f8b1pins", "f8b2pins",
											 "f9b1pins", "f9b2pins", "f10b1pins", "f10b2pins", "f10b3pins"};
	public static final String[] BALL_KEY = {"f1b1ball", "f1b2ball", "f2b1ball", "f2b2ball", 
		 									 "f3b1ball", "f3b2ball", "f4b1ball", "f4b2ball",
		 									 "f5b1ball", "f5b2ball", "f6b1ball", "f6b2ball",
		 									 "f7b1ball", "f7b2ball", "f8b1ball", "f8b2ball",
		 									 "f9b1ball", "f9b2ball", "f10b1ball", "f10b2ball", "f10b3ball"};
	public static final String[] MARK_KEY = {"f1b1mark", "f1b2mark", "f2b1mark", "f2b2mark", 
		 									 "f3b1mark", "f3b2mark", "f4b1mark", "f4b2mark",
		 									 "f5b1mark", "f5b2mark", "f6b1mark", "f6b2mark",
		 									 "f7b1mark", "f7b2mark", "f8b1mark", "f8b2mark",
		 									 "f9b1mark", "f9b2mark", "f10b1mark", "f10b2mark", "f10b3mark"};
	public static final String[] FEET_KEY = {"f1b1feet", "f1b2feet", "f2b1feet", "f2b2feet", 
		 								     "f3b1feet", "f3b2feet", "f4b1feet", "f4b2feet",
		 								     "f5b1feet", "f5b2feet", "f6b1feet", "f6b2feet",
		 								     "f7b1feet", "f7b2feet", "f8b1feet", "f8b2feet",
		 								     "f9b1feet", "f9b2feet", "f10b1feet", "f10b2feet", "f10b3feet"};
	public static final String[] STRIKE_KEY = { "f1b1pins","f2b1pins", 
		 										"f3b1pins","f4b1pins",
		 										"f5b1pins","f6b1pins",
		 										"f7b1pins","f8b1pins",
		 										"f9b1pins","f10b1pins", "f10b2pins", "f10b3pins"};
	public static final String[] PINS_ROW_KEY = {"_id", "f1b1pins", "f1b2pins", "f2b1pins", "f2b2pins", 
		 										"f3b1pins", "f3b2pins", "f4b1pins", "f4b2pins",
		 										"f5b1pins", "f5b2pins", "f6b1pins", "f6b2pins",
		 										"f7b1pins", "f7b2pins", "f8b1pins", "f8b2pins",
		 										"f9b1pins", "f9b2pins", "f10b1pins", "f10b2pins", "f10b3pins"};
	public static final String[] BALL_STRIKE_KEY = { "f1b1pins", "f1b1ball", "f2b1pins", "f2b1ball",
													 "f3b1pins", "f3b1ball", "f4b1pins", "f4b1ball",
													 "f5b1pins", "f5b1ball", "f6b1pins", "f6b1ball",
													 "f7b1pins", "f7b1ball", "f8b1pins", "f8b1ball",
													 "f9b1pins", "f9b1ball", "f10b1pins", "f10b1ball", 
													 "f10b2pins", "f10b2ball", "f10b3pins", "f10b3ball"};
	public static final String[] BALL_SPARE_KEY = { "f1b1pins", "f1b2pins", "f1b2ball", 
													"f2b1pins", "f2b2pins", "f2b2ball",
		 											"f3b1pins", "f3b2pins", "f3b2ball", 
		 											"f4b1pins", "f4b2pins", "f4b2ball",
		 											"f5b1pins", "f5b2pins", "f5b2ball", 
		 											"f6b1pins", "f6b2pins", "f6b2ball",
		 											"f7b1pins", "f7b2pins", "f7b2ball", 
		 											"f8b1pins", "f8b2pins", "f8b2ball",
		 											"f9b1pins", "f9b2pins", "f9b2ball", 
		 											"f10b1pins", "f10b2pins", "f10b2ball", 
		 											"f10b3pins", "f10b3ball"};
	private Context context;
	private SQLiteDatabase database;
	private BowlerDatabaseHelper dbHelper;
	
	public BowlerDatabaseAdapter (Context context) {
		this.context = context;
	}
	
	public BowlerDatabaseAdapter open() throws SQLException {
		dbHelper = new BowlerDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}
	
	//Creates a new bowler and returns the new rowId for the bowler, otherwise returns a -1 to show failure
	public long createBowler(String name, String average) {
		ContentValues initialValues = createBowlerContentValues(name, average);
		return database.insert(BOWLER_TABLE, null, initialValues);
	}
	
	//Creates a new ball entry
	public long createBall(String name, String ball) {
		ContentValues initialValues = createBallContentValues(name, ball);
		return database.insert(BALL_TABLE, null, initialValues);
	}
	
	//updates the bowler information
	public boolean updateBowler (long rowId, String name, String average) {
		ContentValues updateValues = createBowlerContentValues(name, average);
		return database.update(BOWLER_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	//deletes the bowler information
	public boolean deleteBowler(String bowler) {
		database.delete(GAME_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) + "", null);
		//database.delete(LEAGUE_NIGHT_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) + "", null);
		database.delete(LEAGUE_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) + "", null);
		return database.delete(BOWLER_TABLE, KEY_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +"", null) > 0;
	}
	
	//Return a Cursor over the list of all bowlers and averages in the database
	public Cursor fetchAllBowlers() {
		return database.query(BOWLER_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_AVERAGE }, null, null, null, null, null);
	}
	
	//Returns a Cursor for the Names of the Bowlers in the database
	public Cursor fetchAllNames() {
		return database.query(BOWLER_TABLE, new String[] { KEY_ROWID, KEY_NAME }, null, null, null, null, null);
	}
	
	//Returns a Cursor for the all the Bowling Balls for a selected Bowler in the database
	public Cursor fetchBalls(String bowler) {
		return database.query(BALL_TABLE, new String[] { KEY_ROWID, KEY_BALL }, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) + "", null, null, null, null);
	}
	
	//deletes the ball at the selected rowid
	public boolean deleteBall(Long id){
		return database.delete(BALL_TABLE, KEY_ROWID + "=" + id, null) > 0;
	}
	
	//Return a Cursor positioned at the defined bowler
	public Cursor fetchBowler(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, BOWLER_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_AVERAGE }, KEY_ROWID + "=" +rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	//inserts new information to create a new league entry
	public long createLeague(String leagueName, String house, String bowlerName) {
		ContentValues initialValues = createLeagueContentValues(leagueName, house, bowlerName);
		return database.insert(LEAGUE_TABLE, null, initialValues);
	}
	
	public boolean deleteLeague(String bowler, String league) {
		database.delete(GAME_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +"", null);
		//database.delete(LEAGUE_NIGHT_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +"", null);
		return database.delete(LEAGUE_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +"", null) > 0;
	}
	
	//fetches all the data from the league table
	public Cursor fetchAllLeagues() {
		return database.query(LEAGUE_TABLE, new String[] { KEY_ROWID, KEY_LEAGUE_NAME, KEY_HOUSE, KEY_BOWLER_NAME }, null, null, null, null, null);
	}
	
	//fetches all the league names
	public Cursor fetchAllLeagueNames() {
		return database.query(LEAGUE_TABLE, new String[] { KEY_ROWID, KEY_LEAGUE_NAME }, null, null, null, null, null);
	}
	
	//fetches leagues that a certain bowler is associated with
	public Cursor fetchLeaguesForBowler(String bowler) {
		return database.query(LEAGUE_TABLE, new String[] { KEY_ROWID, KEY_LEAGUE_NAME }, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +"", null, null, null, null);
	}
	
	//not used in version 2.0 must rewrite to write to the game table only
	/* public long createLeagueNight(String bowlerName, String leagueName, String date, Integer gameOne, Integer gameTwo, Integer gameThree, Integer series) {
		ContentValues initialValues = createLeagueNightContentValues(bowlerName, leagueName, date, gameOne, gameTwo, gameThree, series);
		return database.insert(LEAGUE_NIGHT_TABLE, null, initialValues);
	} */
	
	/*public Cursor fetchAllLeagueNight() {
		return database.query(LEAGUE_NIGHT_TABLE, new String[] { KEY_ROWID, KEY_BOWLER_NAME, KEY_LEAGUE_NAME, KEY_DATE, KEY_GAME_ONE_SCORE, KEY_GAME_TWO_SCORE,
								KEY_GAME_THREE_SCORE, KEY_SERIES_SCORE }, null, null, null, null, null);
	} */
	
	////not used in version 2.0 must rewrite to write to the game table only
	/* public Cursor fetchScoresForBowler(String bowler) {
		return database.query(LEAGUE_NIGHT_TABLE, new String[] { KEY_ROWID, KEY_BOWLER_NAME, KEY_LEAGUE_NAME, KEY_DATE, KEY_GAME_ONE_SCORE, KEY_GAME_TWO_SCORE,
				KEY_GAME_THREE_SCORE, KEY_SERIES_SCORE }, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +"", null, null, null, null);
	} */
	
	//////not used in version 2.0 must rewrite to write to the game table only
	/*public Cursor fetchScoresForBowlerLeague(String bowler, String league) {
		Log.e("Query", KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +"");
		return database.query(LEAGUE_NIGHT_TABLE, new String[] { KEY_ROWID, KEY_BOWLER_NAME, KEY_LEAGUE_NAME, KEY_DATE, KEY_GAME_ONE_SCORE, KEY_GAME_TWO_SCORE,
				KEY_GAME_THREE_SCORE, KEY_SERIES_SCORE }, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +"", null, null, null, null);
		
	}*/
	
	 public Cursor fetchScoresForBowlerLeagueDate(String bowler, String league, String date) {
		Log.e("Query", KEY_BOWLER_NAME + "='" + bowler +"' AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" +date+"'");
		return database.query(GAME_TABLE, new String[] {KEY_SCORE, KEY_GAME_NUMBER},
				KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" +date+"'", 
				null, null, null, null);
	}
	
	public Cursor fetchPinsData(String bowler, String league, String date, String gameNumber){
		return database.query(GAME_TABLE, PINS_KEY, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
				  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "' AND " + KEY_GAME_NUMBER + "='" + gameNumber + "'", null, null, null, null);
	}
	
	public Cursor fetchBallData(String bowler, String league, String date, String gameNumber){
		return database.query(GAME_TABLE, BALL_KEY, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
				  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "' AND " + KEY_GAME_NUMBER + "='" + gameNumber + "'", null, null, null, null);
	}
	
	public Cursor fetchFeetData(String bowler, String league, String date, String gameNumber){
		return database.query(GAME_TABLE, FEET_KEY, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
				  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "' AND " + KEY_GAME_NUMBER + "='" + gameNumber + "'", null, null, null, null);
	}
	
	public Cursor fetchMarkData(String bowler, String league, String date, String gameNumber){
		return database.query(GAME_TABLE, MARK_KEY, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
				  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "' AND " + KEY_GAME_NUMBER + "='" + gameNumber + "'", null, null, null, null);
	}
	
	//fetches data for the listGames Graphing function
	public Cursor fetchListGamesData(String bowler, String date1, String date2){
		Log.v("ListScoresQuesry", KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'");
		return database.query(GAME_TABLE, new String[] {KEY_SCORE, KEY_DATE, KEY_GAME_NUMBER},
				KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'", 
				null, null, null, KEY_DATE);
	}
	
	//fetches data for the listSeries Graphing function
	//must fix the league night table reference
	public Cursor fetchListSeriesData(String bowler, String date1, String date2){
		return database.rawQuery("SELECT sum(score), date FROM game WHERE bowlername = " + DatabaseUtils.sqlEscapeString(bowler) + " AND date BETWEEN '" + date1 + "' AND '" + date1 + "' GROUP BY date;", null);
		//return database.query(LEAGUE_NIGHT_TABLE, new String[] {KEY_SERIES_SCORE, KEY_DATE},
				//KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'", 
				//null, null, null, KEY_DATE);
	}
	
	//fetches data for the strikeSpare Graphing function
	public Cursor fetchStrikeSpareData(String bowler, String date1, String date2){
		return database.query(GAME_TABLE, PINS_KEY, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'", 
				null, null, null, KEY_DATE +", " + KEY_GAME_NUMBER);
	}
	
	//fetches data for the game table three games and column 4 for series
	//this possibly not used in version 2.0 
	public Cursor fetchGameData(String bowler, String league, String date1, String date2){
		Cursor query;
		if(league.contentEquals("%")){
			query = database.query(GAME_TABLE, new String[] {KEY_SCORE}, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + " LIKE " + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}else{
			query = database.query(GAME_TABLE, new String[] {KEY_SCORE}, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}
		
		return query;
	}
	
	//fetches the series data by totaling up the games by each date
	public Cursor fetchSeriesData(String bowler, String league, String date1, String date2){
		Cursor query;
		if(league.contentEquals("%")){
			query = database.rawQuery("SELECT sum(score), date, leaguename FROM game WHERE bowlername = " + DatabaseUtils.sqlEscapeString(bowler) + " AND " + KEY_LEAGUE_NAME + " LIKE " + DatabaseUtils.sqlEscapeString(league) + " AND date BETWEEN '" + date1 + "' AND '" + date2 + "' GROUP BY date, leaguename", null);
			Log.v("series query", "" + query.getCount());
		}else{
			query = database.rawQuery("SELECT sum(score), date FROM game WHERE bowlername = " + DatabaseUtils.sqlEscapeString(bowler) + " AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) + " AND date BETWEEN '" + date1 + "' AND '" + date2 + "' GROUP BY date", null);
		}
		
		return query;
	}
	
	//fetches data for the strike table
	public Cursor fetchStrikeData(String bowler, String league, String date1, String date2){
		Cursor query;
		if(league.contentEquals("%")){
			query = database.query(GAME_TABLE, STRIKE_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + " LIKE " + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}else{
			query = database.query(GAME_TABLE, STRIKE_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}
		
		return query;
		}
	
	public Cursor fetchSpareData(String bowler, String league, String date1, String date2){
		Cursor query;
		if(league.contentEquals("%")){
			query = database.query(GAME_TABLE, PINS_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + " LIKE " + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}else{
			query = database.query(GAME_TABLE, PINS_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}
		
		return query;
		}
	
	//Fetches data for the Ball Strike Table
	public Cursor fetchBallStrikeData(String bowler, String league, String date1, String date2){
		Cursor query;
		if(league.contentEquals("%")){
			query = database.query(GAME_TABLE, BALL_STRIKE_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + " LIKE " + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}else{
			query = database.query(GAME_TABLE, BALL_STRIKE_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}
		
		return query;
		}
	
	//fetched the data for teh spare portion of the Ball Stats Table
	public Cursor fetchBallSpareData(String bowler, String league, String date1, String date2){
		Cursor query;
		if(league.contentEquals("%")){
			query = database.query(GAME_TABLE, BALL_SPARE_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + " LIKE " + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}else{
			query = database.query(GAME_TABLE, BALL_SPARE_KEY, 
					KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) + " AND " + KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'",
					null, null, null, null);
		}
		
		return query;
		}
	
	//fetches league night data to check if it is blank to erase it
	/*public Cursor checkBlankLeagueData(){
		return database.query(LEAGUE_NIGHT_TABLE, new String[]{ KEY_ROWID, KEY_SERIES_SCORE}, null, null, null, null, null);
	} */
	
	public Cursor checkBlankGameData(){
		return database.query(GAME_TABLE, PINS_ROW_KEY, null, null, null, null, null);
	}
	
	/*
	public boolean deleteBowlerScore(long rowId) {
		return database.delete(LEAGUE_NIGHT_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}*/
	
	/*
	public boolean deleteBowlerLeagueNight(String bowler, String league, String date){
		return database.delete(LEAGUE_NIGHT_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
				  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "'", null) > 0;
	}*/
	
	public boolean deleteGame(String bowler, String league, String date, String gameNumber){
		return database.delete(GAME_TABLE, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
				  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "' AND " + KEY_GAME_NUMBER + "='" + gameNumber +"'", null) > 0;
	}
	
	public boolean deleteGameKey(long rowId) {
		return database.delete(GAME_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public long createGame(String bowlerName, String leagueName, String date, String gameNumber, String[] pins, String[] ball, String[] mark, String[] feet, String score) {
		ContentValues initialValues = createGameContentValues(bowlerName, leagueName, date, gameNumber, pins, ball, mark, feet, score);
		return database.insert(GAME_TABLE, null, initialValues);
	}
	
	public long updateGame(String bowlerName, String leagueName, String date, String gameNumber, String[] pins, String[] ball, String[] mark, String[] feet, String score, int id) {
		ContentValues values = createGameContentValues(bowlerName, leagueName, date, gameNumber, pins, ball, mark, feet, score);
		return database.update(GAME_TABLE, values, "KEY_ROWID = " + id , null);
	}
	
	public long createGameScore(String bowlerName, String leagueName, String date, String gameNumber, String score) {
		ContentValues initialValues = createGameScoreContentValues(bowlerName, leagueName, date, gameNumber, score);
		return database.insert(GAME_TABLE, null, initialValues);
	}

	public long updateGameScore(String score, int id) {
		ContentValues values = updateGameContentScoreValues(score);
		return database.update(GAME_TABLE, values, "KEY_ROWID = " + id , null);
	}

	public Cursor checkGameRecords(String bowler, String league, String date, String gameNumber){
		//boolean exists = false;
		//Cursor c = database.query(GAME_TABLE, new String[] {KEY_BOWLER_NAME}, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
		//						  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "' AND " + KEY_GAME_NUMBER + "='" + gameNumber + "'", null, null, null, null);
		//exists = (c.getCount() > 0);
		//c.close();
		//return exists;
		return database.query(GAME_TABLE, new String[] {KEY_ROWID}, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) +" AND " + KEY_LEAGUE_NAME + 
								  "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + "' AND " + KEY_GAME_NUMBER + "='" + gameNumber + "'", null, null, null, null);
		
	}
	
	//database query that checks to see to see if records for a game already exist
	public boolean checkLeagueNightRecords(String bowler, String league, String date){
		boolean exists = false;
		Cursor c = database.query(GAME_TABLE, new String[] { KEY_BOWLER_NAME, KEY_LEAGUE_NAME, KEY_DATE}, KEY_BOWLER_NAME + "=" + DatabaseUtils.sqlEscapeString(bowler) 
																	 +" AND " + KEY_LEAGUE_NAME + "=" + DatabaseUtils.sqlEscapeString(league) +" AND " + KEY_DATE + "='" + date + 
																	 "'", null, null, null, null);
		exists = (c.getCount() > 0);
		c.close();
		return exists;
	}
	 /*
	public Cursor fetchDates(){
		return database.query(LEAGUE_NIGHT_TABLE, new String[] { KEY_DATE }, null, null, null, null, null);
	} */
	
	/*
	public void fixDate(String oldDate, String newDate){
		ContentValues values = new ContentValues();
		values.put(KEY_DATE, newDate);
		database.update(LEAGUE_NIGHT_TABLE, values, KEY_DATE + "='" + oldDate + "'", null);
		database.update(GAME_TABLE, values, KEY_DATE + "='" + oldDate + "'", null);
	} */

	private ContentValues createBowlerContentValues(String name, String average) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_AVERAGE, average);
		return values;
	}
	
	private ContentValues createBallContentValues(String name, String ball) {
		ContentValues values = new ContentValues();
		values.put(KEY_BOWLER_NAME, name);
		values.put(KEY_BALL, ball);
		return values;
	}
	
	private ContentValues createLeagueContentValues(String leagueName, String house, String bowlerName) {
		ContentValues values = new ContentValues();
		values.put(KEY_LEAGUE_NAME, leagueName);
		values.put(KEY_HOUSE, house);
		values.put(KEY_BOWLER_NAME, bowlerName);
		return values;
	}
	 //not used in version 2.0 table no longer exists
	/* private ContentValues createLeagueNightContentValues(String bowlerName, String leagueName, String date, Integer gameOne, Integer gameTwo, Integer gameThree, Integer series) {
		ContentValues values = new ContentValues();
		values.put(KEY_BOWLER_NAME, bowlerName);
		values.put(KEY_LEAGUE_NAME, leagueName);
		values.put(KEY_DATE, date);
		values.put(KEY_GAME_ONE_SCORE, gameOne);
		values.put(KEY_GAME_TWO_SCORE, gameTwo);
		values.put(KEY_GAME_THREE_SCORE, gameThree);
		values.put(KEY_SERIES_SCORE, series);
		return values;
	} */
	
	private ContentValues createGameContentValues(String bowlerName, String leagueName, String date, String gameNumber, String[] pins, String[] ball, String[] mark, String[] feet, String score) {
		ContentValues values = new ContentValues();
		values.put(KEY_BOWLER_NAME, bowlerName);
		values.put(KEY_LEAGUE_NAME, leagueName);
		values.put(KEY_DATE, date);
		values.put(KEY_GAME_NUMBER, gameNumber);
		values.put(KEY_SCORE, score);
		for(int i=0; i < 21; i++){
			values.put(PINS_KEY[i], pins[i]);
			values.put(BALL_KEY[i], ball[i]);
			values.put(MARK_KEY[i], mark[i]);
			values.put(FEET_KEY[i], feet[i]);
		}
		return values;
		
	}
	
	private ContentValues createGameScoreContentValues(String bowlerName, String leagueName, String date, String gameNumber, String score) {
		ContentValues values = new ContentValues();
		values.put(KEY_BOWLER_NAME, bowlerName);
		values.put(KEY_LEAGUE_NAME, leagueName);
		values.put(KEY_DATE, date);
		values.put(KEY_GAME_NUMBER, gameNumber);
		values.put(KEY_SCORE, score);
	
		return values;
	}
	
	private ContentValues updateGameContentScoreValues(String score) {
		ContentValues values = new ContentValues();
		values.put(KEY_SCORE, score);
	
		return values;
	}
}
