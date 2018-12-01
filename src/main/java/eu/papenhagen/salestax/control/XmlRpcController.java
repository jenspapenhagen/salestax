/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.salestax.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Named;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.json.XML;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

/**
 * All methos for XML-RPC parsing/handling Sepcs:
 * http://xmlrpc.scripting.com/spec.html
 *
 * @author jens.papenhagen
 */
@Named
public class XmlRpcController implements Serializable {

    private static final Logger L = LoggerFactory.getLogger(XmlRpcController.class);

    /**
     * getting the Error Code out of the XML-RPS Foprmate String
     *
     * @param rpcFormatedString
     * @return int 200-223 or 999 for parsing Error
     */
    public int getErrorCode(String rpcFormatedString) {
        Document parseXmlRpc = parseXmlRpc(rpcFormatedString);
        if (parseXmlRpc == null) {
            L.error("Error on parsing the XML");
            return 999;
        }
        return getErrorCode(parseXmlRpc);
    }

    /**
     * unsing teh Enum to hold errorCode and a description for that code
     * together
     *
     * @param errorCode
     * @return the errorCodeDescription
     */
    public String getErrorCodeDescription(int errorCode) {
        String description = "";
        Optional<ErrorCode> findErrorCodeEnum = Stream.of(ErrorCode.values())
                .filter(e -> e.name().matches(".*\\d+.*") && e.name().endsWith("" + errorCode))
                .findFirst();
        if (findErrorCodeEnum.isPresent()) {
            L.debug("found the right ErrorCode in the Enums");
            description = findErrorCodeEnum.get().getDescription();
        }

        return description;
    }

    /**
     * parse a given XML-RPC Formated String to a Document
     *
     * @param content
     * @return the Document
     */
    private Document parseXmlRpc(String content) {
        Document document = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            DocumentBuilder ducumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = ducumentBuilder.parse(inputStream);

        } catch (ParserConfigurationException ex) {
            L.error("ParserConfigurationException on DocumentBuilder {}", ex.getMessage());
        } catch (SAXException ex) {
            L.error("SAXException on builder parsen {}", ex.getMessage());
        } catch (IOException ex) {
            L.error("IOException on builder parsen {}", ex.getMessage());
        }

        return document;
    }

    /**
     * This Methode give back the Number of the error code
     *
     * the naming on this xml-rpc api not so obvious. therefor check this page:
     * https://evatr.bff-online.de/eVatR/xmlrpc/
     *
     * @param document
     * @return int as Error code
     */
    private int getErrorCode(Document document) {
        int errorCode = 999;
        try {
            //transform the Document to XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            String output = stringWriter.getBuffer().toString().replaceAll("\n|\r", "");

            //make a JSON Objet out of it
            JSONObject xmlJSONObj = XML.toJSONObject(output);

            //find root
            JSONObject jsonRootObject = xmlJSONObj.getJSONObject("methodResponse");

            //going down the rabit hole
            JSONObject errorCodejson = (JSONObject) jsonRootObject
                    .getJSONObject("params")
                    .getJSONArray("param")
                    .get(1);

            //getting the JSONObjet called "string" and the return the value int.
            errorCode = errorCodejson.getJSONObject("value")
                    .getJSONObject("array")
                    .getJSONObject("data")
                    .getJSONArray("value")
                    .getJSONObject(1)
                    .getInt("string");

        } catch (TransformerException ex) {
            L.error("TransformerException on transformer.transform {}", ex.getMessage());
        }
        return errorCode;
    }

}
