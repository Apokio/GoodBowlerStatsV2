package com.dewald.goodBowler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ListLeagues extends ListActivity implements OnItemClickListener{
	private BowlerDatabaseAdapter dbHelper;
	private Cursor cursor;
	private ListView lv;
	private long row;
	private String name;
	private String league;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		this.getListView().setDividerHeight(2);
		lv = getListView();
		lv.setOnItemClickListener(this);
		dbHelper = new BowlerDatabaseAdapter(this);
		dbHelper.open();
		fillData();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		cursor.close();
		dbHelper.close();
	}
	
	private void fillData() {
		cursor = dbHelper.fetchAllLeagues();
		startManagingCursor(cursor);
		
		String[] from = new String[] { BowlerDatabaseAdapter.KEY_LEAGUE_NAME, BowlerDatabaseAdapter.KEY_HOUSE, BowlerDatabaseAdapter.KEY_BOWLER_NAME };
		int[] to = new int [] { R.id.leaguename_entry, R.id.house_entry, R.id.bowler_entry };
		
		SimpleCursorAdapter leagues = new SimpleCursorAdapter(this, R.layout.leaguelist, cursor, from, to);
		this.setListAdapter(leagues);
	}
	
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		cursor = dbHelper.fetchAllLeagues();
		cursor.moveToPosition(pos);
		row = cursor.getLong(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_ROWID));
		name = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_BOWLER_NAME));
		league = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_LEAGUE_NAME));
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("DELETE?");
		alert.setMessage("Are you sure you want to delete the league " + league + " for " + name +"? This will delete all records for this bowlers league(scores, detailed scores)");
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dbHelper.deleteLeague(name, league);
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
	}
}
