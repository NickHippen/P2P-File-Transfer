package edu.unomaha.comm.net.vnk.semester.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A utility class for performing general functionality throughout the program
 * @author Nick Hippen
 */
public final class Utils {

	private Utils() {}
	
	/**
	 * Wraps a file found at the file path into a FileWrapper
	 * @param filePath the path to the file
	 * @return the wrapper file as a FileWrapper
	 * @throws IOException
	 */
	public static FileWrapper wrapFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		byte[] data = Files.readAllBytes(path);
		return new FileWrapper(path.toFile().getName(), data);
	}
	
}
