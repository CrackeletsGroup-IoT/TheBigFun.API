package com.crackelets.bigfun.platform.payment.resource;

public class QRCodeRequest {
    private String qr_code_text;
    private String image_format;

    // Getters y Setters
    public String getQr_code_text() {
        return qr_code_text;
    }

    public void setQr_code_text(String qr_code_text) {
        this.qr_code_text = qr_code_text;
    }

    public String getImage_format() {
        return image_format;
    }

    public void setImage_format(String image_format) {
        this.image_format = image_format;
    }
}
