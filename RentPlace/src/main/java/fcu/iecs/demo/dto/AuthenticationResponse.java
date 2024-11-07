package fcu.iecs.demo.dto;

/**
 * Data Transfer Object for authentication responses.
 * Used to send authentication-related information back to the client.
 */
public class AuthenticationResponse {
    private String message;
    private String verificationLink;
    private String token;
    private boolean success;

    // Default constructor
    public AuthenticationResponse() {
    }

    // Full constructor
    public AuthenticationResponse(String message, String verificationLink, String token, boolean success) {
        this.message = message;
        this.verificationLink = verificationLink;
        this.token = token;
        this.success = success;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVerificationLink() {
        return verificationLink;
    }

    public void setVerificationLink(String verificationLink) {
        this.verificationLink = verificationLink;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}