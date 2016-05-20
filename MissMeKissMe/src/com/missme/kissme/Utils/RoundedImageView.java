package com.missme.kissme.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

      public RoundedImageView(Context ctx, AttributeSet attrs) {
             super(ctx, attrs);
      }

      @SuppressWarnings("unused")
	@Override
      protected void onDraw(Canvas canvas) {

             Drawable drawable = getDrawable();

             if (drawable == null) {
                    return;
             }

             if (getWidth() == 0 || getHeight() == 0) {
                    return;
             }
             Bitmap b = ((BitmapDrawable) drawable).getBitmap();
             Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

             int w = getWidth(), h = getHeight();

             Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w);
             canvas.drawBitmap(roundBitmap, 0, 0, null);

      }

      public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
             Bitmap finalBitmap;
             if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
                    finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                                 false);
             else
                    finalBitmap = bitmap;
             Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                          finalBitmap.getHeight(), Config.ARGB_8888);
             Canvas canvas = new Canvas(output);

             final Paint paint = new Paint();
             final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                          finalBitmap.getHeight());

             paint.setAntiAlias(true);
             paint.setFilterBitmap(true);
             paint.setDither(true);
             canvas.drawARGB(0, 0, 0, 0);
             paint.setColor(Color.parseColor("#BAB399"));
           //  paint.setStyle(Paint.Style.STROKE);
             canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                          finalBitmap.getHeight() / 2 + 0.7f,
                          finalBitmap.getWidth() / 2 + 0.1f, paint);
             paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
             canvas.drawBitmap(finalBitmap, rect, rect, paint);

             return output;
      }


		/*public static Bitmap getRoundCroppedBitmapimg(Bitmap bmp, int radius) {
			Bitmap sbmp;
			if(bmp.getWidth() != radius || bmp.getHeight() != radius)
				sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
			else
				sbmp = bmp;
			 Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_8888);
			 
	            Canvas canvas = new Canvas(output);
	            final Paint paint = new Paint();
	            final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
	            paint.setAntiAlias(true);
	            paint.setFilterBitmap(true);
	            paint.setDither(true);
	            canvas.drawARGB(0, 0, 0, 0);
	            paint.setColor(Color.parseColor("#BAB399"));
	 
	            //--CROP THE IMAGE
	            canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2 - 1, paint);
	            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	            canvas.drawBitmap(sbmp, rect, rect, paint);
			
	             final Paint paint2 = new Paint();
	             paint2.setAntiAlias(true);
	             paint2.setColor(Color.parseColor("#FFFFFF"));
	             paint2.setStrokeWidth(4);
	             paint2.setStyle(Paint.Style.STROKE);
	             canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, (float) (sbmp.getWidth() / 2 - Math.ceil(5 / 2)), paint2);
	
			return output;
		}*/
		

}
