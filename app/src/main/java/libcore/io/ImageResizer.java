package libcore.io;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by JackHou on 2017/3/22.
 * 主要用于图片的压缩功能
 */

public class ImageResizer
{
    private static final String TAG = "ImageResizer";

    public ImageResizer()
    {
    }

    /**
     * 加载指定大小的图片
     * @param resources
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight)
    {
        if(reqHeight == 0 || reqWidth == 0)
        {
            Log.e(TAG, "decodeScaledBitmap scaleWidth or scaleHeight can not be 0");
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();

        /*inJustDecodeBounds为true时，只解析图片的原始尺寸不真的加载图片*/
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        /*计算采样率*/
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * 计算采样率
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        if(reqHeight == 0 || reqWidth ==0)
        {
            return 1;
        }
        /*获取图片原始尺寸*/
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.i("zxxx", "height: " + height + " width: " + width);
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth)
        {
            final int harfHeifht = height / 2;
            final int harfWidth = width / 2;
            while ((harfWidth / inSampleSize) >= reqWidth && (harfHeifht / inSampleSize) >= reqHeight)
            {
                inSampleSize *= 2;
            }
        }
        Log.i("zxxx", "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    /**
     * 使用FileDescriptor加载指定大小的图片
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fd, int reqWidth, int reqHeight)
    {
        if(reqHeight == 0 || reqWidth == 0)
        {

            Log.e(TAG, "decodeScaledBitmap scaleWidth or scaleHeight can not be 0");
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }


}
