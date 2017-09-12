package com.fr.plugin.db.xml.ui;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.DescriptionTextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.plugin.db.xml.core.source.impl.LocalXMLSource;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LocalXMLSourcePane extends FurtherBasicBeanPane<LocalXMLSource> {

    private UITextField pathTextField;

    public LocalXMLSourcePane() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        final double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] rowSize = new double[]{p, p};
        double[] columnSize = new double[]{p, f};

        DescriptionTextArea descriptionTextArea = new DescriptionTextArea();
        descriptionTextArea.setText(Inter.getLocText("Plugin-XML_Source_Local_Description_Text"));

        pathTextField = new UITextField();

        UIButton fileButton = new UIButton("...");
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
                int result = fileChooser.showOpenDialog(LocalXMLSourcePane.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    pathTextField.setText(path);
                }
            }
        });

        Component[][] components = new Component[][]{
                {new UILabel(Inter.getLocText("Plugin-XML_Source_Local_Path") + ":"), GUICoreUtils.createBorderLayoutPane(
                        pathTextField, BorderLayout.CENTER,
                        fileButton, BorderLayout.EAST
                )},
                {new UILabel(Inter.getLocText("Plugin-XML_Source_Local_Description") + ":"), descriptionTextArea}
        };
        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        add(centerPane, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-XML_Source_Local");
    }

    @Override
    public boolean accept(Object ob) {
        return ob instanceof LocalXMLSource;
    }

    @Override
    public void reset() {

    }

    @Override
    public void populateBean(LocalXMLSource ob) {
        if (ob == null) {
            return;
        }
        pathTextField.setText(ob.getOriginalPath());
    }

    @Override
    public LocalXMLSource updateBean() {
        LocalXMLSource source = new LocalXMLSource();
        source.setOriginalPath(pathTextField.getText());
        return source;
    }
}
