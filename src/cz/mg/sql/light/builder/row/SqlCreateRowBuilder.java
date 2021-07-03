package cz.mg.sql.light.builder.row;

import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlBind;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlCreateRowBuilder implements SqlBuilderInterface {
    private final String tableName;
    private final List<SqlBind> binds = new List<>();
    private final List<String> columns = new List<>();

    public SqlCreateRowBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    public SqlCreateRowBuilder column(String column){
        validateName(column);
        columns.addLast(column);
        binds.addLast(new SqlBind(column));
        return this;
    }

    public SqlCreateRowBuilder column(String column, Object bind){
        validateName(column);
        columns.addLast(column);
        binds.addLast(new SqlBind(column, bind));
        return this;
    }

    public SqlCreateRowBuilder columns(String[] columns){
        for(String column : columns){
            column(column);
        }
        return this;
    }

    public SqlCreateRowBuilder columns(String[] columns, Object[] binds){
        for(int i = 0; i < columns.length; i++){
            column(columns[i], binds[i]);
        }
        return this;
    }

    @Override
    public Sql build() {
        String columnsText = columns.toText().delim(", ").build().toString();
        String bindsText = columns.toText().delim(", ").convert(column -> "?").build().toString();
        String text = "INSERT INTO " + tableName + " ( " + columnsText + " ) " + " VALUES ( " + bindsText + " )";
        return new Sql(text, binds);
    }
}
