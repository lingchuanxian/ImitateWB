package cn.fjlcx.android.imitatewb.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * class description here
 *
 * @author ling_cx
 * @date 2017/8/15
 */

public class FileUtils {
	/**
	 * InputStrem 转byte[]
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStreamToBytes(InputStream in) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 8];
		int length = -1;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		out.flush();
		byte[] result = out.toByteArray();
		in.close();
		out.close();
		return result;
	}

	/**
	 * 写入文件
	 *
	 * @param in
	 * @param file
	 */
	public static void writeFile(InputStream in, File file) throws IOException {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		if (file != null && file.exists())
			file.delete();

		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[1024 * 128];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		in.close();

	}

	/**
	 * 得到Bitmap的byte
	 *
	 * @return
	 * @author YOLANDA
	 */
	public static byte[] bmpToByteArray(Bitmap bmp) {
		if (bmp == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 80, output);

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	/*
	* 根据view来生成bitmap图片，可用于截图功能
	*/
	public static Bitmap getViewBitmap(View v) {

		v.clearFocus(); //

		v.setPressed(false); //
		// 能画缓存就返回false

		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);

		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			v.destroyDrawingCache();
		}

		v.buildDrawingCache();

		Bitmap cacheBitmap = v.getDrawingCache();

		if (cacheBitmap == null) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view

		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);

		return bitmap;
	}

	/**
	 * 根据文件路径获取文件
	 *
	 * @param filePath 文件路径
	 * @return 文件
	 */
	public static File getFileByPath(String filePath) {
		return isSpace(filePath) ? null : new File(filePath);
	}

	private static boolean isSpace(String s) {
		if (s == null) return true;
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param filePath 文件路径
	 * @return {@code true}: 存在<br>{@code false}: 不存在
	 */
	public static boolean isFileExists(String filePath) {
		return isFileExists(getFileByPath(filePath));
	}


	/**
	 * 判断文件是否存在
	 *
	 * @param file 文件
	 * @return {@code true}: 存在<br>{@code false}: 不存在
	 */
	public static boolean isFileExists(File file) {
		return file != null && file.exists();
	}

	/**
	 * 获取全路径中的文件拓展名
	 *
	 * @param file 文件
	 * @return 文件拓展名
	 */
	public static String getFileExtension(File file) {
		if (file == null) return null;
		return getFileExtension(file.getPath());
	}

	/**
	 * 获取全路径中的文件拓展名
	 *
	 * @param filePath 文件路径
	 * @return 文件拓展名
	 */
	public static String getFileExtension(String filePath) {
		if (isSpace(filePath)) return filePath;
		int lastPoi = filePath.lastIndexOf('.');
		int lastSep = filePath.lastIndexOf(File.separator);
		if (lastPoi == -1 || lastSep >= lastPoi) return "";
		return filePath.substring(lastPoi + 1);
	}

	/**
	 * 获取MIME类型
	 * @param file
	 * @return
	 */
	public static String getMIMEType(File file) {
		String type="*/*";
		String fName = file.getName();
		int dotIndex = fName.lastIndexOf(".");
		if(dotIndex < 0){
			return type;
		}
		String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		if(end=="")return type;
		for(int i = 0; i< MIME.MIME_MapTable.length; i++){
			if(end.equals(MIME.MIME_MapTable[i][0]))
				type = MIME.MIME_MapTable[i][1];
		}
		return type;
	}

	/**
	 * 打开文件
	 * @param file
	 * @param context
	 */
	public static void openFile(File file, Context context){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), type);
		try{
			context.startActivity(intent);
		}catch (Exception e){
			Log.d("FileUtils", "openFile: "+e.getMessage());
		}
	}
}
