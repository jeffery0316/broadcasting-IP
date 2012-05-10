
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class InfoSocketClient{
	private int port = 8765;
	private static String address = "";
	public InfoSocketClient(){

		Socket client = new Socket();
		InetSocketAddress isa = new InetSocketAddress(this.address, this.port);
		try{
				client.connect(isa, 10000);
				BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
				out.write(client.getInetAddress().toString().getBytes());
				///out.write(client.getInetAddress);
				//System.out.println(isa.getHostName());
				out.flush();
				out.close();
				out = null;
				client.close();
				client = null;
		}catch(java.io.IOException e){
				System.out.println("IOE problem"+e.toString());
		}
	}
	public static void main(String args[]){
		Properties prop = new Properties();
		try{
			prop.load(new FileInputStream("master.conf"));
			address = prop.getProperty("masterIP");
		}catch(IOException ioe){
			System.out.println(ioe.toString());
		}
		InfoSocketClient isc = new InfoSocketClient();
	}
}