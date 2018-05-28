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
 * @file: AccountValidator.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-05-28 22:52:17
 */
package com.pepstack.guru;

// https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class AccountValidator {
    /**
     * ^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$
     *  └─────┬────┘└───┬──┘└─────┬─────┘└─────┬─────┘ └───┬───┘
     *        │         │         │            │           no _ or . at the end
     *        │         │         │            │
     *        │         │         │            allowed characters
     *        │         │         │
     *        │         │         no __ or _. or ._ or .. inside
     *        │         │
     *        │         no _ or . at the beginning
     *        │
     *        username is 8~20 characters long
     *
     */

    private static final Pattern USERNAME_PATTERN =
        Pattern.compile("^(?=.{3,20}$)(?![0-9-_.])(?!.*[-_.]{2})[a-zA-Z0-9-._]+(?<![-_.])$", Pattern.CASE_INSENSITIVE);


    /**
     * Validate username with regular expression
     * @param username username for validation
     * @return true valid username, false invalid username
     */
    public static boolean validateUsername(final String username) {
        Matcher matcher = USERNAME_PATTERN.matcher(username);
        return matcher.matches();
    }

    private final static int PASSWORD_MIN_LENGTH = 8;
    private final static int PASSWORD_MAX_LENGTH = 32;

    private static final Pattern PATTERN_HAS_CHARACTER = Pattern.compile("^.*[a-zA-Z]+.*$");
    private static final Pattern PATTERN_HAS_NUMBERIC = Pattern.compile("^.*[0-9]+.*$");
    private static final Pattern PATTERN_HAS_SPECIFIC = Pattern.compile("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$");
    private static final Pattern PATTERN_HAS_REPEAT_CHARS = Pattern.compile("^.*(.)\\1{2,}+.*$");
    private static final Pattern PATTERN_HAS_REPEAT_SETS = Pattern.compile("^.*(.{3})(.*)\\1+.*$");
    private static final Pattern PATTERN_HAS_WHITE_SPACE = Pattern.compile("^.*[\\s]+.*$");


    public final static int SUCCESS_PASSWORD_STRENGTH_OK = 0;

    public final static int ERROR_PASSWORD_IS_NULL = 1;
    public final static int ERROR_PASSWORD_TOO_SHORT = 2;
    public final static int ERROR_PASSWORD_TOO_LONG = 3;
    public final static int ERROR_PASSWORD_LACK_CHAR = 4;
    public final static int ERROR_PASSWORD_LACK_NUMB = 5;
    public final static int ERROR_PASSWORD_LACK_SPEC = 6;
    public final static int ERROR_PASSWORD_REPEAT_CHARS = 7;
    public final static int ERROR_PASSWORD_REPEAT_SETS = 8;
    public final static int ERROR_PASSWORD_HAS_WHITESPACE = 9;

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public static int validatePassword(final String password) {
        if (password == null) {
            // 空字符
            return ERROR_PASSWORD_IS_NULL;
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            // 密码太短
            return ERROR_PASSWORD_TOO_SHORT;
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            // 密码太长
            return ERROR_PASSWORD_TOO_LONG;
        }

        if (! PATTERN_HAS_CHARACTER.matcher(password).matches()) {
            // 密码不包含字母
            return ERROR_PASSWORD_LACK_CHAR;
        }

        if (! PATTERN_HAS_NUMBERIC.matcher(password).matches()) {
            // 密码不包含数字
            return ERROR_PASSWORD_LACK_NUMB;
        }

        if (! PATTERN_HAS_SPECIFIC.matcher(password).matches()) {
            // 密码不包含特殊字符
            return ERROR_PASSWORD_LACK_SPEC;
        }

        if (PATTERN_HAS_REPEAT_CHARS.matcher(password).matches()) {
            // 密码包含3位及以上相同字符的重复: aaa123
            return ERROR_PASSWORD_REPEAT_CHARS;
        }

        if (PATTERN_HAS_REPEAT_SETS.matcher(password).matches()) {
            // 密码包含3位及以上字符组合的重复：AbcAbc
            return ERROR_PASSWORD_REPEAT_SETS;
        }

        if (PATTERN_HAS_WHITE_SPACE.matcher(password).matches()) {
            // 密码包含空格、制表符、换页符等空白字符
            return ERROR_PASSWORD_HAS_WHITESPACE;
        }

        // 密码强度合格
        return SUCCESS_PASSWORD_STRENGTH_OK;
    }


    private static final Pattern PATTERN_EMAIL_ADDRESS =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String email) {
        Matcher matcher = PATTERN_EMAIL_ADDRESS.matcher(email);
        return matcher.find();
    }
}
