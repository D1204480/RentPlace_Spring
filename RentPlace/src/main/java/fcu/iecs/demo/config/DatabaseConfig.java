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

    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

    // 基本設定
    dataSource.setMaximumPoolSize(5);
    dataSource.setMinimumIdle(2);
    dataSource.setIdleTimeout(300000);
    dataSource.setConnectionTimeout(20000);
    dataSource.setValidationTimeout(5000);

    // 連接測試
    dataSource.setConnectionTestQuery("SELECT 1");

    // 重要：設置重試
    dataSource.setInitializationFailTimeout(30000);

    // 額外的連接屬性
    dataSource.addDataSourceProperty("allowPublicKeyRetrieval", "true");
    dataSource.addDataSourceProperty("useSSL", "false");
    dataSource.addDataSourceProperty("serverTimezone", "UTC");
    dataSource.addDataSourceProperty("connectTimeout", "20000");
    dataSource.addDataSourceProperty("socketTimeout", "30000");

    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("fcu.iecs.demo.model");  // 你的實體類包名

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.setShowSql(true);
    em.setJpaVendorAdapter(vendorAdapter);

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
    properties.put("hibernate.hbm2ddl.auto", "update");
    properties.put("hibernate.show_sql", "true");
    properties.put("hibernate.format_sql", "true");
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }
}