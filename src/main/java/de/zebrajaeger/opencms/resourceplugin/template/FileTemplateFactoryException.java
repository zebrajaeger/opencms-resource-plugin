package de.zebrajaeger.opencms.resourceplugin.template;

/**
 * Created by lars on 12.02.2017.
 */
public class FileTemplateFactoryException extends Exception {
    public FileTemplateFactoryException() {
    }

    public FileTemplateFactoryException(String message) {
        super(message);
    }

    public FileTemplateFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileTemplateFactoryException(Throwable cause) {
        super(cause);
    }

    public FileTemplateFactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
