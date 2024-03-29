package com.shopping_app.Shared.Primatives;

public class Error {
    private String ErrorCode;
    private String Message;
    
    public Error(String ErrorCode,String Message) {
        this.ErrorCode= ErrorCode;
        this.Message= Message;
    }

    public String getErrorCode() {
        return ErrorCode;
    }
    public String getMessage() {
        return Message;
    }
    
    public static Error None  = new Error("", "");
    public static Error NullValue = new Error("Error.NullValue", "The specified result value is null.");

}
