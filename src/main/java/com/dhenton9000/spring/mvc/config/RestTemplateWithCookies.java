 

package com.dhenton9000.spring.mvc.config;

 
import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @link https://stackoverflow.com/questions/22853321/resttemplate-client-with-cookies
 * https://devforum.okta.com/t/how-is-session-cookie-used-to-enable-sso/2184/2
 * https://support.okta.com/help/s/article/Okta-Groups-or-Attribute-Missing-from-Id-Token
 */
//@Component
public class RestTemplateWithCookies extends RestTemplate {

    private final List<HttpCookie> cookies = new ArrayList<>();

    public RestTemplateWithCookies() {
    }

    public RestTemplateWithCookies(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    public synchronized List<HttpCookie> getCoookies() {
        return cookies;
    }

    public synchronized void resetCoookies() {
        cookies.clear();
    }

    private void processHeaders(HttpHeaders headers) {
        final List<String> cooks = headers.get("Set-Cookie");
        if (cooks != null && !cooks.isEmpty()) {
            cooks.stream().map((c) -> HttpCookie.parse(c)).forEachOrdered((cook) -> {
                cook.forEach((a) -> {
                    HttpCookie cookieExists = cookies.stream().filter(x -> a.getName().equals(x.getName())).findAny().orElse(null);
                    if (cookieExists != null) {
                        cookies.remove(cookieExists);
                    }
                    cookies.add(a);
                });
            });
        }
    }

    @Override
    protected <T extends Object> T doExecute(URI url, HttpMethod method, final RequestCallback requestCallback, final ResponseExtractor<T> responseExtractor) throws RestClientException {
        final List<HttpCookie> cookieCollection = getCoookies();

        return super.doExecute(url, method, (ClientHttpRequest chr) -> {
            if (cookieCollection != null) {
                StringBuilder sb = new StringBuilder();
                cookieCollection.forEach((cookie) -> {
                    sb.append(cookie.getName()).append(cookie.getValue()).append(";");
                });
                chr.getHeaders().add("Cookie", sb.toString());
            }
            requestCallback.doWithRequest(chr);
        }, (ClientHttpResponse chr) -> {
            processHeaders(chr.getHeaders());
            return responseExtractor.extractData(chr);
        });
    }
}