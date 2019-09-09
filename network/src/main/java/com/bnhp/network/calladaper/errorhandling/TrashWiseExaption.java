package com.bnhp.network.calladaper.errorhandling;

import com.bnhp.network.baseresponses.BaseResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class TrashWiseExaption extends RuntimeException {

    public static TrashWiseExaption httpError(String url, Response response, Retrofit retrofit, int code) {
        String message = response.code() + " " + response.message();
        return new TrashWiseExaption(message, url, response, HttpError.HTTP, getPoalimHttpError(code),null, retrofit);
    }

    public static TrashWiseExaption networkError(IOException exception) {
        return new TrashWiseExaption(exception.getMessage(), null, null, HttpError.NETWORK, null, exception, null);
    }

    public static TrashWiseExaption nullError(NullPointerException exception) {
        return new TrashWiseExaption(exception.getMessage(), null, null, HttpError.UNEXPECTED, TrashWiseHttpError.EMPTY_BODY, exception, null);
    }

    public static TrashWiseExaption unexpectedError(Throwable exception) {
        return new TrashWiseExaption(exception.getMessage(), null, null, HttpError.UNEXPECTED, null, exception, null);
    }

    /** Identifies the event httpError which triggered a {@link TrashWiseExaption}. */
    public enum HttpError {
        /** An {@link IOException} occurred while communicating to the server. */
        NETWORK,
        /** A non-200 HTTP status code was received from the server. */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED,
    }

    private String url;
    private Response response;
    private HttpError httpError;
    private TrashWiseHttpError trashWiseHttpError;
    private Retrofit retrofit;
    private String messageException;
    private String messageCode;
    private String serviceCode;

    public TrashWiseExaption(String message, String url, Response response, HttpError httpError, TrashWiseHttpError trashWiseHttpError, Throwable exception, Retrofit retrofit) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.httpError = httpError;
        this.trashWiseHttpError = trashWiseHttpError;
        this.retrofit = retrofit;
        try {
            BaseResponse baseResponse = getErrorBodyAs(BaseResponse.class);
            if(baseResponse != null) {

//                messageException = baseResponse.getMessageException();
//                messageCode = baseResponse.getMessageCode();
//                serviceCode = baseResponse.getServiceCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** The request URL which produced the error. */
    public String getUrl() {
        return url;
    }

    /** Response object containing status code, headers, body, etc. */
    public Response getResponse() {
        return response;
    }

    /** The event httpError which triggered this error. */
    public HttpError getHttpError() {
        return httpError;
    }

    public TrashWiseHttpError getTrashWiseHttpError() {
        return trashWiseHttpError;
    }

    public void setTrashWiseHttpError(TrashWiseHttpError trashWiseHttpError) {
        this.trashWiseHttpError = trashWiseHttpError;
    }

    /** The Retrofit this request was executed on */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public String getMessageException() {
        return messageException;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);
        return converter.convert(response.errorBody());
    }

    public static TrashWiseHttpError getPoalimHttpError(int code) {
        return TrashWiseHttpError.PERMISSION;
    }
}