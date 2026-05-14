package org.example.utils;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Database {

    public Connection getH2DBConnection() {
        String projectDir = System.getProperty("user.dir");
        String dbPath = Paths.get(projectDir, "src", "main", "resources", "sample-database.db").toString();
        String url = "jdbc:h2:file:" + dbPath + ";AUTO_SERVER=TRUE";

        try {
            return DriverManager.getConnection(url, "", "");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to H2 database: " + dbPath, e);
        }
    }

    public List<Map<String, Object>> executeSqlScript(String sql) {
        try (
                Connection connection = getH2DBConnection();
                Statement statement = connection.createStatement()
        ) {
            boolean hasResultSet = statement.execute(sql);

            if (hasResultSet) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    return getResultSetData(resultSet);
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("Rows affected", statement.getUpdateCount());
            return List.of(result);
        } catch (Exception e) {
            throw new RuntimeException("Cannot execute SQL", e);
        }
    }

    private List<Map<String, Object>> getResultSetData(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Map<String, Object>> data = new ArrayList<>();

        while (resultSet.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            data.add(row);
        }

        return data;
    }
}
