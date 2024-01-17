package ca.cmpt276.project_7f.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Base64Utils {
    public static String bitmapToString(Bitmap bitmap)
    {
        // Bitmap to ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);

        // ByteArrayOutputStream to String
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String imageString)
    {
        // string to ByteArrayInputStream
        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // ByteArrayInputStream to Bitmap
        return BitmapFactory.decodeStream(byteArrayInputStream);
    }
}
