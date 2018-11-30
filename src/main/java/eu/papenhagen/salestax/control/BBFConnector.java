/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.salestax.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connect to the Bundeszentralamt für Steuern 
 * using there BBF XML-RPC API
 *
 * @author jens.papehagen
 */
@Named
public class BBFConnector implements Serializable{

    private static final Logger L = LoggerFactory.getLogger(BBFConnector.class);

    private String BASEURL = "https://evatr.bff-online.de/evatrRPC";

    /**
     * Handel all the URL grepping
     *
     * @param query
     * @return a xml-rpc formated String
     */
    public String grepURL(String query) {
        L.info(BASEURL + "?" + query);
        String content = null;
        try {
            URLConnection connection = new URL(BASEURL + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
            L.debug("The Content Typ is {}", connection.getContentType());

            InputStream response = connection.getInputStream();

            if (response != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8));
                content = br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            if (content == null) {
                L.error("the content is null");
            }
            //adding the "root" element on this xml-rpc repsonse
            content = "<?xml version=\"1.0\"?>\n" + "<methodResponse> \n" + content + "\n</methodResponse>";

        } catch (MalformedURLException ex) {
            L.error("MalformedURLException URL {}", ex.getMessage());
        } catch (IOException ex) {
            L.error("IOException on inputStream {}", ex.getMessage());
        }

        return content;
    }
}
