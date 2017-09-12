package com.fr.plugin.db.xml.core.source;

import com.fr.base.Formula;
import com.fr.base.Parameter;
import com.fr.base.TemplateUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.UtilEvalError;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractXMLSource implements XMLSource {

    private String originalPath;

    @Override
    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    protected String resolvePath(Calculator cal, ParameterProvider[] calculatedParameters) {
        String path = getOriginalPath();

        if (StableUtils.canBeFormula(path)) {
            Formula formula = new Formula(path);
            try {
                path = GeneralUtils.objectToString(cal.eval(formula));
            } catch (UtilEvalError u) {
                FRLogger.getLogger().error(u.getMessage(), u);
            }
        }

        path = calculateRealHost(path, calculatedParameters);
        return  path;
    }

    protected ParameterProvider[] calculatedParameters(Calculator cal, ParameterProvider[] providers) {
        return Parameter.providers2Parameter(Calculator.processParameters(cal, providers));
    }

    protected String calculateRealHost(String query, ParameterProvider[] ps) {
        if (ArrayUtils.isEmpty(ps)) {
            return query;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (ParameterProvider p : ps) {
            map.put(p.getName(), p.getValue());
        }
        try {
            return TemplateUtils.renderParameter4Tpl(query, map);
        } catch (Exception e) {
            return query;
        }
    }

    @Override
    public void readXML(XMLableReader reader) {

    }

    @Override
    public void writeXML(XMLPrintWriter writer) {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AbstractXMLSource
                && ComparatorUtils.equals(((AbstractXMLSource) obj).originalPath, originalPath);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AbstractXMLSource cloned = (AbstractXMLSource) super.clone();
        cloned.originalPath = originalPath;
        return cloned;
    }
}
