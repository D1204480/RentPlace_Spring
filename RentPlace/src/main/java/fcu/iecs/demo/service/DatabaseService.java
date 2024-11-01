package fcu.iecs.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DatabaseService {

  @Value("jdbc:mariadb://140.134.25.66:3306/D1204480_rent_place")
  private String datasourceUrl;

  @Value("root")
  private String username;

  @Value("pb2024")
  private String password;

  public Connection connect() throws SQLException {
    return DriverManager.getConnection(datasourceUrl, username, password);
  }
}
