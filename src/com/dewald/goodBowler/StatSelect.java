package com.dewald.goodBowler;

import java.sql.Date;
import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class StatSelect extends Activity implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener{
	
	private BowlerDatabaseAdapter dbHelper;
	
	private Button statButton;
	private Button date1Button;
	private Button date2Button;
	private Button ballStatButton;
	private Spinner nameSpinner;
	private Spinner leagueSpinner;
	private CheckBox leagueCheckBox;
	private RadioButton singleDateRB;
	private RadioButton dateRangeRB;
	private RadioButton allDatesRB;
	
	private String bowler;
	private String league;
	private String sqlDate1;
	private String sqlDate2;
	private String ball;
	private int year;
	private int month;
	private int day;
	private int picker;
	private AlertDialog ballPick;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statselect);
		
		dbHelper = new BowlerDatabaseAdapter(this);
	    dbHelper.open();
		
	    nameSpinner = (Spinner)findViewById(R.id.bowlerSpinner);
		nameSpinner.setOnItemSelectedListener(this);
		leagueSpinner = (Spinner)findViewById(R.id.leagueSpinner);
		leagueSpinner.setOnItemSelectedListener(this);
		statButton = (Button)findViewById(R.id.statsButton);
		statButton.setOnClickListener(this);
		date1Button = (Button)findViewById(R.id.dateButton1);
		date1Button.setOnClickListener(this);
		date2Button = (Button)findViewById(R.id.dateButton2);
		date2Button.setOnClickListener(this);
		date2Button.setVisibility(View.INVISIBLE);
		ballStatButton = (Button)findViewById(R.id.ballStatsButton);
		ballStatButton.setOnClickListener(this);
		leagueCheckBox = (CheckBox)findViewById(R.id.leagueCheckBox);
		leagueCheckBox.setOnCheckedChangeListener(this);
		leagueCheckBox.setChecked(true);
		singleDateRB = (RadioButton)findViewById(R.id.singleDate);
		singleDateRB.setOnCheckedChangeListener(this);
		dateRangeRB = (RadioButton)findViewById(R.id.dateRange);
		dateRangeRB.setOnCheckedChangeListener(this);
		allDatesRB = (RadioButton)findViewById(R.id.allDates);
		allDatesRB.setOnCheckedChangeListener(this);
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		fillNameSpinner();
		updateDate(1);
		updateDate(2);
		createSQLDate(1);
		createSQLDate(2);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		dbHelper.close();
	}
	
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.statsButton:
			if(bowler == null || league == null){
				Toast toast = Toast.makeText(this, "You Must Select a Bowler and League, If there are none then create one.", Toast.LENGTH_LONG);
				toast.show();
			}else{
			Intent i = new Intent(StatSelect.this, Stats.class);
			if(leagueCheckBox.isChecked() && dateRangeRB.isChecked()){
				i.putExtra("bowler", bowler);
				i.putExtra("league", league);
				i.putExtra("sqlDate1", sqlDate1);
				i.putExtra("sqlDate2", sqlDate2);
			}else if(leagueCheckBox.isChecked() && singleDateRB.isChecked()){
				i.putExtra("bowler", bowler);
				i.putExtra("league", league);
				i.putExtra("sqlDate1", sqlDate1);
				i.putExtra("sqlDate2", sqlDate1);
			}else if(leagueCheckBox.isChecked() && allDatesRB.isChecked()){
				i.putExtra("bowler", bowler);
				i.putExtra("league", league);
				i.putExtra("sqlDate1", "1970-01-01");
				i.putExtra("sqlDate2", "2099-01-01");
			}else if(!leagueCheckBox.isChecked() && dateRangeRB.isChecked()){
				i.putExtra("bowler", bowler);
				i.putExtra("league", "%");
				i.putExtra("sqlDate1", sqlDate1);
				i.putExtra("sqlDate2", sqlDate2);
			}else if(!leagueCheckBox.isChecked() && singleDateRB.isChecked()){
				i.putExtra("bowler", bowler);
				i.putExtra("league", "%");
				i.putExtra("sqlDate1", sqlDate1);
				i.putExtra("sqlDate2", sqlDate1);
			}else if(!leagueCheckBox.isChecked() && allDatesRB.isChecked()){
				i.putExtra("bowler", bowler);
				i.putExtra("league", "%");
				i.putExtra("sqlDate1", "1970-01-01");
				i.putExtra("sqlDate2", "2099-01-01");
			}
			startActivity(i);
			}
			break;
		case R.id.dateButton1:
			showDialog(1);
			picker = 1;
			break;
		case R.id.dateButton2:
			showDialog(2);
			picker = 2;
			break;
		case R.id.ballStatsButton:
			if(bowler == null || league == null){
				Toast toast = Toast.makeText(this, "You Must Select a Bowler and League, If there are none then create one.", Toast.LENGTH_LONG);
				toast.show();
			}else{
				ballPicker();
			}
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
	
	//fills the Spinner with the bowler names from the existing database
	private void fillNameSpinner() {
		Cursor cursor = dbHelper.fetchAllNames();
		startManagingCursor(cursor);
		
		String[] from = new String[] {BowlerDatabaseAdapter.KEY_NAME};
		int[] to = new int[] {android.R.id.text1};
		
		SimpleCursorAdapter names = new SimpleCursorAdapter(this, R.layout.spinnertext, cursor, from, to);
		names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		nameSpinner.setAdapter(names);
	}
	//fills the leaguename spinner with the names of the leagues that correspond with what bowler is selected in the bowler spinner
	private void fillLeagueSpinner() {
		Cursor cursor = dbHelper.fetchLeaguesForBowler(bowler);
		startManagingCursor(cursor);
		
		String[] from = new String[] {BowlerDatabaseAdapter.KEY_LEAGUE_NAME};
		int[] to = new int[] {android.R.id.text1};
		
		SimpleCursorAdapter leagues = new SimpleCursorAdapter(this, R.layout.spinnertext, cursor, from, to);
		leagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		leagueSpinner.setAdapter(leagues);
	}
	
	//Updates the date using StringBuilder
	private void updateDate(int picker) {
		switch(picker){
		case 1:
		date1Button.setText(
				new StringBuilder()
				.append(month +1).append("-")
				.append(day).append("-")
				.append(year).append(" "));
		break;
		case 2:
			date2Button.setText(
					new StringBuilder()
					.append(month +1).append("-")
					.append(day).append("-")
					.append(year).append(" "));
			break;
		}
	}
	
	private void createSQLDate(int picker) {
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
		switch(picker){
		case 1:
			sqlDate1 = s.toString();
		break;
		case 2:
			sqlDate2 = s.toString();
		break;
		}
	}
	
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int y, int m, int d ) {
			year = y;
			month = m;
			day = d;
			switch(picker){
			case 1:
				updateDate(1);
				createSQLDate(1);
				break;
			case 2:
				updateDate(2);
				createSQLDate(2);
				break;
			}
		}
	};
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id) {
		case 1:
			return new DatePickerDialog(this, dateSetListener, year, month, day);
		case 2:
			return new DatePickerDialog(this, dateSetListener, year, month, day);
		}
		return null;
	}
	
	private void ballPicker(){
		
		final Cursor cursor = dbHelper.fetchBalls(bowler);
		startManagingCursor(cursor);
		
		String[] from = new String[] {BowlerDatabaseAdapter.KEY_BALL};
		int[] to = new int[] {android.R.id.text1};
		
		final SimpleCursorAdapter balls = new SimpleCursorAdapter(this, R.layout.balldialog, cursor, from, to);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick A Ball");
		builder.setAdapter(balls, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 cursor.moveToPosition(which);
				 ball = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_BALL));
				 //Log.v("Ball Selected", ball);
				 launchBallStats();
			}
		});
		ballPick = builder.create();
		ballPick.show();
	}
	
	private void launchBallStats(){
		Intent i = new Intent(StatSelect.this, BallStats.class);
		if(leagueCheckBox.isChecked() && dateRangeRB.isChecked()){
			i.putExtra("bowler", bowler);
			i.putExtra("league", league);
			i.putExtra("sqlDate1", sqlDate1);
			i.putExtra("sqlDate2", sqlDate2);
			i.putExtra("ball", ball);
		}else if(leagueCheckBox.isChecked() && singleDateRB.isChecked()){
			i.putExtra("bowler", bowler);
			i.putExtra("league", league);
			i.putExtra("sqlDate1", sqlDate1);
			i.putExtra("sqlDate2", sqlDate1);
			i.putExtra("ball", ball);
		}else if(leagueCheckBox.isChecked() && allDatesRB.isChecked()){
			i.putExtra("bowler", bowler);
			i.putExtra("league", league);
			i.putExtra("sqlDate1", "1970-01-01");
			i.putExtra("sqlDate2", "2099-01-01");
			i.putExtra("ball", ball);
		}else if(!leagueCheckBox.isChecked() && dateRangeRB.isChecked()){
			i.putExtra("bowler", bowler);
			i.putExtra("league", "%");
			i.putExtra("sqlDate1", sqlDate1);
			i.putExtra("sqlDate2", sqlDate2);
			i.putExtra("ball", ball);
		}else if(!leagueCheckBox.isChecked() && singleDateRB.isChecked()){
			i.putExtra("bowler", bowler);
			i.putExtra("league", "%");
			i.putExtra("sqlDate1", sqlDate1);
			i.putExtra("sqlDate2", sqlDate1);
			i.putExtra("ball", ball);
		}else if(!leagueCheckBox.isChecked() && allDatesRB.isChecked()){
			i.putExtra("bowler", bowler);
			i.putExtra("league", "%");
			i.putExtra("sqlDate1", "1970-01-01");
			i.putExtra("sqlDate2", "2099-01-01");
			i.putExtra("ball", ball);
		}
		startActivity(i);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()){
		case R.id.leagueCheckBox:
			if(isChecked){
				leagueSpinner.setVisibility(View.VISIBLE);
			}else {
				leagueSpinner.setVisibility(View.GONE);
			}
			break;
		case R.id.allDates:
			if(isChecked){
				date1Button.setVisibility(View.INVISIBLE);
				date2Button.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.dateRange:
			if(isChecked){
				date1Button.setVisibility(View.VISIBLE);
				date2Button.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.singleDate:
			if(isChecked){
			date1Button.setVisibility(View.VISIBLE);
			date2Button.setVisibility(View.INVISIBLE);
			}
			break;
		}
		
	}

}
