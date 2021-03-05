package cz.mg.sql.light.builder.table;

import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.builder.SqlBuilderInterface;
import cz.mg.sql.light.builder.TableColumnConverter;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlCreateTableBuilder implements SqlBuilderInterface {
    private final String tableName;
    private final List<String> columns = new List<>();

    public SqlCreateTableBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    public SqlCreateTableBuilder column(String column, String datatype){
        validateName(column);
        validateName(datatype);
        columns.addLast(column + " " + datatype);
        return this;
    }

    public <T> SqlCreateTableBuilder columns(T[] columns, TableColumnConverter<T> converter){
        for(T column : columns){
            String[] variable = converter.convert(column);
            column(variable[0], variable[1]);
        }
        return this;
    }

    @Override
    public Sql build() {
        String columnsText = columns.toText().delim(", ").build().toString();
        String text = "CREATE TABLE " + tableName + " ( " + columnsText + " ) ";
        return new Sql(text);
    }
}
