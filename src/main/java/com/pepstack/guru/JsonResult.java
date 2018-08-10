/***********************************************************************
* Copyright (c) 2018 pepstack, pepstack.com
*
* This software is provided 'as-is', without any express or implied
* warranty.  In no event will the authors be held liable for any damages
* arising from the use of this software.
* Permission is granted to anyone to use this software for any purpose,
* including commercial applications, and to alter it and redistribute it
* freely, subject to the following restrictions:
*
* 1. The origin of this software must not be misrepresented; you must not
*   claim that you wrote the original software. If you use this software
*   in a product, an acknowledgment in the product documentation would be
*   appreciated but is not required.
*
* 2. Altered source versions must be plainly marked as such, and must not be
*   misrepresented as being the original software.
*
* 3. This notice may not be removed or altered from any source distribution.
***********************************************************************/
/**
 * @file: JsonResult.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-08-01 18:08:33
 */
package com.pepstack.guru;

import java.util.Date;
import java.util.Map;


public class JsonResult<T> {
    public final static int RESULT_STATUS_SUCCESS = 0;
    public final static int RESULT_STATUS_ERROR = -1;

    public final static String RESULT_STATUS_SUCCESS_MSG = "SUCCESS";
    public final static String RESULT_STATUS_ERROR_MSG = "ERROR";


    // RESTful 请求路径
    private final String path;
    public String getPath() {
        return path;
    }


    // 毫秒为单位的时间戳
    private final long timestamp = (new Date()).getTime();
    public long getTimestamp() {
        return timestamp;
    }


    public JsonResult(String path) {
        this.path = path;
    }


    // 状态码: 0 成功
    private int status = RESULT_STATUS_SUCCESS;
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    // 状态值
    private String error = RESULT_STATUS_SUCCESS_MSG;
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }


    private String message = "";
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


    private T result = null;
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }


    public JsonResult<T> updateResult(T result) {
        this.result = result;
        return this;
    }


    public JsonResult<T> updateStatusError(int status, String error) {
        this.setStatus(status);
        this.setError(error);
        return this;
    }


    public JsonResult<T> updateStatusError(int status, String error, String message) {
        this.setStatus(status);
        this.setError(error);
        this.setMessage(message);
        return this;
    }


    // 从 map 中取得值, 不抛出异常
    public static String mapGetValue(Map<String, Object> map, String key, String defValueIfNull) {
        try {
            if (map == null) {
                return defValueIfNull;
            }

            if (map.containsKey(key)) {
                Object value = map.get(key);

                if (value == null) {
                    return defValueIfNull;
                } else {
                    return value.toString();
                }
            } else {
                return defValueIfNull;
            }
        } catch (Exception ex) {
        }

        return defValueIfNull;
    }
}
