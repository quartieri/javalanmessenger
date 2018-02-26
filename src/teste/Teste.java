package teste;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;

public class Teste {
	
	
	
	public static void main(String[] args) {
		String host = "";
		for (int i = 0; i < 255; i++) {
			if (verificaNaRede("189.123.16."+i,8080,3000)) {
				try {
					System.out.println(InetAddress.getByName("189.123.16."+i).getHostName());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		
	
		
		
		
	}
	
	public static boolean verificaNaRede(String hostName,int port,int timeOut){
        try {

            if (InetAddress.getByName(hostName).isReachable(timeOut)) {
            	return true;
            }else {
            	return false;
            }
            
        } catch (Exception ex) {
            return false;
           }

     }	

}
