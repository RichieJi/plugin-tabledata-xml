package com.fr.plugin.db.xml;

import com.fr.data.impl.Connection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractConnectionProvider;
import com.fr.plugin.db.xml.core.XMLConnection;
import com.fr.plugin.db.xml.ui.XMLConnectionPane;

/**
 * Created by richie on 2017/5/15.
 */
public class XMLConnectionImpl extends AbstractConnectionProvider {
    @Override
    public String nameForConnection() {
        return "XML";
    }

    @Override
    public String iconPathForConnection() {
        return "/com/fr/plugin/db/xml/images/xml.png";
    }

    @Override
    public Class<? extends Connection> classForConnection() {
        return XMLConnection.class;
    }

    @Override
    public Class<? extends BasicBeanPane<? extends Connection>> appearanceForConnection() {
        return XMLConnectionPane.class;
    }
}
