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
 * @file: SimpleHttp.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-06-15 11:51:14
 */
package com.pepstack.guru;


import java.util.List;
import java.util.Map;
import java.util.Locale;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * References:
 * 1) 简单的 http 调用:
 *   http://www.xyzws.com/Javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
 *   https://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java
 *   http://www.cnblogs.com/zhuawang/archive/2012/12/08/2809380.html
 *
 * 2) 更全面地使用 http client:
 *   http://hc.apache.org/httpcomponents-client-ga/index.html
 */
public final class SimpleHttp {

    public static String encodeURLParameters(Map<String, String> parametersMap) {
        try {
            StringBuffer urlParameters = new StringBuffer();
            for (String key : parametersMap.keySet()) {
                if (urlParameters.length() > 0) {
                    urlParameters.append("&" + key + "=");
                } else {
                    urlParameters.append(key + "=");
                }

                urlParameters.append(URLEncoder.encode(parametersMap.get(key), "UTF-8"));
            }

            return urlParameters.toString();
        } catch (UnsupportedEncodingException ne) {
            ne.printStackTrace();
        }

        return null;
    }


    public static String doGetJson(String targetURL, String urlParameters) {
        HttpURLConnection urlconn = null;
        DataOutputStream output = null;
        BufferedReader reader = null;

        try {
            // Create urlconn
            //   https://bbs.csdn.net/topics/390632652?page=1
            URL url = new URL(targetURL + "?" + urlParameters);

            urlconn = (HttpURLConnection) url.openConnection();

            urlconn.setRequestMethod("GET");

            urlconn.setConnectTimeout(5000);  // 5 seconds

            urlconn.setRequestProperty("Connection", "Keep-Alive");
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlconn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            urlconn.setRequestProperty("Content-Language", "en-US");
            urlconn.setRequestProperty("Accept", "application/json");
            urlconn.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            urlconn.setRequestProperty("Accept-Charset", "UTF-8");
            urlconn.setRequestProperty("Referer", targetURL);
            urlconn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2;"
                    + " Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; "
                    + ".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152;"
                    + " .NET CLR 3.5.30729)");

            urlconn.setUseCaches (false);

            urlconn.connect();

            // 获取遍历所有响应头字段
            Map<String, List<String> > map = urlconn.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + ": " + map.get(key));
            }

            // Get Response
            reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));

            StringBuffer response = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
                //response.append('\r');
            }
            reader.close();
            reader = null;

            urlconn.disconnect();
            urlconn = null;

            return response.toString();
        } catch (UnsupportedEncodingException ne) {
            ne.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }

                if (reader != null) {
                    reader.close();
                }

                if (urlconn != null) {
                    urlconn.disconnect();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }

        return null;
    }


    public static String doPostJson(String targetURL, String urlParameters) {
        HttpURLConnection urlconn = null;
        DataOutputStream output = null;
        BufferedReader reader = null;

        try {
            // Create urlconn
            //   https://bbs.csdn.net/topics/390632652?page=1
            URL url = new URL(targetURL);

            urlconn = (HttpURLConnection) url.openConnection();

            urlconn.setRequestMethod("POST");

            urlconn.setConnectTimeout(5000);  // 5 seconds

            urlconn.setRequestProperty("Connection", "Keep-Alive");
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlconn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            urlconn.setRequestProperty("Content-Language", "en-US");
            urlconn.setRequestProperty("Accept", "application/json");
            urlconn.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            urlconn.setRequestProperty("Accept-Charset", "UTF-8");
            urlconn.setRequestProperty("Referer", targetURL);
            urlconn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2;"
                    + " Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; "
                    + ".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152;"
                    + " .NET CLR 3.5.30729)");

            urlconn.setUseCaches (false);

            // 发送POST请求必须设置如下两行
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);

            // Send request
            output = new DataOutputStream(urlconn.getOutputStream());
            output.writeBytes(urlParameters);
            output.flush();
            output.close();
            output = null;

            // Get Response
            reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));

            StringBuffer response = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
                //response.append('\r');
            }
            reader.close();
            reader = null;

            urlconn.disconnect();
            urlconn = null;

            return response.toString();
        } catch (UnsupportedEncodingException ne) {
            ne.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }

                if (reader != null) {
                    reader.close();
                }

                if (urlconn != null) {
                    urlconn.disconnect();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }

        return null;
    }
}
