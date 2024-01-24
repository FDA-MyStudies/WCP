/** */
package com.fdahpstudydesigner.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the users_password_history database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "users_password_history")
@NamedQueries({
  @NamedQuery(
      name = "getPaswordHistoryByUserId",
      query = "From UserPasswordHistory UPH WHERE UPH.userId =:userId ORDER BY UPH.createdDate")
})
public class UserPasswordHistory {
  @Column(name = "created_date")
  private String createdDate;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "password_history_id")
  private Integer passwordHistoryId;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "password")
  private String userPassword;

  @Column(name = "salt")
  private String salt;

  public String getCreatedDate() {
    return createdDate;
  }

  public Integer getPasswordHistoryId() {
    return passwordHistoryId;
  }

  public Integer getUserId() {
    return userId;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public void setPasswordHistoryId(Integer passwordHistoryId) {
    this.passwordHistoryId = passwordHistoryId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }
}
