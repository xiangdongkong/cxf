/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.jaxrs.rx.client;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.AsyncClient;

import rx.Observable;

public class ObservableRxInvokerImpl implements ObservableRxInvoker {
    private ExecutorService ex;
    private AsyncClient wc;
    public ObservableRxInvokerImpl(AsyncClient wc, ExecutorService ex) {
        this.wc = wc;
        this.ex = ex;
    }
    
    @Override
    public Observable<Response> get() {
        return get(Response.class);
    }

    @Override
    public <T> Observable<T> get(Class<T> responseType) {
        return method(HttpMethod.GET, responseType);
    }

    @Override
    public <T> Observable<T> get(GenericType<T> responseType) {
        return method(HttpMethod.GET, responseType);
    }

    @Override
    public Observable<Response> put(Entity<?> entity) {
        return put(entity, Response.class);
    }

    @Override
    public <T> Observable<T> put(Entity<?> entity, Class<T> responseType) {
        return method(HttpMethod.PUT, entity, responseType);
    }

    @Override
    public <T> Observable<T> put(Entity<?> entity, GenericType<T> responseType) {
        return method(HttpMethod.PUT, entity, responseType);
    }

    @Override
    public Observable<Response> post(Entity<?> entity) {
        return post(entity, Response.class);
    }

    @Override
    public <T> Observable<T> post(Entity<?> entity, Class<T> responseType) {
        return method(HttpMethod.POST, entity, responseType);
    }

    @Override
    public <T> Observable<T> post(Entity<?> entity, GenericType<T> responseType) {
        return method(HttpMethod.POST, entity, responseType);
    }

    @Override
    public Observable<Response> delete() {
        return delete(Response.class);
    }

    @Override
    public <T> Observable<T> delete(Class<T> responseType) {
        return method(HttpMethod.DELETE, responseType);
    }

    @Override
    public <T> Observable<T> delete(GenericType<T> responseType) {
        return method(HttpMethod.DELETE, responseType);
    }

    @Override
    public Observable<Response> head() {
        return method(HttpMethod.HEAD);
    }

    @Override
    public Observable<Response> options() {
        return options(Response.class);
    }

    @Override
    public <T> Observable<T> options(Class<T> responseType) {
        return method(HttpMethod.OPTIONS, responseType);
    }

    @Override
    public <T> Observable<T> options(GenericType<T> responseType) {
        return method(HttpMethod.OPTIONS, responseType);
    }

    @Override
    public Observable<Response> trace() {
        return trace(Response.class);
    }

    @Override
    public <T> Observable<T> trace(Class<T> responseType) {
        return method("TRACE", responseType);
    }

    @Override
    public <T> Observable<T> trace(GenericType<T> responseType) {
        return method("TRACE", responseType);
    }

    @Override
    public Observable<Response> method(String name) {
        return method(name, Response.class);
    }

    @Override
    public Observable<Response> method(String name, Entity<?> entity) {
        return method(name, entity, Response.class);
    }

    @Override
    public <T> Observable<T> method(String name, Entity<?> entity, Class<T> responseType) {
        return doInvokeAsync(name, entity, responseType, responseType);
    }

    @Override
    public <T> Observable<T> method(String name, Entity<?> entity, GenericType<T> responseType) {
        return doInvokeAsync(name, entity, responseType.getRawType(), responseType.getType());
    }

    @Override
    public <T> Observable<T> method(String name, Class<T> responseType) {
        return doInvokeAsync(name, null, responseType, responseType);
    }

    @Override
    public <T> Observable<T> method(String name, GenericType<T> responseType) {
        return doInvokeAsync(name, null, responseType.getRawType(), responseType.getType());
    }
    
    protected <T> Observable<T> doInvokeAsync(String httpMethod, 
                                              Object body, 
                                              Class<?> respClass,
                                              Type outType) {
        JaxrsClientObservableCallback<T> cb = new JaxrsClientObservableCallback<T>(respClass, outType, ex);
        wc.prepareAsyncClient(httpMethod, body, null, null, respClass, outType, cb);
        return cb.getObservable();
    }
}
