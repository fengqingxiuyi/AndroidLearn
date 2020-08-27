package com.example.social.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片操作工具类
 */
public class ImageUtil {

    public static String getLocalImagePath(File parentDir, String image) {
        if (TextUtils.isEmpty(image)) {
            return null;
        }
        if (image.startsWith("http")) {
            String fileName = image.substring(image.lastIndexOf("/") + 1);
            if (TextUtils.isEmpty(fileName) || parentDir == null) {
                return null;
            }
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    return null;
                }
            }
            final File file = new File(parentDir, fileName);
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        return null;
                    }
                } catch (IOException e) {
                    return null;
                }
            }
            boolean success = downloadImage(file, image);
            if (success) {
                return file.getAbsolutePath();
            } else {
                return null;
            }
        } else {
            return image;
        }
    }

    /**
     * 下载图片
     *
     * @param file     图片存放位置
     * @param imageUrl 图片地址
     * @return true 下载成功 false 下载失败
     */
    public static boolean downloadImage(File file, String imageUrl) {
        if (file == null || TextUtils.isEmpty(imageUrl)) {
            return false;
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(imageUrl);
            // 打开连接
            conn = (HttpURLConnection) url.openConnection();
            // 输入流
            is = conn.getInputStream();
            // 1K的数据缓冲
            byte[] b = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            if (null != conn) {
                conn.disconnect();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 将图片地址转换为Bitmap
     *
     * @param imageUrl 图片地址
     * @return Bitmap
     */
    public static Bitmap getImageBitmap(String imageUrl, long maxSize) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        if (!imageUrl.startsWith("http")) {
            return compressBitmapGetBitmap(imageUrl, null, imageUrl, maxSize);
        }
        try {
            return compressBitmapGetBitmap(null, new URL(imageUrl), imageUrl, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片地址转换为byte[]
     *
     * @param imageUrl 图片地址
     * @return byte[]
     */
    public static byte[] getImageByte(String imageUrl, long maxSize) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        if (!imageUrl.startsWith("http")) {
            return compressBitmapGetByte(imageUrl, null, imageUrl, maxSize);
        }
        try {
            return compressBitmapGetByte(null, new URL(imageUrl), imageUrl, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片压缩
     *
     * @param url 图片地址
     * @return Bitmap
     */
    public static Bitmap compressBitmapGetBitmap(String localFilePath, URL url, String imageUrl, long maxSize) {
        return compressBitmapQualityGetBitmap(compressBitmapSize(localFilePath, url), maxSize, imageUrl);
    }

    /**
     * 图片压缩
     *
     * @param url 图片地址
     * @return byte[]
     */
    public static byte[] compressBitmapGetByte(String localFilePath, URL url, String imageUrl, long maxSize) {
        return compressBitmapQualityGetByte(compressBitmapSize(localFilePath, url), maxSize, imageUrl);
    }

    /**
     * 尺寸压缩 限制图片的最大边长为200px
     *
     * @param url 图片地址
     * @return Bitmap
     */
    public static Bitmap compressBitmapSize(String localFilePath, URL url) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        options.inJustDecodeBounds = true;
        if (!TextUtils.isEmpty(localFilePath)) {
            BitmapFactory.decodeFile(localFilePath);
        } else {
            try {
                //打开连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                //关闭连接
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        // 获取图片宽高
        int height = options.outHeight;
        int width = options.outWidth;
        // 默认像素压缩比例，压缩为原图的1/1
        int inSampleSize = 1;
        // 获取图片的最大边长
        int maxLen = Math.max(height, width);
        if (maxLen > 200) {
            //向上取整
            inSampleSize = (int) Math.ceil(maxLen / 200f);
        }
        // 计算好压缩比例后，这次可以去加载原图了
        options.inJustDecodeBounds = false;
        // 设置为刚才计算的压缩比例
        options.inSampleSize = inSampleSize;
        //
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (!TextUtils.isEmpty(localFilePath)) {
            return BitmapFactory.decodeFile(localFilePath);
        } else {
            try {
                //重新打开连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                //重新关闭连接
                conn.disconnect();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Bitmap compressBitmapQualityGetBitmap(Bitmap bitmap, long maxSize, String imageUrl) {
        return BitmapFactory.decodeStream(new ByteArrayInputStream(compressBitmapQuality(bitmap, maxSize, imageUrl).toByteArray()));
    }

    public static byte[] compressBitmapQualityGetByte(Bitmap bitmap, long maxSize, String imageUrl) {
        return compressBitmapQuality(bitmap, maxSize, imageUrl).toByteArray();
    }

    /**
     * 质量压缩 压缩Bitmap到指定的大小范围内
     *
     * @param maxSize Bitmap被允许占有的最大大小，单位为KB
     * @return Bitmap
     */
    private static ByteArrayOutputStream compressBitmapQuality(Bitmap bitmap, long maxSize, String imageUrl) {
        // Bitmap默认质量为100，表示从未被压缩过
        int quality = 100;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format = getBmpFormat(imageUrl);
        bitmap.compress(format, quality, baos);
        while (baos.toByteArray().length * 1024 > maxSize && quality > 20) {
            baos.reset();
            bitmap.compress(format, quality, baos);
            quality -= 10;
        }
        bitmap.recycle();

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos;
    }

    public static Bitmap.CompressFormat getBmpFormat(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return Bitmap.CompressFormat.JPEG;
        }
        String pathLower = filePath.toLowerCase();
        Bitmap.CompressFormat format;
        if (!pathLower.endsWith("png") && !pathLower.endsWith("gif")) {
            if (!pathLower.endsWith("jpg") && !pathLower.endsWith("jpeg") && !pathLower.endsWith("bmp") && !pathLower.endsWith("tif")) {
                String mime = getMime(filePath);
                if (!mime.endsWith("png") && !mime.endsWith("gif")) {
                    format = Bitmap.CompressFormat.JPEG;
                } else {
                    format = Bitmap.CompressFormat.PNG;
                }
            } else {
                format = Bitmap.CompressFormat.JPEG;
            }
        } else {
            format = Bitmap.CompressFormat.PNG;
        }

        return format;
    }

    public static String getMime(String file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[8];
            fis.read(bytes);
            fis.close();
            return getMime(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getMime(byte[] bytes) {
        byte[] jpeg = new byte[]{-1, -40, -1, -32};
        byte[] jpeg2 = new byte[]{-1, -40, -1, -31};
        if (!bytesStartWith(bytes, jpeg) && !bytesStartWith(bytes, jpeg2)) {
            byte[] png = new byte[]{-119, 80, 78, 71};
            if (bytesStartWith(bytes, png)) {
                return "png";
            } else {
                byte[] gif = "GIF".getBytes();
                if (bytesStartWith(bytes, gif)) {
                    return "gif";
                } else {
                    byte[] bmp = "BM".getBytes();
                    if (bytesStartWith(bytes, bmp)) {
                        return "bmp";
                    } else {
                        byte[] tiff = new byte[]{73, 73, 42};
                        byte[] tiff2 = new byte[]{77, 77, 42};
                        return !bytesStartWith(bytes, tiff) && !bytesStartWith(bytes, tiff2) ? "" : "tif";
                    }
                }
            }
        } else {
            return "jpg";
        }
    }

    private static boolean bytesStartWith(byte[] target, byte[] prefix) {
        if (target == prefix) {
            return true;
        } else if (target != null && prefix != null) {
            if (target.length < prefix.length) {
                return false;
            } else {
                for (int i = 0; i < prefix.length; ++i) {
                    if (target[i] != prefix[i]) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

}
