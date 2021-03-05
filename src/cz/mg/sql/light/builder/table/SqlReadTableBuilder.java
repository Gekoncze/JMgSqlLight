package cz.mg.sql.light.builder.table;

import cz.mg.sql.light.Sql;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlReadTableBuilder implements SqlBuilderInterface {
    private final String tableName;

    public SqlReadTableBuilder(String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    @Override
    public Sql build() {
        String text = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = " + tableName;
        return new Sql(text);
    }
}
