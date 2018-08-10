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
 * @file: IOUtil.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-08-03 16:11:49
 */
package com.pepstack.guru;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.lang.management.ManagementFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import java.util.Properties;


/**
 * IOUtil
 */
public final class IOUtil {
    private static final String PROCESS_ID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

    public static void print(String text) {
        System.out.println(text);
    }


    public static boolean isOSWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("windows");
    }


    public static String getpid() {
        return PROCESS_ID;
    }


    public static String concatPathFile(String path, String file) {
        return path.trim() + System.getProperty("file.separator") + file;
    }


    public static String readMessage(BufferedReader reader) throws IOException {
        String message = null;
        String strLine = null;

        while ((strLine = reader.readLine()) != null) {
            if (strLine.equals("{{<<")) {
                // message body begin
                message = "";
            } else if (strLine.equals(">>}}")) {
                // message body end
                break;
            } else if (message != null) {
                // concat message line
                message += strLine;
            }
        }

        return message;
    }


    public static String getJarPath(Class clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");
            java.io.File jarFile = new java.io.File(path);
            return java.net.URLDecoder.decode(jarFile.getParentFile().getAbsolutePath(), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean loadProperties(File file, Properties props) {
        boolean ret = false;
        InputStream is = null;

        try {
            is = new FileInputStream(file);
            props.load(is);
            ret = true;
        } catch (FileNotFoundException ex) {
            ret = false;
        } catch (IOException e) {
            ret = false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                ret = false;
            }
        }

        return ret;
    }


    public static void saveProperties(File file, Properties props, String header) {
        OutputStream os = null;

        try {
            os = new FileOutputStream(file);
            if (header != null) {
                props.store(os, header);
            }
            os.flush();
        } catch (IOException e) {
            // do nothing
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }


    public static Properties loadProperties(String filename) {
        Properties props = new Properties();
        InputStream is = null;

        try {
            is = new FileInputStream(filename);
            props.load(is);
        } catch (FileNotFoundException ex) {
            // do nothing
        } catch (IOException e) {
            // do nothing
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }

        return props;
    }


    public static void saveProperties(String filename, Properties props, String header) {
        OutputStream os = null;

        try {
            os = new FileOutputStream(filename);
            if (header != null) {
                props.store(os, header);
            }
            os.flush();
        } catch (IOException e) {
            // do nothing
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }


    public static long readPropertyAsLong(String filename, String propName, long defaultValue) {
        Properties props = loadProperties(filename);

        String val = props.getProperty(propName);
        if (val == null) {
            return defaultValue;
        } else {
            return Long.parseLong(val);
        }
    }


    public static String[] readProperties(String filename, String[] propNames) {
        String[] propValues = new String[propNames.length];
        Properties props = loadProperties(filename);

        for (int i = 0; i < propNames.length; i++) {
            propValues[i] = props.getProperty(propNames[i]);
        }

        return propValues;
    }


    public static void writeLongProperty(String filename, String propName, long propValue, String header) {
        Properties props = loadProperties(filename);
        props.setProperty(propName, Long.toString(propValue));
        saveProperties(filename, props, header);
    }


    public static void writeProperty(String filename, String propName, String propValue, String header) {
        Properties props = loadProperties(filename);
        props.setProperty(propName, propValue);
        saveProperties(filename, props, header);
    }


    public static void writeProperties(String filename, String[] propNames, String[] propValues, String header) {
        Properties props = loadProperties(filename);
        if (props != null) {
            for (int i = 0; i < propNames.length; i++) {
                props.setProperty(propNames[i], propValues[i]);
            }
            saveProperties(filename, props, header);
        }
    }
}