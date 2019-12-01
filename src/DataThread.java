import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DataThread implements Runnable {

	private final Socket socket;
	private final String path;
	
	public DataThread(Socket socket, String path) {
		this.socket = socket;
		this.path = path;
	}
	
	@Override
	public void run() {
	
		try {
			
			InputStream in = socket.getInputStream();
			int type = in.read();
			
			if(type == 0) {
				receiveFileFromClient(in, path);
			} else {
				
				OutputStream out = socket.getOutputStream();
				sendFilesToClient(in,out,path);
				out.close();
			}
			 
			in.close();
			socket.close();
			
		} catch(FileNotFoundException e1 ) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public void receiveFileFromClient(InputStream in, String path) throws FileNotFoundException, IOException {
		
		byte[] fileBytes = new byte[in.read()]; // Read the number of bytes in the filename.
		in.read(fileBytes,0,fileBytes.length); // Read the exact number of bytes.
		
		String filename = new String(fileBytes); // Create a String of the filename.
		
		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(path+"/"+filename.trim()) );
		
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = in.read(data, 0, data.length); // Read bytes from the stream.
		while( bytesRead != -1 ) {
			bos.write( data, 0, data.length );
        	bytesRead = in.read(data, 0, data.length);
        } // While there are more more bytes to read.

        bos.close();
		
	}
	
	public void sendFilesToClient(InputStream in, OutputStream out, String path)  throws FileNotFoundException, IOException {
		
		// Create a filename String from information received from the client.
		
		byte[] fileBytes = new byte[in.read()];
		in.read(fileBytes,0,fileBytes.length); 
		String filename = new String(fileBytes);
		
		// Send the contents of the file to the client.
		
		BufferedInputStream bis = new BufferedInputStream( new FileInputStream( new File(path+"/"+filename) ) );
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = bis.read(data, 0, data.length);
		while( bytesRead != -1 ) {
			out.write(data, 0 , data.length);
			bytesRead = bis.read(data, 0, data.length);
		}
		
		bis.close();
		
	}
	
}
