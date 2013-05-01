package com.sportcoachhelper.util;

import com.sportcoachhelper.CoachApp;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

	private FontManager() {
		loadFonts(CoachApp.getInstance());
	}

	public static FontManager instance;

	public static FontManager getInstance() {
		if (instance == null) {
			instance = new FontManager();
		}
		return instance;
	}

	public static final int CHALK_REGULAR = 0;
	public static final int CHALK_MEDIUM = 1;
	public static final int CHALK_LIGHT = 2;
	public static final int CHALK_THIN = 3;
	public static final int CHALK_BLACK = 4;
	public static final int CHALK_BOLD = 5;
	public static final int CHALK_ITALIC = 6;
	public static final int CHALK_CONDENSED = 7;
	public static final int CHALK_MEDIUM_ITALIC = 8;
	public static final int CHALK_LIGHT_ITALIC = 9;
	public static final int CHALK_THIN_ITALIC = 10;
	public static final int CHALK_BLACK_ITALIC = 11;
	public static final int CHALK_BOLD_CONDENSED = 12;
	public static final int CHALK_BOLD_CONDENSED_ITALIC = 13;
	public static final int CHALK_BOLD_ITALIC = 14;
	public static final int CHALK_CONDENSED_ITALIC = 15;
	public static final int CHALK_SIZE = 1;

	private static Typeface[] CHALKTypeFaces;

	private void loadFonts(Context context) {
		CHALKTypeFaces = new Typeface[CHALK_SIZE];
		CHALKTypeFaces[CHALK_REGULAR] = Typeface.createFromAsset(context.getAssets(), "wit.ttf");
		/*CHALKTypeFaces[CHALK_MEDIUM] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-Medium.ttf");
		CHALKTypeFaces[CHALK_LIGHT] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-Light.ttf");
		CHALKTypeFaces[CHALK_THIN] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-Thin.ttf");
		CHALKTypeFaces[CHALK_BLACK] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-Black.ttf");
		CHALKTypeFaces[CHALK_BOLD] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-Bold.ttf");
		CHALKTypeFaces[CHALK_ITALIC] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-Italic.ttf");
		CHALKTypeFaces[CHALK_CONDENSED] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-Condensed.ttf");
		CHALKTypeFaces[CHALK_MEDIUM_ITALIC] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-MediumItalic.ttf");
		CHALKTypeFaces[CHALK_LIGHT_ITALIC] = Typeface.createFromAsset(context.getAssets(), "fonts/CHALK-LightItalic.ttf");*/

	}

	public Typeface getFont(int font) {
		if (font >= 0 && font < CHALKTypeFaces.length) {
			return CHALKTypeFaces[font];
		}
		return null;
	}

}
