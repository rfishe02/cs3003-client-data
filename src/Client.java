import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
	
	public static void main(String[] args) {
		
		HashMap<String, Integer> fileIndex = new HashMap<>();
		
		try {
			
			ArrayList<String> nodes = readConfig("config.txt");
			int port = 32100;

			processDirectoryOfFiles(fileIndex,nodes,port,"LabFolders/");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getHashValue(String filename, int length, int i) {
		
		int key = Math.abs(filename.hashCode());
		
		int h1 = key % length;
		int h2 = 1 + ( key % (length-1) ); 
		return (h1 + (i * h2)) % length;
				
	}
	
	public static ArrayList<String> readConfig(String filename) throws IOException {
	
		ArrayList<String> res = new ArrayList<>(15);
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String[] spl;
		String read;
		
		while((read = br.readLine())!=null) {
			
			spl = read.split(" ");
			res.add(spl[1]);
			
		}
		
		br.close();
		return res;
		
	}
	
	public static void processDirectoryOfFiles(
			HashMap<String, Integer> fileIndex, 
			ArrayList<String> nodes, 
			int port, 
			String directory 
	) throws UnknownHostException, IOException {
		
		File[] dir = new File(directory).listFiles();
		
		if(dir != null) {
			
			for(int i = 0; i < dir.length; i++) {
				
				int place = getHashValue(dir[i].getName(),nodes.size(),1);
				fileIndex.put(dir[i].getName(),place);
			
				Socket socket = new Socket(nodes.get(place), port);
				OutputStream out = socket.getOutputStream();
					
				sendFile(dir[i], out);
				
				out.close();
				socket.close();
				
			}
		}
		
	}
	
	public static void sendFile(File file, OutputStream out) throws FileNotFoundException, IOException {
		
		BufferedInputStream bis =  new BufferedInputStream(new FileInputStream(new File(file.getPath())));
		
		out.write(file.getName().getBytes().length); // Write how long the filename is in bytes.
		out.write(file.getName().getBytes()); // Write the bytes.
		
		byte[] data = new byte[ 2048 ];
		
		int bytesRead = bis.read(data, 0, data.length); // Read bytes from the file.
		while(bytesRead != -1) {
			out.write(data, 0 , data.length);
			bytesRead = bis.read(data, 0, data.length);
		} // While there are more bytes to send.

		bis.close();
		
	}

}
