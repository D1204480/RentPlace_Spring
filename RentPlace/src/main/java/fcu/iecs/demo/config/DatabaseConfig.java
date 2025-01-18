//package fcu.iecs.demo.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Configuration
//public class DatabaseConfig {
//
//  @Value("${spring.datasource.url}")
//  private String url;
//
//  @Value("${spring.datasource.username}")
//  private String username;
//
//  @Value("${spring.datasource.password}")
//  private String password;
//
//  @Bean
//  public DataSource dataSource() {
//    HikariDataSource dataSource = new HikariDataSource();
//
//    // 基本配置
//    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//    dataSource.setJdbcUrl(url);
//    dataSource.setUsername(username);
//    dataSource.setPassword(password);
//
//    // 連接池配置
//    dataSource.setMaximumPoolSize(10);
//    dataSource.setMinimumIdle(5);
//    dataSource.setIdleTimeout(300000);
//    dataSource.setConnectionTimeout(20000);
//    dataSource.setLeakDetectionThreshold(60000);
//
//    // 連接測試
//    dataSource.setConnectionTestQuery("SELECT 1");
//    dataSource.setValidationTimeout(5000);
//
//    // MySQL 特定配置
//    dataSource.addDataSourceProperty("cachePrepStmts", "true");
//    dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
//    dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//    dataSource.addDataSourceProperty("useServerPrepStmts", "true");
//    dataSource.addDataSourceProperty("useLocalSessionState", "true");
//    dataSource.addDataSourceProperty("rewriteBatchedStatements", "true");
//    dataSource.addDataSourceProperty("cacheResultSetMetadata", "true");
//    dataSource.addDataSourceProperty("cacheServerConfiguration", "true");
//    dataSource.addDataSourceProperty("elideSetAutoCommits", "true");
//    dataSource.addDataSourceProperty("maintainTimeStats", "false");
//
//    return dataSource;
//  }
//}