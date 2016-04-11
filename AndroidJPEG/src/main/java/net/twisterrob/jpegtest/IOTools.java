package net.twisterrob.jpegtest;

import java.io.*;

public class IOTools {
	public static void writeFile(File file, byte[] bytes) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public static  byte[] readFile(File file) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			FileInputStream in = new FileInputStream(file);
			byte[] buffer = new byte[16 * 1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
			}
			in.close();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return out.toByteArray();
	}
}
