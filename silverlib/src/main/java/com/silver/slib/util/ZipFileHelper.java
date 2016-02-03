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

import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipFileHelper {

    public static final int ZIP_HANDLET_START = 1;
    public static final int ZIP_HANDLET_FINISH = 2;
    public static final int ZIP_HANDLET_ERROR = 3;

    private Handler handler;

    public ZipFileHelper() {

    }

    public void setZipFileHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * unzip a file
     *
     * @param srcZipFile
     * @param desPath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void unzipFile(String srcZipFile, String desPath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcZipFile);
            unzipFile(fis,desPath);
        }catch (Exception e){

        }
    }
    /**
     * unzip a file
     * 
     * @param srcZipFile
     * @param desPath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void unzipFile(InputStream fis, String desPath) {

        if (this.handler != null) {
            Message message = Message.obtain();
            message.what = ZIP_HANDLET_START;
            this.handler.sendMessage(message);
        }

//        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry ze = null;

        // open file
        try {
//            fis = new FileInputStream(srcZipFile);
            zis = new ZipInputStream(fis);
        } catch (Exception e) {
            if (this.handler != null) {
                Message message = Message.obtain();
                message.what = ZIP_HANDLET_ERROR;
                this.handler.sendMessage(message);
            }

            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }

                if (zis != null) {
                    zis.close();
                    zis = null;
                }
            } catch (Exception e1) {

            }
        }

        // real buffer
        byte[] osBuffer = new byte[1024];

        File zfile = null,fpath = null;
        while (true) {
            try {
                ze = zis.getNextEntry();
                if (ze == null) {
                	fis.close();
                	fis = null;
                	zis.close();
                	zis = null;
                    break;
                }
            } catch (Exception e) {
                if (this.handler != null) {
                    Message message = Message.obtain();
                    message.what = ZIP_HANDLET_ERROR;
                    this.handler.sendMessage(message);
                }

                try {
                    if (zis != null) {
                        zis.close();
                        zis = null;
                    }
                } catch (IOException e1) {

                }
            }


            zfile = new File(desPath + "/" + ze.getName());
            fpath = new File(zfile.getParentFile().getPath());
            if (ze.isDirectory()) {
                if (!zfile.exists()) {
                    zfile.mkdirs();
                }

                try {
                    zis.closeEntry();
                } catch (IOException e) {
                    if (this.handler != null) {
                        Message message = Message.obtain();
                        message.what = ZIP_HANDLET_ERROR;
                        this.handler.sendMessage(message);
                    }
                }
            } else {
                if (!fpath.exists()) {
                    fpath.mkdirs();
                }
                FileOutputStream fos = null;
                try {
                    int i;
                    fos = new FileOutputStream(zfile);
                    while ((i = zis.read(osBuffer)) != -1) {
                        fos.write(osBuffer, 0, i);
                        fos.flush();
                    }
                } catch (Exception e) {
                    if (this.handler != null) {
                        Message message = Message.obtain();
                        message.what = ZIP_HANDLET_ERROR;
                        this.handler.sendMessage(message);
                    }
                } finally {
                    try {
                    	if(zis!=null){
                    		zis.closeEntry();
                    	}
                        if (fos != null) {
                            fos.close();
                            fos = null;
                        }
                    } catch (IOException e) {

                    }
                }
            }
        }

        if (this.handler != null) {
            Message message = Message.obtain();
            message.what = ZIP_HANDLET_FINISH;
            this.handler.sendMessage(message);
        }
    }
}
