package cz.mg.sql.light.connection.connections;

import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlBind;
import cz.mg.sql.light.connection.SqlConnection;
import cz.mg.sql.light.connection.SqlConnectionException;
import cz.mg.sql.light.connection.SqlResults;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class SqliteConnection implements SqlConnection {
    private final String file;
    private Connection connection;

    public SqliteConnection(String file) {
        this.file = file;
    }

    @Override
    public void close() {
        if(isConnected()) disconnect();
    }

    @Override
    public boolean isConnected(){
        if(connection == null) return false;
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private void connect(){
        String url = "jdbc:sqlite:" + file;
        try {
            this.connection = DriverManager.getConnection(url);
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SqlConnectionException(e);
        }
    }

    private void disconnect(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            // nothing to do
        } finally {
            this.connection = null;
        }
    }

    @Override
    public void begin(){
        if(!isConnected()) connect();
    }

    @Override
    public void commit(){
        assertConnected();
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new SqlConnectionException(e);
        }
    }

    @Override
    public void rollback(){
        if(!isConnected()) return;
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            // transaction should be rolled back automatically
        }
    }

    @Override
    public void executeDdl(Sql sql) {
        assertConnected();
        try (PreparedStatement statement = createStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new SqlConnectionException(e, createSqlDump(sql));
        }
    }

    @Override
    public void executeDml(Sql sql){
        assertConnected();
        try (PreparedStatement statement = createStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new SqlConnectionException(e, createSqlDump(sql));
        }
    }

    @Override
    public SqlResults executeQuery(Sql sql){
        assertConnected();
        try (PreparedStatement statement = createStatement(sql)) {
            return SqlResults.toResults(statement.executeQuery());
        } catch (SQLException e) {
            throw new SqlConnectionException(e, createSqlDump(sql));
        }
    }

    private PreparedStatement createStatement(Sql sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql.getText());
        int i = 1;
        for(SqlBind bind : sql.getBinds()){
            statement.setObject(i, bind.getObject());
            i++;
        }
        return statement;
    }

    private void assertConnected(){
        if(!isConnected()) throw new SqlConnectionException("Disconnected.");
    }

    private String createSqlDump(Sql sql){
        StringBuilder dump = new StringBuilder(sql.getText());
        dump.append("\n");
        for(SqlBind bind : sql.getBinds()){
            dump.append("-- " + SqlBind.class.getSimpleName() + "('" + bind.getId() + "', '" + bind.getObject() + "')\n");
        }
        return dump.toString();
    }
}
