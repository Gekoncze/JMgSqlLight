package cz.mg.sql.light.connection;

import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class SqlResults {
    private final List<SqlResult> results;

    public SqlResults(List<SqlResult> results) {
        this.results = results;
    }

    public List<SqlResult> getResults() {
        return results;
    }

    public SqlResult getSingleResult(){
        if(results.count() != 1) throw new IllegalStateException("Expected 1 row, but got " + results.count() + " rows.");
        return results.getFirst();
    }

    public static SqlResults toResults(ResultSet resultSet) throws SQLException {
        List<SqlResult> results = new List<>();

        while(resultSet.next()){
            results.addLast(toResult(resultSet));
        }

        return new SqlResults(results);
    }

    private static SqlResult toResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        Map<String, Object> nameMap = new Map<>();
        Map<Integer, Object> indexMap = new Map<>();

        for(int c = 0; c < columnCount; c++){
            String columnName = metaData.getColumnName(c);
            Object value = resultSet.getObject(c);
            if(resultSet.wasNull()) value = null;
            nameMap.set(columnName, value);
            indexMap.set(c, value);
        }

        return new SqlResult(nameMap, indexMap);
    }
}
