package net.twisterrob.jpegtest;

import android.graphics.Matrix;

@SuppressWarnings("JavadocReference")
public class Images {
	// Constants for converting RGB to Y (luminance) CbCr (red and blue chromaticities)
	// Various applications use different constants, some increasing the brightness (C*OFFSET), some don't.
	// Some are using floating point values, but most using fast integer arithmetic,
	// to accomodate both this is using double types where needed, in production this could be changed to int.

	private static final double CYR = Skia.CYR;
	private static final double CYG = Skia.CYG;
	private static final double CYB = Skia.CYB;

	private static final double CUR = Skia.CUR;
	private static final double CUG = Skia.CUG;
	private static final double CUB = Skia.CUB;

	private static final double CVR = Skia.CVR;
	private static final double CVG = Skia.CVG;
	private static final double CVB = Skia.CVB;

	private static final int CSHIFT = Skia.CSHIFT;
	private static final int COFFSET = Skia.COFFSET;
	private static final int CYOFFSET = Skia.CYOFFSET;
	private static final int CUOFFSET = Skia.CUOFFSET;
	private static final int CVOFFSET = Skia.CVOFFSET;

	public static int[] getARGBFromNV21(byte[] nv21, int width, int height) {
		int[] argb = new int[width * height];
		decodeYUV420SP(nv21, width, height, argb);
		return argb;
	}

