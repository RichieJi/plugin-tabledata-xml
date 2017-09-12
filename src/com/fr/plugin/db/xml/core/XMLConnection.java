package com.fr.plugin.db.xml.core;

import com.fr.data.impl.AbstractDatabaseConnection;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.plugin.db.xml.core.source.XMLSource;
import com.fr.script.Calculator;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.StableXMLUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;
import org.w3c.dom.Document;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * xml数据连接，包含了xml的文件来源和参数两个属性
 */
public class XMLConnection extends AbstractDatabaseConnection {

    private static final long serialVersionUID = 1450808241614897557L;

    private XMLSource source;
    private ParameterProvider[] parameters;

    public XMLSource getSource() {
        return source;
    }

    public void setSource(XMLSource source) {
        this.source = source;
    }

    public ParameterProvider[] getParameters() {
        return this.parameters == null ? new ParameterProvider[0] : this.parameters;
    }

    public void setParameters(ParameterProvider[] parameters) {
        this.parameters = parameters;
    }


    public Document createXMLDocument(Calculator cal) throws Exception {
        return source.getContent(cal, parameters);
    }

    @Override
    public String getDriver() {
        return null;
    }

    @Override
    public void testConnection() throws Exception {
        if (source != null) {
            createXMLDocument(Calculator.createCalculator());
        }
    }

    @Override
    public String connectMessage(boolean status) {
        if (status) {
            return Inter.getLocText("Datasource-Connection_successfully") + "!";
        } else {
            return Inter.getLocText("Datasource-Connection_failed") + "!";
        }
    }


    @Override
    public String getOriginalCharsetName() {
        String data = super.getOriginalCharsetName();
        if (StringUtils.isEmpty(data)) {
            return EncodeConstants.ENCODING_UTF_8;
        }
        return data;
    }

    @Override
    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (XMLSource.XML_TAG.equals(tagName)) {
                source = (XMLSource) GeneralXMLTools.readXMLable(reader);
            } else if (ParameterProvider.ARRAY_XML_TAG.equals(tagName)) {//读取Parameters.
                final List<ParameterProvider> tmpParameterList = new ArrayList<ParameterProvider>();
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        if (ParameterProvider.XML_TAG.equals(reader.getTagName())) {
                            tmpParameterList.add(StableXMLUtils.readParameter(reader));
                        }
                    }
                });
                //转换数组.
                if (!tmpParameterList.isEmpty()) {
                    this.parameters = new ParameterProvider[tmpParameterList.size()];
                    tmpParameterList.toArray(this.parameters);
                }
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        if (source != null) {
            GeneralXMLTools.writeXMLable(writer, source, XMLSource.XML_TAG);
        }
        StableXMLUtils.writeParameters(writer, parameters);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof XMLConnection
                && ComparatorUtils.equals(((XMLConnection) obj).source, source)
                && ComparatorUtils.equals(((XMLConnection) obj).parameters, parameters);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        XMLConnection cloned = (XMLConnection) super.clone();
        if (source != null) {
            cloned.source = (XMLSource) source.clone();
        }
        if (this.parameters != null) {
            cloned.parameters = new ParameterProvider[this.parameters.length];
            for (int i = 0; i < this.parameters.length; i++) {
                cloned.parameters[i] = (ParameterProvider) this.parameters[i].clone();
            }
        }
        return cloned;
    }

    @Override
    public Connection createConnection() throws Exception {
        return null;
    }
}
