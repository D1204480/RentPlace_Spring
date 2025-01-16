package fcu.iecs.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	// 測試連線
		@Autowired
		private DataSource dataSource;

		@PostConstruct
		public void testConnection() {
			try (Connection conn = dataSource.getConnection()) {
				System.out.println("Database connection successful!");
				System.out.println("Connected to: " + conn.getMetaData().getURL());
			} catch (SQLException e) {
				System.err.println("Database connection failed!");
				e.printStackTrace();
			}
		}

}
