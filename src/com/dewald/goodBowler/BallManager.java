package com.dewald.goodBowler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class BallManager extends Activity implements OnClickListener, OnItemSelectedListener{
	
	private Spinner nameSpinner;
	private EditText etBall;
	private Button btnAccept;
	private Button btnCancel;
	private Button btnList;
	private BowlerDatabaseAdapter mDbHelper;
	
	private String spinnerValue;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newball);
		
		mDbHelper = new BowlerDatabaseAdapter(this);
	    mDbHelper.open();
	    
	    nameSpinner  = (Spinner)findViewById(R.id.bowlerSpinner);
	    nameSpinner.setOnItemSelectedListener(this);
		etBall = (EditText)findViewById(R.id.ballName);
		btnAccept = (Button)findViewById(R.id.accept);
		btnAccept.setOnClickListener(this);
		btnCancel = (Button)findViewById(R.id.cancel);
		btnCancel.setOnClickListener(this);
		btnList = (Button)findViewById(R.id.listBallsButton);
		btnList.setOnClickListener(this);
		
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
        .showSoftInput(etBall, InputMethodManager.SHOW_FORCED);
		
		fillNameSpinner();
	
	}

	public void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.accept:
			String ball = etBall.getText().toString();
			String bowlerName = spinnerValue;
			mDbHelper.createBall(bowlerName, ball);
			etBall.setText("");
            break;
		case R.id.listBallsButton:
			Intent i1 = new Intent(BallManager.this, ListBalls.class);
			bowlerName = spinnerValue;
			i1.putExtra("name", bowlerName);
			startActivity(i1);
			break; 
		}
		
	}
	
	private void fillNameSpinner() {
		Cursor cursor = mDbHelper.fetchAllNames();
		startManagingCursor(cursor);
		
		String[] from = new String[] {BowlerDatabaseAdapter.KEY_NAME};
		int[] to = new int[] {android.R.id.text1};
		
		SimpleCursorAdapter names = new SimpleCursorAdapter(this, R.layout.spinnertext, cursor, from, to);
		names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		nameSpinner.setAdapter(names);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Cursor cursor = mDbHelper.fetchAllNames();
		cursor.moveToPosition(pos);
		spinnerValue = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_NAME));
		btnList.setText("List Bowling Balls for " + spinnerValue);
		cursor.close();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//do nothing
		
	}
}
