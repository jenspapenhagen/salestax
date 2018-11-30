/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.salestax.boundary;

import eu.papenhagen.salestax.control.BBFConnector;
import eu.papenhagen.salestax.control.XmlRpcController;

import javax.inject.Inject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * a REST API for better and cleaner woring with the german sales tax number
 * validator form the german fed (Bundeszentralamt für Steuern) (Umsatzsteuer
 * Nummern Validator)
 *
 * @author jens.papenhagen
 */
@Path("grep")
@Produces(MediaType.APPLICATION_JSON)
public class GrepResource {

    private static final Logger L = LoggerFactory.getLogger(GrepResource.class);

    @Inject
    private XmlRpcController xmlRpcCon;

    @Inject
    private BBFConnector con;

    /**
     * This is the basic Methode to call the XML-RPC API form BFF and return a
     * better readyle JSON This methode only return a simple confirmation of the
     * given tax number
     * <br/>
     * more infos for the XML-RPC API form BFF (only in German)
     * https://evatr.bff-online.de/eVatR/xmlrpc/schnittstelle
     *
     * UstId_1 your own tax number and UstId_2 the requesting tax number
     *
     * @param ustId_1
     * @param ustId_2
     * @return a good readlybe JSON
     */
    @GET
    @Path("/base/{udid1}/{udid2}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject base(@PathParam("udid1") String ustId_1, @PathParam("udid2") String ustId_2) {
        L.debug("staring grepping base");

        //valid. input param agains emptyness
        if (ustId_1 == null || ustId_2 == null) {
            L.error("missing input parameter");
            return null;
        }

        String query = "UstId_1=" + ustId_1 + "&UstId_2=" + ustId_2 + "&Druck=nein";
        String rpcFormatedString = con.grepURL(query);
        if (rpcFormatedString == null) {
            L.error("Error on creating grepping the URL");
            return null;
        }

        int errorCode = xmlRpcCon.getErrorCode(rpcFormatedString);

        return buildJsonResonse(errorCode);
    }

    /**
     * This is the advanced Methode to get a fully qualified confirmation
     *
     * <br/>
     * more infos for the XML-RPC API form BFF (only in German)
     * https://evatr.bff-online.de/eVatR/xmlrpc/schnittstelle
     *
     * UstId_1 your own tax number and UstId_2 the requesting tax number company
     * need the full name of the organisation (Example: Mustermann Computer
     * GmbH) location only need the name of the city/vil. (Example: Hamburg)
     *
     * @param ustId_1
     * @param ustId_2
     * @param company
     * @param location
     * @return
     */
    @GET
    @Path("/advanced/{udid1}/{udid2}/{company}/{location}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject advanced(@PathParam("udid1") String ustId_1,
            @PathParam("udid2") String ustId_2,
            @PathParam("company") String company,
            @PathParam("location") String location) {
        L.debug("staring grepping advanced");

        //valid. input param agains emptyness
        if (ustId_1 == null || ustId_2 == null || company == null || location == null) {
            L.error("missing input parameter");
            return null;
        }

        //building the query
        String query = "UstId_1=" + ustId_1 + "&UstId_2=" + ustId_2 + "&Firmenname=" + company + "&Ort=" + location + "&Druck=nein";
        L.info("The Querry is {}", query);
        String rpcFormatedString = con.grepURL(query);
        if (rpcFormatedString == null) {
            L.error("Error on creating grepping the URL");
            return null;
        }
        int errorCode = xmlRpcCon.getErrorCode(rpcFormatedString);

        return buildJsonResonse(errorCode);
    }

    /**
     * this methode is building up the json respone
     *
     * @param errorCode
     * @return JsonObject with the errorCode and the description
     */
    private JsonObject buildJsonResonse(int errorCode) {
        String description = xmlRpcCon.getErrorCodeDescription(errorCode);

        //build up a Json Object as response
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("ErrorCode", errorCode);
        builder.add("ErrorCodeDescription", description);

        return builder.build();
    }
}
