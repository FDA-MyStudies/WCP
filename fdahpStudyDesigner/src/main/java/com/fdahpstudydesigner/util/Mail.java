package com.fdahpstudydesigner.util;

/**
 * @author 
 * 
 */


import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;



public class Mail  {
	
	/**
	 * 
	 */
    private static Logger logger = Logger.getLogger(Mail.class.getName());
    private Map<?,?> configMap = FdahpStudyDesignerUtil.configMap;
    
	private String toemail;
	private String subject;
	private String messageBody;
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	private String smtp_Hostname="";
	private String smtp_portvalue="";
	static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private String sslFactory = "";
	private String fromEmailAddress="";
	private String fromEmailPassword;
	private String fromEmailName = "";
	private String ccEmail;
	private String bccEmail;
	private String attachmentPath;
	public boolean sendemail() {
		logger.warn("sendemail()====start");
		boolean sentMail = false;
		Session session = null;
		try {
			final String username = this.getFromEmailAddress();
			final String password = this.getFromEmailPassword();
			Properties props = new Properties();
			props.put("mail.smtp.host", this.getSmtp_Hostname());
		    props.put("mail.smtp.port", this.getSmtp_portvalue());
		    
		    if(configMap.get("fda.env") != null && FdahpStudyDesignerConstants.FDA_ENV_LOCAL.equals(configMap.get("fda.env"))) {
		    	props.put("mail.smtp.auth", "true");
		    	props.put("mail.smtp.socketFactory.port", this.getSmtp_portvalue());
			    props.put("mail.smtp.socketFactory.class",this.getSslFactory());
				session = Session.getInstance(props,
						new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username,
										password);
							}
						});
		    } else {
		    	props.put("mail.smtp.auth", "false");
		    	session = Session.getInstance(props);
		    }
		    
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			if(StringUtils.isNotBlank(this.getToemail())){
				if(this.getToemail().indexOf(",") != -1){
					message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(this.getToemail()));
				}else{
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getToemail()));
				}
			}
			if(StringUtils.isNotBlank(this.getCcEmail())){
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(this.getCcEmail()));
			}
			if(StringUtils.isNotBlank(this.getBccEmail())){
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(this.getBccEmail()));
			}
			message.setSubject(this.subject);
			message.setContent(this.getMessageBody(), "text/html");
			Transport.send(message);
			logger.debug("sendemail()====end");
			sentMail = true;
		} catch (Exception e) {
			logger.error("ERROR: sendemail() - ", e);
			sentMail = false;
		}
		logger.info("Mail.sendemail() :: Ends");
		return sentMail;
	}
	public boolean sendMailWithAttachment() {
		logger.debug("sendemail()====start");
		boolean sentMail = false;
		Session session = null;
        BodyPart messageBodyPart = null;
    	Multipart multipart = null;
    	
		try {
			final String username = this.getFromEmailAddress();
			final String password = this.getFromEmailPassword();
			Properties props = new Properties();
			props.put("mail.smtp.auth", "false");
			props.put("mail.smtp.host", this.getSmtp_Hostname());
		    props.put("mail.smtp.port", this.getSmtp_portvalue());
		    
		    if(configMap.get("fda.env") != null && FdahpStudyDesignerConstants.FDA_ENV_LOCAL.equals(configMap.get("fda.env"))) {
		    	props.put("mail.smtp.socketFactory.port", this.getSmtp_portvalue());
			    props.put("mail.smtp.socketFactory.class",this.getSslFactory());
				session = Session.getInstance(props,
						new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username,
										password);
							}
						});
		    } else {
		    	session = Session.getInstance(props);
		    }
		    
			Message message = new MimeMessage(session);
			if(StringUtils.isNotBlank(this.getToemail())){
				if(this.getToemail().indexOf(",") != -1){
					message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(this.getToemail()));
				}else{
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getToemail()));
				}
			}
			if(StringUtils.isNotBlank(this.getCcEmail())){
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(this.getCcEmail()));
			}
			if(StringUtils.isNotBlank(this.getBccEmail())){
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(this.getBccEmail()));
			}
			message.setSubject(this.subject);
			message.setFrom(new InternetAddress(username));
			
			// Create the message part
	        messageBodyPart = new MimeBodyPart();
	    	// Create a multipart message
	    	multipart = new MimeMultipart();
	    	
	    	//String filename = "D:\\temp\\TestLinks.pdf"; // D:\temp\noteb.txt
	    	DataSource source = new FileDataSource(this.getAttachmentPath());
	    	messageBodyPart.setDataHandler(new DataHandler(source));
	    	messageBodyPart.setFileName(source.getName());
	    	messageBodyPart.setHeader("Content-Transfer-Encoding", "base64");
	    	messageBodyPart.setHeader("Content-Type", source.getContentType());
	    	// Send the complete message parts
	    	multipart.addBodyPart(messageBodyPart);
	    	
	    	messageBodyPart = new MimeBodyPart();
	    	messageBodyPart.setText(messageBody);
	    	messageBodyPart.setHeader("MIME-Version" , "1.0");
	    	messageBodyPart.setHeader("Content-Type" , messageBodyPart.getContentType());
	    	multipart.addBodyPart(messageBodyPart);
	    	
	    	message.setContent(multipart);
			Transport.send(message);
			sentMail = true;
		} catch (Exception e) {
			logger.error("ERROR:  sendemail() - ", e);
		}
		logger.info("Mail.sendemail() :: Ends");
		return sentMail;
	}

	public String getToemail() {
		return toemail;
	}

	public void setToemail(String toemail) {
		this.toemail = toemail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	
	public String getSmtp_Hostname() {
		String hostname = "";
		if(this.smtp_Hostname.equals("")){
			hostname = Mail.SMTP_HOST_NAME;
		}else{
			hostname = this.smtp_Hostname;
		}
		return hostname;
	}

	public void setSmtp_Hostname(String smtp_Hostname) {
		this.smtp_Hostname = smtp_Hostname;
	}

	public String getSmtp_portvalue() {
		String portvalue = "";
		if(this.smtp_portvalue.equals("")){
			portvalue = Mail.SMTP_PORT;
		}else{
			portvalue = this.smtp_portvalue;
		}
		
		return portvalue;
	}

	public void setSmtp_portvalue(String smtp_portvalue) {
		this.smtp_portvalue = smtp_portvalue;
	}

	public static String getSSL_FACTORY() {
		return SSL_FACTORY;
	}

	public static void setSSL_FACTORY(String sSL_FACTORY) {
		SSL_FACTORY = sSL_FACTORY;
	}

	public String getSslFactory() {

		String sslfactoryvalue = "";
		if(this.sslFactory.equals("")){
			sslfactoryvalue = Mail.SSL_FACTORY;
		}else{
			sslfactoryvalue = this.sslFactory;
		}
		
		
		return sslfactoryvalue;
		
	}

	public void setSslFactory(String sslFactory) {
		this.sslFactory = sslFactory;
	}

	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}

	public String getFromEmailPassword() {
		return fromEmailPassword;
	}

	public void setFromEmailPassword(String fromEmailPassword) {
		this.fromEmailPassword = fromEmailPassword;
	}

	public static String getSmtpHostName() {
		return SMTP_HOST_NAME;
	}

	public static String getSmtpPort() {
		return SMTP_PORT;
	}
	
	public String getFromEmailName() {
		return fromEmailName;
	}

	public void setFromEmailName(String fromEmailName) {
		this.fromEmailName = fromEmailName;
	}

	public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getBccEmail() {
		return bccEmail;
	}

	public void setBccEmail(String bccEmail) {
		this.bccEmail = bccEmail;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	
}