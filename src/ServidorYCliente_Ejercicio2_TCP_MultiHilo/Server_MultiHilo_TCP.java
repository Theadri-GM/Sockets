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
// Creamos la clase multihilo que va a ser nuestro hilo que va a ejecutarse cada vez que un cliente se conecte.
class MultiHilo extends Thread{
    // Le creamos un atributo a la clase el cual va a ser el Socket.
    private Socket socketComunicacion;
    // Constructor de la clase-
    public MultiHilo(Socket mSocket){
        socketComunicacion = mSocket;
    }
    // Método run que ejecutará nuestro hilo.
    @Override
    public void run(){
        // Creamos un InetAdress que básicamente es donde almacenaremos la ip del cliente, (para saber con qué cliente
        // estamos hablando)
        InetAddress ipCliente;
        // Try-catch...
        try{
            // Creamos un Scanner al que le pasaremos el Input del socket.
            Scanner br = new Scanner(socketComunicacion.getInputStream());
            // Creamos el PrinterWriter que nos almacenará el Output del socket.
            PrintWriter pw = new PrintWriter(socketComunicacion.getOutputStream());
            // Mostramos que estamos esperando los echo de los clientes...
            System.out.println("Ahora, esperamos los echo.");
            // Vamos a almacenar dentro de una variable de tipo String las líneas que haya dentro del Scanner que
            // almacenaba nuestro Input.
            String lineaRecibida;
            // Ahora, mediante el bucle while mientras que, lineaRecibida sea distinto de null y la longitud de
            // lineaRecibida sea mayor que 0 se va a estar ejecutando...
            while( (lineaRecibida=br.nextLine())!=null && lineaRecibida.length()>0){
                // Almacenamos la ip del cliente.
                ipCliente = socketComunicacion.getInetAddress();
                // Mostramos la información que recibimos desde el host del cliente y desde el puerto de este.
                System.out.printf("Recibo desde %s por puerto %d> %s%n",ipCliente.getHostAddress(), socketComunicacion.getPort(), lineaRecibida);
                // Ahora, en la línea recibida, le tenemos que cambiar el formato para que devuelva el formato de la
                // fecha.
                lineaRecibida = new SimpleDateFormat(lineaRecibida).format(new Date());
                // Y mostramos lineaRecibida.
                pw.println(lineaRecibida);
                // Y también vaciamos el PrinterWriter que tenemos creado.
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
    // MAIN
    public static void main(String[] args) {
        // Creamos las variables que vamos a usar en nuestro Servidor.
        int numPuertoServidor;
        ServerSocket socketServidor;
        Socket socketComunicacion = null;
        InetAddress ipCliente;
        int puertoCliente;
        // Comprobamos que le pasemos bien los datos por argumentos al servidor
        if (args.length<1){
            System.out.println("Error, debes pasar el puerto del servidor");
            System.exit(1);
        }
        // Almacenamos en el numPuertoServidor el argumento que le pasemos por args[0]
        numPuertoServidor = Integer.parseInt(args[0]);
        // Try-catch...
        try{
            // Almacenamos en nuestro socoketServidor un nuevo ServerSocket con el numPuertoServidor
            socketServidor = new ServerSocket(numPuertoServidor);
            // Ahora, siempre se va a ejecutar...
            while(true){
                // Le decimos que el socketComunicacion acepte la petición del cliente.
                socketComunicacion = socketServidor.accept();
                // Almacenamos la ip del cliente...
                ipCliente = socketComunicacion.getInetAddress();
                // Almacenamos el puerto del servidor...
                puertoCliente = socketComunicacion.getPort();
                // Y mostramos por pantalla que hemos establecido la conexión con el cliente - ip del cliente -
                // y puerto - puerto del cliente.
                System.out.printf("Conexión establecida con cliente con ip... %s : puerto: %d%n", ipCliente, puertoCliente);
                // Y lanzamos un nuevo hilo de la clase que hemos creado antes, el cual ejecutará el método run de
                // la clase hilo.
                MultiHilo servidorH = new MultiHilo(socketComunicacion);
                servidorH.start();
            }
        } catch (IOException e) {
            System.out.println("Error en flujo de E/S");
            e.printStackTrace();
        }
    }
}
