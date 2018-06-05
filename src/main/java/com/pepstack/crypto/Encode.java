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
 * @file: Encode.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2014-12-29
 *
 * @update: 2018-06-05 23:18:23
 */
package com.pepstack.crypto;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;


public final class Encode {

    public static final String ENCODING_UTF8 = "UTF-8";

    private static final char[] HEX_DIGITS = {
        '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'
    };

    public static String toHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >>> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }


    public static byte[] hexStringToBytes(String hexString) throws IllegalArgumentException {
        if (StringUtils.isEmpty(hexString)) {
            throw new IllegalArgumentException("input empty hex string");
        }

        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("input hex string not even length");
        }

        final byte[] byteArray = new byte[hexString.length() / 2];

        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }

        return byteArray;
    }


    public static String base64Encode(byte[] input) {
        if (input == null || input.length == 0) {
            return null;
        }

        return Base64.encodeBase64String(input);
    }


    public static byte[] base64Decode(String b64str) {
        if (StringUtils.isEmpty(b64str)) {
            return null;
        }
        return Base64.decodeBase64(b64str);
    }

}

