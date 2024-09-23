package eu.europeanstudentcard.generator.client.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europeanstudentcard.generator.client.exceptions.ApiException;
import eu.europeanstudentcard.generator.client.exceptions.ErrorResponse;
import eu.europeanstudentcard.generator.client.exceptions.QRClientException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class QRRestRequestBuilder {

    private final CloseableHttpClient httpClient;
    private final ClassicRequestBuilder requestBuilder;
    private final URIBuilder uriBuilder;
    private final ObjectMapper objectMapper;

    public QRRestRequestBuilder(String httpMethod, String url) throws URISyntaxException {
        this.httpClient = HttpClients.createDefault();
        this.uriBuilder = new URIBuilder(url);
        this.objectMapper = new ObjectMapper();
        switch (httpMethod) {
            case "GET":
                this.requestBuilder = ClassicRequestBuilder.get();
                break;
            case "POST":
                this.requestBuilder = ClassicRequestBuilder.post();
                break;
            case "PUT":
                this.requestBuilder = ClassicRequestBuilder.put();
                break;
            case "DELETE":
                this.requestBuilder = ClassicRequestBuilder.delete();
                break;
            default:
                throw new IllegalArgumentException("HTTP method not supported");
        }
    }

    public QRRestRequestBuilder addQueryParam(String param, Object value) {
        this.uriBuilder.addParameter(param, value.toString());
        return this;
    }

    public QRRestRequestBuilder addQueryParams(Map<String, String> params) {
        params.forEach(this.uriBuilder::addParameter);
        return this;
    }

    public QRRestRequestBuilder addHeaders(Map<String, String> params) {
        params.forEach(this.requestBuilder::addHeader);
        return this;
    }

    public QRRestRequestBuilder addHeaders(ContentType contentType, ContentType accept) {
        if (contentType != null) {
            this.requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, contentType.toString());
        }
        if (accept != null) {
            this.requestBuilder.addHeader(HttpHeaders.ACCEPT, accept.toString());
        }
        return this;
    }

    public QRRestRequestBuilder addBody(Object body, ContentType contentType) throws IOException {
        if (body != null) {
            String jsonBody = objectMapper.writeValueAsString(body);
            StringEntity entity = new StringEntity(jsonBody, contentType);
            this.requestBuilder.setEntity(entity);
        }
        return this;
    }

    public <T> ESCExecute<T> buildRequest(TypeReference<T> responseType) {
        return () -> {
            try {
                URI uri = this.uriBuilder.build();
                this.requestBuilder.setUri(uri);
                return executeRequest(responseType, uri.toString());
            } catch (IOException | ParseException | URISyntaxException e) {
                throw new QRClientException(e);
            }
        };
    }

    private <T> T executeRequest(TypeReference<T> responseType, String url) throws IOException, ParseException {

        HttpClientResponseHandler<T> responseHandler = response -> handleResponse(response, responseType, url);

        ClassicHttpRequest request = this.requestBuilder.build();

        return httpClient.execute(request, responseHandler);
    }

    private <T> T handleResponse(ClassicHttpResponse response, TypeReference<T> responseType, String url) throws IOException, ParseException {
        int statusCode = response.getCode();
        HttpEntity entity = response.getEntity();

        if (entity == null) {
            throw new QRClientException("Response contains no content");
        }

        String responseBody = EntityUtils.toString(entity);

        if (statusCode < 300) {
            if (responseType.getType().equals(byte[].class)) {
                return (T) new byte[0];
            } else if (responseType.getType().equals(String.class)) {
                return (T) responseBody;
            } else {
                return objectMapper.readValue(responseBody, responseType);
            }
        } else {
            return handleErrorResponse(responseBody, url);
        }
    }

    private <T> T handleErrorResponse(String responseBody, String url) throws IOException {
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
        throw new ApiException(errorResponse, url);
    }


    public interface ESCExecute<T> {
        T execute() throws IOException;
    }
}
