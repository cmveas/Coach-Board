package com.sportcoachhelper.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import com.sportcoachhelper.CoachApp;
import com.sportcoachhelper.R;
import com.sportcoachhelper.paths.BallPath;

import java.io.ByteArrayOutputStream;

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
			 activity.setTheme(R.style.Theme_Coachbar);
		 }
	}

    public static int giveMeFieldBall(String field) {
        int resource = BallPath.SOCCER_BALL;
        final String volley = CoachApp.getInstance().getString(R.string.voley);
        final String soccer = CoachApp.getInstance().getString(R.string.soccer);
        final String basket = CoachApp.getInstance().getString(R.string.basketball);
        if (field.equals(soccer)) {
            resource = BallPath.SOCCER_BALL;
        } else if (field.equals(basket)) {
            resource = BallPath.BASKETBALL;
        } else if (field.equals(volley)) {
            resource = BallPath.VOLLEYBALL;
        }
        return resource;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int resource, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resource,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resource,options);
    }

    public static Bitmap decodeBitmapHalfResFromResource(Resources resources, int resource) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // Calculate inSampleSize
        options.inSampleSize = 2;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resource,options);
    }

    public static int getPercentageDimension(int x,int totalDimension) {
        return (x*100)/totalDimension;
    }

    /**
     * Transforms the bitmap into a Byte array
     * @param bmp Bitmap to transform
     * @return Array of bytes that represents the bitmap
     */
    public static byte[] getByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    public static Bitmap decodeSampledBitmapFromArray(byte[] linesArray, int screenWidth, int screenHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(linesArray,0,linesArray.length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, screenWidth, screenHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(linesArray,0,linesArray.length,options);

    }
}
