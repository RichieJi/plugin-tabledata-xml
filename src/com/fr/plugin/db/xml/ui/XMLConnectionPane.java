package com.fr.plugin.db.xml.ui;

import com.fr.base.Parameter;
import com.fr.base.ParameterHelper;
import com.fr.design.data.datapane.connect.DatabaseConnectionPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.plugin.db.xml.core.XMLConnection;
import com.fr.plugin.db.xml.core.source.XMLSource;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * xml数据连接面板，用于设置xml文件的来源
 */
public class XMLConnectionPane extends DatabaseConnectionPane<XMLConnection> {

    private XMLSourcePane sourcePane;

    private UITableEditorPane<ParameterProvider> editorPane;

    @Override
    protected JPanel mainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        sourcePane = new XMLSourcePane();
        panel.add(sourcePane, BorderLayout.CENTER);
        ParameterTableModel model = new ParameterTableModel() {
            @Override
            public UITableEditAction[] createAction() {
                return ArrayUtils.add(super.createAction(), new RefreshAction());
            }
        };
        editorPane = new UITableEditorPane<ParameterProvider>(model);
        editorPane.setPreferredSize(new Dimension(200, 200));

        panel.add(editorPane, BorderLayout.SOUTH);

        return panel;
    }

    protected class RefreshAction extends UITableEditAction {
        public RefreshAction() {
            this.setName(Inter.getLocText("Plugin-XML_Refresh"));
            this.setSmallIcon(IOUtils.readIcon("/com/fr/plugin/db/xml/images/refresh.png"));
        }

        public void actionPerformed(ActionEvent e) {
            refresh();
        }

        @Override
        public void checkEnabled() {
        }
    }

    private void refresh() {
        XMLSource source = sourcePane.updateBean();
        String[] paramTexts = new String[]{source.getOriginalPath()};
        java.util.List<ParameterProvider> existParameterList = editorPane.update();
        Parameter[] ps = existParameterList == null ? new Parameter[0] : existParameterList.toArray(new Parameter[existParameterList.size()]);
        editorPane.populate(ParameterHelper.analyzeAndUnionSameParameters(paramTexts, ps));
    }

    @Override
    protected boolean isFineBI() {
        return false;
    }

    @Override
    protected void populateSubDatabaseConnectionBean(XMLConnection ob) {
        if (ob == null) {
            return;
        }
        sourcePane.populateBean(ob.getSource());
        editorPane.populate(ob.getParameters());
    }

    @Override
    protected XMLConnection updateSubDatabaseConnectionBean() {
        XMLConnection connection = new XMLConnection();
        connection.setSource(sourcePane.updateBean());
        java.util.List<ParameterProvider> parameterList = editorPane.update();
        ParameterProvider[] parameters = parameterList.toArray(new ParameterProvider[parameterList.size()]);
        connection.setParameters(parameters);
        return connection;
    }

    @Override
    protected String title4PopupWindow() {
        return "XML";
    }
}
