
import java.io.IOException;
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
					
					
					
				} // If input is enabled, receive file. Otherwise, send file.
			
				if(false) {
					break;
				}
				
			}
			
			server.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}

