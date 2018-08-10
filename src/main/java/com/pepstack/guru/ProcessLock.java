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
 * @file: ProcessLock.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-05-04
 *
 * @update: 2018-08-03 15:46:08
 */
package com.pepstack.guru;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import java.util.Date;

import com.pepstack.guru.IOUtil;


public final class ProcessLock {
    private final File lockFile;

    private FileLock lock;
    private FileOutputStream outStream;


    public ProcessLock(String path, String filename) throws IOException {
        File lockPath = new File(path);
        if (! lockPath.exists() && ! lockPath.isDirectory()) {
            lockPath.mkdir();
        }

        lockFile = new File(IOUtil.concatPathFile(path, filename));

        if (! lockFile.exists()) {
            lockFile.createNewFile();
        }
    }


    public String getPathFile() throws IOException {
        return lockFile.getCanonicalPath();
    }


    public boolean enterLock() {
        try {
            if (outStream == null) {
                outStream = new FileOutputStream(lockFile);
            }

            FileChannel channel = outStream.getChannel();
            lock = channel.tryLock();

            if (lock == null) {
                leaveLock();
                return false;
            } else {
                return true;
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return false;
    }


    public void leaveLock() {
        try {
            if (lock != null) {
                lock.release();
            }

            if (outStream != null) {
                outStream.close();
            }

            lock = null;
            outStream = null;

            lockFile.delete();
        } catch (IOException e) {
        } finally {
            lock = null;
            outStream = null;
        }
    }
}