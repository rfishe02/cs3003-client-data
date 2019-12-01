import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThreadSender implements Runnable {

	private final Socket socket;
	private final File file;
	
	public ClientThreadSender(Socket socket, File file) {
		this.socket = socket;
		this.file = file;
	}
	
	@Override
	public void run() {
		
		try {
			
			sendFileToNode(file,socket);
			
		} catch(FileNotFoundException e1 ) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public void sendFileToNode(File file, Socket socket) throws FileNotFoundException, IOException {
		
		OutputStream out = socket.getOutputStream();
		
		BufferedInputStream bis = new BufferedInputStream( new FileInputStream( new File(file.getPath()) ) );
		
		out.write(file.getName().getBytes().length); // Write how long the filename is in bytes.
		out.write(file.getName().getBytes()); // Write the bytes.
		
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = bis.read(data, 0, data.length); // Read bytes from the file.
		while( bytesRead != -1 ) {
			out.write(data, 0 , data.length);
			bytesRead = bis.read(data, 0, data.length);
		} // While there are more bytes to send.

		bis.close();
		
		out.close();
		socket.close();
		
	}

}
