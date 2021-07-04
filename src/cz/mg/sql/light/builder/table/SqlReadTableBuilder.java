package cz.mg.sql.light.builder.table;

import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.SqlBind;
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
        String text = "SELECT count(*) FROM sqlite_master WHERE type = ? AND name = ?";
        List<SqlBind> binds = new List<>();
        binds.addLast(new SqlBind("type", "table"));
        binds.addLast(new SqlBind("name", tableName));
        return new Sql(text, binds);
    }
}
