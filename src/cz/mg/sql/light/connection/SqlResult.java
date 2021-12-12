package cz.mg.sql.light.connection;

import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.storage.Part;
import cz.mg.annotations.storage.Shared;
import cz.mg.annotations.storage.Value;
import cz.mg.collections.map.Map;


public class SqlResult {
    private final @Mandatory @Part Map<@Value String, @Shared Object> nameMap;
    private final @Mandatory @Part Map<@Value Integer, @Shared Object> indexMap;

    public SqlResult(@Mandatory Map<String, Object> nameMap, @Mandatory Map<Integer, Object> indexMap) {
        this.nameMap = nameMap;
        this.indexMap = indexMap;
    }

    public @Optional Object get(String name){
        return nameMap.get(name);
    }

    public @Optional Object get(int i){
        return indexMap.get(i);
    }

    public @Optional Integer getInteger(String name){
        return (Integer) nameMap.get(name);
    }

    public @Optional Integer getInteger(int i){
        return (Integer) indexMap.get(i);
    }

    public @Optional Long getLong(String name){
        return (Long) nameMap.get(name);
    }

    public @Optional Long getLong(int i){
        return (Long) indexMap.get(i);
    }

    public @Optional Float getFloat(String name){
        return (Float) nameMap.get(name);
    }

    public @Optional Float getFloat(int i){
        return (Float) indexMap.get(i);
    }

    public @Optional Double getDouble(String name){
        return (Double) nameMap.get(name);
    }

    public @Optional Double getDouble(int i){
        return (Double) indexMap.get(i);
    }

    public @Optional String getString(String name){
        return (String) nameMap.get(name);
    }

    public @Optional String getString(int i){
        return (String) indexMap.get(i);
    }

    public @Optional Boolean getBoolean(String name){
        return (Boolean) nameMap.get(name);
    }

    public @Optional Boolean getBoolean(int i){
        return (Boolean) indexMap.get(i);
    }

    public @Mandatory Object[] get(){
        Object[] objects = new Object[indexMap.keys().count()];
        for(int i = 0; i < objects.length; i++) objects[i] = indexMap.get(i);
        return objects;
    }
}
