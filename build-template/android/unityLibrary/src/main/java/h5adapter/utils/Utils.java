/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package h5adapter.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.SystemClock;
import android.os.TransactionTooLargeException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.blowfire.app.framework.BFApplication;
import com.blowfire.app.framework.BFSessionMgr;
import com.blowfire.common.config.BFConfig;
import com.blowfire.common.connection.BFHttpConnection;
import com.blowfire.common.utils.BFLog;
import com.sportbrain.jewelpuzzle.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * Various utilities shared amongst the Launcher's classes.
 * <p>
 * for module-specific utilities.
 */
public final class Utils {

    private static final String TAG = "Launcher.Utils";

    public static final String PREF_KEY_ICON_DECORATION_DONE = "icon_decoration_done";

    /**
     * Threshold for float decimal equality test.
     */
    public static final float EPSILON = 0.0005f;

    private static final int STREAM_OP_BUFFER_SIZE = 4096;

    public static final long DIALOG_IGNORE_BACK_KEY_DURATION = 1000;

    public static final int RETRY_FAIL_ACTION_ABORT = 0;
    public static final int RETRY_FAIL_ACTION_IGNORE = 1;

    /**
     * Defines the duration in milliseconds between the first click event and
     * the second click event for an interaction to be considered a double-click.
     */
    private static final int DOUBLE_CLICK_TIMEOUT = 500;

    private static final String PREF_KEY_UNINSTALLED_APPS = "uninstalled_apps";

    private static List<String> sUninstalledAppsCache;

    private static final Rect sOldBounds = new Rect();
    private static final DrawFilter sIconDrawFilter = new PaintFlagsDrawFilter(Paint.DITHER_FLAG, Paint.FILTER_BITMAP_FLAG);

    private static final Pattern sTrimPattern = Pattern.compile("^[\\s|\\p{javaSpaceChar}]*(.*)[\\s|\\p{javaSpaceChar}]*$");

    private static int sColors[] = {0xffff0000, 0xff00ff00, 0xff0000ff};
    private static int sColorIndex = 0;

    private static final int[] sLoc0 = new int[2];
    private static final int[] sLoc1 = new int[2];

    public static final int LDPI_DEVICE_SCREEN_HEIGHT = 320;
    private static final long USE_DND_DURATION = 2 * DateUtils.HOUR_IN_MILLIS; // 2 hour don not disturb

    private static long sLastClickTimeForDoubleClickCheck;

    private static long sInstallTime;

    private static int sStreamVolume = -1;

