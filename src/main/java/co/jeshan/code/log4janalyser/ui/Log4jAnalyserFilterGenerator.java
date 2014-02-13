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

package co.jeshan.code.log4janalyser.ui;

import co.jeshan.code.log4janalyser.text.Strings$;
import co.jeshan.code.log4janalyser.ui.filters.LabelFilter;
import com.vaadin.data.Container.Filter;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.FilterGenerator;

import java.io.Serializable;
import java.util.EnumSet;

@SuppressWarnings("serial")
public class Log4jAnalyserFilterGenerator implements FilterGenerator, Serializable {

    private final FilterDecorator filterDecorator;

    public Log4jAnalyserFilterGenerator(FilterDecorator filterDecorator) {
        this.filterDecorator = filterDecorator;
    }

    @Override
    public Filter generateFilter(Object propertyId, Object value) {
        if (WindowWrapper.getColumnsForType(Label.class).contains(propertyId.toString())) {
            return new LabelFilter(propertyId, value);
        }

        // For other properties, use the default filter
        return null;
    }

    @Override
    public Filter generateFilter(Object propertyId, Field<?> originatingField) {
        // Use the default filter
        return null;
    }

    @Override
    public AbstractField<?> getCustomFilterComponent(Object propertyId) {
        // removed custom filter component for id
        if (propertyId.toString().equalsIgnoreCase(Strings$.MODULE$.levelHeader())) {
            return createEnumField(LogLevel.class);
        }
        return null;
    }

    @Override
    public void filterRemoved(Object propertyId) {
        Notification n = new Notification("Filter removed from: " + propertyId,
                Notification.Type.TRAY_NOTIFICATION);
        n.setDelayMsec(800);
        n.show(Page.getCurrent());
    }

    @Override
    public void filterAdded(Object propertyId,
                            Class<? extends Filter> filterType, Object value) {
        Notification n = new Notification("Filter added to: " + propertyId,
                Notification.Type.TRAY_NOTIFICATION);
        n.setDelayMsec(800);
        n.show(Page.getCurrent());
    }

    @Override
    public Filter filterGeneratorFailed(Exception reason, Object propertyId,
                                        Object value) {
        /* Return null -> Does not add any filter on failure */
        return null;
    }

    private AbstractField createEnumField(Class<?> type) {
        ComboBox enumSelect = new ComboBox();
        /* Add possible 'view all' item */
        Object nullItem = enumSelect.addItem();
        enumSelect.setNullSelectionItemId(nullItem);
        enumSelect.setItemCaption(nullItem, WindowWrapper$.MODULE$.filterDecorator().getAllItemsVisibleString());
        /* Add items from enumeration */
        for (Object o : EnumSet.allOf((Class<Enum>) type)) {
            enumSelect.addItem(o);
        }
        return enumSelect;
    }

    private static enum LogLevel {
        OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL
    }
}
