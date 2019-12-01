import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DataThreadReceiver implements Runnable {

	private final Socket socket;
	private final String path;
	
	public DataThreadReceiver(Socket socket, String directory) {
		this.path = directory;
		this.socket = socket;
	}
	
	@Override
	public void run() {
	
		try {
			
			receiveFileFromClient(socket, path);
			
		} catch(FileNotFoundException e1 ) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public void receiveFileFromClient(Socket socket, String path) throws FileNotFoundException, IOException {
		
		InputStream in = socket.getInputStream();
		
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
        
        in.close();
        socket.close();
		
	}
	
}
