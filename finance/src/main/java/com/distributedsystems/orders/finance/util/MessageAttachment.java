package com.distributedsystems.orders.finance.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;

public class MessageAttachment {

	private String fileName;
	private byte[] fileBytes;
	private String contentType;
	
	public MessageAttachment(String fileName, byte[] fileBytes, String contentType) {
		this.fileBytes = fileBytes;
		this.fileName = fileName;
	}
	
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFileBytes() {
		return fileBytes;
	}
	public void setFileBytes(byte[] fileBytes) {
		this.fileBytes = fileBytes;
	}
	
	public InputStreamSource getFileInputStreamResource( ) {
		return new ByteArrayResource(fileBytes);
	}
	
}
