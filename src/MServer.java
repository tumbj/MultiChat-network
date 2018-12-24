//นายธนดล คงสกุล 5910450298 sec:200
//นายนักกระวี คืนคลีบ 5910451073 sec:200
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

class MServer implements Runnable {
    private ServerSocket ss;
    private ArrayList<Communicate> communicates;

    OutputCallback outputCallback = new OutputCallback() {
        @Override
        public void onConnected(Communicate commu) {
            Socket socket = commu.getSocket();
            String hostName =  socket.getInetAddress().getHostName();
            int port = socket.getPort();
            String msg = "010: Client connected from " + hostName
                    + " :" + port;
            System.out.println(msg);
            for (Communicate c : communicates) {
                if(c != commu){
                    c.send(msg);
                }
            }
        }

        @Override
        public void onDisconected(Communicate commu) {
            if(running) {
                communicates.remove(commu);
                Socket socket = commu.getSocket();
                String hostName = socket.getInetAddress().getHostName();
                int port = socket.getPort();
                String msg = "020: Client disconnected from " + hostName
                        + " :" + port;
                System.out.println();

                broadcast(msg);
            }
        }

        @Override
        public void onReceived(Communicate channel, String msg) {
            System.out.println("011: Client send message");
            System.out.println("-----300 :Continue-----");
            System.out.println("-----301 :Process-----");
            System.out.println(msg);

            broadcast(msg);
            System.out.println("-----310 :Chat updated-----");
        }
    };
    private boolean running;

    public MServer(int port) throws IOException {
        ss = new ServerSocket(port);
    }
    public void run() {
        running = true;
        communicates = new ArrayList<>();
        while (true) {
            try {
                Socket s = ss.accept();
                Communicate communicate = new Communicate(s, outputCallback);
                communicate.start();
                communicates.add(communicate);
            } catch (IOException e) {
                break;
            }
        }
        try{
            for(Communicate communicate :communicates){
                communicate.stop();
            }
        }catch (IOException e ){
            e.printStackTrace();
        }
    }

    public void broadcast(String msg){
        if (!running) return;
        for (Communicate communicate : communicates) {
            communicate.send(msg);
        }
    }



        public static void main(String[] args) throws IOException {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Port :");
                int port = Integer.parseInt(scanner.nextLine());
                MServer mserver = new MServer(port);
                Thread thread = new Thread(mserver);
                thread.start();

                //send
                while (true) {
                    try{
                        String msg = scanner.nextLine();
                        if (msg.equals("exit")) {
                            mserver.running = false;
                            mserver.ss.close();
                            scanner.close();
                            break;
                        }
                        mserver.broadcast("Server" + ">> " + msg);

                    }catch (NoSuchElementException ne){
                        System.out.println("Closed");
                    }

                }
                System.out.println("Server closed");

        }


}