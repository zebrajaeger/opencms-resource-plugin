package de.zebrajaeger.opencms.resourceplugin;

/**
 * Created by lars on 12.02.2017.
 */
public class ResourceCreatorException extends Exception {
    public ResourceCreatorException() {
    }

    public ResourceCreatorException(String message) {
        super(message);
    }

    public ResourceCreatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceCreatorException(Throwable cause) {
        super(cause);
    }

    public ResourceCreatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
