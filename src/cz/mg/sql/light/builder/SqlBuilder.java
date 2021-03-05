package cz.mg.sql.light.builder;

import cz.mg.sql.light.builder.row.SqlCreateRowBuilder;
import cz.mg.sql.light.builder.row.SqlDeleteRowBuilder;
import cz.mg.sql.light.builder.row.SqlReadRowBuilder;
import cz.mg.sql.light.builder.row.SqlUpdateRowBuilder;
import cz.mg.sql.light.builder.table.SqlCreateTableBuilder;
import cz.mg.sql.light.builder.table.SqlDeleteTableBuilder;
import cz.mg.sql.light.builder.table.SqlReadTableBuilder;
import cz.mg.sql.light.builder.table.SqlUpdateTableBuilder;


public class SqlBuilder {
    public SqlBuilder() {
    }

    public SqlCreateRowBuilder createRow(String tableName){
        return new SqlCreateRowBuilder(tableName);
    }

    public SqlReadRowBuilder readRow(String tableName){
        return new SqlReadRowBuilder(tableName);
    }

    public SqlUpdateRowBuilder updateRow(String tableName){
        return new SqlUpdateRowBuilder(tableName);
    }

    public SqlDeleteRowBuilder deleteRow(String tableName){
        return new SqlDeleteRowBuilder(tableName);
    }

    public SqlCreateTableBuilder createTable(String tableName){
        return new SqlCreateTableBuilder(tableName);
    }

    public SqlReadTableBuilder readTable(String tableName){
        return new SqlReadTableBuilder(tableName);
    }

    public SqlUpdateTableBuilder updateTable(String tableName){
        return new SqlUpdateTableBuilder(tableName);
    }

    public SqlDeleteTableBuilder deleteTable(String tableName){
        return new SqlDeleteTableBuilder(tableName);
    }
}
