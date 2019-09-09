package com.bnhp.network.serverconfigs;

import okhttp3.Interceptor;

public interface IServerConfig {

    String getPreLoginUrl();

    String getBaseUrl();

    String getStaticUrl();

    String getServerNumber();

    Interceptor getInterceptor();
}
