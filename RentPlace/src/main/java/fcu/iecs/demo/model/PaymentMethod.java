package fcu.iecs.demo.model;

public enum PaymentMethod {
  ONLINE_PAYMENT("線上支付"),
  BANK_TRANSFER("轉帳繳費");

  private final String displayName;

  PaymentMethod(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  // 添加一個方法來根據中文名稱查找對應的 enum
  public static PaymentMethod fromDisplayName(String displayName) {
    for (PaymentMethod method : PaymentMethod.values()) {
      if (method.getDisplayName().equals(displayName)) {
        return method;
      }
    }
    throw new IllegalArgumentException("No payment method found for display name: " + displayName);
  }
}