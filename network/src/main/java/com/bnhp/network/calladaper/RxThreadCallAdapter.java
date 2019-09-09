package com.bnhp.network.calladaper;

import com.bnhp.network.calladaper.errorhandling.TrashWiseExaption;
import io.reactivex.*;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;
import retrofit2.*;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class RxThreadCallAdapter extends CallAdapter.Factory {

    private RxJava2CallAdapterFactory rxFactory = RxJava2CallAdapterFactory.create();
    private Scheduler subscribeScheduler;
    private Scheduler observeScheduler;

    public static RxThreadCallAdapter create(Scheduler subscribeScheduler, Scheduler observeScheduler) {
        return new RxThreadCallAdapter(subscribeScheduler, observeScheduler);
    }

    private RxThreadCallAdapter(Scheduler subscribeScheduler, Scheduler observeScheduler) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
    }

    @Override
    public CallAdapter<?, ?> get(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(type) == Observable.class) {
            CallAdapter<Object, Observable> callAdapter = (CallAdapter<Object, Observable>) rxFactory.get(type, annotations, retrofit);
            return callAdapter != null ? new ThreadCallAdapter(callAdapter, retrofit) : null;
        }

        if (getRawType(type) == Single.class) {
            CallAdapter<Object, Single> callAdapter = (CallAdapter<Object, Single>) rxFactory.get(type, annotations, retrofit);
            return callAdapter != null ? new ThreadCallAdapter(callAdapter, retrofit) : null;
        }

        if (getRawType(type) == Completable.class) {
            CallAdapter<Object, Completable> callAdapter = (CallAdapter<Object, Completable>) rxFactory.get(type, annotations, retrofit);
            return callAdapter != null ? new ThreadCallAdapter(callAdapter, retrofit) : null;
        }

        if (getRawType(type) == Flowable.class) {
            CallAdapter<Object, Flowable> callAdapter = (CallAdapter<Object, Flowable>) rxFactory.get(type, annotations, retrofit);
            return callAdapter != null ? new ThreadCallAdapter(callAdapter, retrofit) : null;
        }

        if (getRawType(type) == Maybe.class) {
            CallAdapter<Object, Maybe> callAdapter = (CallAdapter<Object, Maybe>) rxFactory.get(type, annotations, retrofit);
            return callAdapter != null ? new ThreadCallAdapter(callAdapter, retrofit) : null;
        }
        return null;

    }

    final class ThreadCallAdapter<R> implements CallAdapter<R, Object> {

        CallAdapter<R, Object> delegateAdapter;
        private final Retrofit retrofit;

        public ThreadCallAdapter(CallAdapter delegateAdapter, Retrofit retrofit) {
            this.delegateAdapter = delegateAdapter;
            this.retrofit = retrofit;
        }

        @Override
        public Type responseType() {
            return delegateAdapter.responseType();
        }

        @Override
        public Object adapt(Call<R> call) {

            if(delegateAdapter.adapt(call) instanceof Observable) {
                return ((Observable)delegateAdapter.adapt(call))
                        .subscribeOn(subscribeScheduler).observeOn(observeScheduler)
                        .onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                            @Override
                            public ObservableSource apply(Throwable throwable) throws Exception {
                                return Observable.error(asRetrofitException(throwable));
                            }
                        });
            } else if(delegateAdapter.adapt(call) instanceof Single) {

                return ((Single)delegateAdapter.adapt(call))
                        .subscribeOn(subscribeScheduler).observeOn(observeScheduler)
                        .onErrorResumeNext(new Function<Throwable, SingleSource>() {
                            @Override
                            public SingleSource apply(Throwable throwable) throws Exception {
                                return Single.error(asRetrofitException(throwable));
                            }
                        });
            } else if(delegateAdapter.adapt(call) instanceof Flowable) {

                return ((Flowable)delegateAdapter.adapt(call))
                        .subscribeOn(subscribeScheduler).observeOn(observeScheduler)
                        .onErrorResumeNext(new Function<Throwable, Publisher>() {
                            @Override
                            public Publisher apply(Throwable throwable) throws Exception {
                                return Flowable.error(asRetrofitException(throwable));
                            }
                        });
            } else if(delegateAdapter.adapt(call) instanceof Completable) {

                return ((Completable)delegateAdapter.adapt(call))
                        .subscribeOn(subscribeScheduler).observeOn(observeScheduler)
                        .onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                            @Override
                            public CompletableSource apply(Throwable throwable) throws Exception {
                                return Completable.error(asRetrofitException(throwable));
                            }
                        });
            } else if(delegateAdapter.adapt(call) instanceof Maybe) {

                return ((Maybe)delegateAdapter.adapt(call))
                        .subscribeOn(subscribeScheduler).observeOn(observeScheduler)
                        .onErrorResumeNext(new Function<Throwable, MaybeSource>() {
                            @Override
                            public MaybeSource apply(Throwable throwable) throws Exception {
                                return Maybe.error(asRetrofitException(throwable));
                            }
                        });
            } else {
                return null;
            }
        }

        private TrashWiseExaption asRetrofitException(Throwable throwable) {
            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                return TrashWiseExaption.httpError(response.raw().request().url().toString(), response, retrofit, httpException.code());
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return TrashWiseExaption.networkError((IOException) throwable);
            }

            if(throwable instanceof  NullPointerException) {
                return TrashWiseExaption.nullError((NullPointerException) throwable);
            }

            // We don't know what happened. We need to simply convert to an unknown error

            return TrashWiseExaption.unexpectedError(throwable);
        }
    }


}
