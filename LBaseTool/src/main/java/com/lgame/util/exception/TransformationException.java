/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.exception;

/**
 *
 * @author leroy_boy
 */
public class TransformationException extends RuntimeException {

    private boolean isShowException = true;
    private static final long serialVersionUID = 5216161678584333499L;

    public TransformationException() {
    }

    public TransformationException(String message,boolean  isShowException) {
        super(message);
        this.isShowException = isShowException;
    }

    public boolean isShowException() {
        return isShowException;
    }

    public TransformationException(String message) {
        super(message);
    }

    public TransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformationException(Throwable cause) {
        super(cause);
    }

}
