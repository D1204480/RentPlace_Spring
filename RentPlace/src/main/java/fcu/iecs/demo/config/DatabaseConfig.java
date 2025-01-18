package fcu.iecs.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


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

    // 打印出 URL 以進行調試（注意不要在生產環境打印敏感信息）
    System.out.println("Database URL: " + url);

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

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan("fcu.iecs.demo.model");  // 你的實體類包名

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", "update");
    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }
}