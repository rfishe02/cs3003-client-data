import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
	
	public static void main(String[] args) {
		
		HashMap<String, Integer> fileIndex = new HashMap<>();
		
		try {
			
			ArrayList<String> nodes = readConfig("config.txt"); // Load the nodes listed in the config.
			int port = 32100;

			sendFilesToNodes(fileIndex,nodes,port,"LabFolders/"); // Test indexing a directory of file.
			getFilesFromNodes(fileIndex,nodes,port,"test1.txt"); // Test receiving a single file by name.
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static ArrayList<String> readConfig(String filename) throws IOException {
		
		ArrayList<String> res = new ArrayList<>(15);
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String[] spl;
		String read;
		
		while( (read = br.readLine()) != null ) {
			
			spl = read.split(" ");
			res.add(spl[1]);
			
		}
		
		br.close();
		return res;
		
	}
	
	public static int getHashValue(String filename, int length, int i) {
		
		int key = Math.abs(filename.hashCode());
		
		int h1 = key % length;
		int h2 = 1 + ( key % (length-1) );
		
		return (h1 + (i * h2)) % length;
			
	}
	
	public static void sendFilesToNodes(
		HashMap<String, Integer> fileIndex, 
		ArrayList<String> nodes, 
		int port, 
		String directory 
	) throws UnknownHostException, IOException {
		
		File[] dir = new File(directory).listFiles();
		Socket socket;
		
		int place;
		
		if( dir != null ) {
			
			for(int i = 0; i < dir.length; i++) {
				
				place = getHashValue(dir[i].getName(),nodes.size(),1);
				fileIndex.put(dir[i].getName(),place);
			
				socket = new Socket(nodes.get(place), port);
				socket.shutdownInput(); // Data will see we've enabled output only.
				
				Thread t = new Thread(new ClientThreadSender(socket,dir[i]));
				t.start();
				
			} // For each file, send it using a new Socket connection to a particular Data node.
		}
		
	}
	
	public static void getFilesFromNodes(
		HashMap<String, Integer> fileIndex, 
		ArrayList<String> nodes, 
		int port,
		String filename
	) throws FileNotFoundException, IOException {
				
		Socket socket = new Socket(nodes.get(fileIndex.get(filename)), port);
		Thread t = new Thread(new ClientThreadReceiver(socket,filename));
		t.start();
				
	}
	
}
