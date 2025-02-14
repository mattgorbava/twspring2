package com.example.twspring2.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
public class AuthenticatedUser extends User implements OidcUser, Serializable {
    private String email;
    private String name;
    private String password;
    private boolean isOAuth2;
    private Map<String, Object> claims;
    private OidcUserInfo userInfo;
    private OidcIdToken idToken;
    private Map<String, Object> attributes;

    public AuthenticatedUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities.stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getAuthority()))
                .collect(Collectors.toList()));
        this.email = username;
        this.password = password;
    }

    public AuthenticatedUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = username;
        this.password = password;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        super.getAuthorities().clear();
        super.getAuthorities().addAll(authorities);
    }
}
