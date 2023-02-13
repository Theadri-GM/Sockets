package ServidorYCliente_Ejercicio2_TCP_MultiHilo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente_TCP {
    public static final Scanner ioS = new Scanner(System.in);

    public static void main(String[] args) {
        int numPuertoServidor;
        String hostServidor;
        Socket socketComunicacion;
        InetAddress ipServidor;
        String echo;

        if (args.length <2){
            System.out.println("Error, debes pasar el puerto del servidor y el host servidor");
            System.exit(1);
        }

        numPuertoServidor = Integer.parseInt(args[0]);
        hostServidor = args[1];
        try{
            socketComunicacion = new Socket(hostServidor, numPuertoServidor);
            ipServidor=socketComunicacion.getInetAddress();
            System.out.printf("Cliente conectado con servidor %s...%n", ipServidor.getHostAddress());

            Scanner br = new Scanner (socketComunicacion.getInputStream());
            PrintWriter pw = new PrintWriter(socketComunicacion.getOutputStream());
            System.out.println(">");

            while((echo=ioS.nextLine()).length()>0){
                pw.println(echo);
                pw.flush();
                System.out.println("Ya he mandado al socket");
                System.out.printf("Respuesta : %s%n", br.nextLine());
                System.out.println(">");
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
