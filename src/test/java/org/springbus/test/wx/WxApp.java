package org.springbus.test.wx;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

public class WxApp {


    public static void mainApp(String[] args) {

        String session_key = "RdgsN8W7une2viejS5zpdw==";
        String iv = "OsZWDSsoQDs3EHyKO8wOdg==";
        String encryptData = "ujuxzq0PWlkCsYPDKFhxynksXrFLxJUNbf+8x1v+KvPT0tmjNKUqt+AJtX4XleaxZiF2GO4xrcirV9Hx2uGgCnTc0It6POKH/vD6xqIvOhG4MG2vFIaUQ8jtdydIrF5R03OW/A6P2OFwiZs7aGVH8ckA+N+Qqq/L2yNtjy7UUK+PZkSo6bjWxq3Xo6wEQN4l7r8fsWvisqPI+JxHBZpbqQ==";
        String decrypt = decrypt(session_key, iv, encryptData);
        System.out.println(decrypt);

    }

    public static String decrypt(String session_key, String iv, String encryptData) {
        String decryptString = "";

        byte[] sessionKeyByte = Base64.decodeBase64(session_key);
        byte[] ivByte = Base64.decodeBase64(iv);
        byte[] encryptDataByte = Base64.decodeBase64(encryptData);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key key = new SecretKeySpec(sessionKeyByte, "AES");
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
            algorithmParameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters);
            byte[] bytes = cipher.doFinal(encryptDataByte);
            decryptString = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptString;
    }


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


}
