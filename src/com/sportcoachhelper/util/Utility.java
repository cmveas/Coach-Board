package com.sportcoachhelper.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Utility {

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
	
}
