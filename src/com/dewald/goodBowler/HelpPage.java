package com.dewald.goodBowler;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpPage extends Activity {
	
	private WebView helpView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		helpView = (WebView)findViewById(R.id.helpView);
		helpView.loadUrl("file:///android_asset/goodBowlerHelp.html");
	}
}
