package com.bnhp.network.serverconfigs;

public class ServerConfig {

    private String mBaseUrl;



    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }



    public static class Builder {
        private String baseUrl;


        public Builder(String baseUrl) {
            this.baseUrl = baseUrl;
        }





        public ServerConfig build() {
            ServerConfig serverConfig = new ServerConfig();

            serverConfig.setBaseUrl(this.baseUrl);



            return serverConfig;
        }
    }

}
