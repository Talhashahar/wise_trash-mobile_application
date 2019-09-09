package com.bnhp.network;


import com.bnhp.network.annotations.ServerUrl;
import com.bnhp.network.baseresponses.BaseResponse;
import com.bnhp.network.calladaper.RxThreadCallAdapter;
import com.bnhp.network.calladaper.errorhandling.TrashWiseExaption;
import com.bnhp.network.calladaper.errorhandling.TrashWiseHttpError;
import com.bnhp.network.cookiehandling.RestCookieJar;
import com.bnhp.network.serverconfigs.ServerConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.lang.annotation.Annotation;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class NetworkApi {
    private final long TIMEOUT = 5;

    private static NetworkApi mInstance;
    private ServerConfig mConfig;
    private String serverUrl;


    private NetworkApi(ServerConfig config) {
        mConfig = config;
    }

    public static NetworkApi getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("you must call initNetworkApi before you start working");
        }
        return mInstance;
    }

    public static void initNetworkApi(ServerConfig config) {
        if (mInstance == null) {
            synchronized (NetworkApi.class) {
                if (mInstance == null) {
                    mInstance = new NetworkApi(config);
                }
            }
        }
    }

    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();

        if (BuildConfig.DEBUG) {
            trustEveryone(okHttpClient);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.interceptors().add(interceptor);
        } else {
            okHttpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession sslSession) {
                    return serverUrl.contains(hostname);
                }
            });
            okHttpClient.connectTimeout(TIMEOUT, TimeUnit.MINUTES);
            okHttpClient.readTimeout(TIMEOUT, TimeUnit.MINUTES);
            okHttpClient.writeTimeout(TIMEOUT, TimeUnit.MINUTES);
        }

        okHttpClient.cookieJar(new RestCookieJar());


        return okHttpClient.build();
    }

    public <T> T getRestApi(final Class<T> service) {



        if (service.isAnnotationPresent(ServerUrl.class)) {
            Annotation annotation = service.getAnnotation(ServerUrl.class);
            ServerUrl serverUrlAnn = (ServerUrl) annotation;
            serverUrl = serverUrlAnn.serverUrl();
        } else {
            serverUrl = mConfig.getBaseUrl();
        }



        Gson gson = new GsonBuilder().setLenient().create();
        serverUrl = new StringBuilder(serverUrl).toString();

        Retrofit retrofit = new Retrofit.Builder()
                .client(getHttpClient())
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxThreadCallAdapter.create(Schedulers.io(), AndroidSchedulers.mainThread()))
                .build();

        return retrofit.create(service);
    }

    private void trustEveryone(OkHttpClient.Builder okHttpClient) {
        try {
            final X509TrustManager[] trustAllCerts = new X509TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            okHttpClient.sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts[0]);
            okHttpClient.connectTimeout(TIMEOUT, TimeUnit.MINUTES);
            okHttpClient.readTimeout(TIMEOUT, TimeUnit.MINUTES);
            okHttpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends BaseResponse> Disposable makeNetworkRequest(Observable<T> o, final Callback<T> callback) {
        if (callback != null && o != null) {
            return o.subscribeWith(new DisposableObserver<T>() {
                @Override
                public void onNext(T t) {
                    callback.onSuccess(t);
                }

                @Override
                public void onError(Throwable e) {
                    if(((TrashWiseExaption)e).getTrashWiseHttpError() == TrashWiseHttpError.EMPTY_BODY) {
                        callback.onEmptyResponse((TrashWiseExaption)e);
                    } else {
                        callback.onError((TrashWiseExaption)e);
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            throw new NullPointerException("Check nulls on parameters");
        }
    }

    public <T extends BaseResponse> Disposable makeNetworkRequest(Single<T> o, final Callback<T> callback) {
        if (callback != null && o != null) {
            return o.subscribeWith(new DisposableSingleObserver<T>() {
                @Override
                public void onSuccess(T t) {
                    callback.onSuccess(t);
                }

                @Override
                public void onError(Throwable e) {
                    if(((TrashWiseExaption)e).getTrashWiseHttpError() == TrashWiseHttpError.EMPTY_BODY) {
                        callback.onEmptyResponse((TrashWiseExaption)e);
                    } else {
                        callback.onError((TrashWiseExaption)e);
                    }
                }
            });
        } else {
            throw new NullPointerException("Check nulls on parameters");
        }
    }

    public <T extends BaseResponse> Disposable makeFlowableRequest(Observable<T> o, BackpressureStrategy strategy, final Callback<T> callback) {
        if (callback != null && o != null) {
            return o.toFlowable(strategy)
                    .subscribeWith(new DisposableSubscriber<T>() {
                        @Override
                        public void onNext(T t) {
                            callback.onSuccess(t);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(((TrashWiseExaption)e).getTrashWiseHttpError() == TrashWiseHttpError.EMPTY_BODY) {
                                callback.onEmptyResponse((TrashWiseExaption)e);
                            } else {
                                callback.onError((TrashWiseExaption)e);
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            throw new NullPointerException("Check nulls on parameters");
        }
    }

    public <T extends BaseResponse> Disposable makeMaybeRequest(Maybe<T> o, final Callback<T> callback) {
        if (callback != null && o != null) {
            return o.subscribeWith(new DisposableMaybeObserver<T>() {
                @Override
                public void onSuccess(T t) {
                    callback.onSuccess(t);
                }

                @Override
                public void onError(Throwable e) {
                    if(((TrashWiseExaption)e).getTrashWiseHttpError() == TrashWiseHttpError.EMPTY_BODY) {
                        callback.onEmptyResponse((TrashWiseExaption)e);
                    } else {
                        callback.onError((TrashWiseExaption)e);
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            throw new NullPointerException("Check nulls on parameters");
        }
    }
}
