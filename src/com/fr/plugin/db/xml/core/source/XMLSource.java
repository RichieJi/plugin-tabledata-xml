package com.fr.plugin.db.xml.core.source;

import com.fr.script.Calculator;
import com.fr.stable.ParameterProvider;
import com.fr.stable.xml.XMLable;
import org.w3c.dom.Document;

/**
 * xml内容的来源
 */
public interface XMLSource extends XMLable {

    String XML_TAG = "XMLSource";

    /**
     * 用于描述文件位置的信息
     *
     * @return 文件位置
     */
    String getOriginalPath();

    /**
     * 文件的内容
     *
     * @param cal       算子
     * @param providers 参数集合
     * @return 文件的内容
     */
    Document getContent(Calculator cal, ParameterProvider[] providers) throws Exception;
}
