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

package co.jeshan.code.log4janalyser.ui

import com.vaadin.server.Resource
import com.vaadin.shared.ui.datefield.Resolution
import org.tepi.filtertable.FilterDecorator
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig
import java.io.Serializable
import java.text.DateFormat
import java.util.Locale

class Log4jAnalyserFilterDecorator extends FilterDecorator with Serializable {
    def getEnumFilterDisplayName(propertyId: AnyRef, value: AnyRef): String = null

    override def getEnumFilterIcon(propertyId: AnyRef, value: AnyRef): Resource = {
        null
    }

    override def getBooleanFilterDisplayName(propertyId: AnyRef, value: Boolean): String = {
        null
    }

    override def getBooleanFilterIcon(propertyId: AnyRef, value: Boolean): Resource = {
        null
    }

    override def getFromCaption: String = {
        "Start date:"
    }

    override def getToCaption: String = {
        "End date:"
    }

    override def getSetCaption: String = {
        null
    }

    override def getClearCaption: String = {
        null
    }

    override def isTextFilterImmediate(propertyId: AnyRef): Boolean = {
        true
    }

    override def getTextChangeTimeout(propertyId: AnyRef): Int = {
        500
    }

    override def getAllItemsVisibleString: String = {
        "Show all"
    }

    override def getDateFieldResolution(propertyId: AnyRef): Resolution = Resolution.SECOND

    def getDateFormat(propertyId: AnyRef): DateFormat = ???

    override def usePopupForNumericProperty(propertyId: AnyRef): Boolean = {
        true
    }

    override def getDateFormatPattern(propertyId: AnyRef): String = null //Formatters.uiFormat.toPattern

    override def getLocale: Locale = Locale.UK

    override def getNumberFilterPopupConfig: NumberFilterPopupConfig = {
        null
    }
}
