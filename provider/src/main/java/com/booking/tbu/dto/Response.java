package com.booking.tbu.dto;

import java.math.BigDecimal;
import java.util.List;

public class Response {
    private Response() {}

    public static class Success {
        private String request_string;
        private String request_number;
        private InnerClass request_object;
        private List<BigDecimal> request_array;

        public String getRequest_string() {
            return request_string;
        }

        public void setRequest_string(String request_string) {
            this.request_string = request_string;
        }

        public String getRequest_number() {
            return request_number;
        }

        public void setRequest_number(String request_number) {
            this.request_number = request_number;
        }

        public InnerClass getRequest_object() {
            return request_object;
        }

        public void setRequest_object(InnerClass request_object) {
            this.request_object = request_object;
        }

        public List<BigDecimal> getRequest_array() {
            return request_array;
        }

        public void setRequest_array(List<BigDecimal> request_array) {
            this.request_array = request_array;
        }

        public static class InnerClass {
            private Boolean request_boolean;

            public Boolean getRequest_boolean() {
                return request_boolean;
            }

            public void setRequest_boolean(Boolean request_boolean) {
                this.request_boolean = request_boolean;
            }
        }
    }

    public static class Failure {
        private ErrorCode error_code;
        private String error_message;

        public ErrorCode getError_code() {
            return error_code;
        }

        public void setError_code(ErrorCode error_code) {
            this.error_code = error_code;
        }

        public String getError_message() {
            return error_message;
        }

        public void setError_message(String error_message) {
            this.error_message = error_message;
        }

        public enum ErrorCode {
            VALIDATION,
            UNKNOWN
        }
    }

}
