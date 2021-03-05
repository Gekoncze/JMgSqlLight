package cz.mg.sql.light.builder.row;

import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlBind;
import cz.mg.sql.light.builder.ColumnConverter;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlDeleteRowBuilder implements SqlBuilderInterface {
    private final String tableName;
    private final List<SqlBind> binds = new List<>();
    private final List<String> conditions = new List<>();

    public SqlDeleteRowBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    public SqlDeleteRowBuilder condition(String column){
        validateName(column);
        conditions.addLast(column);
        binds.addLast(new SqlBind(column));
        return this;
    }

    public SqlDeleteRowBuilder conditions(String[] columns){
        for(String column : columns){
            condition(column);
        }
        return this;
    }

    public <T> SqlDeleteRowBuilder conditions(T[] columns, ColumnConverter<T> converter){
        for(T column : columns){
            condition(converter.convert(column));
        }
        return this;
    }

    @Override
    public Sql build() {
        String conditionsText = conditions.toText().delim(" AND ").build().toString();
        String text = "DELETE " + tableName + " WHERE " + conditionsText;
        return new Sql(text, binds);
    }
}