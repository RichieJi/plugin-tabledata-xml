package com.fr.plugin.db.xml.ui;

import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.sqlpane.SQLEditPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.SyntaxConstants;
import com.fr.design.gui.syntax.ui.rtextarea.RTextScrollPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class XPathQueryPane extends BasicPane {

    private SQLEditPane sqlTextPane;

    @Override
    protected String title4PopupWindow() {
        return "XPath Query";
    }

    public XPathQueryPane() {
        setLayout(new BorderLayout());

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] rowSize = {p};
        double[] columnSize = {p, f};

        sqlTextPane = new SQLEditPane();
        Component[][] coms = new Component[][]{
                {new UILabel(Inter.getLocText("Plugin-XML_Query") + ":"), createConditionTextPane(sqlTextPane)},

        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);
        add(panel, BorderLayout.CENTER);
    }

    private RTextScrollPane createConditionTextPane(SQLEditPane sqlTextPane) {
        sqlTextPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        RTextScrollPane sqlTextScrollPane = new RTextScrollPane(sqlTextPane);
        sqlTextScrollPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
        sqlTextScrollPane.setPreferredSize(new Dimension(680, 300));
        return sqlTextScrollPane;
    }

    public String getQuery() {
        return sqlTextPane.getText();
    }

    public void setQuery(String query) {
        sqlTextPane.setText(query);
    }
}
