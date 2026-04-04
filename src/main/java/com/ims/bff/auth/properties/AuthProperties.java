package com.ims.bff.auth.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ims.auth")
public class AuthProperties {

    private final Basic basic = new Basic();
    private final List<ExternalProvider> oauthProviders = new ArrayList<>();
    private final List<ExternalProvider> ssoProviders = new ArrayList<>();

    public Basic getBasic() {
        return basic;
    }

    public List<ExternalProvider> getOauthProviders() {
        return oauthProviders;
    }

    public List<ExternalProvider> getSsoProviders() {
        return ssoProviders;
    }

    public static class Basic {
        private boolean enabled = true;
        private String username = "ims-user";
        private String password = "ims-password";
        private String role = "USER";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public static class ExternalProvider {
        private String key;
        private String displayName;
        private boolean enabled = true;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
