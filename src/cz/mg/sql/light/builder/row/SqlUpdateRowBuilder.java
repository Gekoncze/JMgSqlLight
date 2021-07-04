package cz.mg.sql.light.builder.row;

import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlBind;
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

    public SqlUpdateRowBuilder column(String column, Object bind){
        validateName(column);
        columns.addLast(column);
        binds.addLast(new SqlBind(column, bind));
        return this;
    }

    public SqlUpdateRowBuilder columns(String[] columns){
        for(String column : columns){
            column(column);
        }
        return this;
    }

    public SqlUpdateRowBuilder columns(String[] columns, Object[] binds){
        for(int i = 0; i < columns.length; i++){
            column(columns[i], binds[i]);
        }
        return this;
    }

    public SqlUpdateRowBuilder condition(String column){
        validateName(column);
        conditions.addLast(column);
        binds.addLast(new SqlBind(column));
        return this;
    }

    public SqlUpdateRowBuilder condition(String column, Object bind){
        validateName(column);
        conditions.addLast(column);
        binds.addLast(new SqlBind(column, bind));
        return this;
    }

    public SqlUpdateRowBuilder conditions(String[] columns){
        for(String column : columns){
            condition(column);
        }
        return this;
    }

    public SqlUpdateRowBuilder conditions(String[] columns, Object[] binds){
        for(int i = 0; i < columns.length; i++){
            condition(columns[i], binds[i]);
        }
        return this;
    }

    @Override
    public Sql build() {
        String columnsText = columns.toText().delim(", ").convert(column -> column + " = ?").build().toString();
        String text = "UPDATE " + tableName + " SET " + columnsText;
        if(!conditions.isEmpty()){
            String conditionsText = conditions.toText().delim(" AND ").convert(condition -> condition + " = ?").build().toString();
            text += " WHERE " + conditionsText;
        }
        return new Sql(text, binds);
    }
}
