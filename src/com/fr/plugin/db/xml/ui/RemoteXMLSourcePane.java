package com.fr.plugin.db.xml.ui;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.icombobox.UIDictionaryComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.db.xml.core.RequestType;
import com.fr.plugin.db.xml.core.source.impl.RemoteXMLSource;

import javax.swing.*;
import java.awt.*;

public class RemoteXMLSourcePane extends FurtherBasicBeanPane<RemoteXMLSource> {

    private TinyFormulaPane urlTextPane;

    private UIDictionaryComboBox<RequestType> requestTypeComboBox;

    public RemoteXMLSourcePane() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] rowSize = new double[]{p, p};
        double[] columnSize = new double[]{p, f};

        urlTextPane = new TinyFormulaPane();

        requestTypeComboBox = new UIDictionaryComboBox<RequestType>(RequestType.values(), new String[]{
                "GET",
                "POST"
        });

        Component[][] components = new Component[][]{
                {new UILabel(Inter.getLocText("Plugin-XML_Url") + ":"), urlTextPane},

                {new UILabel(Inter.getLocText("Plugin-XML_Request_Type") + ":"), requestTypeComboBox}
        };
        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        add(centerPane, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-XML_Source_Remote");
    }

    @Override
    public boolean accept(Object ob) {
        return ob instanceof RemoteXMLSource;
    }

    @Override
    public void reset() {

    }

    @Override
    public void populateBean(RemoteXMLSource ob) {
        if (ob == null) {
            return;
        }
        urlTextPane.populateBean(ob.getOriginalPath());
        requestTypeComboBox.setSelectedItem(ob.getRequestType());
    }

    @Override
    public RemoteXMLSource updateBean() {
        RemoteXMLSource source = new RemoteXMLSource();
        source.setOriginalPath(urlTextPane.updateBean());
        source.setRequestType(requestTypeComboBox.getSelectedItem());
        return source;
    }
}
