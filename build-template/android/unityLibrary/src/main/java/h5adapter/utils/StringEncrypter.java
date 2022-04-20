package h5adapter.utils;

import android.util.Base64;

public class StringEncrypter {
    static final String AES_SECRETKEY = "TZp]zx[Ki}96pri/";
    public static String encryptRequestData(String data) {
        try {
            byte[] encrypted = AESEncrypter.encrypt(data.getBytes("utf-8"), AES_SECRETKEY);
            byte[] base64ed = Base64.encode(encrypted, Base64.DEFAULT);
            String result = new String(base64ed, "utf-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dencryptResponse(String encrypted) {
        try {
            byte[] deBase64ed = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] decrypt = AESEncrypter.decrypt(deBase64ed, AES_SECRETKEY);
            String result = new String(decrypt, "utf-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
