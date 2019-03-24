package com.cqut.icode.asss_android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者：hwl
 * 时间：2017/7/19:20:23
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class ImageUtil {

    public static String saveBitmap(String path, String imgName, Bitmap bitmap) {
        String savePath = null;
        if (path == null) { //if path is null
            File fileSDCardDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            String imgPath = fileSDCardDir.getAbsolutePath() + "/asss/waterCamera/";
            File fileDir = new File(imgPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String photoName = imgName + ".JPG";
            imgPath = imgPath + photoName;
            File fileIphoto = new File(imgPath);
            if (!fileIphoto.exists()) {
                try {
                    fileIphoto.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            savePath = fileIphoto.getPath();
            saveBitmap(bitmap, fileIphoto);
            return savePath;
        } else { //if path isn't null, override the photo
            File oldFile = new File(path);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            File newFile = new File(path);
            if (!newFile.exists()) {
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            saveBitmap(bitmap, newFile);
            savePath = newFile.getPath();
            return savePath;
        }
    }

    public static void saveBitmap(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.e("imagePath", file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("save photo error", e.toString());
            e.printStackTrace();
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        return calculateInSampleSize(options.outWidth,options.outHeight,
                reqWidth,reqHeight);
    }

    public static int calculateInSampleSize(int outWidth, int outHeight,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        int inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) outHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) outWidth / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            Log.e("inSampleSize", inSampleSize + "");
        }
        return inSampleSize;
    }

    public static void deleteImageFromSDCard(String picPath) {
        File fileDir = new File(picPath);
        fileDir.deleteOnExit();
    }


    public static boolean imgIsExist(String imgPath){
        File fileIphoto = new File(imgPath);
        if (!fileIphoto.exists())
            return true;
        else
            return false;
    }


    public static Bitmap getPressedBitmap(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);//getBitmapSampleSize(options, width, height)
        options.inJustDecodeBounds = false;
        Bitmap returnBitmap = BitmapFactory.decodeFile(path, options);
        return returnBitmap;
    }

    //获取合适宽度的图片
    public static Bitmap getFitBitMap(String path, int width, int height) {
        Bitmap screenShotBitmap = BitmapFactory.decodeFile(path);
        int fitWidth = screenShotBitmap.getWidth() < screenShotBitmap.getHeight() ? screenShotBitmap.getWidth() : screenShotBitmap.getHeight();
        Bitmap extractBitmap =  getExtractBitmap(screenShotBitmap,screenShotBitmap.getHeight(),
                screenShotBitmap.getWidth(),fitWidth);
        return Bitmap.createScaledBitmap(extractBitmap,width,height,false);
    }

    public static Bitmap getExtractBitmap(Bitmap source,int mapHeight, int mapWidth,int fitWidth) {
        int perx = (mapWidth - fitWidth) / 2;
        int pery = (mapHeight - fitWidth) / 2;
        return Bitmap.createBitmap(source,perx,pery,fitWidth,fitWidth,null,false);
    }


}
