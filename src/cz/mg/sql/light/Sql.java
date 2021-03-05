package cz.mg.sql.light;

import cz.mg.collections.ReadableCollection;
import cz.mg.collections.array.ReadonlyArray;
import cz.mg.collections.list.List;

import static cz.mg.sql.light.SqlValidator.validateBinds;


public class Sql implements SqlTemplate {
    private final String text;
    private final ReadonlyArray<SqlBind> binds;

    public Sql(String text){
        this(text, new List<>());
    }

    public Sql(String text, ReadableCollection<SqlBind> binds) {
        validateBinds(text, binds);
        this.text = text;
        this.binds = new ReadonlyArray<>(binds);
    }

    public String getText() {
        return text;
    }

    public ReadonlyArray<SqlBind> getBinds() {
        return binds;
    }

    public void setBinds(String id, Object object){
        for(SqlBind bind : binds){
            if(bind.getId().equals(id)){
                bind.setObject(object);
            }
        }
    }

    @Override
    public Sql copy() {
        List<SqlBind> newBinds = new List<>();
        for(SqlBind bind : binds) newBinds.addLast(new SqlBind(bind.getId(), bind.getObject()));
        return new Sql(text, newBinds);
    }
}
