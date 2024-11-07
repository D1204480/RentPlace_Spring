package fcu.iecs.demo.model;

public class VerificationResponse {
  private boolean isValid;

  public VerificationResponse(boolean isValid) {
    this.isValid = isValid;
  }

  public boolean isValid() {
    return isValid;
  }
}