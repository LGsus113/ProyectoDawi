package com.cibertec.edu.Proyecto_DAWI.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HikariCpConfig {
    @Value("${DB_USE_URL}")
    private String dbUseUrl;

    @Value("${DB_USE_USER}")
    private String dbUseUser;

    @Value("${DB_USE_PASS}")
    private String dbUsePass;

    @Value("${DB_USE_DRIVER}")
    private String dbUseDriver;

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();

        String finalDbUrl = dbUseUrl.replace("{DB_NAME}", "ProyectoSpring");

        /**
         * Configurar propiedad de conexion a BD
         */
        config.setJdbcUrl(finalDbUrl);
        config.setUsername(dbUseUser);
        config.setPassword(dbUsePass);
        config.setDriverClassName(dbUseDriver);

        /**
         * Configurar propiedades del pool de HikariCP
         * - MaximumPoolSize: Máximo # de conexiones del pool.
         * - MinimumIdle: Mínimo # de conexiones inactivas del pool.
         * - IdleTimeout: Tiempo máximo de espera para
         *      eliminar una conexión inactiva.
         * - ConnectionTimeout: Tiempo máximo de espera
         *      para conectarse a la BD.
         */
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(30000);

        System.out.println("###### HikariCP initialized ######");
        return new HikariDataSource(config);
    }
}
