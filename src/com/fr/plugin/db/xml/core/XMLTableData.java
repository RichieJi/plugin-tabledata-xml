package com.fr.plugin.db.xml.core;

import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.base.TemplateUtils;
import com.fr.data.AbstractParameterTableData;
import com.fr.data.core.DataCoreXmlUtils;
import com.fr.data.impl.Connection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.file.DatasourceManager;
import com.fr.general.Inter;
import com.fr.general.data.DataModel;
import com.fr.plugin.ExtraClassManager;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.fun.FunctionHelper;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.fun.impl.AbstractFunctionProcessor;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by richie on 2017/5/15.
 */
public class XMLTableData extends AbstractParameterTableData {

    private static final FunctionProcessor XML = new AbstractFunctionProcessor() {

        @Override
        public int getId() {
            return FunctionHelper.generateFunctionID(XMLTableDataConstants.PLUGIN_ID);
        }

        @Override
        public String getLocaleKey() {
            return "Plugin-XML";
        }

        @Override
        public String toString() {
            return Inter.getLocText("Plugin-XML");
        }
    };
    private static final long serialVersionUID = -7225652455132679742L;

    private Connection database;
    private String query;

    public XMLTableData() {

    }

    public Connection getDatabase() {
        return database;
    }

    public void setDatabase(Connection database) {
        this.database = database;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public DataModel createDataModel(Calculator calculator) {
        return createDataModel(calculator, TableData.RESULT_ALL);
    }

    @Override
    public DataModel createDataModel(Calculator calculator, int rowCount) {
        FunctionProcessor processor = ExtraClassManager.getInstance().getFunctionProcessor();
        if (processor != null) {
            processor.recordFunction(XML);
        }
        Parameter[] ps = Parameter.providers2Parameter(Calculator.processParameters(calculator, parameters));
        if (database instanceof NameDatabaseConnection) {
            String name = ((NameDatabaseConnection) database).getName();
            XMLConnection connection = DatasourceManager.getProviderInstance().getConnection(name, XMLConnection.class);
            if (connection != null) {
                return new XMLTableDataModel(
                        connection,
                        calculator,
                        calculateQuery(query, ps),
                        rowCount
                );
            }
        }
        return null;
    }

    private String calculateQuery(String query, Parameter[] ps) {
        if (ArrayUtils.isEmpty(ps)) {
            return query;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (Parameter p : ps) {
            map.put(p.getName(), p.getValue());
        }
        try {
            return TemplateUtils.renderParameter4Tpl(query, map);
        } catch (Exception e) {
            return query;
        }
    }


    public void readXML(XMLableReader reader) {
        super.readXML(reader);

        if (reader.isChildNode()) {
            String tmpName = reader.getTagName();
            String tmpVal;

            if (com.fr.data.impl.Connection.XML_TAG.equals(tmpName)) {
                if (reader.getAttrAsString("class", null) != null) {
                    com.fr.data.impl.Connection con = DataCoreXmlUtils.readXMLConnection(reader);
                    this.setDatabase(con);
                }
            } else if ("QueryText".equals(tmpName)) {
                if ((tmpVal = reader.getElementValue()) != null) {
                    this.setQuery(tmpVal);
                }
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        if (this.database != null) {
            DataCoreXmlUtils.writeXMLConnection(writer, this.database);
        }
        writer.startTAG("QueryText").textNode(getQuery()).end();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        XMLTableData cloned = (XMLTableData) super.clone();
        cloned.database = database;
        cloned.query = query;
        return cloned;
    }
}
