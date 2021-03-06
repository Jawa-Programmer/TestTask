package ru.jawaprog.test_task.web.rest.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorInfo {
    private int errorCode;
    private String errorMessage;

    public ErrorInfo(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Error{code=" + errorCode + "; message=\"" + errorMessage + "\";}";
    }
}
