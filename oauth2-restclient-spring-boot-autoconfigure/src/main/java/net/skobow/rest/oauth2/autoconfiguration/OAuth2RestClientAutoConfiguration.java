/*
 * MIT License
 *
 * Copyright (c) 2019 Sven Kobow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.skobow.rest.oauth2.autoconfiguration;

import net.skobow.rest.InMemoryUserTokenService;
import net.skobow.rest.OAuth2RestClient;
import net.skobow.rest.UserTokenService;
import net.skobow.rest.oauth2.AccessTokenDecoder;
import net.skobow.rest.oauth2.ClientCredentialsGrant;
import net.skobow.rest.oauth2.OAuth2Grant;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class OAuth2RestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OAuth2RestClient oAuth2RestClient(final OAuth2Grant oAuth2Grant, final RestTemplate oAuth2RestTemplate) {
        return new OAuth2RestClient(oAuth2RestTemplate, oAuth2Grant);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2Grant defaultOAuth2Grant(
            @Value("${oauth2.client.client_id:client_id}") final String clientId,
            @Value("${oauth2.client.client_secret:@null}") final char[] clientSecret,
            @Value("${oauth2.client.scope:@null}") final String scope,
            @Value("${oauth2.client.token_uri:http://localhost/token}") final URI tokenUri,
            final RestTemplate restTemplate,
            final UserTokenService userTokenService,
            final AccessTokenDecoder accessTokenDecoder) {
        return new ClientCredentialsGrant(clientId, clientSecret, scope, tokenUri, restTemplate, userTokenService, accessTokenDecoder);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserTokenService defaultUserTokenService() {
        return new InMemoryUserTokenService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "oAuth2RestTemplate")
    public RestTemplate oAuth2RestTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        // Enforce TLSv1.2 over TLSv1.3 due to Bug JDK-8211806 in JDK 11.0.1
        // see https://bugs.openjdk.java.net/browse/JDK-8211806 for more information
        final SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);

        final CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(context)
                .build();
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}
