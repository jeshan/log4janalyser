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

import org.apache.log4j.{Logger, Level, Category}
import org.apache.log4j.spi.{ThrowableInformation, LocationInfo, LoggingEvent}
import java.util
import org.apache.commons.lang3.builder.{HashCodeBuilder, ReflectionToStringBuilder, ToStringStyle, ToStringBuilder}
import java.util.Date
import co.jeshan.code.log4janalyser.utils.Formatters

/**
 * User: jeshan
 * Date: 26/10/13
 * Time: 12:38
 */
class LoggingEventExtended(logger: Category, timeStamp: Long, level: Level, message: AnyRef, threadName: String, throwable: ThrowableInformation,
                           ndc: String, locationInfo: LocationInfoExtended, properties: util.Map[_, _], val id: Long = 0, val logId: Long = 0)
    extends LoggingEvent(null, logger: Category, timeStamp: Long, level: Level, message: AnyRef, threadName: String, throwable: ThrowableInformation,
        ndc: String, locationInfo: LocationInfo, properties: util.Map[_, _]) {

    override def toString: String = {
        val builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        if (fqnOfCategoryClass != null) builder.append("fqnOfCategoryClass", fqnOfCategoryClass)
        if (logger != null) builder.append("logger", ReflectionToStringBuilder.toString(logger, ToStringStyle.SHORT_PREFIX_STYLE, true, true))
        if (getLoggerName != null) builder.append("categoryName", getLoggerName)
        if (level != null) builder.append("level", level)
        if (getNDC != null) builder.append("ndc", getNDC)
        //if (getmd != null) builder.append("mdcCopy", getMDCCopy())
        builder.append("ndcLookupRequired", false)
        builder.append("mdcCopyLookupRequired", false)
        if (message != null) builder.append("message", message)
        if (getRenderedMessage != null) builder.append("renderedMessage", getRenderedMessage)
        if (getThreadName != null) builder.append("threadName", getThreadName)
        if (getThrowableInformation != null) builder.append("throwableInfo", getThrowableInformation)
        builder.append("timeStamp", Formatters.formatDate(new Date(timeStamp)))
        builder.append("locationInfo", ReflectionToStringBuilder.toString(getLocationInformation, ToStringStyle.SHORT_PREFIX_STYLE, true, true))
        builder.toString
    }

    override def getLocationInformation = locationInfo

    override def getThrowableInformation = throwable

    override def hashCode() = HashCodeBuilder.reflectionHashCode(this)

    override def equals(obj: Any): Boolean = {
        if (obj == null || !obj.isInstanceOf[LoggingEventExtended]) {
            return false
        }
        val that = obj.asInstanceOf[LoggingEventExtended]
        val thatLocationInfo = that.getLocationInformation
        // don't compare ID as we won't have it before inserting into the database!
        //id == that.id &&
        logId == that.logId && that.timeStamp == timeStamp &&
            (getLoggerName != null && getLoggerName.equals(that.getLoggerName)) &&
            (getLevel != null && getLevel.equals(that.getLevel)) &&
            getMessage != null && getMessage.equals(that.getMessage) &&
            getThreadName != null && getThreadName.equals(that.getThreadName) &&
            getNDC != null && getNDC.equals(that.getNDC) &&
            (getLocationInformation.getFileName != null && getLocationInformation.getFileName.equals(thatLocationInfo.getFileName)) &&
            (getLocationInformation.getClassName != null && getLocationInformation.getClassName.equals(thatLocationInfo.getClassName)) &&
            (getLocationInformation.getMethodName != null && getLocationInformation.getMethodName.equals(thatLocationInfo.getMethodName)) &&
            (getLocationInformation.getLineNumber != null && getLocationInformation.getLineNumber.equals(thatLocationInfo.getLineNumber))
    }

}


object LoggingEventExtended {
    def create(event: LoggingEvent) = new LoggingEventExtended(event.getLogger, event.getTimeStamp, event.getLevel, event.getMessage,
        event.getThreadName, event.getThrowableInformation, event.getNDC, new LocationInfoExtended(event.getLocationInformation),
        event.getProperties)

    def create(loggerName: String, timeStamp: Long, levelName: String, message: String, threadName: String, ndc: String, eventFileName: String,
               className: String,
               methodName: String, lineNumber: Int, logId: Long = 0, id: Long = 0) = {
        new LoggingEventExtended(Logger.getLogger(loggerName), timeStamp, Level.toLevel(levelName), message, threadName, null /*new ThrowableInformationExtended(Array(""))*/ , ndc,
            new LocationInfoExtended(eventFileName, className, methodName, lineNumber.toString), null, id, logId)
    }
}
