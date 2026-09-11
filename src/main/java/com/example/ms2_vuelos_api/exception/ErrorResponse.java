package com.example.ms2_vuelos_api.exception;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {

    private ErrorBody error;

    public ErrorResponse(String code, String message, int status, List<ErrorDetail> details) {
        this.error = new ErrorBody(code, message, status, details);
    }

    public ErrorBody getError() { return error; }

    public static class ErrorBody {
        private String code;
        private String message;
        private int status;
        private List<ErrorDetail> details;
        private String service = "ms2-vuelos-api";
        private String timestamp = Instant.now().toString();

        public ErrorBody(String code, String message, int status, List<ErrorDetail> details) {
            this.code = code;
            this.message = message;
            this.status = status;
            this.details = details;
        }

        public String getCode() { return code; }
        public String getMessage() { return message; }
        public int getStatus() { return status; }
        public List<ErrorDetail> getDetails() { return details; }
        public String getService() { return service; }
        public String getTimestamp() { return timestamp; }
    }

    public static class ErrorDetail {
        private String field;
        private String issue;

        public ErrorDetail(String field, String issue) {
            this.field = field;
            this.issue = issue;
        }

        public String getField() { return field; }
        public String getIssue() { return issue; }
    }
}
