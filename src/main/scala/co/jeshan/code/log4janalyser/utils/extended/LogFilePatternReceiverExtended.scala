/*
 * The Simplified BSD Licence follows:
 * Copyright (c) 2014, Jeshan G. BABOOA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */

package co.jeshan.code.log4janalyser.utils.extended

import _root_.org.apache.log4j.chainsaw.vfs.VFSLogFilePatternReceiver
import _root_.org.tepi.filtertable.FilterTable
import org.apache.log4j.spi.LoggingEvent
import org.apache.log4j.Logger
import org.apache.log4j.chainsaw.LogFilePatternLayoutBuilder
import co.jeshan.code.log4janalyser.ui.WindowWrapper

/**
 * User: jeshan
 * Date: 26/10/13
 * Time: 10:56
 */
class LogFilePatternReceiverExtended(url: String, log4jPattern: String, isTailing: Boolean, table: FilterTable) extends VFSLogFilePatternReceiver {

    val defaultTimestampFormat = "HH:mm:ss,SSS"
    val defaultWaitMillis = 500

    val log = Logger.getLogger(getClass)

    setFileURL(url)
    setLogFormat(LogFilePatternLayoutBuilder.getLogFormatFromPatternLayout(log4jPattern))
    setTailing(isTailing)
    setAppendNonMatches(true)
    setUseCurrentThread(false)

    setWaitMillis(defaultWaitMillis)
    setTimestampFormat(
        if (log4jPattern.contains("%d{")) {
            val regex = ".*%d\\{(.+)}.*".r
            val regex(timestampFormat) = log4jPattern
            timestampFormat
        }
        else if (log4jPattern.contains("%d")) defaultTimestampFormat
        else null
    )

    override def doPost(event: LoggingEvent) = {
        val eventExtended = LoggingEventExtended.create(event)
        WindowWrapper.printToTable(table, eventExtended)
    }

    override def shutdown() {
        super.shutdown()
        if(isTailing) {
            log.info(s"Stopping listening events for $url")
        }
    }

    def readFileAsync() {
        activateOptions()
    }

    override def activateOptions(): Unit = {
        super.activateOptions()
    }
}
