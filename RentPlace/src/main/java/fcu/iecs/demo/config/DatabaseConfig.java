package fcu.iecs.demo.config;

import com.google.api.client.util.Value;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseConfig {

  @Value("${MYSQL_URL}")  // 直接使用環境變量
  private String jdbcUrl;

  @Value("${MYSQLUSER}")
  private String username;

  @Value("${MYSQLPASSWORD}")
  private String password;

  @Bean
  public DataSource dataSource() {
    HikariDataSource ds = new HikariDataSource();

    // 確保 URL 是正確的 JDBC 格式
    if (!jdbcUrl.startsWith("jdbc:")) {
      jdbcUrl = "jdbc:" + jdbcUrl;
    }

    ds.setJdbcUrl(jdbcUrl);
    ds.setUsername(username);
    ds.setPassword(password);
    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

    // 額外的連接屬性
    ds.addDataSourceProperty("allowPublicKeyRetrieval", "true");
    ds.addDataSourceProperty("useSSL", "false");
    ds.setMaximumPoolSize(5);
    ds.setConnectionTimeout(30000);

    System.out.println("Database URL: " + jdbcUrl); // 用於調試

    return ds;
  }
}
