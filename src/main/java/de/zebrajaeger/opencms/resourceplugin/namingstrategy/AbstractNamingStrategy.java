package de.zebrajaeger.opencms.resourceplugin.namingstrategy;

public abstract class AbstractNamingStrategy implements NamingStrategy {
    private String name;
    private String convertedName;

    public AbstractNamingStrategy(String name) {
        this.name = name;
    }

    protected abstract String convert(String newResourceName);

    @Override
    public String getConvertedName() {
        if (convertedName == null) {
            convertedName = convert(name);
        }
        return convertedName;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + name + " -> " + getConvertedName() + ")";
    }
}
