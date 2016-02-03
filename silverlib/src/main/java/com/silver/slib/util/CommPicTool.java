/*
 *
 * Created by smallsilver on 1/6/16 3:19 PM
 * Email dongen_wang@163.com
 *
 * Copyright 2016 SmallSilver Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.silver.slib.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 
 * @ClassName: CommPicTool
 * @Description: 图片统一处理
 * @author dongen_wang@163.com
 * @date 2015年3月10日 下午6:47:07
 */
public class CommPicTool {

	private static String TAG = "CommPicTool";
	public static String PDA_PIC_PATH = CommTool.getUserDatabaseFolder()
			+ "/diaoyutmp/";
	private String cur_file_name;
	private String cur_file_path;
	private String cur_new_file_path;
	private static int PIC_IN_SAMPLE = 4;// 图片压缩的比率 1/5 高宽各压缩到1/5
	private static int PIC_JPG_COMPRESS_RATE = 99;// 图片的质量
//	public static int PIC_MAX_WIDTH = ProjectConst.PIC_MAX_WIDTH;// 图片最大宽度
	public static int PIC_MAX_WIDTH = 120;//ProjectConst.PIC_MAX_WIDTH;// 图片最大宽度
	/**
	 * 检查存储卡是否插入
	 * 
	 * @return
	 */
	public boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到图片存储目录
	 * 
	 * @return String 目录地址，末尾已包括了分隔符\
	 */
	public String getPicPath() {
		return PDA_PIC_PATH;
	}

