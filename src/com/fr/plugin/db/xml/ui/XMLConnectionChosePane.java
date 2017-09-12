package com.fr.plugin.db.xml.ui;

import com.fr.data.impl.Connection;
import com.fr.design.data.datapane.connect.ConnectionComboBoxPanel;
import com.fr.design.dialog.BasicPane;
import com.fr.plugin.db.xml.core.XMLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by richie on 2017/5/16.
 */
public class XMLConnectionChosePane extends BasicPane {

    private ConnectionComboBoxPanel connectionComboBoxPanel;
    private DefaultListModel listModel = new DefaultListModel();

    public XMLConnectionChosePane() {
        setLayout(new BorderLayout(4, 4));
        connectionComboBoxPanel = new ConnectionComboBoxPanel(Connection.class) {

            protected void filterConnection(Connection connection, String conName, List<String> nameList) {
                connection.addConnection(nameList, conName, new Class[]{XMLConnection.class});
            }
        };

        add(connectionComboBoxPanel, BorderLayout.NORTH);
        connectionComboBoxPanel.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        });
        JList list = new JList(listModel);
        add(list, BorderLayout.CENTER);
    }


    public String getSelectJSONConnectionName() {
        return connectionComboBoxPanel.getSelectedItem();
    }

    public void populateConnection(Connection connection) {
        connectionComboBoxPanel.populate(connection);
    }


    @Override
    protected String title4PopupWindow() {
        return "Choose";
    }
}