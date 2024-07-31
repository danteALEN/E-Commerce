package com.example.ecommerce.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @NotBlank(message = "The name field can't be blank")
    private String name;

    @NotBlank(message = "The name field can't be blank")
    @Column(unique = true)
    private String userName;

    @NotBlank(message = "The name field can't be blank")
    @Size(min = 5, message = "The password must have atleat 5 characters")
    private String password;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;

    @Column(unique = true)
    @NotBlank(message = "The name field can't be blank")
    @Email(message = "Please enter email in proper format")
    private String email;

    @Column(nullable = false)
    private boolean isAccountNonExpired=true;
//
    @Column(nullable = false)
    private boolean isAccountNonLocked=true;
//
    @Column(nullable = false)
    private boolean isCredentialsNonExpired=true;
//
    @Column(nullable = false)
    private boolean isEnabled=true;

    @Enumerated(EnumType.STRING)
    private userRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
