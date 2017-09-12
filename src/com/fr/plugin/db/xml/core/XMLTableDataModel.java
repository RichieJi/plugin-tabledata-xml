package com.fr.plugin.db.xml.core;

import com.fr.data.AbstractDataModel;
import com.fr.general.FRLogger;
import com.fr.general.data.TableDataException;
import com.fr.script.Calculator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * xml数据集模型
 */
public class XMLTableDataModel extends AbstractDataModel {

    private static final long serialVersionUID = 4373137111288024503L;
    private ArrayList<String> columnNames;
    private List<List<Object>> data;

    public XMLTableDataModel(XMLConnection connection, Calculator calculator, String query, int rowCount) {
        try {
            initData(connection, calculator, query, rowCount);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
    }

    private void initData(XMLConnection connection, Calculator calculator, String query, int rowCount) throws Exception {
        columnNames = new ArrayList<String>();
        data = new ArrayList<List<Object>>();

        Document doc = connection.createXMLDocument(calculator);

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expression = xpath.compile(query);

        NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
        boolean foundHeader = false;
        for (int i = 0, len = nodes.getLength(); i < len; i++) {
            Node node = nodes.item(i);
            if (node.hasChildNodes()) {
                NodeList children = node.getChildNodes();
                List<Object> rowData = new ArrayList<Object>();
                for (int k = 0, size = children.getLength(); k < size; k ++) {
                    Node current = children.item(k);
                    if (current.getNodeType() == Node.ELEMENT_NODE) {
                        String tagName = current.getNodeName();
                        String value = current.getTextContent();
                        if (!foundHeader) {
                            columnNames.add(tagName);
                        }
                        rowData.add(value);
                    }
                }
                data.add(rowData);
                foundHeader = true;
            }
        }
    }


    @Override
    public int getColumnCount() throws TableDataException {
        return columnNames == null ? 0 : columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) throws TableDataException {
        return columnNames == null ? null : columnNames.get(columnIndex);
    }

    @Override
    public boolean hasRow(int rowIndex) throws TableDataException {
        return data != null && data.size() > rowIndex;
    }

    @Override
    public int getRowCount() throws TableDataException {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws TableDataException {
        if (data != null && data.size() > rowIndex) {
            List<Object> rowData = data.get(rowIndex);
            if (rowData != null && rowData.size() > columnIndex) {
                return rowData.get(columnIndex);
            }
        }
        return null;
    }
}
