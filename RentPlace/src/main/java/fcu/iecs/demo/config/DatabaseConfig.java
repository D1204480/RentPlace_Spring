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
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setMaximumPoolSize(10);
    dataSource.setMinimumIdle(5);
    dataSource.setConnectionTimeout(30000);

    // 添加額外的連接屬性
    dataSource.addDataSourceProperty("useSSL", "false");
    dataSource.addDataSourceProperty("allowPublicKeyRetrieval", "true");
    dataSource.addDataSourceProperty("serverTimezone", "UTC");

    return dataSource;
  }
}
