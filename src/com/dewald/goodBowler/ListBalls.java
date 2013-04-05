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

public class ListBalls extends ListActivity implements OnItemClickListener{
	
	private BowlerDatabaseAdapter dbHelper;
	private Cursor cursor;
	private ListView lv;
	private long row;
	private String name;
	private Bundle extras;
	private String ball;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		this.getListView().setDividerHeight(2);
		lv = getListView();
		lv.setOnItemClickListener(this);
		dbHelper = new BowlerDatabaseAdapter(this);
		dbHelper.open();
		extras = getIntent().getExtras();
		name = extras.getString("name");
		fillData();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		cursor.close();
		dbHelper.close();
	}
	
	private void fillData() {
		cursor = dbHelper.fetchBalls(name);
		startManagingCursor(cursor);
		
		String[] from = new String[] { BowlerDatabaseAdapter.KEY_BALL };
		int[] to = new int [] { R.id.ball_entry};
		
		SimpleCursorAdapter balls = new SimpleCursorAdapter(this, R.layout.listballs, cursor, from, to);
		this.setListAdapter(balls);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		cursor = dbHelper.fetchBalls(name);
		cursor.moveToPosition(pos);
		row = cursor.getLong(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_ROWID));
		ball = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_BALL));
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("DELETE?");
		alert.setMessage("Are you sure you want to delete " + ball + "?");
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dbHelper.deleteBall(row);
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
