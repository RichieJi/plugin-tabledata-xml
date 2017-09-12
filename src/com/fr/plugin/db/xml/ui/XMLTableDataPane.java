package com.fr.plugin.db.xml.ui;

import com.fr.base.Parameter;
import com.fr.base.ParameterHelper;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.menu.ToolBarDef;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.plugin.db.xml.core.XMLTableData;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by richie on 2017/5/15.
 */
public class XMLTableDataPane extends AbstractTableDataPane<XMLTableData> {
    private static final String PREVIEW_BUTTON = Inter.getLocText("Plugin-XML_Preview");
    private static final String REFRESH_BUTTON = Inter.getLocText("Plugin-XML_Refresh");

    private XMLConnectionChosePane chosePane;

    private UITableEditorPane<ParameterProvider> editorPane;

    private XPathQueryPane queryPane;

    public XMLTableDataPane() {
        this.setLayout(new BorderLayout(4, 4));
        Box box = new Box(BoxLayout.Y_AXIS);
        queryPane = new XPathQueryPane();
        JPanel northPane = new JPanel(new BorderLayout(4, 4));
        JToolBar editToolBar = createToolBar();
        northPane.add(editToolBar, BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        ParameterTableModel model = new ParameterTableModel() {
            @Override
            public UITableEditAction[] createAction() {
                return ArrayUtils.add(
                        super.createDBTableAction(),

                        new RefreshAction());
            }
        };
        editorPane = new UITableEditorPane<ParameterProvider>(model);


        box.add(northPane);
        box.add(queryPane);

        box.add(editorPane);

        JPanel sqlSplitPane = new JPanel(new BorderLayout(4, 4));
        sqlSplitPane.add(box, BorderLayout.CENTER);

        chosePane = new XMLConnectionChosePane();
        chosePane.setPreferredSize(new Dimension(200, 200));
        sqlSplitPane.add(chosePane, BorderLayout.WEST);

        this.add(sqlSplitPane, BorderLayout.CENTER);
    }

    @Override
    public void populateBean(XMLTableData tableData) {
        if (tableData == null) {
            return;
        }
        Calculator c = Calculator.createCalculator();
        editorPane.populate(tableData.getParameters(c));

        chosePane.populateConnection(tableData.getDatabase());

        queryPane.setQuery(tableData.getQuery());
    }

    @Override
    public XMLTableData updateBean() {
        XMLTableData tableData = new XMLTableData();

        String connectionName = chosePane.getSelectJSONConnectionName();
        if (StringUtils.isNotEmpty(connectionName)) {
            tableData.setDatabase(new NameDatabaseConnection(connectionName));
        }
        java.util.List<ParameterProvider> parameterList = editorPane.update();
        ParameterProvider[] parameters = parameterList.toArray(new ParameterProvider[parameterList.size()]);
        tableData.setParameters(parameters);

        tableData.setQuery(queryPane.getQuery());

        return tableData;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    private JToolBar createToolBar() {
        ToolBarDef toolBarDef = new ToolBarDef();
        toolBarDef.addShortCut(new PreviewAction());
        UIToolbar editToolBar = ToolBarDef.createJToolBar();
        toolBarDef.updateToolBar(editToolBar);
        return editToolBar;
    }

    private class PreviewAction extends UpdateAction {
        public PreviewAction() {
            this.setName(PREVIEW_BUTTON);
            this.setMnemonic('P');
            this.setSmallIcon(IOUtils.readIcon("/com/fr/plugin/db/xml/images/preview.png"));
        }

        public void actionPerformed(ActionEvent evt) {
            checkParameter();
            PreviewTablePane.previewTableData(XMLTableDataPane.this.updateBean());
        }
    }


    protected class RefreshAction extends UITableEditAction {
        public RefreshAction() {
            this.setName(REFRESH_BUTTON);
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
        String[] paramTexts = new String[]{queryPane.getQuery()};

        java.util.List<ParameterProvider> existParameterList = editorPane.update();
        Parameter[] ps = existParameterList == null ? new Parameter[0] : existParameterList.toArray(new Parameter[existParameterList.size()]);

        editorPane.populate(ParameterHelper.analyzeAndUnionSameParameters(paramTexts, ps));
    }


    private void checkParameter() {
        String[] paramTexts = new String[]{queryPane.getQuery()};

        Parameter[] parameters = ParameterHelper.analyze4Parameters(paramTexts, false);

        if (parameters.length < 1 && editorPane.update().size() < 1) {
            return;
        }
        boolean isIn = true;
        java.util.List<ParameterProvider> list = editorPane.update();
        java.util.List<String> name = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            name.add(list.get(i).getName());
        }
        for (int i = 0; i < parameters.length; i++) {
            if (!name.contains(parameters[i].getName())) {
                isIn = false;
                break;
            }
        }
        if (list.size() == parameters.length && isIn) {
            return;
        }
        // bug:34175  删了是否刷新对话框， 均直接刷新
        refresh();
    }
}
