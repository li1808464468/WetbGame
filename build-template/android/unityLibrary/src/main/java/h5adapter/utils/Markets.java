package h5adapter.utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.blowfire.app.framework.BFApplication;
import com.blowfire.app.utils.BFInstallationUtils;
import com.blowfire.common.config.BFConfig;
import com.blowfire.common.utils.BFLog;
import com.blowfire.common.utils.BFPreferenceHelper;

import java.util.List;

public class Markets {

    public static final String GOOGLE_MARKET = "Google";
    private static final String KEY_PACKAGE_NAME = "AppPackage";
    private static final String KEY_APP_URL = "AppUrl";
    private static final String KEY_WEB_URL = "WebUrl";
    private static final String KEY_DEFAULT_MARKET = "DefaultMarket";
    private static final String KEY_MARKETS = "Markets";
    private static final String KEY_MARKET_ROOT = "Market";
    private static final String KEY_LIB_COMMONS = "libCommons";
    private static final String ORIGINAL_MARKET = "original_market";

    public static String getDefaultMarket() {
        return BFConfig.optString(GOOGLE_MARKET, new String[]{KEY_LIB_COMMONS, KEY_MARKET_ROOT, KEY_DEFAULT_MARKET}).trim();
    }

    public static String getOriginalMarket() {
        BFPreferenceHelper sp = BFPreferenceHelper.create(BFApplication.getContext(), BFApplication.getContext().getPackageName());
        String originalMarket = sp.getString(ORIGINAL_MARKET, (String)null);
        if (null == originalMarket) {
            sp.putString(ORIGINAL_MARKET, getDefaultMarket());
            return getDefaultMarket();
        } else {
            return originalMarket;
        }
    }

    public static String getMarketGroup(String market) {
        if (TextUtils.isEmpty(market)) {
            return null;
        } else {
            int endpos = market.indexOf("-");
            return endpos < 0 ? null : market.substring(0, endpos);
        }
    }

    public static void browseAPP() {
        browseAPP(BFApplication.getContext().getPackageName());
    }

    public static void browseAPP(String packageName) {
        String defaultMarket = getDefaultMarket();
        boolean isMarketAppInstalled = false;
        isMarketAppInstalled = defaultMarket.equals(GOOGLE_MARKET) ? BFInstallationUtils.isGooglePlayInstalled() : BFInstallationUtils.isAppInstalled(getMarketPackageName(defaultMarket));
        Intent intent = null;
        Uri uri;
        if (isMarketAppInstalled) {
            uri = Uri.parse(getMarketAppUrl(defaultMarket) + packageName);
            intent = BFApplication.getContext().getPackageManager().getLaunchIntentForPackage(getMarketPackageName(defaultMarket));
            if (intent == null) {
                intent = new Intent();
            }
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            if (!isAvailable(intent)) {
                intent = null;
            } else {
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }

        if (intent == null) {
            uri = Uri.parse(getMarketWebUrl(defaultMarket) + packageName);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (intent != null) {
            try {
                BFApplication.getContext().startActivity(intent);
            } catch (ActivityNotFoundException var5) {
                var5.printStackTrace();
            }
        }

    }

    public static void browseAPP(String marketName, String appPackageName) {
        String appUrl = getMarketAppUrl(marketName);
        String marketPackageName = getMarketPackageName(marketName);
        Intent intent = null;
        if (appUrl != null && marketPackageName != null && BFInstallationUtils.isAppInstalled(marketPackageName)) {
            Uri uri = Uri.parse(appUrl + appPackageName);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (!isAvailable(intent)) {
                intent = null;
            }
        }

        if (intent == null) {
            String webUrl = getMarketWebUrl(marketName);
            if (webUrl != null) {
                Uri uri = Uri.parse(webUrl + appPackageName);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }

        if (intent != null) {
            try {
                BFApplication.getContext().startActivity(intent);
            } catch (ActivityNotFoundException var7) {
                var7.printStackTrace();
            }
        }

    }

    private static boolean isAvailable(Intent intent) {
        PackageManager mgr = BFApplication.getContext().getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, 0);
        return list.size() > 0;
    }

    public static boolean isMarketInstalled(String market) {
        if (null == market) {
            return false;
        } else {
            market.trim();
            String pkname = getMarketPackageName(market);
            return null != pkname && BFInstallationUtils.isAppInstalled(pkname) && null != getMarketAppUrl(market);
        }
    }

    private static String getMarketPackageName(String marketName) {
        String packageName = null;
        packageName = BFConfig.getString(new String[]{KEY_LIB_COMMONS, KEY_MARKET_ROOT, KEY_MARKETS, marketName, KEY_PACKAGE_NAME});
        BFLog.d("getMarketPackageName(" + marketName + ") = " + packageName);
        return packageName;
    }

    private static String getMarketAppUrl(String marketName) {
        String url = null;
        url = BFConfig.getString(new String[]{KEY_LIB_COMMONS, KEY_MARKET_ROOT, KEY_MARKETS, marketName, KEY_APP_URL});
        BFLog.d("getMarketAppUrl(" + marketName + ") = " + url);
        return url;
    }

    private static String getMarketWebUrl(String marketName) {
        String url = null;
        url = BFConfig.getString(new String[]{KEY_LIB_COMMONS, KEY_MARKET_ROOT, KEY_MARKETS, marketName, KEY_WEB_URL});
        BFLog.d("getMarketWebUrl(" + marketName + ") = " + url);
        return url;
    }
}
