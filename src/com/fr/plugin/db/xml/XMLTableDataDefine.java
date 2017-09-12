package com.fr.plugin.db.xml;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.fun.impl.AbstractTableDataDefineProvider;
import com.fr.general.Inter;
import com.fr.plugin.db.xml.core.XMLTableData;
import com.fr.plugin.db.xml.ui.XMLTableDataPane;

/**
 * Created by richie on 2017/5/15.
 */
public class XMLTableDataDefine extends AbstractTableDataDefineProvider {
    @Override
    public Class<? extends TableData> classForTableData() {
        return XMLTableData.class;
    }

    @Override
    public Class<? extends TableData> classForInitTableData() {
        return XMLTableData.class;
    }

    @Override
    public Class<? extends AbstractTableDataPane> appearanceForTableData() {
        return XMLTableDataPane.class;
    }

    @Override
    public String nameForTableData() {
        return Inter.getLocText("Plugin-XML_Table_Data");
    }

    @Override
    public String prefixForTableData() {
        return "xml";
    }

    @Override
    public String iconPathForTableData() {
        return "/com/fr/plugin/db/xml/images/xml.png";
    }
}
