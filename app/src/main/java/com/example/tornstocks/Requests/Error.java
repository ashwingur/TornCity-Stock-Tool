package com.example.tornstocks.Requests;

import com.google.gson.annotations.SerializedName;

public class Error {

    private int code;
    private String warning;

    public Error(int code, String warning) {
        this.code = code;
        this.warning = warning;
    }

    public int getCode() { return code; }

    public String getWarning() { return warning; }

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", warning='" + warning + '\'' +
                '}';
    }
}
