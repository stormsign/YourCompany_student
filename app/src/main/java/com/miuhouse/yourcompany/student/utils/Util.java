package com.miuhouse.yourcompany.student.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.miuhouse.yourcompany.student.application.App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by khb on 2016/5/13.
 */
public class Util {

    private final static ThreadLocal<SimpleDateFormat> dateFormater =
        new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 =
        new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd");
            }
        };

    private final static ThreadLocal<SimpleDateFormat> dateFormat3 =
        new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm");
            }
        };

    //  判断安卓系统是否高于5.0
    public static boolean isAndroidVersionAboveLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    //    判断是否是华为EMUI系统
    public static boolean isHuaWeiEmui() {
        return !TextUtils.isEmpty(getSystemProperty("ro.build.hw_emui_api_level"));
    }

    //    判断是否是小米MIUI系统
    public static boolean isXiaoMiMIUI() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    public static String getSystemProperty(String propertyName) {
        String property;
        BufferedReader reader = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propertyName);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            property = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return property;
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm =
            (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                    .toUpperCase(Locale.US);
                if (appendString.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对字符串进行MD5加密 输出：MD5加密后的大写16进制密文
     */
    public static String md5String(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 获取 摘要器
        byte[] result = digest.digest(text.getBytes()); // 通过 摘要器对指定的数据进行加密，并返回到byte[]。
        StringBuffer sb = new StringBuffer(); // 保存十六进制的密文

        // 将加密后的数据 由byte(二进制)转化成Integer(十六进制)。
        for (byte b : result) {
            int code = b & 0xff;
            String s = Integer.toHexString(code);
            if (s.length() == 1) {
                sb.append("0");
                sb.append(s);
            } else {
                sb.append(s);
            }
        }
        return sb.toString().toUpperCase(); // 返回数据加密后的十六进制密文
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        //        应该是从配置文件中获取状态栏高度
        int resourceId =
            context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Bitmap createImageThumbnail(Context context, String largeImagePath,
        int square_size) throws IOException {
        // int targetW = mImageView.getWidth();
        // int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(largeImagePath, bmOptions);
        // L.i("TAG", "bitmap:width=" + bitmap.getWidth() + "height=" + bitmap.getHeight());
        // Determine how much to scale down the image

        // L.i("TAG", "calculateInSampleSize=" + calculateInSampleSize(bmOptions, 300, 600));
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions, 600, 600);
        bmOptions.inJustDecodeBounds = false;
        // bmOptions.inPurgeable = true;
        // BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inJustDecodeBounds = false;
        // options.inSampleSize = 5;
        // 原始图片bitmap
        L.i("TAG", "largeImagePath=" + largeImagePath);
        Bitmap cur_bitmap = getBitmapByPath(largeImagePath, bmOptions);
        // Bitmap cur_bitmap = revitionImageSize(largeImagePath);
        L.i("TAG", "cur_bitmap=" + cur_bitmap.getByteCount());
        if (cur_bitmap == null) {
            return null;
        }
        L.i("TAG", "cur_bitmap+width=" + cur_bitmap.getWidth());
        L.i("TAG", "cur_bitmap+height=" + cur_bitmap.getHeight());
        // 原始图片的高宽
        int[] cur_img_size = new int[] {cur_bitmap.getWidth(), cur_bitmap.getHeight()};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);
        // 生成缩放后的bitmap
        Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0], new_img_size[1]);
        // thb_bitmap.recycle();
        // 生成缩放后的图片文件
        // saveImageToSD(null, thumbfilePath, thb_bitmap, quality);
        return cur_bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
        int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 获取bitmap
     */
    public static Bitmap getBitmapByPath(String filePath) {
        L.i("TAG", "filePath=" + filePath);
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);

            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            L.i("TAG", "OUTOFmEMORYError=" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 计算缩放图片的宽高
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size) {
            return img_size;
        }
        double ratio = square_size / (double) Math.max(img_size[0], img_size[1]);
        return new int[] {(int) (img_size[0] * ratio), (int) (img_size[1] * ratio)};
    }

    /**
     * 放大缩小图片
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return newbmp;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是不是手机号
     *
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasInternet() {
        boolean flag;
        if (((ConnectivityManager) App.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取资源id
     *
     * @param type 资源类型
     */
    public static int getResourceId(Context context, String name, String type) {
        Resources themeResources = null;
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            themeResources = pm.getResourcesForApplication(packageName);
            return themeResources.getIdentifier(name, type, packageName);
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return 0;
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point out = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(out);
        } else {
            int width = display.getWidth();
            int height = display.getHeight();
            out.set(width, height);
        }
        return out;
    }

    public static String streamToStringEn(HttpURLConnection urlConnection) {
        // private String readResult(HttpURLConnection urlConnection) throws WeiboException {
        InputStream is = null;
        BufferedReader buffer = null;
        // GlobalContext globalContext = GlobalContext.getInstance();
        // String errorStr = globalContext.getString(R.string.timeout);
        // globalContext = null;
        try {
            is = urlConnection.getInputStream();

            String content_encode = urlConnection.getContentEncoding();

            if (!TextUtils.isEmpty(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            // AppLogger.d("result=" + strBuilder.toString());
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            // throw new WeiboException(errorStr, e);
        } finally {
            closeSilently(is);
            closeSilently(buffer);
            urlConnection.disconnect();
        }
        return null;
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getVersionName() {
        String name = "";

        try {
            name = App.getInstance()
                .getPackageManager()
                .getPackageInfo(App.getInstance().getPackageName(),
                    0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            name = "";
        }
        return name;
    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view, Context context) {
        ((InputMethodManager) context.getSystemService(
            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
            view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftKeyboard(EditText et, Context context) {
        et.requestFocus();
        ((InputMethodManager) context.getSystemService(
            Context.INPUT_METHOD_SERVICE)).showSoftInput(et,
            InputMethodManager.SHOW_FORCED);
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                    .toUpperCase(Locale.US);
                if (appendString.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String friendly_time(long date) {
        Date time = null;
        time = new Date(date);
        //if (Util.isInEasternEightZones()) {
        //time = toDate(date);
        //    L.i("TAG","friendly_time="+time);
        //} else {
        //    time = transformTime(toDate(date), TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        //}
        if (time == null) {
            return "empty";
        }

        Calendar currentCalendar = Calendar.getInstance();
        //        currentCalendar.get(Calendar.MINUTE)
        long contentDay = time.getTime() / 86400000;
        long currentDay = currentCalendar.getTimeInMillis() / 86400000;

        int days = (int) (currentDay - contentDay);
        //表示同一天
        String finaltime = "";
        if (days == 0) {
            int hour = (int) ((currentCalendar.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                int millis = (int) ((currentCalendar.getTimeInMillis() - time.getTime()) / 60000);
                if (millis < 2) {
                    finaltime = "刚刚";
                } else {
                    finaltime =
                        (int) ((currentCalendar.getTimeInMillis() - time.getTime()) / 60000)
                            + "分钟前";
                }
            } else {
                finaltime = hour + "小时前";
            }
        } else if (days == 1) {
            finaltime = "昨天";
        } else if (days == 2) {
            finaltime = "前天 ";
        } else if (days > 2 && days < 31) {
            finaltime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            finaltime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            finaltime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            finaltime = "3个月前";
        } else {
            finaltime = dateFormater2.get().format(time);
        }
        return finaltime;
    }
    public static boolean copyFile(File from, File to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                FileChannel sourceChannel = null;
                FileChannel destChannel = null;

                sourceChannel = new FileInputStream(from).getChannel();
                destChannel = new FileOutputStream(to).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

                sourceChannel.close();
                destChannel.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
