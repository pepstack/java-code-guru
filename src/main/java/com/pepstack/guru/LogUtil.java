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
 * @file: LogUtil.java
 *    Wrapper for Logger
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update:
 */
package com.pepstack.guru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;


public class LogUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // purple
    private final static String EXCEPTION_COLOR_FORMAT = "\033[31m%s:\033[0m \033[35m%s\033[0m";

    // purple
    private final static String FATAL_COLOR_FORMAT = "\033[35m%s\033[0m";

    // red
    private final static String ERROR_COLOR_FORMAT = "\033[31m%s\033[0m";

    // yellow
    private final static String WARN_COLOR_FORMAT = "\033[33m%s\033[0m";

    // green
    private final static String INFO_COLOR_FORMAT = "\033[32m%s\033[0m";

    // cyan
    private final static String DEBUG_COLOR_FORMAT = "\033[36m%s\033[0m";


    public final void testLogging() {
        trace(logger, "This is a trace message!");
        debug(logger, "This is a debug message!");
        info(logger, "This is a info message!");
        warn(logger, "This is a warn message!");
        error(logger, "This is a error message!");
        fatal(logger, "This is a fatal message!");
    }


    /**
     * 完整的堆栈信息
     *
     * @param e Exception
     * @return Full StackTrace
     */
    public static String getStackTrace(Exception e) {
        String msg = "";

        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);

            e.printStackTrace(pw);

            pw.flush();
            sw.flush();

            msg = sw.toString();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (pw != null) {
                pw.close();
            }
        }

        return msg;
    }


    public final static void exception(Logger log, Exception e) {
        if (log.isErrorEnabled()) {
            log.error(String.format(EXCEPTION_COLOR_FORMAT, e.getClass().getCanonicalName(), e.getStackTrace()));
        }
    }


    public final static void fatal(Logger log, String fmt, String... args) {
        if (log.isErrorEnabled()) {
            log.error(String.format(FATAL_COLOR_FORMAT, String.format(fmt, args)));
        }
    }


    public final static void error(Logger log, String fmt, String... args) {
        if (log.isErrorEnabled()) {
            log.error(String.format(ERROR_COLOR_FORMAT, String.format(fmt, args)));
        }
    }


    public final static void warn(Logger log, String fmt, String... args) {
        if (log.isWarnEnabled()) {
            log.warn(String.format(WARN_COLOR_FORMAT, String.format(fmt, args)));
        }
    }


    public final static void info(Logger log, String fmt, String... args) {
        if (log.isInfoEnabled()) {
            log.info(String.format(INFO_COLOR_FORMAT, String.format(fmt, args)));
        }
    }


    public final static void debug(Logger log, String fmt, String... args) {
        if (log.isDebugEnabled()) {
            log.debug(String.format(DEBUG_COLOR_FORMAT, String.format(fmt, args)));
        }
    }


    public final static void trace(Logger log, String fmt, String... args) {
        if (log.isTraceEnabled()) {
            log.trace(fmt, args);
        }
    }
}