    private static Random sRandom = new Random();

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) < EPSILON;
    }

    public static boolean isPropertyEnabled(String propertyName) {
        return Log.isLoggable(propertyName, Log.VERBOSE);
    }

    public static long getPackageLastModifiedTime(String packageName) {
        ApplicationInfo appInfo;
        try {
            appInfo = BFApplication.getContext().getPackageManager().getApplicationInfo(packageName, 0);
        } catch (Exception e) {
            return -1;
        }
        String appFile = appInfo.sourceDir;
        return new File(appFile).lastModified();
    }

    public static int validateIndex(List<? extends Object> sizeLimit, int rawIndex) {
        return Math.max(0, Math.min(rawIndex, sizeLimit.size() - 1));
    }

    public static final int FLASHLIGHT_STATUS_FAIL = -1;
    public static final int FLASHLIGHT_STATUS_OFF = 0;
    public static final int FLASHLIGHT_STATUS_ON = 1;


    public static float celsiusToFahrenheit(float celsius) {
        return celsius * 1.8f + 32f;
    }

    public static float celsiusCoolerByToFahrenheit(float celsius) {
        return celsius * 1.8f;
    }

    public static boolean setMobileDataStatus(Context context, boolean enabled) {
        if (Compats.IS_HUAWEI_DEVICE && isWifiEnabled()) {
            return false;
        }
        ConnectivityManager connectivityManager;
        Class connectivityManagerClz;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManagerClz = connectivityManager.getClass();
            Method method = ReflectionHelper.getMethod(connectivityManagerClz, "setMobileDataEnabled", boolean.class);
            // Asynchronous invocation
            method.invoke(connectivityManager, enabled);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean getMobileDataStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        String methodName = "getMobileDataEnabled";
        Class cmClass = connectivityManager.getClass();
        Boolean isOpen;

        try {
            @SuppressWarnings("unchecked")
            Method method = ReflectionHelper.getMethod(cmClass, methodName);
            isOpen = (Boolean) method.invoke(connectivityManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return isOpen;
    }

    public static boolean isWifiEnabled() {
        try {
            WifiManager wifiManager = (WifiManager) BFApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return wifiManager.isWifiEnabled();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean hasUpdate() {
        int latestVersionCode = getLatestVersionCode();
        return BFApplication.getCurrentLaunchInfo().appVersionCode < latestVersionCode;
    }

    public static int getLatestVersionCode() {
        return BFConfig.optInteger(10000000, "Application", "Update", "LatestVersionCode");
    }


    public static boolean isSpecialApp(String[] keywords, String packageName) {
        for (String keyword : keywords) {
            if (packageName.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBrowserApp(String packageName) {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        List<ResolveInfo> resolveInfos;
        try {
            resolveInfos = BFApplication.getContext().getPackageManager()
                    .queryIntentActivities(browserIntent, PackageManager.GET_META_DATA);
        } catch (Exception ignored) {
            return false;
        }
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (TextUtils.equals(packageName, resolveInfo.activityInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void setTypefaceRecursive(View root, Typeface typeface) {
        if (!(root instanceof ViewGroup)) {
            if (root instanceof TextView) {
                ((TextView) root).setTypeface(typeface);
            }
            return;
        }
        int childCount = ((ViewGroup) root).getChildCount();
        for (int i = 0; i < childCount; i++) {
            setTypefaceRecursive(((ViewGroup) root).getChildAt(i), typeface);
        }
    }

    public static void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, so we can grab the correct window token from it
        View view = activity.getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Given a coordinate relative to the descendant, find the coordinate in a parent view's
     * coordinates.
     *
     * @param descendant        The descendant to which the passed coordinate is relative.
     * @param root              The root view to make the coordinates relative to.
     * @param outCoord          The coordinate that we want mapped.
     * @param includeRootScroll Whether or not to account for the scroll of the descendant:
     *                          sometimes this is relevant as in a child's coordinates within the descendant.
     * @return The factor by which this descendant is scaled relative to this DragLayer. Caution
     * this scale factor is assumed to be equal in X and Y, and so if at any point this
     * assumption fails, we will need to return a pair of scale factors.
     */
    public static float getDescendantCoordRelativeToParent(
            View descendant, View root, int[] outCoord, boolean includeRootScroll) {
        ArrayList<View> ancestorChain = new ArrayList<>();

        float[] pt = {outCoord[0], outCoord[1]};

        View v = descendant;
        while (v != root && v != null) {
            ancestorChain.add(v);
            v = (View) v.getParent();
        }
        ancestorChain.add(root);

        float scale = 1.0f;
        int count = ancestorChain.size();
        for (int i = 0; i < count; i++) {
            View v0 = ancestorChain.get(i);
            // For TextViews, scroll has a meaning which relates to the text position
            // which is very strange... ignore the scroll.
            if (v0 != descendant || includeRootScroll) {
                pt[0] -= v0.getScrollX();
                pt[1] -= v0.getScrollY();
            }

            v0.getMatrix().mapPoints(pt);
            pt[0] += v0.getLeft();
            pt[1] += v0.getTop();
            scale *= v0.getScaleX();
        }

        outCoord[0] = Math.round(pt[0]);
        outCoord[1] = Math.round(pt[1]);
        return scale;
    }

    /**
     * Utility method to determine whether the given point, in local coordinates,
     * is inside the view, where the area of the view is expanded by the slop factor.
     * This method is called while processing touch-move events to determine if the event
     * is still within the view.
     */
    public static boolean pointInView(View v, float localX, float localY, float slop) {
        return localX >= -slop && localY >= -slop && localX < (v.getWidth() + slop) &&
                localY < (v.getHeight() + slop);
    }

    public static void scaleRect(Rect r, float scale) {
        if (scale != 1.0f) {
            r.left = (int) (r.left * scale + 0.5f);
            r.top = (int) (r.top * scale + 0.5f);
            r.right = (int) (r.right * scale + 0.5f);
            r.bottom = (int) (r.bottom * scale + 0.5f);
        }
    }

    public static void scaleRectAboutCenter(Rect r, float scale) {
        int cx = r.centerX();
        int cy = r.centerY();
        r.offset(-cx, -cy);
        Utils.scaleRect(r, scale);
        r.offset(cx, cy);
    }

    public static int mirrorIndexIfRtl(boolean isRtl, int total, int ltrIndex) {
        if (isRtl) {
            return total - ltrIndex - 1;
        } else {
            return ltrIndex;
        }
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static boolean isSystemApp(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ComponentName cn = intent.getComponent();
        String packageName = null;
        if (cn == null) {
            ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if ((info != null) && (info.activityInfo != null)) {
                packageName = info.activityInfo.packageName;
            }
        } else {
            packageName = cn.getPackageName();
        }
        if (packageName != null) {
            try {
                PackageInfo info = pm.getPackageInfo(packageName, 0);
                return (info != null) && (info.applicationInfo != null) &&
                        ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            } catch (NameNotFoundException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isSystemApp(ApplicationInfo appInfo) {
        return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
    }

    /**
     * @return Default to {@code false} on error.
     */
    public static boolean isSystemApp(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        if (packageName.contains("com.google") || packageName.contains("com.android") || packageName.contains("android.process")) {
            return true;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            return null != applicationInfo && (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isLaunchAbleApp(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(packageName);
        return null != context.getPackageManager().resolveActivity(intent, 0);
    }

    /**
     * This picks a dominant color, looking for high-saturation, high-value, repeated hues.
     *
     * @param bitmap  The bitmap to scan
     * @param samples The approximate max number of samples to use.
     */
    public static int findDominantColorByHue(Bitmap bitmap, int samples) {
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        int sampleStride = (int) Math.sqrt((height * width) / samples);
        if (sampleStride < 1) {
            sampleStride = 1;
        }

        // This is an out-param, for getting the hsv values for an rgb
        float[] hsv = new float[3];

        // First get the best hue, by creating a histogram over 360 hue buckets,
        // where each pixel contributes a score weighted by saturation, value, and alpha.
        float[] hueScoreHistogram = new float[360];
        float highScore = -1;
        int bestHue = -1;

        for (int y = 0; y < height; y += sampleStride) {
            for (int x = 0; x < width; x += sampleStride) {
                int argb = bitmap.getPixel(x, y);
                int alpha = 0xFF & (argb >> 24);
                if (alpha < 0x80) {
                    // Drop mostly-transparent pixels.
                    continue;
                }
                // Remove the alpha channel.
                int rgb = argb | 0xFF000000;
                Color.colorToHSV(rgb, hsv);
                // Bucket colors by the 360 integer hues.
                int hue = (int) hsv[0];
                if (hue < 0 || hue >= hueScoreHistogram.length) {
                    // Defensively avoid array bounds violations.
                    continue;
                }
                float score = hsv[1] * hsv[2];
                hueScoreHistogram[hue] += score;
                if (hueScoreHistogram[hue] > highScore) {
                    highScore = hueScoreHistogram[hue];
                    bestHue = hue;
                }
            }
        }

        SparseArray<Float> rgbScores = new SparseArray<>();
        int bestColor = 0xff000000;
        highScore = -1;
        // Go back over the RGB colors that match the winning hue,
        // creating a histogram of weighted s*v scores, for up to 100*100 [s,v] buckets.
        // The highest-scoring RGB color wins.
        for (int y = 0; y < height; y += sampleStride) {
            for (int x = 0; x < width; x += sampleStride) {
                int rgb = bitmap.getPixel(x, y) | 0xff000000;
                Color.colorToHSV(rgb, hsv);
                int hue = (int) hsv[0];
                if (hue == bestHue) {
                    float s = hsv[1];
                    float v = hsv[2];
                    int bucket = (int) (s * 100) + (int) (v * 10000);
                    // Score by cumulative saturation * value.
                    float score = s * v;
                    Float oldTotal = rgbScores.get(bucket);
                    float newTotal = oldTotal == null ? score : oldTotal + score;
                    rgbScores.put(bucket, newTotal);
                    if (newTotal > highScore) {
                        highScore = newTotal;
                        // All the colors in the winning bucket are very similar. Last in wins.
                        bestColor = rgb;
                    }
                }
            }
        }
        return bestColor;
    }

    /**
     * Compresses the bitmap to a byte array for serialization.
     */
    public static byte[] flattenBitmap(Bitmap bitmap) {
        // Try go guesstimate how much space the icon will take when serialized
        // to avoid unnecessary allocations/copies during the write.
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            // ArrayIndexOutOfBoundsException may be thrown during byte array copy
            BFLog.w(TAG, "Could not write bitmap");
            return null;
        }
    }

    /**
     * Find the first vacant cell, if there is one.
     *
     * @param vacant Holds the x and y coordinate of the vacant cell
     * @param spanX  Horizontal cell span.
     * @param spanY  Vertical cell span.
     * @return true if a vacant cell was found
     */
    public static boolean findVacantCell(int[] vacant, int spanX, int spanY, int xCount, int yCount, boolean[][] occupied) {
        for (int y = 0; (y + spanY) <= yCount; y++) {
            for (int x = 0; (x + spanX) <= xCount; x++) {
                boolean available = !occupied[x][y];
                out:
                for (int i = x; i < x + spanX; i++) {
                    for (int j = y; j < y + spanY; j++) {
                        available = available && !occupied[i][j];
                        if (!available)
                            break out;
                    }
                }

                if (available) {
                    vacant[0] = x;
                    vacant[1] = y;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Trims the string, removing all whitespace at the beginning and end of the string.
     * Non-breaking whitespaces are also removed.
     */
    public static String trim(CharSequence s) {
        if (s == null) {
            return null;
        }

        // Just strip any sequence of whitespace or java space characters from the beginning and end
        Matcher m;
        try {
            m = sTrimPattern.matcher(s);
        } catch (IllegalArgumentException e) {
            return s.toString();
        }
        return m.replaceAll("$1");
    }

    /**
     * Convenience println with multiple args.
     */
    public static void println(String key, Object... args) {
        StringBuilder b = new StringBuilder();
        b.append(key);
        b.append(": ");
        boolean isFirstArgument = true;
        for (Object arg : args) {
            if (isFirstArgument) {
                isFirstArgument = false;
            } else {
                b.append(", ");
            }
            b.append(arg);
        }
        System.out.println(b.toString());
    }

    public static float dpiFromPx(int size) {
        return (size / Dimensions.getDensityRatio());
    }

    public static int pxFromDp(float size, DisplayMetrics metrics) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, metrics));
    }

    public static int pxFromSp(float size, DisplayMetrics metrics) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, metrics));
    }

    public static String createDbSelectionQuery(String columnName, Iterable<?> values) {
        return String.format(Locale.ENGLISH, "%s IN (%s)", columnName, TextUtils.join(", ", values));
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Height with status bar but not navigation bar
     */
    public static Point getScreenSize(Activity launcher) {
        Display display = launcher.getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        return screenSize;
    }

    public static float getPhysicalScreenHeight(Activity launcher) {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            launcher.getWindowManager().getDefaultDisplay().getRealSize(point);
        } else {
            launcher.getWindowManager().getDefaultDisplay().getSize(point);
        }

        DisplayMetrics dm = launcher.getResources().getDisplayMetrics();
        return point.y / dm.ydpi;
    }

    public static int getPhysicalScreenHeight(Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }

//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return point.y;
    }

    public static void showNavigationBarMenuButton(Activity activity) {
        if (!Dimensions.hasNavBar(activity))
            return;
        int menuFlag;
        try {
            Field f = ReflectionHelper.getField(WindowManager.LayoutParams.class, "FLAG_NEEDS_MENU_KEY");
            menuFlag = f.getInt(null);
            Window window = activity.getWindow();
            window.addFlags(menuFlag);
            return;
        } catch (Exception ignored) {
        }

        try {
            Field menuFlagField = ReflectionHelper.getField(WindowManager.LayoutParams.class, "NEEDS_MENU_SET_TRUE");
            menuFlag = menuFlagField.getInt(null);
            Method method = ReflectionHelper.getDeclaredMethod(Window.class, "setNeedsMenuKey", int.class);
            method.setAccessible(true);
            method.invoke(activity.getWindow(), menuFlag);
        } catch (Exception ignored) {
        }
    }

    public static void hideNavigationBarMenuButton(Activity activity) {
        if (!Dimensions.hasNavBar(activity))
            return;
        int menuFlag;
        try {
            Field f = ReflectionHelper.getField(WindowManager.LayoutParams.class, "FLAG_NEEDS_MENU_KEY");
            menuFlag = f.getInt(null);
            Window window = activity.getWindow();
            window.clearFlags(menuFlag);
            return;
        } catch (Exception ignored) {
        }
        try {
            Field menuFlagField = WindowManager.LayoutParams.class.getField("NEEDS_MENU_SET_FALSE");
            menuFlag = menuFlagField.getInt(null);
            Method method = ReflectionHelper.getDeclaredMethod(Window.class, "setNeedsMenuKey", int.class);
            method.setAccessible(true);
            method.invoke(activity.getWindow(), menuFlag);
        } catch (Exception ignored) {
        }
    }

    public static byte[] readFile(File file) {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[STREAM_OP_BUFFER_SIZE];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } catch (IOException e) {
            return new byte[0];
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException ignored) {
            }
        }
        return ous.toByteArray();
    }

    public static String readFileString(File file) {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[STREAM_OP_BUFFER_SIZE];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } catch (IOException e) {
            return "";
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException ignored) {
            }
        }
        return ous.toString();
    }

    public static void writeToFile(File file, byte[] data) {
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    BFLog.d(TAG, "Create file " + file.getAbsolutePath());
                }
            }
            fos = new FileOutputStream(file);
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public static void saveBitmapToFile(Bitmap bitmap, String fileOutPath, int quality) {
        try {
            saveBitmapToFileInternal(bitmap, new FileOutputStream(fileOutPath), quality);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmapToFile(Bitmap bitmap, File fileOutPath, int quality) {
        try {
            saveBitmapToFileInternal(bitmap, new FileOutputStream(fileOutPath), quality);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveBitmapToFileInternal(Bitmap bitmap, FileOutputStream fos, int quality) {
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveInputStreamToFile(byte[] preData, InputStream is, File fileOut) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(fileOut);
            if (null != preData) {
                output.write(preData);
            }

            byte[] buffer = new byte[STREAM_OP_BUFFER_SIZE];
            int read;

            while ((read = is.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ignored) {
            }
        }
        return true;
    }

    /**
     * Retrieve, creating if needed, a new directory of given name in which we
     * can place our own custom data files.
     */
    public static @Nullable File getDirectory(String dirPath) {
        File file = BFApplication.getContext().getFilesDir();
        String[] path = dirPath.split(File.separator);
        for (String dir : path) {
            file = new File(file, dir);
            if (!file.exists() && !file.mkdir()) {
                BFLog.w(TAG, "Error making directory");
                return null;
            }
        }
        return file;
    }

    /**
     * Retrieve, creating if needed, a new sub-directory in cache directory.
     * Internal cache directory is used if external cache directory is not available.
     */
    public static File getCacheDirectory(String subDirectory) {
        return getCacheDirectory(subDirectory, false);
    }

    /**
     * @param useInternal Only uses internal cache directory when {@code true}.
     */
    public static File getCacheDirectory(String subDirectory, boolean useInternal) {
        Context context = BFApplication.getContext();
        String cacheDirPath;
        File externalCache = null;
        if (!useInternal) {
            try {
                externalCache = context.getExternalCacheDir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (externalCache != null) {
            cacheDirPath = externalCache.getAbsolutePath() + File.separator + subDirectory + File.separator;
        } else {
            cacheDirPath = context.getCacheDir().getAbsolutePath() + File.separator + subDirectory + File.separator;
        }
        File cacheDir = new File(cacheDirPath);
        if (!cacheDir.exists()) {
            if (cacheDir.mkdirs()) {
                BFLog.d("Utils.Cache", "Created cache directory: " + cacheDir.getAbsolutePath());
            } else {
                BFLog.e("Utils.Cache", "Failed to create cache directory: " + cacheDir.getAbsolutePath());
            }
        }
        return cacheDir;
    }

    public static File getFilesDirectory(String subDirectory) {
        Context context = BFApplication.getContext();
        String filesDirPath;
        File internalFiles = null;
        try {
            internalFiles = context.getFilesDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (internalFiles != null) {
            filesDirPath = internalFiles.getAbsolutePath() + File.separator + subDirectory + File.separator;
        } else {
            filesDirPath = context.getFilesDir().getAbsolutePath() + File.separator + subDirectory + File.separator;
        }
        File filesDir = new File(filesDirPath);
        if (!filesDir.exists()) {
            if (filesDir.mkdirs()) {
                BFLog.d("Utils.Files", "Files cache directory: " + filesDir.getAbsolutePath());
            } else {
                BFLog.e("Utils.Files", "Failed to create files directory: " + filesDir.getAbsolutePath());
            }
        }
        return filesDir;
    }

    public static boolean copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                BFLog.d("Utils.copyDirectory", "target mkdir failed: " + targetLocation.getAbsolutePath());
                return false;
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                if (!copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]))) {
                    return false;
                }
            }
        } else {
            if (!copyFile(sourceLocation, targetLocation)) {
                return false;
            }
        }

        return true;
    }

    public static boolean copyFile(File src, File dst) throws IOException {
        if (!src.exists()) {
            return false;
        }
        if (dst.exists()) {
            boolean removed = dst.delete();
            if (removed) BFLog.d(TAG, "Replacing file " + dst);
        }
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        boolean result = false;
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            outChannel.close();
        }
        return result;
    }

    /**
     * Retrieve, creating if needed, a new directory of given name in which we
     * can place our own custom data files.
     */
    public static @Nullable File getDirectory(File rootDir, String dirPath) {
        File file = rootDir;
        String[] path = dirPath.split(File.separator);
        for (String dir : path) {
            file = new File(file, dir);
            if (!file.exists() && !file.mkdir()) {
                BFLog.w(TAG, "Error making directory");
                return null;
            }
        }
        return file;
    }

    public static boolean upZipFile(File zipFile, File desDir) {
        try {
            ZipFile zfile = new ZipFile(zipFile);
            Enumeration zList = zfile.entries();
            byte[] buf = new byte[STREAM_OP_BUFFER_SIZE];
            while (zList.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) zList.nextElement();
                Log.d(TAG, "ze.getName() = " + ze.getName());
                File file = new File(desDir, ze.getName());

                if (ze.isDirectory()) {

                    if (!file.exists()) {
                        file.mkdir();
                    }
                    continue;
                }

                int last = ze.getName().lastIndexOf(File.separator);
                if (last > 0) {
                    getDirectory(desDir, ze.getName().substring(0, last));
                }

                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, STREAM_OP_BUFFER_SIZE)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
            zfile.close();
        } catch (IOException ignored) {
            return false;
        }

        return true;
    }

    public static boolean deleteRecursive(File fileOrDirectory) {
        boolean result = true;
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) {
                    result &= deleteRecursive(child);
                }
            }
        }
        boolean success = fileOrDirectory.delete();
        BFLog.v("H5.Files", "Delete " + (fileOrDirectory.isDirectory() ? "directory " : "file ")
                + fileOrDirectory.getName() + ", success: " + success);
        result &= success;
        return result;
    }


    public static long getMemoryTotalSize() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long memory = 0;
        FileReader localFileReader = null;
        BufferedReader localBufferedReader = null;
        try {
            localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            if (str2 != null) {
                arrayOfString = str2.split("\\s+");
                memory = Long.valueOf(arrayOfString[1]) * 1024;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (localFileReader != null) {
                    localFileReader.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
            } catch (IOException ignored) {
            }
        }
        return memory;
    }

    public static long getMemoryAvailableSize() {
        ActivityManager am = (ActivityManager) BFApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem + getSelfMemoryUsed();
    }

    private static long getSelfMemoryUsed() {
        long memSize = 0;
        ActivityManager am = (ActivityManager) BFApplication.getContext().getSystemService(BFApplication.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAPP : runningApps) {
            if (BFApplication.getContext().getPackageName().equals(runningAPP.processName)) {
                int[] pids = new int[]{runningAPP.pid};
                Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(pids);
                memSize = memoryInfo[0].getTotalPss() * 1024;
                break;
            }
        }
        return memSize;
    }

    public static String getRemoteFileExtension(String url) {
        String extension = "";
        if (url != null) {
            int i = url.lastIndexOf('.');
            int p = Math.max(url.lastIndexOf('/'), url.lastIndexOf('\\'));
            if (i > p) {
                extension = url.substring(i + 1);
            }
        }
        return extension;
    }

    /**
     * @return {@code n} unique integers in range [start, end). Or {@code null} when end - start < n
     * or end <= start. Note that this implementation is for "dense" params, where end - start is no
     * larger than a reasonable size of array list allocation, and where n takes a substantial
     * portion of the range.
     */
    public static int[] getUniqueRandomInts(int start, int end, int n) {
        if (n > end - start || end <= start) {
            return null;
        }
        List<Integer> numberList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            numberList.add(i);
        }
        Collections.shuffle(numberList);
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = numberList.get(i);
        }
        return result;
    }

    public static float formatNumberOneDigit(double number) {
        return (float) (Math.round(number * 10)) / 10;
    }

    public static double formatNumberTwoDigit(double number) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @MainThread
    public static boolean checkDoubleClickGlobal() {
        long time = SystemClock.elapsedRealtime();
        long timeD = time - sLastClickTimeForDoubleClickCheck;
        if (0 < timeD && timeD < DOUBLE_CLICK_TIMEOUT) {
            return true;
        }
        sLastClickTimeForDoubleClickCheck = time;
        return false;
    }

    public static String getAppLabel(String packageName) {
        PackageManager packageManager = BFApplication.getContext().getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (Exception ignored) {
        }
        return (String) ((null != applicationInfo) ? packageManager.getApplicationLabel(applicationInfo) : "");
    }

    public static boolean isNewUser() {
        return BFApplication.getFirstLaunchInfo().appVersionCode == BFApplication.getCurrentLaunchInfo().appVersionCode;
    }

    public static boolean isInDay(int day) {
        long current = System.currentTimeMillis();
        long first = BFSessionMgr.getFirstSessionStartTime();
        BFLog.d(TAG, "current = " + current + "; first = " + first);
        return (current - first) < day * Dates.TIME_DAY;
    }

    public static boolean isNewUserByVersionName() {
        return TextUtils.equals(BFApplication.getCurrentLaunchInfo().appVersionName,
                BFApplication.getFirstLaunchInfo().appVersionName);
    }

    public static boolean checkFileValid(File file) {
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean isTouchInView(View view, MotionEvent event) {
        if (null == view) {
            return false;
        }

        Rect rect = new Rect();
        view.getDrawingRect(rect);

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        RectF viewRectF = new RectF(rect);
        viewRectF.offset(location[0], location[1]);
        return viewRectF.contains(event.getRawX(), event.getRawY());
    }

    public static float formatNumberToOneDigit(double number) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
        double result = number;
        try {
            result = Double.parseDouble(df.format(number));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return (float) result;
    }

    public static boolean isStringNumber(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static boolean isKeyguardLocked(Context context, boolean defaultValue) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        try {
            Method declaredMethod = ReflectionHelper.getDeclaredMethod(KeyguardManager.class, "isKeyguardLocked");
            declaredMethod.setAccessible(true);
            defaultValue = (Boolean) declaredMethod.invoke(keyguardManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        return defaultValue;
    }

    public static Notification buildNotificationSafely(NotificationCompat.Builder builder) {
        try {
            return builder.build();
        } catch (Exception e) {
            BFLog.e(TAG, "Error building notification: " + builder + ", exception: " + e);
            return null;
        }
    }

    public static void handleBinderSizeError(Exception e) {
        if (isBinderSizeError(e)) {
            e.printStackTrace();
        } else {
            throw new RuntimeException("Unexpected exception that is not a binder size error", e);
        }
    }

    public static boolean isBinderSizeError(Throwable e) {
        return e.getCause() instanceof TransactionTooLargeException
                || e.getCause() instanceof DeadObjectException;
    }

    public static Object callWithRetry(Callable<Object> action, int totalTryCount, long interval, int failAction) {
        for (int i = 0; i < totalTryCount; i++) {
            try {
                return action.call();
            } catch (Throwable e) {
                BFLog.d(TAG, "Failed, try #" + i);
            }
            if (interval > 0) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        switch (failAction) {
            case RETRY_FAIL_ACTION_ABORT:
                BFLog.e(TAG, "Failed after " + totalTryCount + " retries, abort");
                System.exit(-1);
                break;
            case RETRY_FAIL_ACTION_IGNORE:
                BFLog.w(TAG, "Failed after " + totalTryCount + " retries, ignore");
                break;
        }
        return null;
    }

    public static boolean isEn() {
        String lg = Locale.getDefault().getLanguage();
        return lg.startsWith("en");
    }

    public static boolean isRu() {
        String lg = Locale.getDefault().getLanguage();
        return lg.startsWith("ru");
    }

    public static boolean isIntentExist(Context context, Intent intent) {
        if (intent == null) {
            return false;
        }
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        }
        return true;
    }

    public static boolean hasPermission(String permission) {
        boolean granted = false;
        if (!TextUtils.isEmpty(permission)) {
            try {
                granted = ContextCompat.checkSelfPermission(BFApplication.getContext(), permission)
                        == PackageManager.PERMISSION_GRANTED;
            } catch (RuntimeException e) {
            }
        }
        return granted;
    }

    public static int getPixelInsetTop(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * It's guaranteed that exactly one of {@link ResolveInfo#activityInfo},
     * {@link ResolveInfo#serviceInfo}, or {@link ResolveInfo#providerInfo} will be non-null.
     *
     * @return The only non-null {@link ComponentInfo} in the {@link ResolveInfo}. Or {@code null}.
     */
    public static ComponentInfo getComponentInfo(ResolveInfo resolveInfo) {
        if (resolveInfo.activityInfo != null) {
            return resolveInfo.activityInfo;
        }
        if (resolveInfo.serviceInfo != null) {
            return resolveInfo.serviceInfo;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && resolveInfo.providerInfo != null) {
            return resolveInfo.providerInfo;
        }
        return null;
    }

    public static void mute() {
        try {
            AudioManager audioManager = (AudioManager) BFApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
            int streamVolume = audioManager.getStreamVolume(3);
            if (streamVolume != 0) {
                sStreamVolume = streamVolume;
            }
            audioManager.setStreamVolume(3, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resumeVolume() {
        try {
            if (sStreamVolume != -1) {
                ((AudioManager) BFApplication.getContext().getSystemService(Context.AUDIO_SERVICE)).setStreamVolume(3, sStreamVolume, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getRandomInt(int bound) {
        return sRandom.nextInt(bound);
    }

    public static String getNetworkStatus(Context ctx) {

    	return "wifi";

        // ConnectivityManager connMgr =ctx == null ? null : (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // if (connMgr == null) {
        //     return "no_network";
        // }

        // NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //     Network network = connMgr.getActiveNetwork();
        //     if (network == null) {
        //         return "no_network";
        //     }
        //     NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(network);
        //     if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        //         return "wifi";
        //     }
        // } else {
        //     if (networkInfo != null && networkInfo.isConnected()
        //             && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        //         return "wifi";
        //     }
        // }

        // if (networkInfo != null && networkInfo.isConnected()) {
        //     TelephonyManager telephonyManager = (TelephonyManager)
        //             ctx.getSystemService(Context.TELEPHONY_SERVICE);
        //     if (telephonyManager == null) {
        //         return "no_network";
        //     }
        //     int networkType = telephonyManager.getNetworkType();
        //     switch (networkType) {
        //         case TelephonyManager.NETWORK_TYPE_GPRS:
        //         case TelephonyManager.NETWORK_TYPE_EDGE:
        //         case TelephonyManager.NETWORK_TYPE_CDMA:
        //         case TelephonyManager.NETWORK_TYPE_1xRTT:
        //         case TelephonyManager.NETWORK_TYPE_IDEN:
        //             return "2G";
        //         case TelephonyManager.NETWORK_TYPE_UMTS:
        //         case TelephonyManager.NETWORK_TYPE_EVDO_0:
        //         case TelephonyManager.NETWORK_TYPE_EVDO_A:
        //         case TelephonyManager.NETWORK_TYPE_HSDPA:
        //         case TelephonyManager.NETWORK_TYPE_HSUPA:
        //         case TelephonyManager.NETWORK_TYPE_HSPA:
        //         case TelephonyManager.NETWORK_TYPE_EVDO_B:
        //         case TelephonyManager.NETWORK_TYPE_EHRPD:
        //         case TelephonyManager.NETWORK_TYPE_HSPAP:
        //             return "3G";
        //         case TelephonyManager.NETWORK_TYPE_LTE:
        //             return "4G";
        //         default:
        //             return "no_network";
        //     }
        // }

        // return "no_network";
    }



    public static boolean isWifiEnabled(Context context) {
        WifiManager mng = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mng.isWifiEnabled();
    }

    public static void openBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivitySafely(context, browserIntent);
    }

    public static void startActivitySafely(Context context, Intent intent) {
        try {
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (RuntimeException e) {
            Toasts.showToast(R.string.setting_device_not_support_message);
            BFLog.e("StartActivity", "Cannot start activity: " + intent);
        }
    }


    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getLocalIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static void queryIP(BFHttpConnection.OnConnectionFinishedListener finish, BFHttpConnection.OnDataReceivedListener data) {
        Threads.runOnMainThread(new Runnable() {
            @Override public void run() {
                final BFHttpConnection serverAPIConnection = new BFHttpConnection(BFConfig.optString("http://www.ip-api.com/json","Application.GED.IPurl"));
                serverAPIConnection.setConnectionFinishedListener(finish);
                serverAPIConnection.setDataReceivedListener(data);
                serverAPIConnection.startAsync();
            }
        });
    }

}
