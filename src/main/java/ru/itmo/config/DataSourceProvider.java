package ru.itmo.config;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {
    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            PGSimpleDataSource ds = new PGSimpleDataSource();

            ds.setUrl("jdbc:postgresql://localhost:5432/studs");
            ds.setUser("");
            ds.setPassword("");

            dataSource = ds;
        }

        return dataSource;
    }
}
