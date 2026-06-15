package com.apitesting.demo.models;

/**
 * MockHttpResponse Model - Represents a mocked HTTP response for network mocking tests
 * Used in conjunction with WireMock to simulate API responses
 */
public class MockHttpResponse {
    private int statusCode;
    private String contentType;
    private String responseBody;
    private String endpoint;
    private String requestMethod;

    // Constructors
    public MockHttpResponse() {
    }

    public MockHttpResponse(int statusCode, String contentType, String responseBody, String endpoint, String requestMethod) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.responseBody = responseBody;
        this.endpoint = endpoint;
        this.requestMethod = requestMethod;
    }

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return "MockHttpResponse{" +
                "statusCode=" + statusCode +
                ", contentType='" + contentType + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                '}';
    }
}

