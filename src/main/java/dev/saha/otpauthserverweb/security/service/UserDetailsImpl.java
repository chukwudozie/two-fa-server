package dev.saha.otpauthserverweb.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.saha.otpauthserverweb.domain.Employee;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String userName;

    private String fullName;

    @JsonIgnore
    private String password;


    private String email;

    private Boolean locked = false;

    private Boolean enabled = true;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String userName, String fullName, String password,
                           String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserDetails build(Employee user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getUserRole(user)));
        return new UserDetailsImpl(
                user.getId(),
                user.getUserName(),
                user.getFullName(),
                user.getPassword(),
                user.getEmail(),
                authorities
        );
    }

    private static String getUserRole(Employee user){
        String role = Objects.equals(user.getRole(), "Yes") ? "ADMIN":"USER";
        System.out.println("Role for the user "+user.getFullName()+" is "+user.getRole());
        return role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}