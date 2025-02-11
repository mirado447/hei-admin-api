package school.hei.haapi.endpoint.rest.security.model;

import static school.hei.haapi.model.User.Status.ENABLED;
import static school.hei.haapi.model.User.Status.SUSPENDED;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import school.hei.haapi.model.User;

@Getter
@AllArgsConstructor
@ToString
public class Principal implements UserDetails {

  private final User user;
  private final String bearer;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(Role.fromValue(user.getRole().toString()));
  }

  @Override
  public String getPassword() {
    return bearer;
  }

  @Override
  public String getUsername() {
    return user.getLastName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isAccountNonLocked() {
    return isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isEnabled() {
    // TODO: make a stronger security to not authorize SUSPENDED user to access other resources
    return ENABLED.equals(user.getStatus()) || SUSPENDED.equals(user.getStatus());
  }

  public String getUserId() {
    return user.getId();
  }

  public String getRole() {
    return user.getRole().toString();
  }
}
