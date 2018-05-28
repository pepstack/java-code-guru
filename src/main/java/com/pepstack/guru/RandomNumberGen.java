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
 * @file: RandomNumberGen.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-05-28 22:52:17
 */
package com.pepstack.guru;


import java.util.Date;
import java.util.UUID;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

import java.math.BigInteger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public final class RandomNumberGen {
    private final static int ENCODER_ROUNDS = 4;

    private final static int RANDOM_MIN_NUMBER = 100000;
    private final static int RANDOM_MAX_NUMBER = 999999;


    public static final String md5Message(String msg) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 计算md5函数
            md.update(msg.getBytes());

            // digest()最后确定返回md5 hash值，返回值为8为字符串
            // 因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值,
            // 用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            System.exit(-22);
        }

        return null;
    }


    public static final String createNumber(Long salt) {
        try {
            SecureRandom secRandom = SecureRandom.getInstance("SHA1PRNG");
            secRandom.setSeed(salt + secRandom.nextInt(16777216));

            Integer number = secRandom.nextInt(RANDOM_MAX_NUMBER) % (RANDOM_MAX_NUMBER - RANDOM_MIN_NUMBER + 1) + RANDOM_MIN_NUMBER;

            return number.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.exit(-21);
        }

        return null;
    }


    public static final String createSessionid(String prefix, String tag, Long salt) {
        String sessionkey = String.format("%s/%s/%s/%s", prefix, tag, salt.toString(), UUID.randomUUID().toString());
        return md5Message(sessionkey);
    }


    public static final String encodeCipher(String plain) {
        String cipher = null;

        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(ENCODER_ROUNDS);
            cipher = encoder.encode(plain);
        } catch (Exception ex) {
        } finally {
            if (cipher == null) {
                return "";
            }
        }

        return cipher;
    }


    public static final boolean matchesCipher(String plain, String cipher) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(ENCODER_ROUNDS);
        return encoder.matches(plain, cipher);
    }
}
