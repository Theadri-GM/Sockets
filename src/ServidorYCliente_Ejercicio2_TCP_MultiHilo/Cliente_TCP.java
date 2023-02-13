package ServidorYCliente_Ejercicio2_TCP_MultiHilo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

// Cliente TCP
public class Cliente_TCP {
    // Creamos un scanner para almacenar datos.
    public static final Scanner ioS = new Scanner(System.in);
    // MAIN
    public static void main(String[] args) {
        // Variables que vamos a usar.
        int numPuertoServidor;
        String hostServidor;
        Socket socketComunicacion;
        InetAddress ipServidor;
        String echo;
        // Comprobación de que se le han pasado los datos correctamente.
        if (args.length <2){
            System.out.println("Error, debes pasar el puerto del servidor y el host servidor");
            System.exit(1);
        }
        // Le añadimos a las variables, numPuertoServidor el valor de args[0].
        numPuertoServidor = Integer.parseInt(args[0]);
        // Y a hostServidor el valor de args[1].
        hostServidor = args[1];
        // Ahora en el try-catch...
        try{
            // Nos creamos el cocketComunicacion con el host del servidor y el puerto del servidor.
            socketComunicacion = new Socket(hostServidor, numPuertoServidor);
            // En la ip del servidor almacenamos su Ip-
            ipServidor=socketComunicacion.getInetAddress();
            // Y ahora mostramos por pantalla con quien se ha conectado nuestro cliente...
            System.out.printf("Cliente conectado con servidor %s...%n", ipServidor.getHostAddress());
            // Ahora, mediante otro scanner, le vamos pasando el InputStream.
            Scanner br = new Scanner (socketComunicacion.getInputStream());
            // En este PrintWriter, vamos a pasarle el OutPutStream.
            PrintWriter pw = new PrintWriter(socketComunicacion.getOutputStream());
            System.out.println(">");
            // Ahora, mientras que el cliente pase algo...
            while((echo=ioS.nextLine()).length()>0){
                // Lo escribimos por pantalla
                pw.println(echo);
                // Vaciamos
                pw.flush();
                // Indicamos que ya hemos mandado el socket.
                System.out.println("Ya he mandado al socket");
                // Y mostramos la respuesta de nuestro servidor
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
