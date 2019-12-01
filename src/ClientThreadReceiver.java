import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThreadReceiver implements Runnable {

	private final Socket socket;
	private final String filename;
	private String outcome;
	
	public ClientThreadReceiver(
		Socket socket,
		String filename
	) {
		this.socket = socket;
		this.filename = filename;
	}
	
	@Override
	public void run() {
		
		try {
			
			setOutcome(getFileFromNodes(socket,filename));
			socket.close();
			
		} catch(FileNotFoundException e1 ) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public String getOutcome() {
		return outcome;
	}
	
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
	public String getFileFromNodes(Socket socket, String filename) throws FileNotFoundException, IOException {
		
		// Provide information about the filename to the node.
		
		OutputStream out = socket.getOutputStream();
		out.write(1);
		
		out.write(filename.getBytes().length);
		out.write(filename.getBytes());
			
		// Receive the contents of the file we asked for.
		
		InputStream in = socket.getInputStream();
		StringBuilder sb = new StringBuilder();	
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = in.read(data, 0, data.length);
		while( bytesRead != -1 ) {
			sb.append(new String(data));
			bytesRead = in.read(data, 0, data.length);
		}
     
		in.close();
		out.close();
		
		return sb.toString();
		
	}

}
