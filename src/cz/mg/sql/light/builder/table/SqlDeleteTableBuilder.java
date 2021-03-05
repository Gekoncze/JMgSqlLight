package cz.mg.sql.light.builder.table;

import cz.mg.sql.light.Sql;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlDeleteTableBuilder implements SqlBuilderInterface {
    private final String tableName;

    public SqlDeleteTableBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    @Override
    public Sql build() {
        String text = "DROP TABLE " + tableName;
        return new Sql(text);
    }
}
