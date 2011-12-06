/*******************************************************************************
 *
 * Copyright (c) 2011 Oracle Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *
 *    Winston Prakash
 *     
 *******************************************************************************/

package org.hudsonci.update.client;

import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.hudsonci.update.client.model.UpdateSiteMetadata;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.core.MediaType;

/**
 * Update Center Utilities
 * @author Winston Prakash
 */
public class UpdateCenterUtils {

    private static ObjectMapper jsonObjectMapper = new ObjectMapper();

    static {
        jsonObjectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    }

    public static UpdateSiteMetadata parseFromUrl(String updateCenterURL) throws IOException {
        try {
            WebRequest webRequest = new WebRequest(updateCenterURL);
            WebResponse webResponse = webRequest.get();
            if (webResponse.getResponseCode() < HttpURLConnection.HTTP_MULT_CHOICE) {
                String response = webResponse.getResponse();
                //System.out.println(response);

                if (response.startsWith("updateCenter.post(")) {
                    response = response.substring("updateCenter.post(".length());
                }

                if (response.endsWith(");")) {
                    response = response.substring(0, response.lastIndexOf(");"));
                }

                return parse(response);
            } else {
                throw new IOException("Error fetching Json. Server returned code - " + webResponse.getResponseCode());
            }
        } catch (URISyntaxException ex) {
            throw new IOException(ex.getCause());
        }

    }

    public static UpdateSiteMetadata parse(String jsonString) throws IOException {
        return jsonObjectMapper.readValue(jsonString, UpdateSiteMetadata.class);
    }

    public static String getAsString(UpdateSiteMetadata updateSiteMetadata) throws IOException {

        Writer writer = new StringWriter();
        jsonObjectMapper.writeValue(writer, updateSiteMetadata);
        return writer.toString();
    }


    public static UpdateSiteMetadata getNewUpdates(String baseUri, String pluginPath) throws IOException {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(baseUri).path("artifacts").queryParam("path", pluginPath);

        String json = webResource.accept(MediaType.TEXT_PLAIN).get(String.class);
        client.destroy();

        return parse(json);
    }

    public static boolean downloadFile(String uriStr, File target) throws IOException {
        InputStream stream = null;
        OutputStream out = null;
        try {
            HttpURLConnection connection;

            WebRequest webRequest = new WebRequest(uriStr);
            connection = webRequest.getConnection();

            //just make sure to connect with multiple connect attempt
            connection.connect();

            byte[] buff = new byte[4096];
            stream = new BufferedInputStream(connection.getInputStream());

            out = new FileOutputStream(target);
            
            int bytesRead;
            while ((bytesRead = stream.read(buff)) != -1) {
                out.write(buff, 0, bytesRead);
            }

            return false;
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        } finally {
            if (null != stream) {
                stream.close();
            }

            if (null != out) {
                out.close();
            }
        }
    }
}
