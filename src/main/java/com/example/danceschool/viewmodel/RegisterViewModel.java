package com.example.danceschool.viewmodel;

public class RegisterViewModel {
    private String errorMessage;
    private String successMessage;
    private boolean hasError;
    private boolean hasSuccess;

    public RegisterViewModel() {
    }

    public RegisterViewModel(String errorMessage, String successMessage) {
        this.errorMessage = errorMessage;
        this.successMessage = successMessage;
        this.hasError = errorMessage != null && !errorMessage.isEmpty();
        this.hasSuccess = successMessage != null && !successMessage.isEmpty();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.hasError = errorMessage != null && !errorMessage.isEmpty();
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
        this.hasSuccess = successMessage != null && !successMessage.isEmpty();
    }

    public boolean isHasError() {
        return hasError;
    }

    public boolean isHasSuccess() {
        return hasSuccess;
    }
}
