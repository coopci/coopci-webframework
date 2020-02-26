package learn.statsd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PrintingUdpServer extends Thread {
 
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
 
    public PrintingUdpServer() throws SocketException {
        socket = new DatagramSocket(8125);
    }
 
    public void run() {
        running = true;
 
        while (running) {
            DatagramPacket packet 
              = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                continue;
            }
             
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received 
              = new String(packet.getData(), 0, packet.getLength());
            
            System.out.println("received: " + received);
            if (received.equals("end")) {
                running = false;
                continue;
            }
            // socket.send(packet);
        }
        socket.close();
    }
    

    public static void main(String args[]) throws SocketException {
        PrintingUdpServer server  = new PrintingUdpServer();
        server.run();
    }
}