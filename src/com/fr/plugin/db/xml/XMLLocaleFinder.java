package com.fr.plugin.db.xml;

import com.fr.plugin.db.xml.core.XMLTableDataConstants;
import com.fr.stable.fun.Authorize;
import com.fr.stable.fun.impl.AbstractLocaleFinder;

/**
 * Created by richie on 2017/5/15.
 */
@Authorize(callSignKey = XMLTableDataConstants.PLUGIN_ID)
public class XMLLocaleFinder extends AbstractLocaleFinder {
    @Override
    public String find() {
        return "com/fr/plugin/db/xml/locale/xml";
    }
}
