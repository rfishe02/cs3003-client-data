
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Data {

	public static void main(String[] args) {
		
		try {
			
			ServerSocket server = new ServerSocket(32100);
			System.out.println("Data: Waiting for Client...");
			
			while(true) {
		
				Socket socket = server.accept();
				InputStream in = socket.getInputStream();
				
				readFile("Lab_Output",in);
				
				in.close();
				socket.close();
				
				if(false) {
					break; // Just makes the IDE happy.
				}
				
			} // This is an infinite loop.
			
			server.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void readFile(String path, InputStream in) throws FileNotFoundException, IOException {
		
		byte[] fileBytes = new byte[in.read()]; // Read the number of bytes in the filename.
		in.read(fileBytes,0,fileBytes.length); // Read the exact number of bytes.
		
		String filename = new String(fileBytes); // Create a String of the filename.
		
		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(path+"/"+filename.trim()) );
		
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = in.read(data, 0, data.length); // Read bytes from the stream.
		while(bytesRead != -1) {
        	bos.write( data, 0, data.length );
        	bytesRead = in.read(data, 0, data.length);
        } // While there are more more bytes to read.

        bos.close();
		
	}
	
}

