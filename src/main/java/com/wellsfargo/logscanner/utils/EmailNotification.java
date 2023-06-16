package com.wellsfargo.logscanner.utils;

public class EmailNotification {

   private Long id;

    private String subject;

    private String content;

     
    
	public EmailNotification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailNotification(Long id, String subject, String content) {
		super();
		this.id = id;
		this.subject = subject;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "EmailNotification [id=" + id + ", subject=" + subject + ", content=" + content + "]";
	}
	
	

    
}
