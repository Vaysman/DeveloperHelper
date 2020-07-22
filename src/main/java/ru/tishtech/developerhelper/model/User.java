package ru.tishtech.developerhelper.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
// FIXME: rename table
@Table(name = "usr")
// FIXME: use lombok
// FIXME: separate User and UserDetails
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Pattern(
      regexp = "^[a-zA-Z0-9_-]{3,16}$",
      message =
          "Username must have only latin letters and digits, "
              + "also must be 3 to 16 characters long!")
  private String username;

  @Email(message = "Email is not correct!")
  private String email;

  @NotBlank(message = "Password is required!")
  private String password;

  private boolean active;
  private String activationCode;
  private String forgotPasswordCode;

  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Role> roles;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Project> projects;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username.toLowerCase();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email.toLowerCase();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getActivationCode() {
    return activationCode;
  }

  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  public String getForgotPasswordCode() {
    return forgotPasswordCode;
  }

  public void setForgotPasswordCode(String forgotPasswordCode) {
    this.forgotPasswordCode = forgotPasswordCode;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
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
    return isActive();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles();
  }
}
