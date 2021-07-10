package se.ifmo.mobile.server.domain;

import static java.util.Collections.emptyList;
import static org.springframework.data.mongodb.core.mapping.FieldType.ARRAY;
import static org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID;

import java.util.Collection;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.ifmo.mobile.server.constraints.PhoneNumber;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true)
public class User implements UserDetails, Persistable<String> {

  @MongoId(OBJECT_ID)
  @EqualsAndHashCode.Include
  private String id;

  @Indexed(unique = true)
  @Email(message = "User's username should be a correctly formatted email")
  @NotBlank(message = "User's username should not be blank")
  @EqualsAndHashCode.Include
  private String username;

  @NotBlank(message = "User's password should not be blank")
  @Size(min = 6, message = "User's password should be at least 6 characters long")
  private String password;

  @Field(targetType = ARRAY)
  private List<GrantedAuthority> authorities;

  private String name;

  private String surname;

  @PhoneNumber(message = "User's phone number should be valid")
  private String phoneNumber;

  private String city;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return id == null || id.isEmpty();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities == null ? emptyList() : authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
