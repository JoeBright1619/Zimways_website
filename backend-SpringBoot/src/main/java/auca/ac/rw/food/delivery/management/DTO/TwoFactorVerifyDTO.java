package auca.ac.rw.food.delivery.management.DTO;

public class TwoFactorVerifyDTO {
    private String code;
    private String secret;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
} 