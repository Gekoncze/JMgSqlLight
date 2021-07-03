package cz.mg.sql.light.entities;

import cz.mg.collections.array.ReadonlyArray;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlTemplate;
import cz.mg.sql.light.builder.SqlBuilder;
import cz.mg.sql.light.connection.SqlConnection;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlTable {
    private final String name;
    private final ReadonlyArray<SqlColumn> identityColumns;
    private final ReadonlyArray<SqlColumn> valueColumns;
    private final SqlTemplate createTableSql;
    private final SqlTemplate readTableSql;
    private final SqlTemplate deleteTableSql;
    private final SqlTemplate createRowSql;
    private final SqlTemplate readRowSql;
    private final SqlTemplate updateRowSql;
    private final SqlTemplate deleteRowSql;
    private final SqlTemplate rowCountSql;

    public SqlTable(String name, SqlColumn[] identityColumns, SqlColumn[] valueColumns) {
        validateName(name);
        this.name = name;
        this.identityColumns = new ReadonlyArray<>(identityColumns);
        this.valueColumns = new ReadonlyArray<>(valueColumns);

        this.createTableSql = new SqlBuilder()
            .createTable(name)
            .columns(valueColumns, column -> new String[]{column.getName(), column.getType()})
            .build();

        this.readTableSql = new SqlBuilder()
            .readTable(name)
            .build();

        this.deleteTableSql = new SqlBuilder()
            .deleteTable(name)
            .build();

        this.createRowSql = new SqlBuilder()
            .createRow(name)
            .columns(identityColumns, SqlColumn::getName)
            .columns(valueColumns, SqlColumn::getName)
            .build();

        this.readRowSql = new SqlBuilder()
            .readRow(name)
            .columns(valueColumns, SqlColumn::getName)
            .conditions(identityColumns, SqlColumn::getName)
            .build();

        this.updateRowSql = new SqlBuilder()
            .updateRow(name)
            .columns(valueColumns, SqlColumn::getName)
            .conditions(identityColumns, SqlColumn::getName)
            .build();

        this.deleteRowSql = new SqlBuilder()
            .deleteRow(name)
            .conditions(identityColumns, SqlColumn::getName)
            .build();

        this.rowCountSql = new SqlBuilder()
            .readRow(name)
            .column("count(*)")
            .build();
    }

    public String getName() {
        return name;
    }

    public ReadonlyArray<SqlColumn> getIdentityColumns() {
        return identityColumns;
    }

    public ReadonlyArray<SqlColumn> getValueColumns() {
        return valueColumns;
    }

    public boolean exist(SqlConnection connection){
        return !connection.executeQuery(readTableSql.copy()).getResults().isEmpty();
    }

    public void create(SqlConnection connection){
        connection.executeDdl(createTableSql.copy());
        connection.commit();
    }

    public void delete(SqlConnection connection){
        connection.executeDdl(deleteTableSql.copy());
        connection.commit();
    }

    public void saveRow(SqlConnection connection, Object[] ids, Object[] values){
        checkIds(ids);
        checkValues(values);
        if(existsRow(connection, ids)){
            updateRow(connection, ids, values);
        } else {
            createRow(connection, ids, values);
        }
    }

    public void createRow(SqlConnection connection, Object[] ids, Object[] values){
        checkIds(ids);
        checkValues(values);
        Sql sql = createRowSql.copy();
        for(int i = 0; i < ids.length; i++){
            sql.setBinds(identityColumns.get(i).getName(), ids[i]);
        }
        for(int i = 0; i < values.length; i++){
            sql.setBinds(valueColumns.get(i).getName(), values[i]);
        }
        connection.executeDml(sql);
    }

    public boolean existsRow(SqlConnection connection, Object[] ids){
        checkIds(ids);
        Sql sql = readRowSql.copy();
        for(int i = 0; i < ids.length; i++){
            sql.setBinds(identityColumns.get(i).getName(), ids[i]);
        }
        return !connection.executeQuery(sql).getResults().isEmpty();
    }

    public Object[] readRow(SqlConnection connection, Object[] ids){
        checkIds(ids);
        Sql sql = readRowSql.copy();
        for(int i = 0; i < ids.length; i++){
            sql.setBinds(identityColumns.get(i).getName(), ids[i]);
        }
        return connection.executeQuery(sql).getSingleResult().get();
    }

    public void updateRow(SqlConnection connection, Object[] ids, Object[] values){
        checkIds(ids);
        checkValues(values);
        Sql sql = updateRowSql.copy();
        for(int i = 0; i < ids.length; i++){
            sql.setBinds(identityColumns.get(i).getName(), ids[i]);
        }
        for(int i = 0; i < values.length; i++){
            sql.setBinds(valueColumns.get(i).getName(), values[i]);
        }
        connection.executeDml(sql);
    }

    public void deleteRow(SqlConnection connection, Object[] ids){
        checkIds(ids);
        Sql sql = deleteRowSql.copy();
        for(int i = 0; i < ids.length; i++){
            sql.setBinds(identityColumns.get(i).getName(), ids[i]);
        }
        connection.executeDml(sql);
    }

    private void checkIds(Object[] ids){
        if(ids.length != identityColumns.count()){
            throw new IllegalArgumentException("Id count mismatch. Expected " + identityColumns.count() + ", got " + ids.length + ".");
        }
    }

    private void checkValues(Object[] values){
        if(values.length != valueColumns.count()){
            throw new IllegalArgumentException("Value count mismatch. Expected " + valueColumns.count() + ", got " + values.length + ".");
        }
    }

    public int rowCount(SqlConnection connection){
        Sql sql = rowCountSql.copy();
        return (int) connection.executeQuery(sql).getSingleResult().get(0);
    }
}
