package de.zebrajaeger.opencms.resourceplugin.namingstrategy;

@SuppressWarnings("unused")
public class PassThroughNamingStrategy extends AbstractNamingStrategy {
    public PassThroughNamingStrategy(String newResourceName) {
        super(newResourceName);
    }

    @Override
    protected String convert(String newResourceName) {
        return newResourceName;
    }
}
