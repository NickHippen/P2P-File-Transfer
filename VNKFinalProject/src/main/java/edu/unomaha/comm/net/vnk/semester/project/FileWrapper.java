package edu.unomaha.comm.net.vnk.semester.project;

import java.io.Serializable;

/**
 * An object that wraps a file's byte source with a file name for later retrieval
 * @author Nick Hippen
 */
public class FileWrapper implements Serializable {

	private static final long serialVersionUID = 626608604602561462L;
	
	private String fileName;
	private byte[] bytes;
	
	/**
	 * Constructs a new FileWrapper with the given fileName and bytes
	 * @param fileName the file name to set
	 * @param bytes the bytes to set
	 */
	public FileWrapper(String fileName, byte[] bytes) {
		this.fileName = fileName;
		this.bytes = bytes;
	}

	/**
	 * Gets the file name
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name
	 * @param fileName the file name to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the bytes of the file
	 * @return the bytes of the file
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * Sets the bytes of the file
	 * @param bytes the bytes of the file to set
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}
