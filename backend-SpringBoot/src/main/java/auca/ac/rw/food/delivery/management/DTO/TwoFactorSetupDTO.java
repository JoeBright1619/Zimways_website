package auca.ac.rw.food.delivery.management.DTO;

public class TwoFactorSetupDTO {
    private String qrCodeImage;
    private String secret;

    public TwoFactorSetupDTO(String qrCodeImage, String secret) {
        this.qrCodeImage = qrCodeImage;
        this.secret = secret;
    }

    public String getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(String qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
} 