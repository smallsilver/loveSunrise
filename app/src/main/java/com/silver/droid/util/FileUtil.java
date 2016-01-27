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

package com.silver.droid.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>
 * @ClassName: FileUtil
 * </p>
 *
 * <p>
 * @Description: FileUtil
 * </p>
 *
 * <p>
 * @author wangde dongen_wang@163.com
 * </p>
 *
 * <p>
 * @version 1.00.00
 * </p>
 *
 * @date 2014年12月1日 / 下午4:59:36
 */
public class FileUtil {

	private static String[] filter = new String[] { ".cr" };

	public static List<File> getAllFile(String rootPath) {
		return getAllFile(rootPath, filter);
	}

	/***
	 * 删除指定目录下的所有文件
	 * @param filePath 目录名称
	 * @param flag 是否删除目录
	 */
	public static void deleteFolder(String filePath,boolean flag){
		File file = new File(filePath);
		if(!file.exists()){
			return;
		}

		File[] listFile = file.listFiles();
		if(listFile.length == 0){
			//删除空文件夹
			new File(filePath).delete();
			return ;
		}
		for(int i=0 ; i< listFile.length ;i++){
			File currentFile = listFile[i];
			//如果是文件直接删除
			if(currentFile.isFile()){
				currentFile.delete();
			}
			//是文件夹迭代删除
			if(currentFile.isDirectory()){
				String filepath = listFile[i].getPath();
				deleteFolder(filepath,true);
			}
		}
		if(flag){
			file.delete();
		}
	}


	public static List<File> getAllFile(String rootPath, String[] filter) {
		File file = new File(rootPath);
		List<File> result = new ArrayList<File>();
		File[] files = file.listFiles(new CrFileFilter(filter));
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				getAllFile(files[i].getPath(), filter);
			} else {
				result.add(files[i]);
			}
		}
		return result;
	}
	public static byte[] File2byte(File file) {
		byte[] buffer = null;
		FileInputStream fis =null;
		ByteArrayOutputStream bos =null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
				bos.flush();
			}
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis!=null)
					fis.close();
				if(bos!=null)
					bos.close();
				fis = null;
				bos = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buffer;
	}

	public static byte[] File2byte(String filePath) {
		try {
			File file = new File(filePath);
			return File2byte(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[]{};
	}
	
	// 读取文件内容
    public static String readFileByChars(String fileName) {
    	StringBuffer sbBuffer = new StringBuffer();
        Reader reader = null;
        try {
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                if ((charread == tempchars.length)){
                    sbBuffer.append(tempchars);
                }  else {
                    for (int i = 0; i < charread; i++) {
                        sbBuffer.append(tempchars[i]);
                    }
                }
            }
        } catch (Exception e1) {
//            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e1) {
                }
            }
        }
        return sbBuffer.toString();
    }
	
	/**
	 * 
	* @Title: deleteFile 
	* @Description: 
	* @param @param filePath    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public static void deleteFile(String filePath){
		DLog.i(FileUtil.class.getName(), FileUtil.class.getName()+"-->"+filePath);
		if(filePath!=null){
			File file = new File(filePath);
			if(file!=null && file.exists())
				file.delete();
		}
	}

	/**
	 *
	 * @Title: deleteFile
	 * @Description:
	 * @param @param filePath    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static void deleteFile(List<String> filePath){
		for(String path :filePath){
			deleteFile(path);
		}
	}
	/**
	 * 
	* @Title: isFileExist 
	* @Description: 文件是否存在
	* @param @param filePath
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean isFileExist(String filePath){
		if(filePath!=null){
			File file = new File(filePath);
			if(file!=null && file.exists())
				return true;
		}
		return false;
	}
	
	
	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
					bos = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void byte2File(byte[] buf, File filetmp) {
		byte2File(buf, filetmp, false);
	}
	
	public static void byte2File(byte[] buf, File filetmp,boolean append) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = filetmp.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = filetmp;
			fos = new FileOutputStream(file,append);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			DLog.e(FileUtil.class.getName(), e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
					bos = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static long getFileSizes(File f){//取得文件大小
	       long s=0;
	       try {
			if (f.exists()) {
				s = f.length();
			   } else {
			       System.out.println("文件不存在");
			   }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       return s;
	  }
	
	// 递归
    public static long getFileSize(File f)//取得文件夹大小
    {
       long size = 0;
       try {
		   File flist[] = f.listFiles();
		   for (int i = 0; i < flist.length; i++)
		   {
		       if (flist[i].isDirectory())
		       {
		           size = size + getFileSize(flist[i]);
		       } else
		       {
		           size = size + flist[i].length();
		       }
		   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return size;
    }
    public static String FormetFileSize(long fileS) {//转换文件大小
       DecimalFormat df = new DecimalFormat("0.00");
       String fileSizeString = "";
       if (fileS < 1024) {
           fileSizeString = df.format((double) fileS) + "B";
       } else if (fileS < 1048576) {
           fileSizeString = df.format((double) fileS / 1024) + "K";
       } else if (fileS < 1073741824) {
           fileSizeString = df.format((double) fileS / 1048576) + "M";
       } else {
           fileSizeString = df.format((double) fileS / 1073741824) +"G";
       }
       return fileSizeString;
    }
   
    public static long getlist(File f){//递归求取目录文件个数
       long size = 0;
       File flist[] = f.listFiles();
       size=flist.length;
       for (int i = 0; i < flist.length; i++) {
           if (flist[i].isDirectory()) {
               size = size + getlist(flist[i]);
               size--;
           }
       }
       return size;
    }
    
    
    /**
     * 使用文件通道的方式复制文件
     * 
     * @param s
     *            源文件
     * @param t
     *            复制到的新文件
     */
     public static void fileChannelCopy(File s, File t) {

         FileInputStream fi = null;

         FileOutputStream fo = null;

         FileChannel in = null;

         FileChannel out = null;

         try {

             fi = new FileInputStream(s);

             fo = new FileOutputStream(t);

             in = fi.getChannel();//得到对应的文件通道

             out = fo.getChannel();//得到对应的文件通道

             in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

         } catch (IOException e) {

             e.printStackTrace();

         } finally {

             try {
            	 
                 fi.close();
                 in.close();
                 fo.close();
                 out.close();

             } catch (Exception e) {

                 e.printStackTrace();

             }
             
         }
         
     }
	/**
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
			File a=new File(oldPath);
			String[] file=a.list();
			File temp=null;
			for (int i = 0; i < file.length; i++) {
				if(oldPath.endsWith(File.separator)){
					temp=new File(oldPath+file[i]);
				}
				else{
					temp=new File(oldPath+File.separator+file[i]);
				}

				if(temp.isFile()){
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" +
							(temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ( (len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if(temp.isDirectory()){//如果是子文件夹
					copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
				}
			}
		}
		catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}
}

class CrFileFilter implements FileFilter {

	String[] extend = new String[] {};

	public CrFileFilter(String[] ext) {
		extend = ext;
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory())
			return true;
		else {
			String name = file.getName();
			for (int i = 0, len = extend.length; i < len; i++) {
				if (name.endsWith(extend[i]))
					return true;
			}
			return false;
		}

	}

}
