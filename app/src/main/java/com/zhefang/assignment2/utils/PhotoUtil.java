package com.zhefang.assignment2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class PhotoUtil {
	public static final int NONE = 0;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int PHOTOGRAPH = 1;
	public static final int PHOTOZOOM = 2;
	public static final int PHOTORESOULT = 3;
	public static final int PICTURE_HEIGHT = 500;
	public static final int PICTURE_WIDTH = 500;
	public static String imageName;

	public static void selectPictureFromAlbum(Activity activity){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				IMAGE_UNSPECIFIED);

		activity.startActivityForResult(intent, PHOTOZOOM);
	}
	

	

	



	public static void startPhotoZoom(Activity activity,Uri uri,int height,int width,Uri destUri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}

		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);



		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);

		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, destUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, PHOTORESOULT);
	}
	

	

	

 	public static String getPath(Context context){
		File CropPhoto=new File(context.getExternalCacheDir(),System.currentTimeMillis()+"crop.jpg");
		try{
			if(CropPhoto.exists()){
				CropPhoto.delete();
			}
			CropPhoto.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		return CropPhoto.getAbsolutePath();
 	}
 	

 	public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

}