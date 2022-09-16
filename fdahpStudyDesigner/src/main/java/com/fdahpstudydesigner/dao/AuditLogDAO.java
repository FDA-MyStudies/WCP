package com.fdahpstudydesigner.dao;

import com.fdahpstudydesigner.bo.AuditLogBO;
import com.fdahpstudydesigner.util.SessionObject;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public interface AuditLogDAO {

  public List<AuditLogBO> getTodaysAuditLogs();

  public String saveToAuditLog(
      Session session,
      Transaction transaction,
      SessionObject sessionObject,
      String activity,
      String activityDetails,
      String classsMethodName);

  public String updateDraftToEditedStatus(
      Session session, Transaction transaction, Integer userId, String actionType, Integer studyId);

  public String saveToAuditLogs(
          Session session,
          Transaction transaction,
          SessionObject sessionObject,
          String activity);
}
