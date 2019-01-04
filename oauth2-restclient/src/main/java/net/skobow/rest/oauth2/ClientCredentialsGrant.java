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

package net.skobow.rest.oauth2;

import net.skobow.rest.HeadersEnhancer;
import net.skobow.rest.UserToken;
import net.skobow.rest.UserTokenService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

public class ClientCredentialsGrant implements OAuth2Grant, DisposableBean {

    private final String clientId;
    private final char[] clientSecret;
    private final String scope;
    private final URI tokenUri;
    private final RestTemplate restTemplate;
    private final UserTokenService userTokenService;
    private final AccessTokenDecoder accessTokenDecoder;

    private HeadersEnhancer authorizationHeadersEnhancer;
    private HeadersEnhancer requestHeadersEnhancer;

    public ClientCredentialsGrant(
            final String clientId,
            final char[] clientSecret,
            final String scope,
            final URI tokenUri,
            final RestTemplate restTemplate,
            final UserTokenService userTokenService,
            final AccessTokenDecoder accessTokenDecoder) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
        this.tokenUri = tokenUri;
        this.restTemplate = restTemplate;
        this.userTokenService = userTokenService;
        this.accessTokenDecoder = accessTokenDecoder;
    }

    @Override
    public RequestEntity getRequest(final URI uri, final HttpMethod httpMethod) {

        UserToken userToken = userTokenService.getUserToken(clientId);
        if (userToken == null || userToken.isExpired()) {
            userToken = getAccessToken();
            userTokenService.setUserToken(clientId, userToken);
        }

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(userToken.getAccessToken());

        if (requestHeadersEnhancer != null) {
            requestHeadersEnhancer.enhance(httpHeaders);
        }

        return new RequestEntity(httpHeaders, HttpMethod.GET, uri);
    }

    private UserToken getAccessToken() {

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", new String(clientSecret));
        body.add("scope", scope);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (authorizationHeadersEnhancer != null) {
            authorizationHeadersEnhancer.enhance(httpHeaders);
        }

        final RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(body, httpHeaders, HttpMethod.POST, tokenUri);
        final ResponseEntity responseEntity = restTemplate.exchange(requestEntity, Object.class);
        return accessTokenDecoder.decode(responseEntity);
    }

    public void destroy() {
        Arrays.fill(clientSecret, '0');
    }

    @Override
    public HeadersEnhancer getAuthorizationHeadersEnhancer() {
        return authorizationHeadersEnhancer;
    }

    @Override
    public void setAuthorizationHeadersEnhancer(final HeadersEnhancer authorizationHeadersEnhancer) {
        this.authorizationHeadersEnhancer = authorizationHeadersEnhancer;
    }

    @Override
    public HeadersEnhancer getRequestHeadersEnhancer() {
        return requestHeadersEnhancer;
    }

    @Override
    public void setRequestHeadersEnhancer(final HeadersEnhancer requestHeadersEnhancer) {
        this.requestHeadersEnhancer = requestHeadersEnhancer;
    }
}
