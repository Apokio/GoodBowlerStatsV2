package com.dewald.goodBowler;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog;

public class ListLeagueNight extends ListActivity implements OnItemClickListener {
	private BowlerDatabaseAdapter dbHelper;
	private Cursor cursor;
	private ListView lv;
	private long row;
	private Bundle extras;
	private String sqlDate;
	private String regDate;
	private String bowler;
	private String league;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		extras = getIntent().getExtras();
        sqlDate = extras.getString("sqlDate");
        regDate = extras.getString("date");
        bowler = extras.getString("bowler");
        league = extras.getString("league");
        //Log.v("sqlDate", sqlDate);
		this.getListView().setDividerHeight(2);
		dbHelper = new BowlerDatabaseAdapter(this);
		dbHelper.open();
		lv = getListView();
		lv.setOnItemClickListener(this);
		//fillData(); TODO
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		cursor.close();
		dbHelper.close();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO remove later when we uncomment the other code ion this class
		
	}
	
	//TODO fix this when we add the option to add more than three game
	/* private void fillData() {
		cursor = dbHelper.fetchScoresForBowlerLeague(bowler, league);
		startManagingCursor(cursor);
		
		String[] from = new String[] { BowlerDatabaseAdapter.KEY_BOWLER_NAME, BowlerDatabaseAdapter.KEY_LEAGUE_NAME, BowlerDatabaseAdapter.KEY_DATE, 
									   BowlerDatabaseAdapter.KEY_GAME_ONE_SCORE, BowlerDatabaseAdapter.KEY_GAME_TWO_SCORE, BowlerDatabaseAdapter.KEY_GAME_THREE_SCORE,
									   BowlerDatabaseAdapter.KEY_SERIES_SCORE};
		int[] to = new int [] { R.id.bowler_entry, R.id.league_entry, R.id.date_entry, R.id.g1_entry, R.id.g2_entry, R.id.g3_entry, R.id.series_entry};
		
		SimpleCursorAdapter leagues = new SimpleCursorAdapter(this, R.layout.leaguenightlist, cursor, from, to);
		this.setListAdapter(leagues);
	}
	
	//listens for the click for the user to delete the selected item displays a alert to make sure the user wants to delete that item
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		cursor = dbHelper.fetchScoresForBowlerLeague(bowler, league);
		cursor.moveToPosition(pos);
		row = cursor.getLong(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_ROWID));
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("DELETE?");
		alert.setMessage("Are you sure you want to delete this record? This will delete the detailed score information too.");
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dbHelper.deleteBowlerScore(row);
				fillData();
			}
		});
		
		alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do Nothing
				
			}
			
		});
		
		alert.show();
	} */

}
