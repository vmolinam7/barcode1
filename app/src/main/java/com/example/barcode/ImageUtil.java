package com.example.barcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class ImageUtil {
    public static Bitmap loadImageFromUri(Uri uri, Context context) {
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
