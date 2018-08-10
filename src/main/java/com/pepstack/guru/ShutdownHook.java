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
 * @file: ShutdownHook.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-08-03 15:36:20
 */
package com.pepstack.guru;


public class ShutdownHook implements Runnable {

    public ShutdownHook() {
        // register a shutdown hook for this class.
        // a shutdown hook is an initialzed but not started thread, which will get up and run
        // when the JVM is about to exit. this is used for short clean up tasks.
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    // this method will be executed of course, since it's a Runnable.
    // tasks should not be light and short, accessing database is alright though.
    public void run() {
        cleanUp();
    }

    // (-: a very simple task to execute
    public void cleanUp() {
    }
}