
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
					
					Thread t2 = new Thread(new DataThreadSender(socket,"Lab_Output"));
					t2.start();
					
				} // If output & input is enabled, send file to client. Otherwise, accept file from client.
			
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

