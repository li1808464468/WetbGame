/**
 *
 */
package h5adapter.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 */
public class AESEncrypter {

    public static byte[] encrypt(byte[] unencrypted, String sKey) throws Exception {
        if (sKey == null || sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");//"算法"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(unencrypted);
        return encrypted;
    }

    public static byte[] decrypt(byte[] encrypted, String sKey) throws Exception {
        if (sKey == null || sKey.length() != 16) {
            return null;
        }

        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] dencrypted = cipher.doFinal(encrypted);
        return dencrypted;
    }

}
