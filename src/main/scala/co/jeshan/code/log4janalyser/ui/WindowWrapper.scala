
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

import _root_.com.vaadin.data.util.converter.Converter
import _root_.com.vaadin.event.ItemClickEvent
import _root_.com.vaadin.shared.ui.label.ContentMode
import _root_.com.vaadin.ui.CustomTable.RowHeaderMode
import _root_.java.util.{Locale, Date}
import _root_.org.tepi.filtertable.FilterTable
import com.vaadin.ui.Button.{ClickEvent, ClickListener}

import scala.collection.JavaConverters.asScalaIteratorConverter
import com.vaadin.ui._
import co.jeshan.code.log4janalyser.utils.extended.{LoggingEventExtended, LogFilePatternReceiverExtended}
import org.apache.log4j.Logger
import co.jeshan.code.log4janalyser.text.Strings
import co.jeshan.code.log4janalyser.ui.WindowWrapper.Name
import co.jeshan.code.log4janalyser.utils.{Formatters, Helpers}

/**
 * User: jeshan
 * Date: 26/01/14
 * Time: 11:30
 */
class WindowWrapper {

    import Name._
    import WindowWrapper._

    val defaultLog4jPattern = "%d{HH:mm:ss,SSS} [%t] %p [%C] - %m%n"
    val defaultPath = "/home/jeshan/ideaProjects/log4janalyser/simple-short.log"

    var window = new Log4jAnalyserWindow

    val mainLayout = window.iterator().next().asInstanceOf[Layout]

    // Components are retrieved this way so as to avoid making manual changes to Log4jAnalyserWindow.
    // The latter is generated via the Vaadin plug-in for Eclipse

    val inputLayout = findComponent(inputLayoutId, mainLayout).asInstanceOf[Layout]

    val tableLayout = findComponent(tableLayoutId, mainLayout).asInstanceOf[Layout]

    val submitButton = findComponent(submitButtonId, inputLayout).asInstanceOf[Button]

    val clearButton = findComponent(clearButtonId, inputLayout).asInstanceOf[Button]

    val closeButton = findComponent(closeButtonId, inputLayout).asInstanceOf[Button]

    val inputPathField = findComponent(inputPathFieldId, inputLayout).asInstanceOf[TextField]

    val log4jPatternField = findComponent(log4jPatternFieldId, inputLayout).asInstanceOf[TextField]

    val isTailingBox = findComponent(isTailingBoxId, inputLayout).asInstanceOf[CheckBox]

    val logTable = findComponent(logTableId, tableLayout).asInstanceOf[FilterTable]


    val submitListener = new ClickListener {
        def buttonClick(event: ClickEvent) {
            submitClickEventHandler(getInputPath, log4jPatternField.getValue, isTailingBox.getValue, logTable)
        }
    }

    val clearListener = new ClickListener {
        override def buttonClick(event: ClickEvent) {
            clearClickEventHandler(getInputPath, logTable, isTailingBox.getValue)
        }
    }

    val closeListener = new ClickListener {
        override def buttonClick(event: ClickEvent) {
            closeClickEventHandler(getInputPath, logTable)
        }
    }

    def getInputPath = "file://" + inputPathField.getValue

    def initUi() {
        log4jPatternField.setValue(defaultLog4jPattern)
        inputPathField.setValue(defaultPath)
        submitButton.addClickListener(submitListener)
        submitButton.setDisableOnClick(true)
        clearButton.addClickListener(clearListener)
        closeButton.addClickListener(closeListener)

        initTable()
    }

    initUi()

    import Helpers._

    def submitClickEventHandler(uri: String, pattern: String, isTailing: Boolean, table: FilterTable) {
        val receiver = findOrAddReceiver(uri, pattern, isTailing, table)
        receiver.readFileAsync()
        disableFields(inputPathField, log4jPatternField, isTailingBox)

    }

    def clearClickEventHandler(uri: String, table: CustomTable, isTailing: Boolean) {
        table.removeAllItems()
        if (!isTailing) {
            closeClickEventHandler(uri, table)
        }
    }

    def closeClickEventHandler(uri: String, table: CustomTable) {
        closeReceiver(uri)
        enableFields(inputPathField, log4jPatternField, isTailingBox, submitButton)
    }


    def findComponent(styleName: String, layout: Layout): Component = {
        layout.iterator().asScala.find(x => x.getStyleName.equals(styleName)).get
    }


    def initTable() {

        logTable.setFilterBarVisible(true)
        logTable.setFilterGenerator(new Log4jAnalyserFilterGenerator(filterDecorator))

        logTable.setSelectable(true)
        logTable.setImmediate(true)
        logTable.setRowHeaderMode(RowHeaderMode.INDEX)
        logTable.setPageLength(Int.MaxValue)
        import Strings._

        logTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            def itemClick(event: ItemClickEvent) {
                if (!event.isDoubleClick) {
                    return
                }
                event.getPropertyId match {
                    case id if id.equals(messageHeader) =>
                        val item = event.getItem
                        val message = item.getItemProperty(messageHeader).getValue.asInstanceOf[Label].getValue
                        val array = item.getItemProperty(throwableHeader).getValue.asInstanceOf[Array[String]]
                        if (array.length == 1) return
                        val window = new Window(throwableHeader)

                        UI.getCurrent.addWindow(window)
                        window.setContent(new VerticalLayout(new Label(message, contentMode) +: array.map(new Label(_, contentMode)): _*))
                    //window.setVisible(true)
                    case _ =>
                }
            }
        })
        setColumnProperties(logTable)

    }

}

