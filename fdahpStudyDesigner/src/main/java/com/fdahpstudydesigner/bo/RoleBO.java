package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The persistent class for the roles database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "roles")
@NamedQueries({
  @NamedQuery(
      name = "getUserRoleByRoleId",
      query = "SELECT RBO FROM RoleBO RBO WHERE RBO.roleId =:roleId"),
})
public class RoleBO implements Serializable {

  /** */
  private static final long serialVersionUID = -7663912527828944778L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Integer roleId;

  @Column(name = "role_name")
  private String roleName;

  public Integer getRoleId() {
    return roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
}
