package com.example.danceschool.viewmodel;

public class LoginViewModel {
    private String errorMessage;
    private boolean hasError;

    public LoginViewModel() {
    }

    public LoginViewModel(String errorMessage) {
        this.errorMessage = errorMessage;
        this.hasError = errorMessage != null && !errorMessage.isEmpty();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.hasError = errorMessage != null && !errorMessage.isEmpty();
    }

    public boolean isHasError() {
        return hasError;
    }
}