object WindowWrapper {

    val log = Logger.getLogger(getClass)

    object Name {
        val logTableId = "logTable"

        val inputLayoutId = "inputLayout"

        val tableLayoutId = "tableLayout"

        val submitButtonId = "submitButton"

        val clearButtonId = "clearButton"

        val closeButtonId = "closeButton"

        val inputPathFieldId = "inputPathField"

        val log4jPatternFieldId = "log4jPatternField"

        val isTailingBoxId = "isTailingBox"

        val controlsLayoutId = "controlsLayout"
    }


    val contentMode = ContentMode.HTML

    val filterDecorator = new
            Log4jAnalyserFilterDecorator

    val threadFunction = (event: LoggingEventExtended) => event.getThreadName.take(40)
    val levelFunction = (event: LoggingEventExtended) => {
        val label = new
                Label(event.getLevel.toString, contentMode)
        label.setStyleName(event.getLevel.toString.toLowerCase)
        label
    }
    val messageFunction = (event: LoggingEventExtended) => {
        val message = event.getRenderedMessage
        val label = new
                Label(message, contentMode)
        label
    }
    val throwableFunction = (event: LoggingEventExtended) => event.getThrowableInformation.getThrowableStrRep
    val timeFunction = (event: LoggingEventExtended) => new
            Date(event.getTimeStamp)


    import Strings._

    val columns: Map[String, ((LoggingEventExtended) => Any, Class[_], Int)] = Map(dateHeader ->(timeFunction, classOf[Date], 19), threadHeader ->(threadFunction,
        classOf[String], 19),
        levelHeader ->(levelFunction, classOf[Label], 19),
        messageHeader ->(messageFunction, classOf[Label], 40))
    val otherColumns: Map[String, ((LoggingEventExtended) => Any, Class[_], Int)] = Map(throwableHeader ->(throwableFunction, classOf[Array[String]], 3))

    import scala.collection.JavaConverters._

    def getColumnsForType(cls: Class[_]) = columns.filter(_._2._2.isAssignableFrom(cls)).keySet.asJava

    private val receivers: scala.collection.mutable.Map[String, LogFilePatternReceiverExtended] = new
            collection.mutable.HashMap[String, LogFilePatternReceiverExtended]()

    def findReceiver(uri: String) = receivers.synchronized {
        receivers.get(uri)
    }

    def addReceiver(uri: String, pattern: String, isTailing: Boolean, table: FilterTable) = receivers.synchronized {
        val receiver = new
                LogFilePatternReceiverExtended(uri, pattern, isTailing, table)
        receivers.put(uri, receiver)
        receiver
    }

    def findOrAddReceiver(uri: String, pattern: String, isTailing: Boolean, table: FilterTable) = receivers.synchronized {
        val option = findReceiver(uri)
        if (option.isDefined) option.get
        else addReceiver(uri, pattern, isTailing, table)
    }

    def closeReceiver(uri: String) = receivers.synchronized {
        val receiver = findReceiver(uri)
        if (receiver.isDefined) {
            receiver.get.shutdown()
            UI.getCurrent.setPollInterval(-1)
            receivers.remove(uri)
        }
    }

    def setColumnProperties(table: FilterTable) {
        table.setColumnCollapsingAllowed(true)
        columns.foreach(column => {
            table.addContainerProperty(column._1, column._2._2, null)
            table.setFilterFieldVisible(column._1, true)
            table.setColumnWidth(column._1, -1)
        })

        otherColumns.foreach(column => {
            table.addContainerProperty(column._1, column._2._2, null)
            table.setFilterFieldVisible(column._1, true)
            table.setColumnCollapsed(column._1, true)
            table.setColumnWidth(column._1, -1)
        })

        table.setConverter(dateHeader, DateConverter)
        table.setFilterDecorator(filterDecorator)
    }

    def printToTable(logTable: FilterTable, event: LoggingEventExtended) {
        val array: Array[AnyRef] = (columns.map(_._2._1(event).asInstanceOf[AnyRef]) ++ otherColumns.map(_._2._1(event).asInstanceOf[AnyRef])).toArray
        UI.getCurrent.accessSynchronously(new Runnable {
            override def run() {
                logTable.addItem(array, null)
            }
        })
    }

    object DateConverter extends Converter[String, Date] {
        def getPresentationType: Class[String] = classOf[String]

        def getModelType: Class[Date] = classOf[Date]

        def convertToPresentation(value: Date, targetType: Class[_ <: String], locale: Locale): String = if (value != null) Formatters.formatDate(value) else null

        def convertToModel(value: String, targetType: Class[_ <: Date], locale: Locale): Date = if (value != null) Formatters.parseDate(value) else null
    }

}
