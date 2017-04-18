/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.exception;

import com.lgame.util.PrintTool;

/**
 * @author lvxiaohui
 *
 */
public class AppException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AppException(String message, Object... error) {
        super(message);
        PrintTool.error(message);
    }
}
