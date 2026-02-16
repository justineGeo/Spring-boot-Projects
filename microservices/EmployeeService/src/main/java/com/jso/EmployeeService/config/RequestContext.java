package com.jso.EmployeeService.config;

public final class RequestContext {
    private static final ThreadLocal<String> CLIENT_IP = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    private RequestContext() {}

    public static void setContext(String userId, String ip) {
        USER_ID.set(userId);
        CLIENT_IP.set(ip);
    }

    public static String getUserId() { return USER_ID.get(); }
    public static String getClientIp() { return CLIENT_IP.get(); }
    public static void clear() {
        USER_ID.remove();
        CLIENT_IP.remove();
    }
}