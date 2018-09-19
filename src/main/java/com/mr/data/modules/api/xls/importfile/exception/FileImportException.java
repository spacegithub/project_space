package com.mr.data.modules.api.xls.importfile.exception;

/**
 * Created by stark.zhang on 2015/11/2.
 */
public class FileImportException extends Exception {

    public FileImportException(Throwable throwable, String message){
        super(message, throwable);
    }

    public FileImportException(String message) {
        super(message);
    }
}
