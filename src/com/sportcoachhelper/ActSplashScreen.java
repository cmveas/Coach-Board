package com.sportcoachhelper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.sportcoachhelper.managers.FontManager;
import com.sportcoachhelper.util.Utility;

public class ActSplashScreen extends Activity {

	private TextView splash_title;
	private Button enter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Utility.setHoloTheme(this);
		
		setContentView(R.layout.splash);
		
		splash_title = (TextView) findViewById(R.id.splash_title);
		enter = (Button) findViewById(R.id.enter);
		
		splash_title.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));
		enter.setTypeface(FontManager.getInstance().getFont(FontManager.CHALK_REGULAR));


        setLikeButton();
	}
	
	
	public void proceed(View view){
		Intent intent = new Intent(this,ActHomeScreen.class);
		startActivity(intent);
	}

    public void setLikeButton(){
        Button foll_fb = (Button) findViewById(R.id.btn_visit_fb_page);
        foll_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/CoachAssistant"));
                startActivity(myIntent);
            }
        });
    }
	
}
