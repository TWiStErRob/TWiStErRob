package net.twisterrob.jpegtest;

import java.io.*;
import java.util.Locale;

import android.annotation.TargetApi;
import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.os.Build.*;
import android.os.Bundle;
import android.support.v4.view.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {
	public static final int IGNORE_QUALITY = 100;
	private Bitmap bitmap;
	private ImageView result;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			bitmap = loadBitmap(getAssets().open("image.jpg"));
		} catch (Throwable ex) {
			Log.e("BITMAP", "Cannot load original image.", ex);
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		setContentView(R.layout.activity_main);
		ViewPager images = (ViewPager)findViewById(R.id.images);
		images.setAdapter(new ImagesPagerAdapter());
		result = (ImageView)findViewById(R.id.result);
	}
	private void update(File file) {
		// Make the file immediately discoverable through USB
		MediaScannerConnection.scanFile(getApplicationContext(),
				new String[] {file.getAbsolutePath()}, null, null);
		((TextView)findViewById(R.id.name)).setText(file.getAbsolutePath());
		try {
			Bitmap bitmap = loadBitmap(new FileInputStream(file));
			result.setImageBitmap(bitmap);
		} catch (Throwable ex) {
			Log.e("BITMAP", "Cannot load result image.", ex);
			result.setImageDrawable(null);
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private Bitmap loadBitmap(InputStream stream) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inPreferQualityOverSpeed = false;
		options.inDither = false;
		options.inSampleSize = 1;
		options.inScaled = false;
		return BitmapFactory.decodeStream(stream, null, options);
	}

	public int getQuality() {
		CharSequence quality = ((TextView)findViewById(R.id.quality)).getText();
		try {
			return Integer.parseInt(String.valueOf(quality));
		} catch (NumberFormatException ex) {
			Log.w("SAVE", "Invalid quality: " + quality);
			return 100;
		}
	}

	public void saveRaw(View view) throws Exception {
		File file = getFile("orig-lossless", IGNORE_QUALITY, "raw");
		IOTools.writeFile(file, Bitmaps.getRawBytes(bitmap));
	}

	public void savePNG(View view) throws Exception {
		File file = getFile("orig-lossless", IGNORE_QUALITY, "png");
		bitmap.compress(CompressFormat.PNG, IGNORE_QUALITY, new FileOutputStream(file));
	}
	public void saveJPEG(View view) throws Exception {
		File file = getFile("orig", getQuality(), "jpg");
		bitmap.compress(CompressFormat.JPEG, getQuality(), new FileOutputStream(file));
	}
	@TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
	public void saveWEBP(View view) throws Exception {
		File file = getFile("orig", getQuality(), "webp");
		bitmap.compress(CompressFormat.WEBP, getQuality(), new FileOutputStream(file));
	}
	public void saveJPEGAllQuality(View view) throws Exception {
		for (int quality = 0; quality <= 100; ++quality) {
			File file = getFile("orig", quality, "jpg");
			bitmap.compress(CompressFormat.JPEG, quality, new FileOutputStream(file));
		}
	}
	@TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
	public void saveWEBPAllQuality(View view) throws Exception {
		for (int quality = 0; quality <= 100; ++quality) {
			File file = getFile("orig", quality, "jpg");
			bitmap.compress(CompressFormat.WEBP, quality, new FileOutputStream(file));
		}
	}

	public void dumpYUV(View view) throws Exception {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		byte[] nv21 = Bitmaps.getNV21(bitmap);
		int[] rgb = Images.getARGBFromNV21(nv21, w, h);
		Bitmap yuvRgb = Bitmaps.bitmapFromARGB(rgb, w, h);
		File file = getFile("nv21-conversion-loss", IGNORE_QUALITY, "png");
		yuvRgb.compress(CompressFormat.PNG, IGNORE_QUALITY, new FileOutputStream(file));
	}
	public void dumpYCC(View view) throws Exception {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		byte[] ycc = Bitmaps.getYCC(bitmap);
		int[] rgb = Images.getARGBFromYCC(ycc, w, h);
		Bitmap yccRgb = Bitmaps.bitmapFromARGB(rgb, w, h);
		File file = getFile("ycc-conversion-loss", IGNORE_QUALITY, "png");
		yccRgb.compress(CompressFormat.PNG, IGNORE_QUALITY, new FileOutputStream(file));
	}
	public void dumpLibJPEGYCC(View view) throws Exception {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int[] argb = new int[w * h];
		bitmap.getPixels(argb, 0, w, 0, 0, w, h);
		byte[] ycc = Images.getAccurateNV21FromARGB(argb, w, h);
		File file = getFile("libjpeg-like-good", IGNORE_QUALITY, "jpg");
		byte[] bytes = Bitmaps.compressYUVImage(ycc, w, h, ImageFormat.NV21);
		IOTools.writeFile(file, bytes);
	}

	/** manual YUV compression */
	public void saveNV21JPEG(View view) throws Exception {
		File file = getFile("nv21-YuvImage", getQuality(), "jpg");
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		byte[] nv21 = Bitmaps.getNV21(bitmap);
		byte[] bytes = Bitmaps.compressYUVImage(nv21, w, h, ImageFormat.NV21);
		IOTools.writeFile(file, bytes);
	}

	private File getFile(String base, int quality, String ext) {
		String name = String.format(Locale.ROOT, "%d_%s_%dx%d.%d.%s",
				System.currentTimeMillis(), base, bitmap.getWidth(), bitmap.getHeight(), quality, ext);
		final File file = new File(getExternalFilesDir(null), name);
		Log.d("SAVE", "Preparing file: " + file);

		// Run it on UI thread later (runOnUIThread is immediate), so it's called after writing the contents
		result.post(new Runnable() {
			@Override public void run() {
				update(file);
			}
		});
		return file;
	}

	private static native byte[] compress(Bitmap bitmap, int quality);
	private static native void dumpPixels(Bitmap bitmap, String fileName);
	private static native void test(String fileName, int width, int height, int quality);

	static {
		System.loadLibrary("libjpeg_wrapper");
	}

	public void dumpPixels(View view) {
		File file = getFile("libjpeg", IGNORE_QUALITY, "raw");
		dumpPixels(bitmap, file.getAbsolutePath());
	}

	public void test(View view) {
		File file = getFile("test", getQuality(), "jpg");
		test(file.getAbsolutePath(), bitmap.getWidth(), bitmap.getHeight(), getQuality());
	}

	public void compress(View view) {
		File file = getFile("jni", getQuality(), "jpg");
		byte[] jpeg = compress(bitmap, getQuality());
		IOTools.writeFile(file, jpeg);
	}
	private class ImagesPagerAdapter extends PagerAdapter {
		public static final int PAGE_ORIGINAL = 0;
		public static final int PAGE_RESULT = 1;

		@Override public int getCount() {
			return 2;
		}

		@Override public Object instantiateItem(final ViewGroup container, int position) {
			ImageView view;
			switch (position) {
				case PAGE_ORIGINAL:
					view = (ImageView)container.findViewById(R.id.original);
					break;
				case PAGE_RESULT:
					view = result;
					break;
				default:
					throw new IllegalArgumentException("Invalid position: " + position);
			}
			view.setImageBitmap(bitmap);
			view.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View v) {
					int nextPosition = (getItemPosition(v) + 1) % getCount();
					((ViewPager)container).setCurrentItem(nextPosition, false);
				}
			});
			return view;
		}

		@Override public int getItemPosition(Object object) {
			return result == object? PAGE_RESULT : PAGE_ORIGINAL;
		}

		@Override public CharSequence getPageTitle(int position) {
			switch (position) {
				case PAGE_ORIGINAL:
					return "Original";
				case PAGE_RESULT:
					return "Result";
				default:
					throw new IllegalArgumentException("Invalid position: " + position);
			}
		}

		@Override public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
}
