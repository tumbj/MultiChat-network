//นายธนดล คงสกุล 5910450298 sec:200
//นายนักกระวี คืนคลีบ 5910451073 sec:200
import java.io.*;
import java.net.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

class MClient implements  Calculate{


    public static void main(String[] args) {
        MClient  mClient = new MClient();
        Scanner sc = new Scanner(System.in);

        OutputCallback outputCallback = new OutputCallback() {
            @Override
            public void onConnected(Communicate commu) {
                System.out.println("200 : Connected Complete.");
            }

            @Override
            public void onDisconected(Communicate commu) {
                System.out.println("400 : Disconnected from server.");
            }

            @Override
            public void onReceived(Communicate channel, String msg) {
                System.out.println(msg);
            }
        };
        System.out.print("Name : ");
        String name = sc.nextLine();

        System.out.print("IP : ");
        String ip = sc.nextLine();

        System.out.print("Port : ");
        int port = Integer.parseInt(sc.nextLine());

        try {
            Socket socket = new Socket(ip, port);
            //recieve from server
            Communicate commu = new Communicate(socket, outputCallback);
            commu.start();
            System.out.println("211: Ip correct");
            //send
            while (true) {
                try {
                    String msg = sc.nextLine();
                    if (msg.equals("exit")) {
                        socket.close();
                        break;
                    } else if (msg.equals("date")) {
                        System.out.println("100: Command complete");
                        System.out.println(Date.valueOf(LocalDate.now()) + "");
                    } else if (msg.equals("time")) {
                        System.out.println("100: Command complete");
                        System.out.println(LocalTime.now().toString() + "");
                    } else if (msg.equals("#")) {
                        System.out.println("Command for chat.");
                        System.out.println("date : check date now");
                        System.out.println("time : check time now");
                        System.out.println("cal : calculate number between 2 operand");
                        System.out.println("exit : exit chat (client)");
                    } else if (msg.equals("cal")) {
                        System.out.println("Please insert data(+,-,*,/). eq: 10 + 20, 20 / 5");
                        String[] in = sc.nextLine().split(" ");
                        double a = Double.parseDouble(in[0]);
                        char b = in[1].charAt(0);
                        double c = Double.parseDouble(in[2]);
                        System.out.println(a + " " + b + " " + c + " = " + mClient.sum(a, c, b));
                    } else {
                        commu.send(name + " >> " + msg);

                    }
                } catch (NoSuchElementException ne) {
                    System.out.println("Closed");
                } catch (SocketException s) {
                    System.out.println("212: Invalid ip");
                } catch (ArithmeticException e) {
                    System.out.println("221: Invalid operator");
                }
            }
            sc.close();
            commu.stop();
            System.out.println("Closed");
        } catch (ConnectException co) {
            System.out.println("201 :Invalid port");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public  double sum(double a, double b, char d) {
        if(d=='+')return a+b;
        else if(d=='-')return a-b;
        else if(d=='*')return a*b;
        else if(d=='/')return a/b;
        else{
            throw new ArithmeticException();
        }
    }
}