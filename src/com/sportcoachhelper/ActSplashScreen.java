package com.sportcoachhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sportcoachhelper.util.FontManager;

public class ActSplashScreen extends Activity {

	private TextView splash_title;
	private Button enter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
		
		splash_title = (TextView) findViewById(R.id.splash_title);
		enter = (Button) findViewById(R.id.enter);
		
		splash_title.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
		enter.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
	}
	
	
	public void proceed(View view){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	
}
