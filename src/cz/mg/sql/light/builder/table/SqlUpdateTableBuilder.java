package cz.mg.sql.light.builder.table;

import cz.mg.sql.light.Sql;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlUpdateTableBuilder implements SqlBuilderInterface {
    private final String tableName;

    public SqlUpdateTableBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    @Override
    public Sql build() {
        throw new UnsupportedOperationException();
    }
}
