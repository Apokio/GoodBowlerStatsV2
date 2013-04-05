package com.dewald.goodBowler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class NewBowler extends Activity implements OnClickListener {
    
	private EditText etName;
	private EditText etAvg;
	private Button btnAccept;
	private Button btnCancel;
	private Button btnList;
	private BowlerDatabaseAdapter mDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newbowler);
        
        mDbHelper = new BowlerDatabaseAdapter(this);
        mDbHelper.open();
        
        etName = (EditText)findViewById(R.id.bowlerName);
        etAvg = (EditText)findViewById(R.id.average);
        btnAccept = (Button)findViewById(R.id.acceptButton);
        btnCancel = (Button)findViewById(R.id.cancelButton);
        btnList = (Button)findViewById(R.id.listButton);
        btnAccept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnList.setOnClickListener(this);
        
    }
    
    @Override
	public void onDestroy(){
		super.onDestroy();
		mDbHelper.close();
	}

	@Override
	public void onClick(View v) {
			switch(v.getId()) {
			case R.id.cancelButton:
				finish();
				break;
			case R.id.acceptButton:
				String name = etName.getText().toString();
				String average = etAvg.getText().toString();
				mDbHelper.createBowler(name, average);
				etName.setText("");
				etAvg.setText("");
	            break;
			case R.id.listButton:
				Intent i1 = new Intent(NewBowler.this, ListBowlers.class);
				startActivity(i1);
				break;
			}
	}
    
    
}