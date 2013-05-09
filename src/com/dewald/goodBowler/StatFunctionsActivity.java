package com.dewald.goodBowler;
import java.sql.Date;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class StatFunctionsActivity extends Activity implements OnClickListener, OnItemSelectedListener{
	
	private BowlerDatabaseAdapter dbHelper;
	
	private Spinner nameSpinner;
	
	private TextView tvDate1;
	private TextView tvDate2;
	
	private Button datePickButton1;
	private Button datePickButton2;
	private Button listGamesButton;
	private Button listSeriesButton;
	private Button strikeSparesButton;
	private Button sparesByPinButton;
	
	private String bowler;
	private String sqlDate1;
	private String sqlDate2;
	private int year;
	private int month;
	private int day;
	private int picker;
	
	private ScoreCalculator calculator = new ScoreCalculator();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statfunctions);
		
		dbHelper = new BowlerDatabaseAdapter(this);
	    dbHelper.open();
	    
	    tvDate1 = (TextView)findViewById(R.id.tvDate1);
	    tvDate2 = (TextView)findViewById(R.id.tvDate2);
	    datePickButton1 = (Button)findViewById(R.id.dateButton1);
	    datePickButton1.setOnClickListener(this);
	    datePickButton2 = (Button)findViewById(R.id.dateButton2);
	    datePickButton2.setOnClickListener(this);
	    listGamesButton = (Button)findViewById(R.id.listGames);
	    listGamesButton.setOnClickListener(this);
	    listSeriesButton = (Button)findViewById(R.id.listScores);
	    listSeriesButton.setOnClickListener(this);
	    strikeSparesButton = (Button)findViewById(R.id.strikeSpares);
	    strikeSparesButton.setOnClickListener(this);
	    sparesByPinButton = (Button)findViewById(R.id.sparesByPin);
	    sparesByPinButton.setOnClickListener(this);
	    nameSpinner = (Spinner)findViewById(R.id.bowlerSpinner);
		nameSpinner.setOnItemSelectedListener(this);
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		updateDate(1);
		updateDate(2);
		createSQLDate(1);
		createSQLDate(2);
		
		fillNameSpinner();
	}
	
	public void onDestroy(){
		super.onDestroy();
		dbHelper.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dateButton1:
			showDialog(1);
			picker = 1;
			break;
		case R.id.dateButton2:
			showDialog(2);
			picker = 2;
			break;
		case R.id.listGames:
			Intent intent = ChartFactory.getTimeChartIntent(this, listGamesDataset(), listGamesRenderer(), "MM/dd/yyyy");
		      startActivity(intent);
			break;
		case R.id.listScores:
			intent = ChartFactory.getTimeChartIntent(this, listSeriesDataset(), listSeriesRenderer(), "MM/dd/yyyy");
			startActivity(intent);
			break;
		case R.id.strikeSpares:
			intent = ChartFactory.getBarChartIntent(this, strikeSparesDataset(), strikeSpareRenderer(), Type.DEFAULT);
			startActivity(intent);
			break;
		case R.id.sparesByPin:
			intent = ChartFactory.getBarChartIntent(this, sparesByPinDataset(), sparesByPinRenderer(), Type.DEFAULT);
			startActivity(intent);
			break;
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		switch (parent.getId()) {
		case R.id.bowlerSpinner:	
			 Cursor cursor = dbHelper.fetchAllNames();
			cursor.moveToPosition(pos);
			bowler = cursor.getString(cursor.getColumnIndex(BowlerDatabaseAdapter.KEY_NAME));
			//Log.e("Bowler Name", bowler );
			cursor.close();
		break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
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
	
	//Updates the date using StringBuilder
	private void updateDate(int picker) {
		switch(picker){
		case 1:
		tvDate1.setText(
				new StringBuilder()
				.append(month +1).append("-")
				.append(day).append("-")
				.append(year).append(" "));
		break;
		case 2:
			tvDate2.setText(
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
	
	private Date createDate(String s){
		int y;
		int m;
		int d;
		StringTokenizer st = new StringTokenizer(s, "-");
		y = Integer.parseInt(st.nextToken()) - 1900;
		m = Integer.parseInt(st.nextToken()) - 1;
		d = Integer.parseInt(st.nextToken());
		//Log.v("Date", new StringBuilder().append(y).append("-").append(m).append("-").append(d).toString());
		return new Date(y, m, d);
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
	
	private XYMultipleSeriesDataset listGamesDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		Cursor cursor = dbHelper.fetchListGamesData(bowler, sqlDate1, sqlDate2);
			for(int i = 0; i < 20; i++){
				TimeSeries series = new TimeSeries("Game " + (i + 1));
					cursor.moveToFirst();
					for(int r = 0; r < cursor.getCount(); r++){
						if (cursor.getInt(2) == i + 1){
							series.add(createDate(cursor.getString(1)), cursor.getDouble(0));
						}
						cursor.moveToNext();
					}
					dataset.addSeries(series);
			}
			cursor.close();
			return dataset;
	}
	
	private XYMultipleSeriesRenderer listGamesRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setChartTitle(bowler + " Game Scores");
	    renderer.setXTitle("Date");
	    renderer.setYTitle("Scores");
	    renderer.setYAxisMax(325);
	    renderer.setYAxisMin(0);
	    renderer.setXAxisMin(createDate(sqlDate1).getTime() - 86400000);
	    renderer.setXAxisMax(createDate(sqlDate2).getTime() + 86400000);
	    renderer.setZoomButtonsVisible(true);
	    renderer.setPanEnabled(true, false);
	    renderer.setXLabels(8);
	    renderer.setMargins(new int[] {20, 30, 15, 0});
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.SQUARE);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.DIAMOND);
	    r.setColor(Color.RED);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.TRIANGLE);
	    r.setColor(Color.YELLOW);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.SQUARE);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.DIAMOND);
	    r.setColor(Color.RED);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.TRIANGLE);
	    r.setColor(Color.YELLOW);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.SQUARE);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.DIAMOND);
	    r.setColor(Color.RED);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.TRIANGLE);
	    r.setColor(Color.YELLOW);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.SQUARE);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.DIAMOND);
	    r.setColor(Color.RED);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.TRIANGLE);
	    r.setColor(Color.YELLOW);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.SQUARE);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.DIAMOND);
	    r.setColor(Color.RED);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.TRIANGLE);
	    r.setColor(Color.YELLOW);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    renderer.setAxesColor(Color.DKGRAY);
	    renderer.setLabelsColor(Color.LTGRAY);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
	      seriesRenderer.setDisplayChartValues(true);
	      seriesRenderer.setChartValuesTextSize(25f);
	    }
	    return renderer;
	  }
	
	private XYMultipleSeriesDataset listSeriesDataset() {
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    Cursor cursor = dbHelper.fetchListSeriesData(bowler, sqlDate1, sqlDate2);
			cursor.moveToFirst();
			TimeSeries series = new TimeSeries("Series Score");
			for(int i = 0; i < cursor.getCount(); i++){
				series.add(createDate(cursor.getString(1)), cursor.getDouble(0));
				cursor.moveToNext();
			}
	    dataset.addSeries(series);
		cursor.close();
	    return dataset;
	  }
	
	private XYMultipleSeriesRenderer listSeriesRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setChartTitle(bowler + " Series Scores");
	    renderer.setXTitle("Date");
	    renderer.setYTitle("Scores");
	    renderer.setXAxisMin(createDate(sqlDate1).getTime() - 86400000);
	    renderer.setXAxisMax(createDate(sqlDate2).getTime() + 86400000);
	    renderer.setZoomButtonsVisible(true);
	    renderer.setYAxisMax(900);
	    renderer.setYAxisMin(0);
	    renderer.setPanEnabled(true, false);
	    renderer.setXLabels(8);
	    renderer.setMargins(new int[] {20, 30, 15, 0});
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    renderer.setAxesColor(Color.DKGRAY);
	    renderer.setLabelsColor(Color.LTGRAY);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
	      seriesRenderer.setDisplayChartValues(true);
	      seriesRenderer.setChartValuesTextSize(25f);
	    }
	    return renderer;
	  }
	
	private XYMultipleSeriesRenderer strikeSpareRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setChartTitle(bowler + " Strikes Spares Opens per game");
	    renderer.setXTitle("Game Number");
	    renderer.setYTitle("Number of Strikes Spares Opens");
	    renderer.setYAxisMax(20);
	    renderer.setXAxisMin(0);
	    renderer.setXLabels(1);
	    renderer.setZoomButtonsVisible(true);
	    renderer.setMargins(new int[] {20, 30, 15, 0}); 
	    renderer.setPanEnabled(true, false);
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.GREEN);
	    renderer.addSeriesRenderer(r);
	    r = new SimpleSeriesRenderer();
	    r.setColor(Color.YELLOW);
	    renderer.addSeriesRenderer(r);
	    r = new SimpleSeriesRenderer();
	    r.setColor(Color.RED);
	    renderer.addSeriesRenderer(r);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
	      seriesRenderer.setDisplayChartValues(true);
	      seriesRenderer.setChartValuesTextSize(30f);
	    }
	    return renderer;
	}

	private XYMultipleSeriesDataset strikeSparesDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		String[] scoreArray = {"-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-"}; 
		Cursor cursor = dbHelper.fetchStrikeSpareData(bowler, sqlDate1, sqlDate2);
	    CategorySeries series = new CategorySeries("Strike");
	      cursor.moveToFirst();
	      for (int k = 0; k < cursor.getCount(); k++) {
				for(int i = 0; i < 21; i++){
					scoreArray[i] = cursor.getString(i);
				}
	    	series.add(calculator.strikesPerGame(scoreArray));
	    	cursor.moveToNext();
	      }
	      dataset.addSeries(series.toXYSeries());
	      
	      series = new CategorySeries("Spare");
	      cursor.moveToFirst();
	      for (int k = 0; k < cursor.getCount(); k++) {
				for(int i = 0; i < 21; i++){
					scoreArray[i] = cursor.getString(i);
				}
	    	series.add(calculator.sparesPerGame(scoreArray));
	    	cursor.moveToNext();
	      }
	      dataset.addSeries(series.toXYSeries());
	      
	      series = new CategorySeries("Open");
	      cursor.moveToFirst();
	      for (int k = 0; k < cursor.getCount(); k++) {
				for(int i = 0; i < 21; i++){
					scoreArray[i] = cursor.getString(i);
				}
	    	series.add(calculator.opensPerGame(scoreArray));
	    	cursor.moveToNext();
	      }
	      dataset.addSeries(series.toXYSeries());
	      cursor.close();
	    return dataset;
	}
	
	private XYMultipleSeriesRenderer sparesByPinRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setChartTitle(bowler + " Spares vs Open");
	    renderer.setXTitle("Pin Number");
	    renderer.setYTitle("Number of Spares/Opens");
	    renderer.setZoomButtonsVisible(true);
	    renderer.setYAxisMax(50);
	    renderer.setXAxisMin(0);
	    renderer.setXLabels(0);
	    renderer.addXTextLabel(1, "1 Pin");
	    renderer.addXTextLabel(2, "2 Pin");
	    renderer.addXTextLabel(3, "3 Pin");
	    renderer.addXTextLabel(4, "4 Pin");
	    renderer.addXTextLabel(5, "5 Pin");
	    renderer.addXTextLabel(6, "6 Pin");
	    renderer.addXTextLabel(7, "7 Pin");
	    renderer.addXTextLabel(8, "8 Pin");
	    renderer.addXTextLabel(9, "9 Pin");
	    renderer.addXTextLabel(10, "10 Pin");
	    renderer.setMargins(new int[] {20, 30, 15, 0}); 
	    renderer.setPanEnabled(true, false);
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.GREEN);
	    renderer.addSeriesRenderer(r);
	    r = new SimpleSeriesRenderer();
	    r.setColor(Color.RED);
	    renderer.addSeriesRenderer(r);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
	      seriesRenderer.setDisplayChartValues(true);
	      seriesRenderer.setChartValuesTextSize(30f);
	    }
	    return renderer;
	}
	
	private XYMultipleSeriesDataset sparesByPinDataset() {
		Double count = 0.0;
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		String[] scoreArray = {"-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-"}; 
		Cursor cursor = dbHelper.fetchStrikeSpareData(bowler, sqlDate1, sqlDate2);
	    CategorySeries series = new CategorySeries("Spares");
	    for(int i = 1; i < 11; i++){
	    	count = 0.0;
	    	cursor.moveToFirst();
	    	for (int k = 0; k < cursor.getCount(); k++){
	    		for(int p = 0; p < 21; p++){
	    			scoreArray[p] = cursor.getString(p);
	    		}
	    		count = count + calculator.singleSparesByPin(scoreArray, i);
	    		cursor.moveToNext();
	    	}
	    series.add(count);
	    }
	    dataset.addSeries(series.toXYSeries());
	    
	    series = new CategorySeries("Opens");
	    for(int i = 1; i < 11; i++){
	    	count = 0.0;
	    	cursor.moveToFirst();
	    	for (int k = 0; k < cursor.getCount(); k++){
	    		for(int p = 0; p < 21; p++){
	    			scoreArray[p] = cursor.getString(p);
	    		}
	    		count = count + calculator.singlePinOpen(scoreArray, i);
	    		cursor.moveToNext();
	    	}
	    series.add(count);
	    }
	    dataset.addSeries(series.toXYSeries());
	    cursor.close();
	    return dataset;
	}
}
