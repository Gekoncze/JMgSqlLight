package cz.mg.sql.light.builder;

public interface TableColumnConverter<T> {
    public String[] convert(T column);
}