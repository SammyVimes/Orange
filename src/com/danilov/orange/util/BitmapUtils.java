package com.danilov.orange.util;

import android.graphics.Bitmap;

public class BitmapUtils {
	
	public static long getSizeInBytes(final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
	}
	
	public static int getSizeInKilobytes(final Bitmap bitmap) {
		return (int) (getSizeInBytes(bitmap) / 1024);
	}
	
}