	// http://stackoverflow.com/q/12469730/253468 extended to reverse any transformation
	public static void decodeYUV420SP(byte[] yuv, int width, int height, int[] outARGB) {
		final int frameSize = width * height;

		final int ii = 0;
		final int ij = 0;
		final int di = +1;
		final int dj = +1;

		Matrix matrix = new Matrix();
		matrix.setValues(new float[] {
				(float)CYR, (float)CYG, (float)CYB,
				(float)CUR, (float)CUG, (float)CUB,
				(float)CVR, (float)CVG, (float)CVB,
		});
		Matrix inverse = new Matrix();
		matrix.invert(inverse);
		float[] INV = new float[9];
		inverse.getValues(INV);

		double mul = 1 << CSHIFT;
		int index = 0;
		for (int i = 0, ci = ii; i < height; ++i, ci += di) {
			for (int j = 0, cj = ij; j < width; ++j, cj += dj) {
				int y = yuv[ci * width + cj] & 0xff;
				int v = yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 0] & 0xff;
				int u = yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 1] & 0xff;
				y = y < CYOFFSET? CYOFFSET : y;
				double r1 = INV[0] * (y - CYOFFSET) * mul - COFFSET;
				double r3 = INV[2] * (v - CVOFFSET) * mul - COFFSET;
				int r = (int)(r1 + r3);
				double g1 = INV[3] * (y - CYOFFSET) * mul - COFFSET;
				double g2 = INV[4] * (u - CUOFFSET) * mul - COFFSET;
				double g3 = INV[5] * (v - CVOFFSET) * mul - COFFSET;
				int g = (int)(g1 + g2 + g3);
				double b1 = INV[6] * (y - CYOFFSET) * mul - COFFSET;
				double b2 = INV[7] * (u - CUOFFSET) * mul - COFFSET;
				int b = (int)(b1 + b2);

				r = r < 0? 0 : (r > 255? 255 : r);
				g = g < 0? 0 : (g > 255? 255 : g);
				b = b < 0? 0 : (b > 255? 255 : b);

				outARGB[index++] = 0xff000000 | (r << 16) | (g << 8) | b;
			}
		}
	}

	public static byte[] getNV21FromARGB(int[] argb, int width, int height) {
		byte[] yuv = new byte[width * height * 3 / 2];
		encodeYUV420SP(argb, width, height, yuv);
		return yuv;
	}

	// http://stackoverflow.com/a/13055615/253468
	public static void encodeYUV420SP(int[] argb, int width, int height, byte[] outYUV420SP) {
		final int frameSize = width * height;

		int yIndex = 0;
		int uvIndex = frameSize;

		int a, R, G, B, Y, U, V;
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
				R = (argb[index] & 0xff0000) >> 16;
				G = (argb[index] & 0xff00) >> 8;
				B = (argb[index] & 0xff) >> 0;

				Y = ((int)(CYR * R + CYG * G + CYB * B + COFFSET) >> CSHIFT) + CYOFFSET;
				U = ((int)(CUR * R + CUG * G + CUB * B + COFFSET) >> CSHIFT) + CUOFFSET;
				V = ((int)(CVR * R + CVG * G + CVB * B + COFFSET) >> CSHIFT) + CVOFFSET;

				// NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
				//    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
				//    pixel AND every other scanline.
				outYUV420SP[yIndex++] = (byte)((Y < 0)? 0 : ((Y > 255)? 255 : Y));
				if (y % 2 == 0 && index % 2 == 0) {
					outYUV420SP[uvIndex++] = (byte)((V < 0)? 0 : ((V > 255)? 255 : V));
					outYUV420SP[uvIndex++] = (byte)((U < 0)? 0 : ((U > 255)? 255 : U));
				}

				index++;
			}
		}
	}

	public static byte[] getYV12FromARGB(int[] argb, int width, int height) {
		byte[] yuv = new byte[width * height * 3 / 2];
		encodeYV12(argb, width, height, yuv);
		return yuv;
	}
	// http://stackoverflow.com/a/13055615/253468
	public static void encodeYV12(int[] argb, int width, int height, byte[] outYVU12) {
		final int frameSize = width * height;

		int yIndex = 0;
		int uIndex = frameSize;
		int vIndex = frameSize + (frameSize / 4);

		int a, R, G, B, Y, U, V;
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
				R = (argb[index] & 0xff0000) >> 16;
				G = (argb[index] & 0xff00) >> 8;
				B = (argb[index] & 0xff) >> 0;

				Y = ((int)(CYR * R + CYG * G + CYB * B + COFFSET) >> CSHIFT) + CYOFFSET;
				U = ((int)(CUR * R + CUG * G + CUB * B + COFFSET) >> CSHIFT) + CUOFFSET;
				V = ((int)(CVR * R + CVG * G + CVB * B + COFFSET) >> CSHIFT) + CVOFFSET;

				// YV12 has a plane of Y and two chroma plans (U, V) planes each sampled by a factor of 2
				//    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
				//    pixel AND every other scanline.
				outYVU12[yIndex++] = (byte)((Y < 0)? 0 : ((Y > 255)? 255 : Y));
				if (y % 2 == 0 && index % 2 == 0) {
					outYVU12[uIndex++] = (byte)((V < 0)? 0 : ((V > 255)? 255 : V));
					outYVU12[vIndex++] = (byte)((U < 0)? 0 : ((U > 255)? 255 : U));
				}

				index++;
			}
		}
	}

	public static byte[] getYCbCrFromRGB(int[] argb, int width, int height) {
		byte[] ycc = new byte[width * height * 3];
		encodeYCbCr(argb, width, height, ycc);
		return ycc;
	}
	public static void encodeYCbCr(int[] argb, int width, int height, byte[] outYCbCr) {
		int a, R, G, B, Y, Cb, Cr;
		int index = 0, outIndex = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
				R = (argb[index] & 0x00ff0000) >> 16;
				G = (argb[index] & 0x0000ff00) >> 8;
				B = (argb[index] & 0x000000ff) >> 0;

				Y = ((int)(CYR * R + CYG * G + CYB * B + COFFSET) >> CSHIFT) + CYOFFSET;
				Cb = ((int)(CUR * R + CUG * G + CUB * B + COFFSET) >> CSHIFT) + CUOFFSET;
				Cr = ((int)(CVR * R + CVG * G + CVB * B + COFFSET) >> CSHIFT) + CVOFFSET;

				outYCbCr[outIndex++] = (byte)((Y < 0)? 0 : ((Y > 255)? 255 : Y));
				outYCbCr[outIndex++] = (byte)((Cb < 0)? 0 : ((Cb > 255)? 255 : Cb));
				outYCbCr[outIndex++] = (byte)((Cr < 0)? 0 : ((Cr > 255)? 255 : Cr));

				index++;
			}
		}
	}

	public static int[] getARGBFromYCC(byte[] ycc, int width, int height) {
		int[] argb = new int[width * height];
		decodeYCbCr(ycc, width, height, argb);
		return argb;
	}
	public static void decodeYCbCr(byte[] ycc, int width, int height, int[] outARGB) {
		Matrix matrix = new Matrix();
		matrix.setValues(new float[] {
				(float)CYR, (float)CYG, (float)CYB,
				(float)CUR, (float)CUG, (float)CUB,
				(float)CVR, (float)CVG, (float)CVB,
		});
		Matrix inverse = new Matrix();
		matrix.invert(inverse);
		float[] INV = new float[9];
		inverse.getValues(INV);

		double mul = 1 << CSHIFT;
		int index = 0, inIndex = 0;
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int Y = ycc[inIndex++] & 0xff;
				int Cb = ycc[inIndex++] & 0xff;
				int Cr = ycc[inIndex++] & 0xff;
				Y = Y < CYOFFSET? CYOFFSET : Y;
				double r1 = INV[0] * (Y - CYOFFSET) * mul - COFFSET;
				double r3 = INV[2] * (Cr - CVOFFSET) * mul - COFFSET;
				int r = (int)(r1 + r3);
				double g1 = INV[3] * (Y - CYOFFSET) * mul - COFFSET;
				double g2 = INV[4] * (Cb - CUOFFSET) * mul - COFFSET;
				double g3 = INV[5] * (Cr - CVOFFSET) * mul - COFFSET;
				int g = (int)(g1 + g2 + g3);
				double b1 = INV[6] * (Y - CYOFFSET) * mul - COFFSET;
				double b2 = INV[7] * (Cb - CUOFFSET) * mul - COFFSET;
				int b = (int)(b1 + b2);

				r = r < 0? 0 : (r > 255? 255 : r);
				g = g < 0? 0 : (g > 255? 255 : g);
				b = b < 0? 0 : (b > 255? 255 : b);

				outARGB[index++] = 0xff000000 | (r << 16) | (g << 8) | b;
			}
		}
	}

	public static byte[] getAccurateNV21FromARGB(int[] argb, int width, int height) {
		byte[] ycc = new byte[width * height * 3 / 2];
		LibJPEG.rgb_ycc_convert(argb, width, height, ycc);
		return ycc;
	}

	/**
	 * Android 4.4.2 uses these constants
	 * @see http://stackoverflow.com/a/8117210/253468
	 */
	@SuppressWarnings("unused")
	private static final class Skia {
		private static final int CYR = 77; // 0.299
		private static final int CYG = 150; // 0.587
		private static final int CYB = 29; // 0.114

		private static final int CUR = -43; // -0.16874
		private static final int CUG = -85; // -0.33126
		private static final int CUB = 128; // 0.5

		private static final int CVR = 128; // 0.5
		private static final int CVG = -107; // -0.41869
		private static final int CVB = -21; // -0.08131

		private static final int CSHIFT = 8;
		private static final int COFFSET = 0;
		private static final int CYOFFSET = 0;
		private static final int CUOFFSET = 128;
		private static final int CVOFFSET = 128;
	}

	/**
	 * Very little difference to Android 4.4.2
	 * @see http://stackoverflow.com/a/8117210/253468
	 */
	@SuppressWarnings("unused")
	private static final class SkiaH {
		private static final int CYR = 19595;  // 0.299
		private static final int CYG = 38470;  // 0.587
		private static final int CYB = 7471;  // 0.114

		private static final int CUR = -11059;  // -0.16874
		private static final int CUG = -21709;  // -0.33126
		private static final int CUB = 32768;  // 0.5

		private static final int CVR = 32768;  // 0.5
		private static final int CVG = -27439;  // -0.41869
		private static final int CVB = -5329;  // -0.08131

		private static final int CSHIFT = 16;
		private static final int COFFSET = 0;
		private static final int CYOFFSET = 0;
		private static final int CUOFFSET = 128;
		private static final int CVOFFSET = 128;
	}

	/**
	 * Weirdest one, being "well known", but changes brightness.
	 * Probably has some other application, but these were the original values that came with the code.
	 * @see http://stackoverflow.com/a/13055615/253468
	 */
	@SuppressWarnings("unused")
	private static final class SO {
		private static final int CYR = 66; // 0.2588
		private static final int CYG = 129; // 0.5058
		private static final int CYB = 25; // 0.98

		private static final int CUR = -38;
		private static final int CUG = -74;
		private static final int CUB = 112;

		private static final int CVR = 112;
		private static final int CVG = -94;
		private static final int CVB = -18;

		private static final int CSHIFT = 8;
		private static final int COFFSET = 128;
		private static final int CYOFFSET = 16;
		private static final int CUOFFSET = 128;
		private static final int CVOFFSET = 128;
	}

	/**
	 * Official mathematical constants in LibJPEG
	 * @see https://en.wikipedia.org/wiki/YCbCr#JPEG_conversion
	 */
	@SuppressWarnings("unused")
	private static final class JPEG {
		private static final double CYR = 0.299;
		private static final double CYG = 0.587;
		private static final double CYB = 0.114;

		private static final double CUR = -0.168736;
		private static final double CUG = -0.331264;
		private static final double CUB = 0.5;

		private static final double CVR = 0.5;
		private static final double CVG = -0.418688;
		private static final double CVB = -0.081312;

		private static final int CSHIFT = 0;
		private static final int COFFSET = 0;
		private static final int CYOFFSET = 0;
		private static final int CUOFFSET = 128;
		private static final int CVOFFSET = 128;
	}

	private static class LibJPEG {
		// constants from jcolor.c

		private static final int JSAMPLE_SIZE = 255 + 1;
		private static final int CENTERJSAMPLE = 128;
		/** speediest right-shift on some machines */
		private static final int SCALEBITS = 16;
		private static final int CBCR_OFFSET = CENTERJSAMPLE << SCALEBITS;
		private static final int ONE_HALF = 1 << (SCALEBITS - 1);
		private static int FIX(double x) {
			return ((int)((x) * (1L << SCALEBITS) + 0.5));
		}

		/* offsets to RGB => YCbCr sections in the table */
		private static final int R_Y_OFFSET = 0 * JSAMPLE_SIZE;
		private static final int G_Y_OFFSET = 1 * JSAMPLE_SIZE;
		private static final int B_Y_OFFSET = 2 * JSAMPLE_SIZE;
		private static final int R_CB_OFFSET = 3 * JSAMPLE_SIZE;
		private static final int G_CB_OFFSET = 4 * JSAMPLE_SIZE;
		/** B=>Cb, R=>Cr are the same */
		private static final int B_CB_OFFSET = 5 * JSAMPLE_SIZE;
		/** B=>Cb, R=>Cr are the same */
		private static final int R_CR_OFFSET = 6 * JSAMPLE_SIZE;
		private static final int G_CR_OFFSET = 7 * JSAMPLE_SIZE;
		private static final int B_CR_OFFSET = 8 * JSAMPLE_SIZE;
		private static final int TABLE_SIZE = 9 * JSAMPLE_SIZE;
		/**
		 * We allocate one big table and divide it up into eight parts, instead of doing eight alloc_small requests.
		 * This lets us use a single table base address, which can be held in a register in the inner loops on many machines
		 * (more than can hold all eight addresses, anyway).
		 */
		private static final int[] rgb_ycc_tab = new int[TABLE_SIZE];

		static { // rgb_ycc_start
			for (int i = 0; i < JSAMPLE_SIZE; i++) {
				rgb_ycc_tab[R_Y_OFFSET + i] = FIX(0.299) * i;
				rgb_ycc_tab[G_Y_OFFSET + i] = FIX(0.587) * i;
				rgb_ycc_tab[B_Y_OFFSET + i] = FIX(0.114) * i + ONE_HALF;
				rgb_ycc_tab[R_CB_OFFSET + i] = -FIX(0.168735892) * i;
				rgb_ycc_tab[G_CB_OFFSET + i] = -FIX(0.331264108) * i;
				/* We use a rounding fudge-factor of 0.5-epsilon for Cb and Cr.
				 * This ensures that the maximum output will round to MAXJSAMPLE
				 * not MAXJSAMPLE+1, and thus that we don't have to range-limit.
				 */
				rgb_ycc_tab[B_CB_OFFSET + i] = FIX(0.5) * i + CBCR_OFFSET + ONE_HALF - 1;
				rgb_ycc_tab[R_CR_OFFSET + i] = FIX(0.5) * i + CBCR_OFFSET + ONE_HALF - 1;
				rgb_ycc_tab[G_CR_OFFSET + i] = -FIX(0.418687589) * i;
				rgb_ycc_tab[B_CR_OFFSET + i] = -FIX(0.081312411) * i;
			}
		}

		static void rgb_ycc_convert(int[] argb, int width, int height, byte[] ycc) {
			int[] tab = LibJPEG.rgb_ycc_tab;
			final int frameSize = width * height;

			int yIndex = 0;
			int uvIndex = frameSize;
			int index = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
//				    int a = (argb[index] & 0xff000000) >> 24;
					int r = (argb[index] & 0x00ff0000) >> 16;
					int g = (argb[index] & 0x0000ff00) >> 8;
					int b = (argb[index] & 0x000000ff) >> 0;

					/* If the inputs are 0..MAXJSAMPLE, the outputs of these equations must be too;
					 * we do not need an explicit range-limiting operation.
					 * Hence the value being shifted is never negative, and we don't need the general RIGHT_SHIFT macro.
					 */
					byte Y = (byte)((tab[r + R_Y_OFFSET] + tab[g + G_Y_OFFSET] + tab[b + B_Y_OFFSET]) >> SCALEBITS);
					byte Cb = (byte)((tab[r + R_CB_OFFSET] + tab[g + G_CB_OFFSET] + tab[b + B_CB_OFFSET]) >> SCALEBITS);
					byte Cr = (byte)((tab[r + R_CR_OFFSET] + tab[g + G_CR_OFFSET] + tab[b + B_CR_OFFSET]) >> SCALEBITS);

					ycc[yIndex++] = Y;
					if (y % 2 == 0 && index % 2 == 0) {
						ycc[uvIndex++] = Cr;
						ycc[uvIndex++] = Cb;
					}
					index++;
				}
			}
		}
	}
}
