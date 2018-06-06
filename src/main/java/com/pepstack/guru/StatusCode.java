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
 * @file: StatusCode.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-06-06 10:15:12
 */
package com.pepstack.guru;


/**
 * 常用的返回值
 */
public class StatusCode {
    // 成功
    public final static int STATUS_SUCCESS = 0;

    // 无错误
    public final static int STATUS_NOERROR = 0;


    // 一般错误
    public final static int STATUS_ERROR = -1;


    // 空值错误
    public final static int STATUS_ERROR_NULL = -2;


    // 数字签名错误
    public final static int STATUS_ERROR_SIGNATURE = -3;


    // 系统层错误
    public final static int STATUS_ERROR_SYSTEM = -4;


    // 数据库层错误
    public final static int STATUS_ERROR_DATABASE = -5;


    // 执行外部脚本错误
    public final static int STATUS_ERROR_SHELLEXEC = -6;


    // 错误的请求参数
    public final static int STATUS_ERROR_REQUEST = -7;


    // 异常错误。无法预知的错误！
    public final static int STATUS_ERROR_UNEXCEPTED = -8;


    // Java 异常错误。一般是编程错误！
    public final static int STATUS_JAVA_EXCEPTION = -9;


    // 应用程序错误。一般是编程错误！
    public final static int STATUS_APPLICATION_ERROR = -10;

    // 要求的参数未提供
    public final static int STATUS_ERROR_PARAM_MISSING = -11;

    // 参数错误
    public final static int STATUS_ERROR_PARAM_INVALID = -12;


    // 图片验证码错误
    public final static int STATUS_ERROR_KAPTCHACODE = -13;

    // TOKEN 错误
    public final static int STATUS_ERROR_TOKEN = -14;

    // SESSIONID 错误
    public final static int STATUS_ERROR_SESSIONID = -15;

    // SESSIONID 使用中, 没有过期
    public final static int STATUS_SESSIONID_INUSE = -16;

    // SESSIONID 已经过期, 不能继续使用
    public final static int STATUS_SESSIONID_EXPIRED = -17;


    // 凭证未授权
    public final static int STATUS_CREDENTIAL_NOT_AUTHORIZED = -20;

    // 凭证未启用
    public final static int STATUS_CREDENTIAL_NOT_ENABLED = -21;

    // 凭证已经锁定
    public final static int STATUS_CREDENTIAL_IS_LOCKED = -22;

    // 凭证已经过期
    public final static int STATUS_CREDENTIAL_IS_EXPIRED = -23;

    // 凭证使用中, 没有过期
    public final static int STATUS_CREDENTIAL_NOT_EXPIRED = -24;

    // 权限不足
    public final static int STATUS_INSUFFICENT_PRIVILEDGE = -25;


    // APPID 或 APPSECRET 错误
    public final static int STATUS_ERROR_APPID_OR_APPSECRET = -30;

    // APPID 错误
    public final static int STATUS_ERROR_APPID = -31;

    // APPSECRET 错误
    public final static int STATUS_ERROR_APPSECRET = -32;


    // 用户名或密码不正确
    public final static int STATUS_ERROR_USERNAME_OR_PASSWORD = -40;

    // 用户名不正确
    public final static int STATUS_ERROR_USERNAME = -41;

    // 密码不正确
    public final static int STATUS_ERROR_PASSWORD = -42;

    // 登录验证被拒绝
    public final static int STATUS_ERROR_PASSPORT_REFUSED = -44;


    // 短信验证码错误。不匹配!
    public final static int STATUS_ERROR_SMSCODE = -50;

    // 发送短信验证码错误
    public final static int STATUS_ERROR_SEND_SMSCODE = -51;

    // 短信验证码已经过期
    public final static int STATUS_SMSCODE_IS_EXPIRED = -52;

    // 短信验证码未过期
    public final static int STATUS_SMSCODE_NOT_EXPIRED = -53;
}
