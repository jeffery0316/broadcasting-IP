import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoSocket extends java.lang.Thread{

	private boolean OutServer = false;
	private ServerSocket server;
	private final int ServerPort = 8765;
	public InfoSocket(){
		try{
			server = new ServerSocket(ServerPort);
		}catch(IOException ioe){
			System.out.println(ioe.toString());
		}
	}
	public void run(){
		Socket socket;
		BufferedInputStream in;
		System.out.println("start to transfer ip info.");
		while(!OutServer){
			socket = null;
			try{
				synchronized(server){
					socket = server.accept();
				}
				System.out.println("InetAddress = "+socket.getInetAddress());
				socket.setSoTimeout(15000);
				in = new java.io.BufferedInputStream(socket.getInputStream());
				
				byte[] b = new byte[1024];
				String data = "", pattern = "", ipdetect = "";
				int length, found, readin = 0, doubleTuple = 0, idx = 0, lineNumber = 0;
				char[] ipdata = new char[2000];
				
				while((length = in.read(b)) > 0){
					data += new String(b, 0, length);
				}
				System.out.println("get value: "+ data);
				FileReader fr = new FileReader(new File("cluster.conf"));
				while((readin = fr.read()) != -1){
					if(readin == '\n'){
						//find whether the ip double appears
						pattern = new String(ipdata).trim();
						System.out.println(pattern);
						ipdetect= socket.getInetAddress().toString().replaceAll("/", "");
						if(checkIP(pattern, ipdetect)==1){
							doubleTuple = 1;
							System.out.println("error!! double");
						}
						ipdata = new char[2000];
						lineNumber +=1;
					}else{
						ipdata[idx] = (char)readin;
						idx++;
					}
				}
				fr.close();
				FileWriter fw = new FileWriter(new File("cluster.conf"), true);
				if(doubleTuple==0){
					fw.write("PC"+lineNumber+"("+ipdetect+")=" + ipdetect + ";");
					fw.write("port=9010;");
					fw.write("mhz=800;\r\n");
				}
				fw.close();
				in.close();
				in = null;
				socket.close();
			}catch(java.io.IOException e){
				System.out.println("IOException: "+ e.toString());
			}
		}
	}
	public int checkIP(String inputStr, String ip){
		String patternStr = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		boolean matchFound = matcher.find();
		while(matchFound) {
			for(int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				if (groupStr.equals(ip))
					return 1;
			}    
			if(matcher.end() + 1 <= inputStr.length()) {
				matchFound = matcher.find(matcher.end());
			}else{
				break;
			}    
		} 
		return 0;
	}
	public static void main(String [] args){
		InfoSocket ss = new InfoSocket();
		ss.start();
	}
}