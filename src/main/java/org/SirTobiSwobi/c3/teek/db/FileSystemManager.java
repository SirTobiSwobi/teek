package org.SirTobiSwobi.c3.teek.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileSystemManager {
	
	public void writeToFile(InputStream uploadedInputStream, String fileLocation) throws IOException{
		int read;
		final int BUFFER_LENGTH = 1024;
		final byte[] buffer = new byte[BUFFER_LENGTH];
		OutputStream out = new FileOutputStream(new File(fileLocation));
		while((read = uploadedInputStream.read(buffer))!=-1){
			out.write(buffer, 0, read);
		}
		out.flush();
		out.close();
	}
	
	public ArrayList<File> getAllFiles(String directory){
		File documentDirectory = new File(directory);
		ArrayList<File> documents = new ArrayList<File>();
		File[] fList = documentDirectory.listFiles();
		    for (File file : fList) {
		        if (file.isFile()) {
		            documents.add(file);
		        } else if (file.isDirectory()) {
		            listf(file.getAbsolutePath(), documents);
		        }
		    }
		
		return documents;
	}
	
	public void listf(String directoryName, ArrayList<File> files) {
	    File directory = new File(directoryName);

	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath(), files);
	        }
	    }
	   
	}
	
	public void deleteWordEmbedding(String localFilePath){
		File file = new File(localFilePath);
		file.delete();
	}

}
