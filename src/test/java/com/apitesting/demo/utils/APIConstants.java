package com.apitesting.demo.utils;

/**
 * API Constants and Endpoints
 */
public class APIConstants {
    
    // Base URL
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    
    // Endpoints
    public static final String POSTS_ENDPOINT = "/posts";
    public static final String COMMENTS_ENDPOINT = "/comments";
    public static final String USERS_ENDPOINT = "/users";
    public static final String ALBUMS_ENDPOINT = "/albums";
    public static final String PHOTOS_ENDPOINT = "/photos";
    public static final String TODOS_ENDPOINT = "/todos";
    
    // Request/Response Constants
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final int OK_STATUS = 200;
    public static final int CREATED_STATUS = 201;
    public static final int BAD_REQUEST_STATUS = 400;
    public static final int NOT_FOUND_STATUS = 404;
    
    // Response Time Constants
    public static final long MAX_RESPONSE_TIME = 5000L; // 5 seconds
}

