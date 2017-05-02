package com.lgame.util.load.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/28.
 */
public class XmlLoad {
    private String path;
    private Document doc;
    private Element root;

    public XmlLoad(String path){
        this.path = path;
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db=factory.newDocumentBuilder();
            doc=db.parse(new File(path));
            root=doc.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *  详细参数参考 XPath 语法（w3c）
     * @param express:/root/note[@att=1]  @代表属性，没有@代表内容
     *
     * @param source
     * @return
     */
    public Element searchSingleNode(String express, Element source) {//查找节点，并返回第一个符合条件节点
        Element result=null;
        XPathFactory xpathFactory=XPathFactory.newInstance();
        XPath xpath=xpathFactory.newXPath();
        try {
            result=(Element) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void save() {//将Document输出到文件
        TransformerFactory transFactory=TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            DOMSource source=new DOMSource();
            source.setNode(doc);
            StreamResult result=new StreamResult();
            result.setOutputStream(new FileOutputStream(path));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Element getRoot() {
        return root;
    }
}
