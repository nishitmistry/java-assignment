package com.shopping_app.Shared.Primatives;
public class Result {
    protected Result(Boolean isSuccess, Error error)
    {
        if (isSuccess && error != null)
        {
            throw new IllegalStateException();
        }

        if (!isSuccess && error == null)
        {
            throw new IllegalStateException();
        }

        IsSuccess = isSuccess;
        IsFailure =!isSuccess;
        Error = error;
    }
    private Boolean IsSuccess;
    private Boolean IsFailure;
    private Error Error;
    
    public Boolean getIsSuccess() {
        return IsSuccess;
    }
    public Error getError() {
        return Error;
    }
    public Boolean getIsFailure() {
        return IsFailure;
    }

    public static Result Success(){
        return new Result(true, null);
    } 

    public static Result Failure(Error error){
        return new Result(false, error);
    } 

}
