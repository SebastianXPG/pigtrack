package com.pigtrack.pigtrack_api.infrastructure;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class TestConnectionRunner {

    private final DataSource dataSource;

    public TestConnectionRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Conexión a SQL Server exitosa: " + connection.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("❌ Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }
}
