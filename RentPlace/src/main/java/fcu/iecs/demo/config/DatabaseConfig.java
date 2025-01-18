package fcu.iecs.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
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

    // 使用 @Value 注入的值
    dataSource.setJdbcUrl(url);  // 使用 setJdbcUrl 而不是 setUrl
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

    // 設置連接池屬性
    dataSource.setMaximumPoolSize(5);
    dataSource.setMinimumIdle(2);
    dataSource.setConnectionTimeout(30000);

    return dataSource;
  }
}