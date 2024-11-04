package fcu.iecs.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GeneratedColumn;

import java.util.Date;

@Data
@Entity
@Table(name = "Users_Table")
public class User {

  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)  資料庫自動生成id
  @Column(name = "user_id", nullable = false, length = 50)
  private String userId;

  @Column(name = "username", nullable = false, length = 255)
  private String username;

  @Column(name = "password", nullable = false, length = 255)
  private String password;

  @Column(name = "email", nullable = false, length = 255)
  private String email;

  @Column(name = "phone", nullable = false, length = 100)
  private String phone;

  @Column(name = "gender", length = 50)
  private String gender;

  @Column(name = "birth")
  @Temporal(TemporalType.DATE)
  private Date birth;

}