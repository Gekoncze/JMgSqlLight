package cz.mg.sql.light.builder.table;

import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.storage.Part;
import cz.mg.collections.list.List;
import cz.mg.sql.light.Sql;
import cz.mg.sql.light.builder.SqlBuilderInterface;

import static cz.mg.sql.light.SqlValidator.validateName;


public class SqlCreateTableBuilder implements SqlBuilderInterface {
    private final @Optional @Part String tableName;
    private final @Mandatory @Part List<String> columns = new List<>();

    public SqlCreateTableBuilder(@Mandatory String tableName) {
        validateName(tableName);
        this.tableName = tableName;
    }

    public SqlCreateTableBuilder column(@Mandatory String columnName, @Mandatory String datatype){
        validateName(columnName);
        validateName(datatype);
        columns.addLast(columnName + " " + datatype);
        return this;
    }

    @Override
    public @Mandatory Sql build() {
        String columnsText = columns.toText().delim(", ").build().toString();
        String text = "CREATE TABLE " + tableName + " ( " + columnsText + " ) ";
        return new Sql(text);
    }
}
