package com.sportcoachhelper.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;

public class Utility {

	public static final boolean DEBUG_ENABLED = true;

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		if (bm != null) {
			int width = bm.getWidth();
			int height = bm.getHeight();
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// create a matrix for the manipulation
			Matrix matrix = new Matrix();
			// resize the bit map
			matrix.postScale(scaleWidth, scaleHeight);

			// recreate the new Bitmap

			Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, false);

			return resizedBitmap;
		} else
			return bm;

	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void setHoloTheme(Activity activity){
		
		 if( (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)) {
			 activity.setTheme(android.R.style.Theme_Holo_Light);			 
		 }
	}
	
}
