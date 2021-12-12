package cz.mg.sql.light.builder.table;

import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.storage.Part;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlDeleteTableBuilder implements SqlBuilderInterface {
    private final @Optional @Part String tableName;

    public SqlDeleteTableBuilder(@Mandatory String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    @Override
    public @Mandatory Sql build() {
        String text = "DROP TABLE " + tableName;
        return new Sql(text);
    }
}
