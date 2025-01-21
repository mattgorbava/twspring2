package com.example.twspring2.service.users;

import com.example.twspring2.database.users.model.UserEntity;
import com.example.twspring2.database.users.repository.RoleRepository;
import com.example.twspring2.database.users.repository.UserRepository;
import com.example.twspring2.security.AuthenticatedUser;
import com.example.twspring2.security.PasswordGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends OidcUserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent()) {
            UserEntity appUser = optUser.get();
            List<SimpleGrantedAuthority> authorities = appUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                    .collect(Collectors.toList());
            return new User(appUser.getUsername(), appUser.getPassword(), true, true, true, true
            , authorities);
        }
        throw new UsernameNotFoundException(username);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();


        Optional<UserEntity> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setPassword(PasswordGeneratorUtil.generatePassword());
            newUser.setIsOAuthAccount(true);
            newUser.setRoles(roleRepository.findAllByName("USER"));
            this.save(newUser);
            return newUser.toAuthenticatedUser();
        }

        AuthenticatedUser authenticatedUser = optUser.get().toAuthenticatedUser();
        authenticatedUser.setIdToken(oidcUser.getIdToken());
//        List<SimpleGrantedAuthority> authorities = authenticatedUser.getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getAuthority()))
//                .collect(Collectors.toList());
//        authenticatedUser.setAuthorities(authorities);
        return authenticatedUser;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public UserEntity save(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles().isEmpty()) {
            user.setRoles(roleRepository.findAllByName("USER"));
        }
        logger.info("Saving user: " + user);
        return userRepository.save(user);
    }

    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public List<UserEntity> findAll() {
        return this.userRepository.findAll();
    }

    public void removeRole(Long userId, String roleName) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        user.getRoles().removeIf(role -> role.getName().equals(roleName));
        roleService.removeRole(userId, roleName);
        userRepository.save(user);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void addRole(Long userId, String roleName) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        user.addRole(roleRepository.findByName(roleName).orElseThrow());
        userRepository.save(user);
    }
}
