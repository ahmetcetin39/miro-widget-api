package com.miro.exception;

/**
 * This is a custom runtime exception which is thrown when requested widget can't be find.
 *
 * @author ahmetcetin
 */
public class WidgetNotFoundException extends RuntimeException {
    public WidgetNotFoundException(String message) {
        super(message);
    }
}
