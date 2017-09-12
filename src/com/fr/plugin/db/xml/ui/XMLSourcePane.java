package com.fr.plugin.db.xml.ui;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIRadioPane;
import com.fr.plugin.db.xml.core.source.XMLSource;

import java.util.ArrayList;
import java.util.List;

public class XMLSourcePane extends UIRadioPane<XMLSource> {

    @Override
    protected List<FurtherBasicBeanPane<? extends XMLSource>> initPaneList() {
        List<FurtherBasicBeanPane<? extends XMLSource>> list = new ArrayList<FurtherBasicBeanPane<? extends XMLSource>>();
        list.add(new LocalXMLSourcePane());
        list.add(new RemoteXMLSourcePane());
        return list;
    }

    @Override
    protected String title4PopupWindow() {
        return "XML Source";
    }
}
