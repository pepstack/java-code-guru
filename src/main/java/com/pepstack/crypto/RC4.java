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
 * @file: RC4.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2014-12-29
 *
 * @update: 2018-06-15 11:51:14
 */
package com.pepstack.crypto;


public final class RC4 {

    private final int[] sbox = new int[256];

    public RC4(String aKey) {
        final int keylen = aKey.length();
        final int[] iK = new int[256];
        int i, j, t;
        for (i = 0; i < 256; i++) {
            sbox[i] = i;
            iK[i] = aKey.charAt((i % keylen)) & 0xff;
        }
        j = 0;
        for (i = 0; i < 256; i++) {
            j = (j + sbox[i] + iK[i]) % 256;
            t = sbox[i];
            sbox[i] = sbox[j];
            sbox[j] = t;
        }
    }


    public RC4(byte[] key) {
        final int keylen = key.length;
        final int[] iK = new int[256];
        int i, j, t;
        for (i = 0; i < 256; i++) {
            sbox[i] = i;
            iK[i] = key[i % keylen] & 0xff;
        }
        j = 0;
        for (i = 0; i < 256; i++) {
            j = (j + sbox[i] + iK[i]) % 256;
            t = sbox[i];
            sbox[i] = sbox[j];
            sbox[j] = t;
        }
    }


    private static int charToHex(char c) {
        int ZERO = 48;
        int NINE = ZERO + 9;
        int littleA = 97;
        int littleZ = littleA + 25;
        int bigA = 65;
        int bigZ = 65 + 25;
        int result;

        if (c >= ZERO && c <= NINE) {
            result = c - ZERO;
        } else if (c >= bigA && c <= bigZ) {
            result = 10 + c - bigA;
        } else if (c >= littleA && c <= littleZ) {
            result = 10 + c - littleA;
        } else {
            result = 0;
        }
        return result;
    }


    private static int hexToDigit(char[] s, int offset) {
        int result = 0;
        for (int i = offset; i < offset + 4; ++i) {
            result <<= 4;
            result |= charToHex(s[i]);
        }
        return result;
    }


    public static int[] hexStringToDigits(String hexstr) {
        char[] hexarr = hexstr.toCharArray();

        int num = hexarr.length / 4;
        int[] outarr = new int[num];

        for (int n = 0; n < num; n++) {
            outarr[n] = hexToDigit(hexarr, n*4);
        }

        return outarr;
    }


    public static char[] hexStringToChars(String hexstr) {
        char[] hexarr = hexstr.toCharArray();

        int num = hexarr.length / 4;
        char[] outarr = new char[num];

        for (int n = 0; n < num; n++) {
            outarr[n] = (char) hexToDigit(hexarr, n*4);
        }

        return outarr;
    }


    // http://blog.sina.com.cn/s/blog_672149bc0100hul0.html
    public static String toHexString(String str) {
        int slen = str.length();
        int ival;
        StringBuilder sb = new StringBuilder(slen*4);
        for (int i = 0; i < slen; i++) {
            ival = str.charAt(i);
            String hexcode = Integer.toHexString((ival & 0x0000FFFF) | 0xFFFF0000).substring(4);
            sb.append(hexcode);
        }
        return sb.toString();
    };


    public char[] encrypt(char[] inChars) {
        char[] outChars = new char[inChars.length];

        final int[] iS = new int[256];
        System.arraycopy(sbox, 0, iS, 0, 256);

        int i, j, k, tmp;

        i = 0;
        j = 0;
        for (k = 0; k <inChars.length; k++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;

            tmp = iS[i];
            iS[i] = iS[j];
            iS[j] = tmp;

            tmp = (iS[i] + (iS[j] % 256)) % 256;
            tmp = iS[tmp];

            outChars[k] = (char) (inChars[k] ^ tmp);
        }
        return outChars;
    }


    public String encrypt(String aInput) {
        return new String(encrypt(aInput.toCharArray()));
    }


    public static String encrypt(String aInput, String aKey) {
        return new RC4(aKey).encrypt(aInput);
    }


    public static String encrypt(String aInput, byte[] bKey) {
        return new RC4(bKey).encrypt(aInput);
    }


    public static char[] encrypt(char[] inputChars, byte[] bKey) {
        return new RC4(bKey).encrypt(inputChars);
    }
}
