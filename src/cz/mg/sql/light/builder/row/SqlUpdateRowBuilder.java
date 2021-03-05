package cz.mg.sql.light.builder.row;

import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlBind;
import cz.mg.sql.light.builder.ColumnConverter;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlUpdateRowBuilder implements SqlBuilderInterface {
    private final String tableName;
    private final List<SqlBind> binds = new List<>();
    private final List<String> columns = new List<>();
    private final List<String> conditions = new List<>();

    public SqlUpdateRowBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    public SqlUpdateRowBuilder column(String column){
        validateName(column);
        columns.addLast(column);
        binds.addLast(new SqlBind(column));
        return this;
    }

    public SqlUpdateRowBuilder columns(String[] columns){
        for(String column : columns){
            column(column);
        }
        return this;
    }

    public <T> SqlUpdateRowBuilder columns(T[] columns, ColumnConverter<T> converter){
        for(T column : columns){
            column(converter.convert(column));
        }
        return this;
    }

    public SqlUpdateRowBuilder condition(String column){
        validateName(column);
        conditions.addLast(column);
        binds.addLast(new SqlBind(column));
        return this;
    }

    public SqlUpdateRowBuilder conditions(String... columns){
        for(String column : columns){
            condition(column);
        }
        return this;
    }

    public <T> SqlUpdateRowBuilder conditions(T[] columns, ColumnConverter<T> converter){
        for(T column : columns){
            condition(converter.convert(column));
        }
        return this;
    }

    @Override
    public Sql build() {
        String columnsText = columns.toText().delim(", ").convert(column -> column + " = ?").build().toString();
        String conditionsText = conditions.toText().delim(" AND ").convert(condition -> condition + " = ?").build().toString();
        String text = "UPDATE " + tableName + " SET " + columnsText + " WHERE " + conditionsText;
        return new Sql(text, binds);
    }
}
