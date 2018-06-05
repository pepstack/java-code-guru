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
 * @file: MinutesRollingFileAppender.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-28
 *
 * @update: 2018-06-01 06:29:19
 */
package com.pepstack.guru;


// https://stackoverflow.com/questions/4858022/how-to-configure-a-log4j-file-appender-which-rolls-the-log-file-every-15-minutes
// https://logback.qos.ch/apidocs/ch/qos/logback/core/rolling/RollingFileAppender.html
//
import ch.qos.logback.core.rolling.RollingFileAppender;


public class MinutesRollingFileAppender<E> extends RollingFileAppender<E>
{
    // 默认是 10 分钟生成一个日志文件, 用户可以配置: logback-spring.xml
    // <appender name="YourRollingFile" class="com.ztgame.utils.MinutesRollingFileAppender">
    //     <!-- 每分钟生成一个日志文件 -->
    //     <param name="rollingIntervalMinutes" value="1"/>
    //     ...
    //
    private int rollingIntervalMinutes = 10;

    public int getRollingIntervalMinutes() {
        return rollingIntervalMinutes;
    }

    public void setRollingIntervalMinutes(int rollingIntervalMinutes) {
        this.rollingIntervalMinutes = rollingIntervalMinutes;
    }


    private static long rollingStart = 0L;
    private static long rollingIntervalMillis = 600000L;


    /**
     * Appender 初始化的时候被调用
     */
    @Override
    public void start() {
        rollingIntervalMillis = rollingIntervalMinutes * 60000;
        rollingStart = System.currentTimeMillis() / rollingIntervalMillis;

        super.start();
    }


    @Override
    public void rollover() {
        long rollingCurrent = System.currentTimeMillis() / rollingIntervalMillis;

        if (rollingCurrent > rollingStart) {
            // 生成下一个日志文件
            super.rollover();

            rollingStart = rollingCurrent;
        }
    }
}
