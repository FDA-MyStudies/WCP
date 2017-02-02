package com.fdahpStudyDesigner.util;

/**
 * @author 
 * 
 */


import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
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
	private static final long serialVersionUID = -486030201292436116L;
	
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
	private String fromEmailPassword="";
	private String fromEmailName = "";
	private String ccEmail;
	private String bccEmail;
	private String attachmentPath;
	/*public boolean sendemail() throws Exception{
			
		//logger.info("Mail - Starts: sendemail() - "+AppUtilClass.getCurrentDateTime());
		// Get system properties
		Properties props = System.getProperties();
	
		// Setup mail server
		props.put("mail.smtp.user", this.getFromEmailAddress());
		props.put("mail.smtp.host", this.getSmtp_Hostname());
		props.put("mail.smtp.port", this.getSmtp_portvalue());
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", this.getSmtp_portvalue());
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		boolean sentMail = false;
		// Define message
		try {
			
//			Authenticator auth = this.new SMTPAuthenticator();
			Session session = Session.getInstance(props, new GMailAuthenticator(this.getFromEmailAddress(), this.getFromEmailPassword()));
			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			InternetAddress fromEmail = new InternetAddress(this.getFromEmailAddress());
			fromEmail.setPersonal(this.getFromEmailName());
			message.setFrom(fromEmail);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.getToemail()));
			message.setSubject(this.subject);
			message.setContent(this.getMessageBody(), "text/html");
			Transport.send(message);
			sentMail = true;
		} catch (AddressException e) {
			sentMail = false;
	        logger.error("ERROR:  sendemail() - "+e+" : ");
	        throw new Exception("Exception in Mail.sendemail() "+ e.getMessage(), e);

		} catch (MessagingException e) {
			sentMail = false;
	        logger.error("ERROR:  sendemail() - "+e+" : ");
	        throw new Exception("Exception in Mail.sendemail() "+ e.getMessage(), e);
		}
		return sentMail;
				
	}*/
	public boolean sendemail() throws Exception{
		logger.warn("sendemail()====start");
		boolean sentMail = false;
		try {
			final String username = this.getFromEmailAddress();
			final String password = this.getFromEmailPassword();
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", this.getSmtp_Hostname());
			props.put("mail.smtp.socketFactory.port", this.getSmtp_portvalue());
		    props.put("mail.smtp.socketFactory.class",this.getSslFactory());
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.port", this.getSmtp_portvalue());
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					  });
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
		} catch (MessagingException e) {
	        logger.error("ERROR:  sendemail() - "+e+" : ");
	        sentMail = false;
	        e.printStackTrace();
		} catch (Exception e) {
			logger.error("ERROR:  sendemail() - "+e+" : ");
			e.printStackTrace();
		}
		logger.info("Mail.sendemail() :: Ends");
		return sentMail;
	}
	public boolean sendMailWithAttachment() throws Exception{
		logger.debug("sendemail()====start");
		boolean sentMail = false;
		try {
			final String username = this.getFromEmailAddress();
			final String password = this.getFromEmailPassword();
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", this.getSmtp_Hostname());
			props.put("mail.smtp.socketFactory.port", this.getSmtp_portvalue());
		    props.put("mail.smtp.socketFactory.class",this.getSslFactory());
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.port", this.getSmtp_portvalue());
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					});
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
			//message.setText("Check attachment in Mail");
			//message.setContent(messageBody, "text/html");
			// Create the message part
	        BodyPart messageBodyPart = new MimeBodyPart();
	    	// Create a multipar message
	    	Multipart multipart = new MimeMultipart();
	    	// Part two is attachment
	    	messageBodyPart = new MimeBodyPart();
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
		} catch (MessagingException e) {
	        logger.error("ERROR:  sendemail() - "+e+" : ");
	        sentMail = false;
		} catch (Exception e) {
			logger.error("ERROR:  sendemail() - "+e+" : ");
		}
		logger.info("Mail.sendemail() :: Ends");
		return sentMail;
	}
	/*private class SMTPAuthenticator extends javax.mail.Authenticator {

		public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication (getFromEmailAddress(), getFromEmailPassword()); // password not displayed here, but gave the right password in my actual code.
		}
	}*/

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