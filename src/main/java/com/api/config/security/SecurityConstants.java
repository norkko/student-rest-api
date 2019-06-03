package com.api.config.security;

/**
 * Class containing constants used in the security configuration.
 */
public class SecurityConstants {
    public static String SECRET = "secre";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 864_000_000L; // 1 day
}