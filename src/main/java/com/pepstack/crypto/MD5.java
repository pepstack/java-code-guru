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
 * @file: MD5.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2014-12-29
 *
 * @update: 2018-06-05 23:17:46
 */
package com.pepstack.crypto;


import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;


public final class MD5 {
    private static final String ALGORITHM = "MD5";

    private static final String ENCODING = "UTF-8";

    public static byte[] sign(byte[] plain) {
        if (plain == null) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(plain);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static byte[] sign(String value, String encoding) {
        if (value == null) {
            return null;
        }

        try {
            if (StringUtils.isEmpty(encoding)) {
                return sign(value.getBytes());
            } else {
                return sign(value.getBytes(encoding));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String hexSign(String value) {
        return Encode.toHexString(sign(value, null));
    }


    public static String hexSign(String value, String encoding) {
        return Encode.toHexString(sign(value, encoding));
    }


    private static byte[] concatBytes(byte[] b1, byte[] b2) {
        byte[] b3 = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, b3, 0, b1.length);
        System.arraycopy(b2, 0, b3, b1.length, b2.length);
        return b3;
    }


    private static byte[] intArray2bytes(int[] iarr) {
        byte[] barr = new byte[iarr.length * 4];
        for (int i = 0; i < iarr.length; i++) {
            int v = iarr[i];
            barr[i * 4 + 0]  = (byte) ((v >>> 24) & 0xFF);
            barr[i * 4 + 1]  = (byte) ((v >>> 16) & 0xFF);
            barr[i * 4 + 2]  = (byte) ((v >>> 8) & 0xFF);
            barr[i * 4 + 3]  = (byte) ((v >>> 0) & 0xFF);
        }
        return barr;
    }


    public static byte[] hmacSign(byte[] value, byte[] key) {
        if (value == null) {
            return null;
        }

        if (key == null) {
            return sign(value);
        }

        byte[] bkey = sign(key);

        int[] ipad = new int[bkey.length];
        int[] opad = new int[bkey.length];

        for (int i = 0; i < bkey.length; i++) {
            int vb = bkey[i] & 0xFF;
            ipad[i] = vb ^ 0x36363636;
            opad[i] = vb ^ 0x5C5C5C5C;
        }

        byte[] byipad = intArray2bytes(ipad);
        byte[] byopad = intArray2bytes(opad);

        String hash = Encode.toHexString(sign(concatBytes(byipad, value)));

        return sign(concatBytes(byopad, hash.getBytes()));
    }


    public static String hmacHexSign(String value, String key, String encoding) {
        if (value == null) {
            return null;
        }
        try {
            if (StringUtils.isEmpty(encoding)) {
                if (key == null) {
                    return Encode.toHexString(sign(value.getBytes()));
                } else {
                    return Encode.toHexString(hmacSign(value.getBytes(), key.getBytes()));
                }
            } else {
                if (key == null) {
                    return Encode.toHexString(sign(value.getBytes(encoding)));
                } else {
                    return Encode.toHexString(hmacSign(value.getBytes(encoding), key.getBytes(encoding)));
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String hmacHexSign(String value, String key) {
        return hmacHexSign(value, key, null);
    }
}
