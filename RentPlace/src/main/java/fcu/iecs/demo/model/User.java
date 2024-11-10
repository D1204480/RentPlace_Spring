package fcu.iecs.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratedColumn;

import java.util.Date;

@Data
@Entity
@Table(name = "Users_Table")
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)  資料庫自動生成id
  @Column(name = "user_id", nullable = false, length = 50)
  private String userId;

  @Column(name = "username", nullable = false, length = 255)
  private String username;

  @Column(name = "password", nullable = true, length = 255)   // 允許為空
  private String password;

  @Column(name = "email", nullable = false, length = 255, unique = true)
  private String email;

  @Column(name = "phone", nullable = true, length = 100)   // 允許為空
  private String phone;

  @Column(name = "gender", length = 50)
  private String gender;

  @Column(name = "birth")
  @Temporal(TemporalType.DATE)
  private Date birth;

  @Column(name = "provider", nullable = false, length = 100)
  private String provider;  // GOOGLE, FACEBOOK 等

}