	/**
	 * 得到图像采集的Intent
	 *
	 * @param _context
	 *            context
	 * @return 图像采集的intent
	 */
	public Intent getPicIntent(Context _context) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File path = new File(getPicPath());
		if (!path.exists())
			path.mkdirs();
		cur_file_name = System.currentTimeMillis() + ".jpg";
		cur_file_path = getPicPath() + cur_file_name;
		File file = new File(cur_file_path);
		Uri uri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		return intent;
	}


	/**
	 * 得到图片采集的Intent
	 *
	 * @param _context
	 *            context
	 * @return 图像采集的intent
	 */
	public Intent getPicContentIntent(Context _context) {
		Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		cur_file_name = null;
		cur_file_path = null;
		return intent;
	}

	public void deleteAll(){
		if(getCurrentNewFilePath()==null)
			return;
		File file = new File(getCurrentNewFilePath());
		if(file!=null && file.exists()){
			file.delete();
		}
		cur_file_path = null;
		cur_new_file_path = null;
	}

	/**
	 * 得到当前处理的文件名
	 *
	 * @return 文件名
	 */
	public String getCurrentFileName() {
		return cur_file_name;
	}

	/**
	 * 得到当前处理的文件名
	 *
	 * @return 文件名
	 */
	public String getCurrentFilePath() {
		return cur_file_path;
	}

	/**
	 * 得到当前处理的文件名
	 *
	 * @return 文件名
	 */
	public String getCurrentNewFilePath() {
		return cur_new_file_path;
	}

	public String getRealPathFromURI(Uri uri,Context context) {
		String resStr = "";
		Cursor cursor = null;
	    try {
	    	cursor = context.getContentResolver().query(uri, null, null, null, null);
			cursor.moveToFirst(); 
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
			resStr = cursor.getString(idx);
			if(cursor!=null)
				cursor.close();
			cursor = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resStr;
	}

	/**
	 * 得到返回的图像
	 *
	 * @param data
	 *            Intent对象
	 * @return Bitmap
	 */
	public Object[] getPic(Intent data,Context context) {
		Object[] ret = new Object[2];
		Bitmap bitmap = null;
		String imagePath = getCurrentFilePath();
		if(imagePath==null){//picContent
			if (data != null) {
				Uri uri = data.getData();
				Cursor cursor = context.getContentResolver().query(uri, null, null, null,null);
				if (cursor != null && cursor.moveToFirst()) {
					String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
					cur_file_path = path;
					if(cursor!=null){
						cursor.close();
						cursor = null;
					}
					bitmap = getBitmapPic(path);//经过压缩处理
					ret[0] = bitmap;
					ret[1] = getCurrentNewFilePath();
				}
			}
			return ret;
		}
		// 存储卡可用
		if (isHasSdcard()) {
			bitmap = getBitmapPic(imagePath);//经过压缩处理
			ret[0] = bitmap;
			ret[1] = getCurrentNewFilePath();
		} else {
			// 存储卡不可用直接返回缩略图
			if (data != null && data.getExtras() != null) {
				Bundle extras = data.getExtras();
				bitmap = (Bitmap) extras.get("data");
				ret[0] = bitmap;
				ret[1] = "";
			}
		}
		return ret;
	}

	public Bitmap getPic(byte[] data) {
		if (data == null || data.length == 0)
			return null;
		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = PIC_IN_SAMPLE;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	// 根据图片路径 得到缩略图
	public Bitmap getImageThumbnail(String imagePath,int targetWidth) {
		Bitmap bitmap = null;
		if (isHasSdcard()) {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(imagePath, options);
			options.inJustDecodeBounds = false;
			options.inSampleSize = getTargetSampleSize(options,targetWidth);
			bitmap = BitmapFactory.decodeFile(imagePath, options);
			return bitmap;
		}
		return bitmap;

	}
	
	private int getTargetSampleSize(Options options,int tartgetWidth){
		Double dd = Math.ceil(options.outWidth/tartgetWidth);
		return dd.intValue();
	}

	/**
	 * 按照图片的宽度进行缩放
	* @Title: getBitmapPic
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param path
	* @param @return    设定文件
	* @return Bitmap    返回类型
	* @throws
	 */
	public Bitmap getBitmapPic(Bitmap bitmap) {
		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		DLog.i(TAG, "post width*height-->"+newWidth+"*"+newHeight);
		float scale = (PIC_MAX_WIDTH + 0.1f) / bitmap.getWidth();//按照图片的宽度进行缩放
		if (bitmap.getWidth() > PIC_MAX_WIDTH) {
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					newWidth, newHeight, matrix, false);
			if(bitmap!=null){
				bitmap.recycle();
				bitmap = null;
				System.gc();
			}
			bitmap = resizeBitmap;
		}
		newHeight = Math.round(newHeight*scale);
		if(getCurrentFilePath() == null){
			cur_file_name = UUID.randomUUID().toString() + ".jpg";
			cur_file_path = getPicPath() + cur_file_name;
		}
		String path = getCurrentFilePath().substring(getCurrentFilePath().lastIndexOf("/")+1);//.lastIndexOf("/");
		String newPath = path.substring(0,path.lastIndexOf("."))+"_"+PIC_MAX_WIDTH+"_"+newHeight+path.substring(path.lastIndexOf("."));
		cur_new_file_path = getPicPath()+newPath;
		File file = new File(cur_new_file_path);
		try{
			File parentFile = file.getParentFile();
			if(parentFile == null || !parentFile.exists()){
				parentFile.mkdirs();
			}
		    OutputStream os  =  new FileOutputStream(file);
		    String fileExtend = cur_new_file_path.substring(cur_new_file_path.lastIndexOf(".")+1);
		    if("jpg".equalsIgnoreCase(fileExtend) ||  "jpe".equalsIgnoreCase(fileExtend) ||"jpeg".equalsIgnoreCase(fileExtend) ){
		    	bitmap.compress(CompressFormat.JPEG,PIC_JPG_COMPRESS_RATE,os);
			}else if("png".equalsIgnoreCase(fileExtend)){
				bitmap.compress(CompressFormat.PNG,PIC_JPG_COMPRESS_RATE,os);
			}
		}catch(FileNotFoundException e){
		   e.printStackTrace();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			DLog.i(TAG, "bitmap size-->"+bitmap.getByteCount()/(1024f));
		}
		return bitmap;
	}

	/**
	 * 按照图片的宽度进行缩放
	* @Title: getBitmapPic
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param path
	* @param @return    设定文件
	* @return Bitmap    返回类型
	* @throws
	 */
	public Bitmap getBitmapPic(String path) {
		if (path == null || path.length() == 0)
			return null;
		Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		DLog.i(TAG, "pre width*height-->"+options.outWidth+"*"+options.outHeight);
		options.inJustDecodeBounds = false;
		int sampleSize = getTargetSampleSize(options, PIC_MAX_WIDTH);
		if( sampleSize > 1) {
			options.inSampleSize = sampleSize;
			// 获取这个图片的宽和高
			bitmap = BitmapFactory.decodeFile(path, options);
		}else{
			options.inSampleSize = 1;
			bitmap = BitmapFactory.decodeFile(path, options);
		}
		if(bitmap!=null){
			return getBitmapPic(bitmap);
		}else{
			return null;
		}
	}

	/**
	 * 
	* @Title: computeSampleSize 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param options 原本bitmap的options
	* @param @param minSideLength 生成缩略图中较小的值
	* @param @param maxNumOfPixels 希望生成缩略图的总像素
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int computeSampleSize(Options options,int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	/**
	 * 
	* @Title: computeInitialSampleSize 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param options 原本bitmap的options
	* @param @param minSideLength 生成缩略图中较小的值
	* @param @param maxNumOfPixels 希望生成缩略图的总像素
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	private int computeInitialSampleSize(Options options,int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 :(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength),Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) &&(minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	/**
	 * 拍照获取图片
	* @Title: getPic
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param path
	* @param @return    设定文件
	* @return byte[]    返回类型
	* @throws
	 */
	public byte[] getPic(String path) {
		if (path == null || path.length() == 0)
			return null;
		Options options = new Options();
		options.inJustDecodeBounds = false;
		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		bitmap = getBitmapPic(bitmap);
		// 获取这个图片的宽和高
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, os);
		byte[] data = os.toByteArray();
		bitmap.recycle();
		bitmap = null;
		return data;
	}

	/** 
	 * 读取图片属性：旋转的角度 
	 * @param path 图片绝对路径 
	 * @return degree旋转的角度 
	 */  
	/*public int readPictureDegree(String path) {
	    int degree = 0;  
	    try {  
	        ExifInterface exifInterface = new ExifInterface(path);  
	        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);  
	        switch (orientation) {  
	        case ExifInterface.ORIENTATION_ROTATE_90:  
	            degree = 90;  
	            break;  
	        case ExifInterface.ORIENTATION_ROTATE_180:  
	            degree = 180;  
	            break;  
	        case ExifInterface.ORIENTATION_ROTATE_270:  
	            degree = 270;  
	            break;  
	        }  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	        return degree;  
	    }  
	    return degree;  
	}  */

	/** 
	 * 旋转图片，使图片保持正确的方向。 
	 * @param bitmap 原始图片 
	 * @param degrees 原始图片的角度 
	 * @return Bitmap 旋转后的图片 
	 */  
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
	    if (degrees == 0 || null == bitmap) {  
	        return bitmap;  
	    }  
	    Matrix matrix = new Matrix();  
	    matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);  
	    Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	    if (null != bitmap) {  
	        bitmap.recycle();  
	        bitmap = null;
	    }  
	    return bmp;  
	} 
	
	/** 
	 * 旋转图片，使图片保持正确的方向。 
	 * @param path 原始图片
	 * @param degrees 原始图片的角度
	 * @return Bitmap 旋转后的图片 
	 */  
	public Bitmap rotateBitmap(String path, int degrees) {
		if(path==null)
			return null;
		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 1;
		Bitmap bitmap = BitmapFactory.decodeFile(path,options);
	    if (degrees == 0 || null == bitmap) {  
	        return bitmap;  
	    }  
	    Matrix matrix = new Matrix();  
	    matrix.reset();
	    matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);  
	    Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);  
	    if (null != bitmap) {  
	        bitmap.recycle();  
	        bitmap = null;
	    } 
	    File file = new File(path);  
		try{  
			if(file.exists())
				file.delete();
		    OutputStream os  =  new FileOutputStream(file);  
		    bmp.compress(CompressFormat.JPEG,PIC_JPG_COMPRESS_RATE,os);  
		}catch(FileNotFoundException e){  
		   e.printStackTrace();  
		}  
	    return bmp;  
	}

	//bitmap to file
	public static void saveToFile(String filename,Bitmap bmp) {
	      try {
	          FileOutputStream out = new FileOutputStream(filename);
	          bmp.compress(CompressFormat.JPEG,PIC_JPG_COMPRESS_RATE, out);
	          out.flush();
	          out.close();
	      } catch(Exception e) {
	    	  
	      }
	      finally{
	    	  if(bmp!=null){
		    	  bmp.recycle();
		    	  bmp = null;
	    	  }
	      }
	  }
	
	public static int getPIC_IN_SAMPLE() {
		return PIC_IN_SAMPLE;
	}

	public static void setPIC_IN_SAMPLE(int pIC_IN_SAMPLE) {
		PIC_IN_SAMPLE = pIC_IN_SAMPLE;
	}

	public static int getPIC_JPG_COMPRESS_RATE() {
		return PIC_JPG_COMPRESS_RATE;
	}

	public static void setPIC_JPG_COMPRESS_RATE(int pIC_JPG_COMPRESS_RATE) {
		PIC_JPG_COMPRESS_RATE = pIC_JPG_COMPRESS_RATE;
	}

	public static int getPIC_MAX_WIDTH() {
		return PIC_MAX_WIDTH;
	}

	public static void setPIC_MAX_WIDTH(int pIC_MAX_WIDTH) {
		PIC_MAX_WIDTH = pIC_MAX_WIDTH;
	}

}
