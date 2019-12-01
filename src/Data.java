
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Data {

	public static void main(String[] args) {
		
		
	
		try {
			
			ServerSocket server = new ServerSocket(32100);
			System.out.println("Data: Waiting for Client...");
			
			while(true) {
				
				Socket socket = server.accept();
				
				if(socket.isInputShutdown()) {
					
					Thread t = new Thread(new DataThreadReceiver(socket,"Lab_Output"));
					t.start();
					
				} else {
					
					sendFilesToClient(socket,"Lab_Output");
					
				} // If output is enabled, receive file. Otherwise, send file.
			
				if(false) {
					break;
				}
				
			}
			
			server.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void sendFilesToClient(Socket socket, String path)  throws FileNotFoundException, IOException {
		
		InputStream in = socket.getInputStream();
		byte[] fileBytes = new byte[in.read()]; // Read the number of bytes in the filename.
		in.read(fileBytes,0,fileBytes.length); // Read the exact number of bytes.
		String filename = new String(fileBytes); // Create a String of the filename.
		
		OutputStream out = socket.getOutputStream();	
		BufferedInputStream bis = new BufferedInputStream( new FileInputStream( new File(path+"/"+filename) ) );
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = bis.read(data, 0, data.length); // Read bytes from the file.
		while( bytesRead != -1 ) {
			out.write(data, 0 , data.length);
			bytesRead = bis.read(data, 0, data.length);
		} // While there are more bytes to send.

		bis.close();
		
		in.close();
		out.close();
		socket.close();
		
	}
	
}

