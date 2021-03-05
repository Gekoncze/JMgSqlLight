package cz.mg.sql.light.builder.row;

import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlBind;
import cz.mg.sql.light.builder.ColumnConverter;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlReadRowBuilder implements SqlBuilderInterface {
    private final String tableName;
    private final List<SqlBind> binds = new List<>();
    private final List<String> columns = new List<>();
    private final List<String> conditions = new List<>();

    public SqlReadRowBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    public SqlReadRowBuilder column(String column){
        validateName(column);
        columns.addLast(column);
        return this;
    }

    public SqlReadRowBuilder columns(String[] columns){
        for(String column : columns){
            column(column);
        }
        return this;
    }

    public <T> SqlReadRowBuilder columns(T[] columns, ColumnConverter<T> converter){
        for(T column : columns){
            column(converter.convert(column));
        }
        return this;
    }

    public SqlReadRowBuilder condition(String column){
        validateName(column);
        conditions.addLast(column);
        binds.addLast(new SqlBind(column));
        return this;
    }

    public SqlReadRowBuilder conditions(String[] columns){
        for(String column : columns){
            condition(column);
        }
        return this;
    }

    public <T> SqlReadRowBuilder conditions(T[] columns, ColumnConverter<T> converter){
        for(T column : columns){
            condition(converter.convert(column));
        }
        return this;
    }

    @Override
    public Sql build() {
        String columnsText = columns.toText().delim(", ").build().toString();
        String conditionsText = conditions.toText().delim(" AND ").convert(condition -> condition + " = ?").build().toString();
        String text = "SELECT " + columnsText + " FROM " + tableName + " WHERE " + conditionsText;
        return new Sql(text, binds);
    }
}
