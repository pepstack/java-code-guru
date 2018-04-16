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
package com.pepstack.guru;

/**
 * JsonResult.java
 *
 */

import java.util.Date;
import java.util.Map;


public class JsonResult<T> {
    public final static int STATUS_SUCCESS = 0;
    public final static String ERROR_SUCCESS = "SUCCESS";

    public final static int STATUS_ERROR = -1;
    public final static int STATUS_NOTIMPL = -2;
    public final static int STATUS_INVALIDARG = -3;
    public final static int STATUS_UNEXPECTED = -4;
    
    public final static String ERROR_ERROR = "ERROR";
    public final static String ERROR_NOTIMPL = "E_NOTIMPL";
    public final static String ERROR_INVALIDARG = "E_INVALIDARG";
    public final static String ERROR_UNEXPECTED = "E_UNEXPECTED";


    // 毫秒为单位的时间戳
    private final Long timestamp = (new Date()).getTime();

    private final String path;

    private int status = STATUS_SUCCESS;

    private String error = ERROR_SUCCESS;

    private String message = "";

    private T result = null;


    public JsonResult(String path) {
        this.path = path;
    }


    public static String mapGetValueString(Map<String, Object> map, String key, String defValueIfNull) {
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
    }


    public String getPath() {
        return path;
    }


    public Long getTimestamp() {
        return timestamp;
    }


    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }


    public JsonResult<T> setStatusError(int status, String error) {
        setStatus(status);
        setError(error);
        return this;
    }


    public JsonResult<T> setResultStatus(T result, int statusIfNull, String errorIfNull) {
        setResult(result);

        if (this.result == null) {
            setStatusError(statusIfNull, errorIfNull);
        }

        return this;
    }
}
