/*
 * MIT License
 *
 * Copyright (c) 2020 Sven Kobow
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

public class AuthorizationCodeGrant implements OAuth2Grant {

    @Override
    public RequestEntity getRequest(final URI uri, final HttpHeaders httpHeaders, final HttpMethod httpMethod) {
        return null;
    }

    @Override
    public <T> RequestEntity<T> getRequest(final URI uri, final HttpHeaders httpHeaders, final HttpMethod httpMethod, final T body, final Class<T> type) {
        return null;
    }

    @Override
    public HeadersEnhancer getAuthorizationHeadersEnhancer() {
        return null;
    }

    @Override
    public HeadersEnhancer getRequestHeadersEnhancer() {
        return null;
    }

    @Override
    public void setRequestHeadersEnhancer(final HeadersEnhancer requestHeadersEnhancer) {

    }

    @Override
    public void setAuthorizationHeadersEnhancer(final HeadersEnhancer authorizationHeadersEnhancer) {

    }
}
