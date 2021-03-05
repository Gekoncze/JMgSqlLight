package cz.mg.sql.light.builder;

public interface ColumnConverter<T> {
    public String convert(T column);
}