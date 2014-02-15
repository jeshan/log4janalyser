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

import _root_.org.apache.log4j.Logger
import _root_.org.tepi.filtertable.FilterTable
import scala.actors.Actor
import co.jeshan.code.log4janalyser.ui.WindowWrapper._

/**
 * User: jeshan
 * Date: 31/01/14
 * Time: 17:29
 */
class LoggingEventActor(receiver: LogFilePatternReceiverExtended, table: FilterTable) extends Actor {
    val log = Logger.getLogger(getClass)

    def act() {
        log.info(s"Starting receiving events for ${receiver.getFileURL}")
        loop {
            react {
                case event: LoggingEventExtended =>
                    log.trace(s"Received event $event in ${receiver.getFileURL}")
                    printToTable(table, event)
                case Shutdown =>
                    log.info(s"Stopping receiving events for ${receiver.getFileURL}")
                    exit()
            }
        }
    }
}


case object Shutdown
