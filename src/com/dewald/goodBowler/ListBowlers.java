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

public class ListBowlers extends ListActivity implements OnItemClickListener {
	private BowlerDatabaseAdapter dbHelper;
	private Cursor cursor;
	private ListView lv;
	private long row;
	private String name;
	
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
		cursor = dbHelper.fetchAllBowlers();
		startManagingCursor(cursor);
		
		String[] from = new String[] { BowlerDatabaseAdapter.KEY_NAME, BowlerDatabaseAdapter.KEY_AVERAGE };
		int[] to = new int [] { R.id.name_entry, R.id.average_entry };
		
		SimpleCursorAdapter bowlers = new SimpleCursorAdapter(this, R.layout.bowlerlist, cursor, from, to);
		this.setListAdapter(bowlers);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		cursor = dbHelper.fetchAllBowlers();
		cursor.moveToPosition(pos);
		row = cursor.getLong(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_ROWID));
		name = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_NAME));
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("DELETE?");
		alert.setMessage("Are you sure you want to delete " + name + "? This will delete all data for that bowler(leagues, scores, etc.)");
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dbHelper.deleteBowler(name);
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
