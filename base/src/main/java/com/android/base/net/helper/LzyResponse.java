
package com.android.base.net.helper;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LzyResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public int status_code;
    public String msg;
    public String message;



    public T result;
    public int status;
    public T data;

    @Override
    public String toString() {
        return "LzyResponse{\n" +//
               "\tcode=" + code + "\n" +//
               "\tmsg='" + msg + "\'\n" +//
               "\tdata=" + result + "\n" +//
               '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getState_code() {
        return status_code;
    }

    public void setState_code(int state_code) {
        this.status_code = state_code;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
