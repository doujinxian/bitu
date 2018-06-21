package com.bitm.model.vo;

import java.io.Serializable;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018\6\8
 */
public class ResultVo implements Serializable {

    private String code;
    private String errorMsg;
    private boolean success;
    private Object data;

    public static ResultVo failure(String errorMsg) {
        ResultVo vo = new ResultVo();
        vo.setSuccess(true);
        vo.setErrorMsg(errorMsg);
        return vo;
    }

    public static ResultVo success(Object data) {
        ResultVo vo = new ResultVo();
        vo.setSuccess(true);
        vo.setCode("200");
        vo.setData(data);
        return vo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
