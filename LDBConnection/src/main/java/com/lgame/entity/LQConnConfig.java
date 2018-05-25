package com.lgame.entity;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/25.
 */
public interface LQConnConfig {

    public static class LQRedisConnConfig implements LQConnConfig{
        private String url;

        public String getUrl() {
            return url;
        }

        public LQRedisConnConfig(String url) {
            this.url = url;
        }
    }

    public static class LQDBConnConfig extends LQRedisConnConfig{
        private String userName;
        private String password;

        public LQDBConnConfig(String url, String userName, String password) {
            super(url);
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }
}
