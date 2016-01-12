package uk.ac.uea.nostromo.bishop.tests;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.ApplicationTestCase;
import android.widget.ImageView;


import junit.framework.Assert;

import java.io.ByteArrayOutputStream;

import uk.ac.uea.nostromo.bishop.R;
import uk.ac.uea.nostromo.mother.implementation.Graphics;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testImageViewLoadsCorrectly() throws Exception {
        Graphics g = new Graphics(this.getContext());

        ImageView output = g.newImage(R.mipmap.uealogo2);

        Drawable expected = getContext().getResources().getDrawable(R.mipmap.uealogo2);
        Drawable actual = output.getDrawable();

        if (expected != null && actual != null) {

            Bitmap expectedBitmap = getBitmapOfDrawable(expected);

            Bitmap actualBitmap = getBitmapOfDrawable(actual);

            Assert.assertTrue(areBitmapsEqual(expectedBitmap, actualBitmap));
        }
        else {
            throw new Exception("Null reference trying to compare image views");
        }
    }

    public void testGoogleMapApi() throws Exception {
        ApplicationInfo info = getContext().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
    }

    private static Bitmap getBitmapOfDrawable(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        return bitmap;
    }
    private static boolean areBitmapsEqual(Bitmap bitmap1, Bitmap bitmap2) {
        // compare two bitmaps by their bytes
        byte[] array1 = BitmapToByteArray(bitmap1);
        byte[] array2 = BitmapToByteArray(bitmap2);
        if (java.util.Arrays.equals(array1, array2)) {
            return true;
        }
        return false;
    }

    private static byte[] BitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] result = bos.toByteArray();
        return result;
    }
}