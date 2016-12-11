package edu.unomaha.comm.net.vnk.semester.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Utils {

	public static FileWrapper wrapFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		byte[] data = Files.readAllBytes(path);
		return new FileWrapper(path.toFile().getName(), data);
	}
	
}
