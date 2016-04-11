package net.twisterrob.jpegtest;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import android.graphics.*;
import android.graphics.Bitmap.Config;

public class Bitmaps {
	public static byte[] getRawBytes(Bitmap bitmap) {
		return getRawBytesWithBuffer(bitmap);
	}
	/**
	 * @see #getRawBytesWithBuffer(Bitmap)
	 */
	private static byte[] getRawBytesManually(Bitmap bitmap) {
		int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		byte[] argb = new byte[pixels.length * 4];
		for (int p = 0, c = 0; p < pixels.length; p++, c += 4) {
			int pixel = pixels[p];
			argb[c + 0] = (byte)Color.alpha(pixel);
			argb[c + 1] = (byte)Color.red(pixel);
			argb[c + 2] = (byte)Color.green(pixel);
			argb[c + 3] = (byte)Color.blue(pixel);
		}
		return argb;
	}
	/**
	 * @see #getRawBytesManually(Bitmap)
	 */
	private static byte[] getRawBytesWithBuffer(Bitmap bitmap) {
		ByteBuffer buffer = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
		bitmap.copyPixelsToBuffer(buffer);
		return buffer.array();
	}

	public static byte[] getNV21(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int[] argb = new int[w * h];
		bitmap.getPixels(argb, 0, w, 0, 0, w, h);
		return Images.getNV21FromARGB(argb, w, h);
	}
	public static byte[] getYV12(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int[] argb = new int[w * h];
		bitmap.getPixels(argb, 0, w, 0, 0, w, h);
		return Images.getYV12FromARGB(argb, w, h);
	}
	public static byte[] getYCC(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int[] argb = new int[w * h];
		bitmap.getPixels(argb, 0, w, 0, 0, w, h);
		return Images.getYCbCrFromRGB(argb, w, h);
	}

	public static Bitmap bitmapFromARGB(int[] argb, int w, int h) {
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		bitmap.setPixels(argb, 0, w, 0, 0, w, h);
		return bitmap;
	}

	public static byte[] compressYUVImage(byte[] yuv, int width, int height, int format) {
		ByteArrayOutputStream jpeg = new ByteArrayOutputStream();
		YuvImage yuvImage = new YuvImage(yuv, format, width, height, null);
		yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, jpeg);
		return jpeg.toByteArray();
	}
}
