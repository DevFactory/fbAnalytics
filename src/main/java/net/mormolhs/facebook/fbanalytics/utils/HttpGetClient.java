package net.mormolhs.facebook.fbanalytics.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpStatus;

import java.io.IOException;

/**
 * Created by toikonomakos on 3/31/14.
 */
public class HttpGetClient {

    public String sendGetRequest(String url) throws URIException {
        // Create an instance of HttpGetClient.
        HttpClient client = new HttpClient();
        System.out.println(url);
        // Create a method instance.
        HttpMethod method = new GetMethod(url);
        // Provide custom retry handler is necessary
        String responseBody = null;

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            responseBody = method.getResponseBodyAsString();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            System.out.println(responseBody);

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return responseBody;
    }

}
