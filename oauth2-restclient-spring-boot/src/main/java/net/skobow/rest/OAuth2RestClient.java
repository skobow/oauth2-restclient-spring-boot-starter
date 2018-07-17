/*
 * Copyright (c) 2018 Sven Kobow
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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.skobow.rest;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class OAuth2RestClient {

    private final RestTemplate restTemplate;
    private final UserTokenService userTokenService;

    public OAuth2RestClient(final RestTemplate restTemplate, final UserTokenService userTokenService) {
        this.restTemplate = restTemplate;
        this.userTokenService = userTokenService;
    }

    public <T> ResponseEntity<T> get(final URI uri) {
        new RequestEntity(getHeaders(), HttpMethod.GET, uri);

        return null;
    }

    private HttpHeaders getHeaders() {
        return HttpHeaders.EMPTY;
    }

    public <T> ResponseEntity<T> post(final URI uri) {
        return null;
    }

    private <T> ResponseEntity<T> exchange(final RequestEntity requestEntity, final Class<T> responseType) {
        return restTemplate.exchange(requestEntity, responseType);
    }
}
