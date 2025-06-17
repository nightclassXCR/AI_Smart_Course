package com.dd.ai_smart_course.entity;



public class Result<T> {
    private int code;
    private String message;
    private T Data;

    public Result() {}
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        Data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    // 成功：默认信息
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 成功：自定义信息
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // 失败：默认状态码
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    // 失败：自定义状态码和信息
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
