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
 * @file: ExtendedPatternLayoutEncoder.java
 *
 *   https://stackoverflow.com/questions/36875541/process-id-in-logback-logging-pattern
 *
 * @author: master@pepstack.com
 *
 * @create: 2018-08-03
 *
 * @update:
 */
package com.pepstack.guru;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;


public class ExtendedPatternLayoutEncoder extends PatternLayoutEncoder {
    @Override
    public void start() {
        // put your converter
        PatternLayout.defaultConverterMap.put("process_id", ProcessIdConverter.class.getName());

        super.start();
    }
}
