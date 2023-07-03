package id.codecorn.bpjsapi.utils;

public class Signature {
    private static Signature instance;
    private String username;
    private String password;
    private String kode;
    private String secretKey;
    private String consId;
    private String userKey;

    public Signature(String username, String password, String kode, String secretKey, String consId, String userKey) {
        this.username = username;
        this.password = password;
        this.kode = kode;
        this.secretKey = secretKey;
        this.consId = consId;
        this.userKey = userKey;
    }

    public static Signature getInstance(String username, String password, String kode, String secretKey, String consId, String userKey) {
        if (instance == null) {
            instance = new Signature(username, password, kode, secretKey, consId, userKey);
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getKode() {
        return kode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getConsId() {
        return consId;
    }

    public String getUserKey() {
        return userKey;
    }
}
