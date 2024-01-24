package com.fdahpstudydesigner.bo;

import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the user_permissions database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "user_permissions")
public class UserPermissions {

  private String permissions;

  private Integer userRoleId;

  private Set<UserBO> users;

  public UserPermissions() {
    // Do nothing
  }

  public UserPermissions(Set<UserBO> users, String permissions) {
    this.setUsers(users);
    this.setPermissions(permissions);
  }

  @Column(name = "permissions", nullable = false, length = 45)
  public String getPermissions() {
    return permissions;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "permission_id", unique = true, nullable = false)
  public Integer getUserRoleId() {
    return this.userRoleId;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  public Set<UserBO> getUsers() {
    return users;
  }

  public void setPermissions(String permissions) {
    this.permissions = permissions;
  }

  public void setUserRoleId(Integer userRoleId) {
    this.userRoleId = userRoleId;
  }

  public void setUsers(Set<UserBO> users) {
    this.users = users;
  }
}
