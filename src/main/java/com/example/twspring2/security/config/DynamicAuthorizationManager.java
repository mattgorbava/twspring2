package com.example.twspring2.security.config;

import com.example.twspring2.database.users.model.RoleEntity;
import com.example.twspring2.service.users.RoleService;
import org.slf4j.Logger;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Supplier;

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final RoleService roleService;
    private final AntPathMatcher matcher;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(DynamicAuthorizationManager.class);

    public DynamicAuthorizationManager(RoleService roleService) {
        this.roleService = roleService;
        this.matcher = new AntPathMatcher();
    }



    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String requestUrl = context.getRequest().getRequestURI();
        String requestMethod = context.getRequest().getMethod();

        List<RoleEntity> roles = roleService.findAll();

        logger.info("Checking authorization for URL: {} and Method: {}", requestUrl, requestMethod);

        logger.info("User has authorities: {}", authentication.get().getAuthorities());

        for (RoleEntity role : roles) {
            logger.info("Checking role: {}", role.getName());
            if (authentication.get().getAuthorities().stream().anyMatch
                    (auth -> auth.getAuthority().equals("ROLE_" + role.getName()))) {
                logger.info("User has role: {}", role.getName());
                boolean hasPermission = role.getPermissions().stream().anyMatch
                        (permission -> matcher.match(permission.getUrl(), requestUrl) &&
                                permission.getHttpMethod().equalsIgnoreCase(requestMethod));

                if (hasPermission) {
                    logger.info("User has permission for URL: {} and Method: {}", requestUrl, requestMethod);
                    return new AuthorizationDecision(true);
                }
            }
        }
        return new AuthorizationDecision(false);
    }
}
