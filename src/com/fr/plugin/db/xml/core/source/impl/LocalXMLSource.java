package com.fr.plugin.db.xml.core.source.impl;

import com.fr.base.FRContext;
import com.fr.plugin.db.xml.core.XMLTableDataConstants;
import com.fr.plugin.db.xml.core.source.AbstractXMLSource;
import com.fr.script.Calculator;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * 本地xml文件
 */
public class LocalXMLSource extends AbstractXMLSource {

    @Override
    public Document getContent(Calculator cal, ParameterProvider[] providers) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(createOriginalFile(cal, providers));
    }

    private File createOriginalFile(Calculator cal, ParameterProvider[] providers) {
        String path = resolvePath(cal, calculatedParameters(cal, providers));
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (path.startsWith(XMLTableDataConstants.ROOT_PATH)) {
            String envPath = FRContext.getCurrentEnv().getPath();
            File envFile = new File(envPath);
            path = path.replace(XMLTableDataConstants.ROOT_PATH, envFile.getParentFile().getAbsolutePath());
        }
        return new File(path);
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Attr".equals(tagName)) {
                setOriginalPath(reader.getAttrAsString("originalPath", StringUtils.EMPTY));
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("Attr");
        if (StringUtils.isNotEmpty(getOriginalPath())) {
            writer.attr("originalPath", getOriginalPath());
        }
        writer.end();
    }
}
