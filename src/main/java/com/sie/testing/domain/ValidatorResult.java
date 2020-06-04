package com.sie.testing.domain;

public class ValidatorResult {

    private boolean valid;
    private String message;

    public ValidatorResult(){
        this.valid = true;
        this.message = null;
    }

    public ValidatorResult(boolean valid, String message){
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
