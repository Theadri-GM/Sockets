package ServidorYCliente_Ejercicio2_TCP_MultiHilo;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

class MultiHilo extends Thread{

    private Socket socketComunicacion;

    public MultiHilo(Socket mSocket){
        socketComunicacion = mSocket;
    }
    @Override
    public void run(){
        InetAddress ipCliente;

        try{
            Scanner br = new Scanner(socketComunicacion.getInputStream());
            PrintWriter pw = new PrintWriter(socketComunicacion.getOutputStream());

            System.out.println("Ahora, esperamos los echo.");
            String lineaRecibida;
            while( (lineaRecibida=br.nextLine())!=null && lineaRecibida.length()>0){
                ipCliente = socketComunicacion.getInetAddress();
                System.out.printf("Recibo desde %s por puerto %d> %s%n",ipCliente.getHostAddress(), socketComunicacion.getPort(), lineaRecibida);
                lineaRecibida = new SimpleDateFormat(lineaRecibida).format(new Date());
                pw.println(lineaRecibida);
                pw.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchElementException e){
            System.out.println("El cliente ha cerrado su conexión...");
            if (socketComunicacion != null)
                try{
                    System.out.printf("Socket servidor Multihio, cerrado%n");
                    socketComunicacion.close();
                } catch (IOException ex) {
                    System.out.println("Error en flujo de E/S al cerrar el Socket una vez desconectado con cliente.");
                    ex.printStackTrace();
                }
        }
    }
}
public class Server_MultiHilo_TCP {

    public static void main(String[] args) {
        int numPuertoServidor;
        ServerSocket socketServidor;
        Socket socketComunicacion = null;
        InetAddress ipCliente;
        int puertoCliente;

        if (args.length<1){
            System.out.println("Error, debes pasar el puerto del servidor");
            System.exit(1);
        }
        numPuertoServidor = Integer.parseInt(args[0]);
        try{
            socketServidor = new ServerSocket(numPuertoServidor);
            while(true){
                socketComunicacion = socketServidor.accept();
                ipCliente = socketComunicacion.getInetAddress();
                puertoCliente = socketComunicacion.getPort();
                System.out.printf("Conexión establecida con cliente con ip... %s : puerto: %d%n", ipCliente, puertoCliente);
                MultiHilo servidorH = new MultiHilo(socketComunicacion);
                servidorH.start();
            }
        } catch (IOException e) {
            System.out.println("Error en flujo de E/S");
            e.printStackTrace();
        }
    }
}
