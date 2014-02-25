package com.dewald.goodBowler;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleCursorAdapter.ViewBinder;


public class Main extends ActionBarActivity implements OnClickListener, OnItemSelectedListener, OnDateSetListener {
	private BowlerDatabaseAdapter dbHelper;
	
	private Spinner nameSpinner;
	private Spinner leagueSpinner;
	//private Button addBowlerButton;
	private Button goButton;
	//private Button addLeagueButton;
	private Button datePickButton;
	private Button statButton;
	private Button graphButton;
	//private Button ballButton;
	private ImageButton fbButton;
	private TextView tvDate;
	private int year;
	private int month;
	private int day;
	private String sqlDate;

	private final int DATE_DIALOG_ID = 1;
	
	private String bowler;
	private String league;
	private String regDate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		dbHelper = new BowlerDatabaseAdapter(this);
	    dbHelper.open();
	    
	    //methods to fix errors remove in version 2.0
	    //checkBlankData(); 
	    //fixDates();
		
		nameSpinner = (Spinner)findViewById(R.id.bowlerSpinner);
		nameSpinner.setOnItemSelectedListener(this);
		leagueSpinner = (Spinner)findViewById(R.id.leagueSpinner);
		leagueSpinner.setOnItemSelectedListener(this);
		//addBowlerButton = (Button)findViewById(R.id.newBowlerButton);
		//addBowlerButton.setOnClickListener(this);
		goButton = (Button)findViewById(R.id.scoreButton);
		goButton.setOnClickListener(this);
		//addLeagueButton = (Button)findViewById(R.id.newLeagueButton);
		//addLeagueButton.setOnClickListener(this);
		datePickButton = (Button)findViewById(R.id.dateButton);
		datePickButton.setOnClickListener(this);
		statButton = (Button)findViewById(R.id.statButton);
		statButton.setOnClickListener(this);
		graphButton = (Button)findViewById(R.id.graphicalStatButton);
		graphButton.setOnClickListener(this);
		//ballButton = (Button)findViewById(R.id.newBallButton);
		//ballButton.setOnClickListener(this);
		fbButton = (ImageButton)findViewById(R.id.fbButton);
		fbButton.setOnClickListener(this);
		
