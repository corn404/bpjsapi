package io.github.corn404;

import io.github.corn404.dto.AesKeySpec;
import io.github.corn404.utils.Signature;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * BPJS API
 *
 */
public class BpjsApi
{
    private Signature signature;
    public static String ENCRYPT_KEY;

    public BpjsApi(Signature signature) {
        this.signature = signature;
    }

    public String generateHmacSHA256Signature(String data, String key) throws GeneralSecurityException {
        byte[] hmacData = null;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacData);
    }

    public long getTimes(){
        long millis = System.currentTimeMillis();
        return millis/1000;
    }

    public AesKeySpec generateKey(String key) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] _hashKey = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        byte[] _hashIv = new byte[16];
        for (int i = 0; i < 16; i++) {
            _hashIv[i] = _hashKey[i];
        }

        AesKeySpec aesKeySpec = new AesKeySpec();
        SecretKeySpec _key = new SecretKeySpec(_hashKey, "AES");
        IvParameterSpec _iv = new IvParameterSpec(_hashIv);
        aesKeySpec.setKey(_key);
        aesKeySpec.setIv(_iv);
        return aesKeySpec;
    }

    public String decrypt(String cipherText, SecretKeySpec key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(java.util.Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public String encrypt(String cipherText, SecretKeySpec key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(cipherText.getBytes(StandardCharsets.UTF_8)));
    }



    public HttpHeaders getHeader() throws GeneralSecurityException {
        long time = getTimes();
        String salt = this.signature.getConsId()+"&"+ time;
        String signature = generateHmacSHA256Signature(salt, this.signature.getSecretKey());
        ENCRYPT_KEY = this.signature.getConsId() + this.signature.getSecretKey() + time;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-cons-id", this.signature.getConsId());
        headers.set("X-timestamp", String.valueOf(time));
        headers.set("X-signature", signature);
        headers.set("X-authorization", generateAuthorization());
        headers.set("user_key", this.signature.getUserKey());
        return headers;
    }

    private String generateAuthorization() {
        String str = signature.getUsername() + ":" + signature.getPassword() + ":" + signature.getKode();
        return "Basic " + Base64.getEncoder().encodeToString(str.getBytes());
    }

}
