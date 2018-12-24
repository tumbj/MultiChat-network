//นายธนดล คงสกุล 5910450298 sec:200
//นายนักกระวี คืนคลีบ 5910451073 sec:200
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Communicate implements Runnable {
    //can receive and send
    private Socket socket;
    private Scanner reader;
    private boolean running;

    private PrintWriter printWriter;

    private OutputCallback outputCallback;

    public Communicate(Socket socket, OutputCallback outputCallback) {
        this.socket = socket;
        this.outputCallback = outputCallback;
    }
    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }
    public void stop() throws IOException {
        running = false;
        reader.close();
        printWriter.close();
        socket.close();
    }
    @Override
    public void run() {
        try {
            //out to client
            OutputStream opStream = socket.getOutputStream();
            printWriter = new PrintWriter(opStream,true);
            //in to server
            InputStream ipStream = socket.getInputStream();
            reader = new Scanner(ipStream);

            if( outputCallback != null)  {
                outputCallback.onConnected(this);
            }
            running = true;
            while (running){
                try
                {
                    String msg = reader.nextLine();

                    if(null != outputCallback)
                        outputCallback.onReceived(this, msg);
                }
                catch(NoSuchElementException e)
                {
                    this.stop();
                    break;
                }
            }
            if(null != outputCallback){
                outputCallback.onDisconected(this);
            }

        } catch (IOException e) {
            System.out.println("Server closed");
            e.printStackTrace();
        }

    }
    public void send(String msg){
        printWriter.println(msg);
    }
    public Socket getSocket(){
        return socket;
    }
}