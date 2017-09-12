package com.fr.plugin.db.xml.core.source.impl;

import com.fr.base.Formula;
import com.fr.base.Parameter;
import com.fr.base.TemplateUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.plugin.db.xml.core.HttpClientBuilder;
import com.fr.plugin.db.xml.core.RequestType;
import com.fr.plugin.db.xml.core.source.AbstractXMLSource;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.UtilEvalError;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 远程xml文件
 */
public class RemoteXMLSource extends AbstractXMLSource {

    private RequestType requestType;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public Document getContent(Calculator cal, ParameterProvider[] providers) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        return builder.parse(createInputStream(cal, providers));
    }

    private InputStream createInputStream(Calculator cal, ParameterProvider[] providers) {

        ParameterProvider[] calculatedParameters = calculatedParameters(cal, providers);

        String resolvedPath = resolvePath(cal, calculatedParameters);

        String content;
        if (requestType == RequestType.GET) {
            content = HttpClientBuilder.getInstance().sendHttpGet(resolvedPath, EncodeConstants.ENCODING_UTF_8);
        } else {
            content = HttpClientBuilder.getInstance().sendHttpPost(resolvedPath, createParameterMap(calculatedParameters));
        }
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(content.getBytes(EncodeConstants.ENCODING_UTF_8));
        } catch (UnsupportedEncodingException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return in;
    }

    protected Map<String, String> createParameterMap(ParameterProvider[] parameters) {
        Map<String, String> map = new HashMap<String, String>();
        if (parameters != null) {
            for (ParameterProvider parameter : parameters) {
                map.put(parameter.getName(), GeneralUtils.objectToString(parameter.getValue()));
            }
        }
        return map;
    }


    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Attr".equals(tagName)) {
                setOriginalPath(reader.getAttrAsString("originalPath", StringUtils.EMPTY));
                requestType = RequestType.parse(reader.getAttrAsInt("request", 0));
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("Attr");
        if (StringUtils.isNotEmpty(getOriginalPath())) {
            writer.attr("originalPath", getOriginalPath());
        }
        writer.attr("request", requestType.toInt());
        writer.end();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RemoteXMLSource
                && super.equals(obj)
                && ComparatorUtils.equals(((RemoteXMLSource) obj).requestType, requestType);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RemoteXMLSource cloned = (RemoteXMLSource) super.clone();
        cloned.requestType = requestType;
        return cloned;
    }
}
