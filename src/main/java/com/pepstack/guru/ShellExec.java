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
 * @file: ShellExec.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-05-29 16:51:24
 */
package com.pepstack.guru;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import java.util.ArrayList;
import java.util.List;


/**
 * Refer:
 *   https://blog.csdn.net/zyw_java/article/details/54024347
 */
public final class ShellExec {
    /**
     * 运行shell脚本
     * @param shCmd 需要运行的shell脚本
     */
    public static  void execShell(String shCmd) {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec(shCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 运行shell得到返回值
     *
     * @param shCmdStr
     *   需要执行的shell命令
     *
     * @return
     *
     * @throws IOException
     *
     * 注: 如果sh中含有awk, 一定要按 new String[]{"/bin/sh", "-c", shCmdStr}, 才可以获得流.
     */
    public static  List<String> runShell(String shCmdStr) throws Exception {
        List<String> resultsList = new ArrayList<String>();

        Process proc = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", shCmdStr}, null, null);

        InputStreamReader inReader = new InputStreamReader(proc.getInputStream());

        LineNumberReader input = new LineNumberReader(inReader);

        proc.waitFor();

        String line;
        while ((line = input.readLine()) != null) {
            resultsList.add(line);
        }
        return resultsList;
    }
}
