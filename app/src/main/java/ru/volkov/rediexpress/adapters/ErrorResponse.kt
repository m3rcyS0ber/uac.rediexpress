package ru.volkov.rediexpress.adapters

public class ErrorResponse {
    private var code: Int = 0;
    private var message: String = "";

    fun getCode(): Int {
        return code;
    }

    fun setCode(code: Int) {
        this.code = code;
    }

    fun getMessage(): String {
        return message;
    }

    fun setMessage(message: String) {
        this.message = message;
    }
}