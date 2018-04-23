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
package com.pepstack.guru;


// source:
//   https://github.com/HaraldWalker/user-agent-utils

// 测试浏览器 User-Agent:
//   http://www.useragentstring.com/index.php
//   https://blog.csdn.net/java_faep/article/details/73838915


import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/** pom.xml
    <!-- https://mvnrepository.com/artifact/com.github.servanter/netsfjson-support-spring -->
    <dependency>
        <groupId>com.github.servanter</groupId>
        <artifactId>netsfjson-support-spring</artifactId>
        <version>1.0.0</version>
    </dependency>
*/
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;


/** pom.xml

    <!-- https://mvnrepository.com/artifact/eu.bitwalker/UserAgentUtils -->
    <dependency>
        <groupId>eu.bitwalker</groupId>
        <artifactId>UserAgentUtils</artifactId>
        <version>1.21</version>
    </dependency>
*/
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import eu.bitwalker.useragentutils.OperatingSystem;


public final class UserAgentProfile {
    private final static int API_TIMEOUT_MILLISECONDS = 2000;

    // 淘宝 API
    private final static String TAOBAO_API_URL = "http://ip.taobao.com/service/getIpInfo.php";
    
    // 新浪 API
	private final static String SINA_API_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php";

    private final static String USER_LOCATION_FORMAT = "{country(%s);region(%s);city(%s);county(%s);isp(%s);area(%s);}";

    private final UserAgent userAgent;

    private final String userRemoteIp;


    public UserAgentProfile(HttpServletRequest httpRequest) {
        this.userAgent = UserAgent.parseUserAgentString(httpRequest.getHeader("User-Agent"));
        this.userRemoteIp = getRemoteRealIp(httpRequest);
    }


    public UserAgent getUserAgent() {
        return userAgent;
    }


    public String getUserAgentAsString() {
        return userAgent.toString();
    }


    public String getUserRealIp() {
        return userRemoteIp;
    }


    /**
	 * 获取用户端的真实 IP 地址
	 */
	public static String getRemoteRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (! StringUtils.isEmpty(ip) && ! "unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值, 第1个ip是真实ip
            int index = ip.indexOf(",");

            if(index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        ip = request.getHeader("X-Real-IP");

        if (! StringUtils.isEmpty(ip) && ! "unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }


    public static String getGeoLocationFromRealIp(String realIp) {
        HttpURLConnection urlConn = null;
        String resultString = null;

        try {   
            URL url = new URL(TAOBAO_API_URL);

			urlConn = (HttpURLConnection) url.openConnection();

			urlConn.setConnectTimeout(API_TIMEOUT_MILLISECONDS);
			urlConn.setReadTimeout(API_TIMEOUT_MILLISECONDS);
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setRequestMethod("POST");
			urlConn.setUseCaches(false);

            urlConn.connect();

			DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
			out.writeBytes(String.format("ip=%s", realIp));
			out.flush();
			out.close();

            String line;

            StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(
                new InputStreamReader(urlConn.getInputStream(), "utf-8"));
			
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			reader.close();

            urlConn.disconnect();
            urlConn = null;

            resultString = buffer.toString();

            JSONObject json = JSONObject.fromObject(resultString);
            JSONObject jsonOb = JSONObject.fromObject(json.get("data"));

            resultString = String.format(USER_LOCATION_FORMAT,
                jsonOb.get("country_id").toString(),
                jsonOb.get("region_id").toString(),
                jsonOb.get("city_id").toString(),
                jsonOb.get("county_id").toString(),
                jsonOb.get("isp_id").toString(),
                jsonOb.get("area_id").toString());
        } catch (UnsupportedEncodingException uee) {
        } catch (MalformedURLException mue) {
        } catch (IOException ioe) {
        } finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}

        if (resultString == null) {
            resultString = "{}";
        }

        return resultString;
    }
}
