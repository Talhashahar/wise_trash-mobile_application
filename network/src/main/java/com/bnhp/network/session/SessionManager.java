package com.bnhp.network.session;

import android.text.TextUtils;
import com.bnhp.network.BuildConfig;
import okhttp3.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionManager {
    private List<Cookie> mCookies;
    private String mToken;
    private String mAccountNumber;
    private String mBankNumber;
    private String mBranchNumber;
    private String mAppId;
    private String mGuid;
    private String mServerNumber;
    private boolean mOverrideServer;
    private HashMap<String, String> mHeaders;
    private boolean mDebug;
    private static SessionManager instance;


    public static SessionManager getInstance(){
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public String getQueryAccount() {
        if(!TextUtils.isEmpty(mBankNumber) && !TextUtils.isEmpty(mBranchNumber) && !TextUtils.isEmpty(mAccountNumber)) {
            return String.format("%s-%s-%s", mBankNumber, mBranchNumber, mAccountNumber);
        } else {
            throw new NullPointerException("missing aacount info");
        }

    }

    private SessionManager() {
        mCookies = new ArrayList<>();
        mHeaders = new HashMap<>();

        if(BuildConfig.DEBUG == true) {
            mBankNumber = "12";
            mBranchNumber = "123";
            mAccountNumber = "55555";
            mToken = "myToken";
            mGuid = "myGuid";
            mAppId = "myAppId";
        }
    }

    public List<Cookie> getCookies() {
        return mCookies;
    }

    public void setCookies(List<Cookie> cookies) {
        mCookies = cookies;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getAppId() {
        return mAppId;
    }

    public void addHeader(String headerName, String headerValue) {
        mHeaders.put(headerName, headerValue);
    }

    public String getHeader(String headerName) {
        return mHeaders.get(headerName);
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String guid) {
        mGuid = guid;
    }

    public String getServerNumber() {
        return mServerNumber;
    }

    public void setServerNumber(String serverNumber) {
        mServerNumber = serverNumber;
    }

    public boolean isOverrideServer() {
        return mOverrideServer;
    }

    public void setOverrideServer(boolean overrideServer) {
        mOverrideServer = overrideServer;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public void setDebug(boolean debug) {
        mDebug = debug;
    }


}
