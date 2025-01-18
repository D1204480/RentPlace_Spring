package fcu.iecs.demo.config;

import com.google.api.client.util.Value;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public DataSource dataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    // 改用 setJdbcUrl 而不是 setUrl
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

    // 添加連接配置
    dataSource.addDataSourceProperty("allowPublicKeyRetrieval", "true");
    dataSource.addDataSourceProperty("useSSL", "false");

    return dataSource;
  }
}