		tvDate = (TextView)findViewById(R.id.tvDate);
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		updateDate();
		createSQLDate();
		
		
		fillNameSpinner();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		dbHelper.close();
	}

	@Override
	//listener for the buttons
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		/*case R.id.newBowlerButton:
			Intent i = new Intent(Main.this, NewBowler.class);
			startActivity(i);
			break;*/
		case R.id.scoreButton:
			if(bowler == null || league == null){
				Toast toast = Toast.makeText(this, "You Must Select a Bowler and League, If there are none then create one.", Toast.LENGTH_LONG);
				toast.show();
			}else{
					i = new Intent(Main.this, LeagueNightActivity.class);
					i.putExtra("SQLDate", sqlDate);
					i.putExtra("date", regDate);
					i.putExtra("bowler", bowler);
					i.putExtra("league", league);
					startActivity(i);
					}
			break;
		/*case R.id.newLeagueButton:
			i = new Intent(Main.this, NewLeague.class);
			startActivity(i);
			break;*/
		case R.id.dateButton:
			showDatePickerDialog();
			break;
		case R.id.statButton:
			i = new Intent(Main.this, StatSelect.class);
			startActivity(i);
			break;
		case R.id.graphicalStatButton:
			i = new Intent(Main.this, StatFunctionsActivity.class);
			startActivity(i);
			break;
		/*case R.id.newBallButton:
			i = new Intent(Main.this, BallManager.class);
			startActivity(i);
			break;*/
		case R.id.fbButton:
    		Intent fbIntent = facebookIntent();
    		startActivity(fbIntent);
			break;
		}
		
	}

	@Override
	//listens for the bowler selection to made to set the bowler name to be passed to fill the league spinner
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			switch (parent.getId()) {
			case R.id.bowlerSpinner:	
			Cursor cursor = dbHelper.fetchAllNames();
				cursor.moveToPosition(pos);
				bowler = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_NAME));
				fillLeagueSpinner();
				//Log.e("Bowler Name", bowler );
				cursor.close();
			break;
			case R.id.leagueSpinner:
				cursor = dbHelper.fetchLeaguesForBowler(bowler);
				cursor.moveToPosition(pos);
				league = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_LEAGUE_NAME));
				//Log.e("League Name", league );
				cursor.close();
			break;
			}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//do nothing
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_createBowler:
    		 Intent i = new Intent(Main.this, NewBowler.class);
			 startActivity(i);
    		 return true;
    	case R.id.action_createLeague:
    		 i = new Intent(Main.this, NewLeague.class);
			 startActivity(i);
    		 return true;
    	case R.id.action_ball:
    		 i = new Intent(Main.this, BallManager.class);
			 startActivity(i);
    		 return true;
    	case R.id.action_about:
    		i = new Intent(Main.this, About.class);
    		startActivity(i);
    		return true;
    	case R.id.help:
    		i = new Intent(Main.this, HelpPage.class);
    		startActivity(i);
    		return true;
    	case R.id.exportdata:
    		ExportFile export = new ExportFile();
    		if(export.exportData()){
    			Toast exportToast = Toast.makeText(this, "Database bowlerdata written to the sdcard", Toast.LENGTH_LONG);
    			exportToast.show();
    		}else{
    			Toast exportToast = Toast.makeText(this, "Error database bowlerdata not written to the sdcard", Toast.LENGTH_LONG);
    			exportToast.show();
    		}
    		return true;
    	case R.id.importdata:
    		ImportFile importFile = new ImportFile();
    		if(importFile.exportData()){
    			Toast importToast = Toast.makeText(this, "New database loaded succesfully", Toast.LENGTH_LONG);
    			importToast.show();
    			onCreate(null);
    		}else{
    			Toast importToast = Toast.makeText(this, "Error database not loaded", Toast.LENGTH_LONG);
    			importToast.show();
    		}
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	//fills the Spinner with the bowler names from the existing database
	private void fillNameSpinner() {
		Cursor cursor = dbHelper.fetchAllNames();
		startManagingCursor(cursor);
		
		String[] from = new String[] {BowlerDatabaseAdapter.KEY_NAME};
		int[] to = new int[] {android.R.id.text1};
		
		SimpleCursorAdapter names = new SimpleCursorAdapter(this, R.layout.spinnertext, cursor, from, to);
		names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//check to see if any names are entered, if the names are empty then take the user to create a user
		if(names.isEmpty()){
			Intent i = new Intent(Main.this, NewBowler.class);
			startActivity(i);
		}
		nameSpinner.setAdapter(names);
	}
	//fills the leaguename spinner with the names of the leagues that correspond with what bowler is selected in the bowler spinner
	private void fillLeagueSpinner() {
		//method to add Open Bowling option in the league spinner 
	    addOpenBowling();
		
		 Cursor cursor = dbHelper.fetchLeaguesForBowler(bowler);
		startManagingCursor(cursor);
		
		String[] from = new String[] {BowlerDatabaseAdapter.KEY_LEAGUE_NAME};
		int[] to = new int[] {android.R.id.text1};
		
		SimpleCursorAdapter leagues = new SimpleCursorAdapter(this, R.layout.spinnertext, cursor, from, to);
		leagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		leagueSpinner.setAdapter(leagues);
	}
	
	//Updates the date using StringBuilder
	private void updateDate() {
		tvDate.setText(
				new StringBuilder()
				.append(month +1).append("-")
				.append(day).append("-")
				.append(year).append(" "));
		regDate = tvDate.getText().toString();
	}

	private String createSQLDate() {
		StringBuilder s;
		int y = year;
		int m = month + 1;
		int d = day;
		if(m < 10 && d < 10){
			 s = new StringBuilder().append(y).append("-0").append(m).append("-0").append(d);
		}else if(m < 10){
			 s = new StringBuilder().append(y).append("-0").append(m).append("-").append(d);
		}else if(d < 10){
			 s = new StringBuilder().append(y).append("-").append(m).append("-0").append(d);
		}else{
		 s = new StringBuilder().append(y).append("-").append(m).append("-").append(d);
		}
		sqlDate = s.toString();
		return sqlDate;
	}
	
	private void showDatePickerDialog() {
		DialogFragment newFragment = DatePickerFragment.newInstance(1);
	    newFragment.show(getFragmentManager(), "datePicker");
		
	}
	/*private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int y, int m, int d) {
			year = y;
			month = m;
			day = d;
			updateDate();
			createSQLDate();
		}
	};
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id) {
		case DATE_DIALOG_ID:
		return new DatePickerDialog(this, dateSetListener, year, month, day);
		}
		return null;
	}*/ 
	
	

	private void checkBlankData(){
		//removed for version 2.0 since league table does not exist only need to check blank data in the game table
		/* Cursor cursor = dbHelper.checkBlankLeagueData();
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			if(cursor.getInt(1) == 0){
				boolean delete = dbHelper.deleteBowlerScore(cursor.getLong(0));
				Log.v("Deleted", cursor.getString(0) + delete);
			}
			cursor.moveToNext();
		}
		cursor.close(); */
		
		Cursor cursor = dbHelper.checkBlankGameData();
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			if(!checkDataEntered(i)){
				boolean delete = dbHelper.deleteGameKey(cursor.getLong(0));
				//Log.v("Deleted", cursor.getString(0) + delete);
			}
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	private boolean checkDataEntered(int row){
		Cursor cursor = dbHelper.checkBlankGameData();
		cursor.moveToPosition(row);
		boolean data = false;
		for(int i = 1; i < cursor.getColumnCount(); i++){
			if(!cursor.getString(i).equals("-")){
				data = true;
			}
		}
		cursor.close();
		return data;
	}
	
	//fix for dates stored without 0 before single digits
	//not needed in version 2.0 leaving for historical purposes
	/* private void fixDates(){
		Cursor cursor = dbHelper.fetchDates();
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			if(cursor.getString(0).length() < 10){
				String oldDate = cursor.getString(0);
				String newDate = fixSqlDate(oldDate);
				dbHelper.fixDate(oldDate, newDate);
				Log.v("oldDate", oldDate);
				Log.v("newDate", newDate);
			}
			cursor.moveToNext();
		}
		cursor.close();
	} 
	
	public String fixSqlDate(String s){
		String oldDate = s;
		String newDate = "";
		String[] ymd = {"","",""};
		String[] temp;
		int y = 0;
		int m = 0;
		int d = 0;

		temp = oldDate.split("-");
		for(int i=0; i < temp.length; i++){
			ymd[i] = temp[i];
		}

		y = Integer.parseInt(ymd[0]);
		m = Integer.parseInt(ymd[1]);
		d = Integer.parseInt(ymd[2]);

		StringBuilder sb = new StringBuilder()
					.append(y).append("-")
					.append(m<10?"0"+m:m).append("-")
					.append(d<10?"0"+d:d);
		newDate = sb.toString();
		return newDate;
	} */
	
	//method to add open bowling to the league spinner
	//adds an open bowling "league" to the database so the user can select open bowling
	private void addOpenBowling(){
		 Boolean openExists = false;	
		 Cursor cursor = dbHelper.fetchLeaguesForBowler(bowler);
		 cursor.moveToFirst();
		 for(int i = 0; i < cursor.getCount(); i++){
			 //Log.v("League Name", cursor.getString(1));
			 if(cursor.getString(1).equals("Open Bowling")){
				 openExists = true;
			 }
			 cursor.moveToNext();
		 }
		 if(!openExists){
			 dbHelper.createLeague("Open Bowling", "EveryHouse", bowler);
		 }
		 cursor.close();	
	}
	
	private Intent facebookIntent(){
		try{
			this.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/170468596448040"));
		}catch(Exception e){
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/GoodBowlerStats"));
		}
	}

	@Override
	public void onDateSet(DatePicker view, int y, int m, int d) {
		year = y;
		month = m;
		day = d;
		updateDate();
		createSQLDate();	
	}
}
