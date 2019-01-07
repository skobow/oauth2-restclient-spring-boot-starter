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

package net.skobow.rest;

import net.skobow.rest.oauth2.ClientCredentialsGrant;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class OAuth2RestClientTest {

    private static final String ACCESS_TOKEN = "token";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String TOKEN_TYPE = "bearer";
    private static final String CLIENT_ID = "client";
    private static final String CLIENT_SECRET = "secret";
    private static final String SCOPE = "scope";
    private static final String HTTP_LOCALHOST_TOKEN = "http://localhost/token";
    private static final String HTTP_LOCALHOST = "http://localhost/";
    private OAuth2RestClient client;
    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setUp() {
        final RestTemplate restTemplate = new RestTemplate();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServiceServer
                .expect(requestTo(HTTP_LOCALHOST_TOKEN))
                .andExpect(content().string(String.format("grant_type=client_credentials&client_id=%s&client_secret=%s&scope=%s", CLIENT_ID, CLIENT_SECRET, SCOPE)))
                .andRespond(withSuccess());

        final ClientCredentialsGrant grant = new ClientCredentialsGrant(
                CLIENT_ID,
                CLIENT_SECRET.toCharArray(),
                SCOPE,
                URI.create(HTTP_LOCALHOST_TOKEN),
                restTemplate,
                new InMemoryUserTokenService(),
                httpResponse -> new UserToken(ACCESS_TOKEN, REFRESH_TOKEN, TOKEN_TYPE, LocalDateTime.now()));

        client = new OAuth2RestClient(restTemplate, grant);
    }

    @Test
    @SuppressWarnings("squid:S00100")
    public void get_should_request_token_and_set_authorization_header() {
        mockRestServiceServer
                .expect(requestTo(HTTP_LOCALHOST))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andRespond(withSuccess());

        final ResponseEntity<Object> responseEntity = client.get(HTTP_LOCALHOST, Object.class);
        assertThat(responseEntity).isNotNull();
    }

    @Test
    @SuppressWarnings("squid:S00100")
    public void post_should_request_token_and_set_authorization_header() {
        mockRestServiceServer
                .expect(requestTo(HTTP_LOCALHOST))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andRespond(withSuccess());

        final ResponseEntity<Object> responseEntity = client.post(HTTP_LOCALHOST, Object.class, null, Object.class);
        assertThat(responseEntity).isNotNull();
    }
}