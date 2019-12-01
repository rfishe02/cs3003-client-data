import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DataThreadSender implements Runnable {

	private final Socket socket;
	private final String path;
	
	public DataThreadSender(Socket socket, String path) {
		this.socket = socket;
		this.path = path;
	}
	
	@Override
	public void run() {
		
		try {
			
			sendFilesToClient(socket,path);
			
		} catch(FileNotFoundException e1 ) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public void sendFilesToClient(Socket socket, String path)  throws FileNotFoundException, IOException {
		
		// Create a filename String from information received from the client.
		
		InputStream in = socket.getInputStream();
		byte[] fileBytes = new byte[in.read()];
		in.read(fileBytes,0,fileBytes.length); 
		String filename = new String(fileBytes);
		
		// Send the contents of the file to the client.
		
		OutputStream out = socket.getOutputStream();
		BufferedInputStream bis = new BufferedInputStream( new FileInputStream( new File(path+"/"+filename) ) );
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = bis.read(data, 0, data.length);
		while( bytesRead != -1 ) {
			out.write(data, 0 , data.length);
			bytesRead = bis.read(data, 0, data.length);
		}
		bis.close();
		
		out.close();
		in.close();
		socket.close();
		
	}

}
