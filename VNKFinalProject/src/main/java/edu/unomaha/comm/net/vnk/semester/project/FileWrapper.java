package edu.unomaha.comm.net.vnk.semester.project;

import java.io.Serializable;

public class FileWrapper implements Serializable {

	private static final long serialVersionUID = 626608604602561462L;
	
	private String fileName;
	private byte[] bytes;
	
	public FileWrapper(String fileName, byte[] bytes) {
		this.fileName = fileName;
		this.bytes = bytes;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}
