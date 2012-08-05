/*
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
package org.apache.deltaspike.cdise.weld;

import javax.enterprise.inject.Typed;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * This is the internal helper class for JDK proxies
 * TODO remove it and use org.apache.deltaspike.core.impl.util.ProxyUtils instead (discussion needed)
 */

@Typed()
abstract class ProxyUtils
{
    private ProxyUtils()
    {
        // prevent instantiation
    }

    static <T> T createProxy(Class<T> type, InvocationHandler invocationHandler)
    {
        return type.cast(Proxy.newProxyInstance(getClassLoader(null),
                new Class<?>[]{type}, invocationHandler));
    }

    private static ClassLoader getClassLoader(Object o)
    {
        return getClassLoaderInternal(o);

        //TODO if we don't introduce the dependency to core, we have to copy GetClassLoaderAction as well
        /*
        if (System.getSecurityManager() != null)
        {
            return AccessController.doPrivileged(new GetClassLoaderAction(o));
        }
        else
        {
            return getClassLoaderInternal(o);
        }
        */
    }

    private static ClassLoader getClassLoaderInternal(Object o)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null && o != null)
        {
            loader = o.getClass().getClassLoader();
        }

        if (loader == null)
        {
            loader = ProxyUtils.class.getClassLoader();
        }

        return loader;
    }
}