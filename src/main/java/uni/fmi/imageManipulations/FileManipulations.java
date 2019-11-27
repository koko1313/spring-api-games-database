package uni.fmi.imageManipulations;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileManipulations {

	/**
	 * Saves the file to the disk with custom name
	 * 
	 * @param fileFullName
	 * @param file
	 * @param pathToSaveFolder
	 * @return
	 */
	public static String saveFileToDisk(String fileFullName, MultipartFile file, String pathToSaveFolder) {
		try {
			String fileName = fileFullName + "." + file.getContentType().split("/")[1];
			
            if(!new File(pathToSaveFolder).exists()) {
                new File(pathToSaveFolder).mkdirs();
            }

            String filePath = pathToSaveFolder + fileName;
            File dest = new File(filePath);
            
			file.transferTo(dest);
			
			return fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Deletes file from the disk
	 * 
	 * @param fileName
	 * @param pathToContainingFolder
	 */
	public static void deleteFileFromDisk(String fileName, String pathToContainingFolder) {
		
        String filePath = pathToContainingFolder + fileName;
        File dest = new File(filePath);
        
        dest.delete();
	}
	
}
