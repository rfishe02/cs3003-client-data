
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
				
				Thread t = new Thread(new DataThread(socket,"Lab_Output"));
				t.start();
				
				if(false) {
					break;
				}
				
			} // This is an infinite loop.
			
			server.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
		
}

