package com.apitesting.demo.utils;

/**
 * API Constants and Endpoints
 */
public class APIConstants {

    // Base URL
    public static final String BASE_URL = ConfigReader.getString("base.url");

    // Mock server
    public static final String MOCK_HOST = ConfigReader.getString("mock.host");
    public static final int MOCK_PORT = ConfigReader.getInt("mock.port");
    public static final String MOCK_BASE_URL = ConfigReader.getString("mock.base.url");

    // Endpoints
    public static final String POSTS_ENDPOINT = ConfigReader.getString("endpoint.posts");
    public static final String COMMENTS_ENDPOINT = ConfigReader.getString("endpoint.comments");
    public static final String USERS_ENDPOINT = ConfigReader.getString("endpoint.users");
    public static final String ALBUMS_ENDPOINT = ConfigReader.getString("endpoint.albums");
    public static final String PHOTOS_ENDPOINT = ConfigReader.getString("endpoint.photos");
    public static final String TODOS_ENDPOINT = ConfigReader.getString("endpoint.todos");

    // Mock endpoints
    public static final String MOCK_USERS_ENDPOINT = ConfigReader.getString("mock.endpoint.users");
    public static final String MOCK_POSTS_ENDPOINT = ConfigReader.getString("mock.endpoint.posts");

    // Request/Response Constants
    public static final String CONTENT_TYPE_JSON = ConfigReader.getString("content.type.json");
    public static final int OK_STATUS = ConfigReader.getInt("status.ok");
    public static final int CREATED_STATUS = ConfigReader.getInt("status.created");
    public static final int BAD_REQUEST_STATUS = ConfigReader.getInt("status.bad.request");
    public static final int NOT_FOUND_STATUS = ConfigReader.getInt("status.not.found");

    // Response Time Constants
    public static final long MAX_RESPONSE_TIME = ConfigReader.getLong("response.max.time.ms");
}

