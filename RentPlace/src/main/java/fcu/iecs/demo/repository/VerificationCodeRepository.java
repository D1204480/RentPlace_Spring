package fcu.iecs.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class VerificationCodeRepository {

  private Map<String, VerificationCodeEntry> verificationCodes = new HashMap<>();

  public void save(String email, String code) {
    VerificationCodeEntry entry = new VerificationCodeEntry(code, System.currentTimeMillis() + 10 * 60 * 1000); // 有效期 10 分鐘
    verificationCodes.put(email, entry);
  }

  public boolean isCodeValid(String email, String code) {
    VerificationCodeEntry entry = verificationCodes.get(email);
    return entry != null && entry.getCode().equals(code) && entry.getExpiresAt() > System.currentTimeMillis();
  }

  private static class VerificationCodeEntry {
    private final String code;
    private final long expiresAt;

    VerificationCodeEntry(String code, long expiresAt) {
      this.code = code;
      this.expiresAt = expiresAt;
    }

    public String getCode() {
      return code;
    }

    public long getExpiresAt() {
      return expiresAt;
    }
  }
}
