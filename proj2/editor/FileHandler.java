import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    private String inputFilename;
    private String outputFilename;
	
	public FileHandler(String inputFile, String outputFile){
		inputFilename=inputFile;
		outputFilename=outputFile;
	}
	
	public String Read(){
		String s="";
		try {
            File inputFile = new File(inputFilename);
            // Check to make sure that the input file exists!
            if (!inputFile.exists()) {
                System.out.println("Unable to read because file with name " + inputFilename
                    + " does not exist");
                return null;
            }
            FileReader reader = new FileReader(inputFile);
            // It's good practice to read files using a buffered reader.  A buffered reader reads
            // big chunks of the file from the disk, and then buffers them in memory.  Otherwise,
            // if you read one character at a time from the file using FileReader, each character
            // read causes a separate read from disk.  You'll learn more about this if you take more
            // CS classes, but for now, take our word for it!
            BufferedReader bufferedReader = new BufferedReader(reader);
            int intRead = -1;
            while ((intRead = bufferedReader.read()) != -1) {
                // The integer read can be cast to a char, because we're assuming ASCII.
                s += (char) intRead;
            }
            System.out.println("Successfully read file " + inputFilename);
            bufferedReader.close();
		}	catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found! Exception was: " + fileNotFoundException);
		}	catch (IOException ioException) {
        System.out.println("Error when copying; exception was: " + ioException);
		}
		return s;
	}
	
	public void Save(String s){
		try {
			FileWriter writer = new FileWriter(outputFilename);
			for (char c:s.toCharArray()){
				writer.write(c);
			}
            System.out.println("Successfully saved file to "
                    + outputFilename);
            writer.close();
		}	catch (IOException ioException) {
            System.out.println("Error when saving; exception was: " + ioException);
        }
		
	}
}
