package ServidorAndClient_Echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

// Este será nuestro servidor sin hilos. Solo se ejecutará para un solo cliente.
public class ServerSinHilos {

    // Declaramos las variables necesarias para nuestro funcionamiento.
    // Declaramos el número máximo de bytes que va a soportar de mensaje.
    private static final int MAXBYTES=1400;
    // Y también declaramos, como queremos que se comporte nuestro texto.
    private static final String CODTEXTO="UTF-8";
    // Declaramos nuestro main.
    public static void main(String[] args) {
        // Ahora, inicializamos nuestro puerto de servidor ( que será el puerto que va a usar nuestro servidor).
        // Y nuestro puerto del cliente, que será el que recuperemos de nuestro cliente cuando el conecte con nosotros.
        int numPuertoServidor, numPuertoCliente;
        // El DatagramPacket será el que usemos cuando nuestro cliente quiera comunicarse con nosotros y viceversa.
        DatagramPacket paqueteUdp;
        // La InetAdress es donde guardaremos la ip del cliente.
        InetAddress ipCliente;
        // En este if, tratamos que si no le hemos pasado el puerto al servidor, no podremos arrancarlo.
        if (args.length <1 ){
            System.out.println("Error, debes pasar el puerto.");
            System.exit(1);
        }
        // Ahora, el puerto del servidor, tenemos que igualarlo al puerto que le estamos pasando por argumentos.
        numPuertoServidor = Integer.parseInt(args[0]);
        // Creamos el socket asociado al puerto. Se pone a la escucha.
        try (DatagramSocket socket = new DatagramSocket(numPuertoServidor)){
           while(true){ // Siempre va a estar ejecutandose esta función.
               System.out.println("Esperando algún datagrama");
               byte[] bufferEntrada = new byte[MAXBYTES]; // Creamos el buffer
               // Ahora, en el paqueteUdp le tenemos que pasar, tanto el buffer de entrada como la longitud de este.
               paqueteUdp = new DatagramPacket(bufferEntrada, bufferEntrada.length);
               socket.receive(paqueteUdp); // Recibinos un datagrama paqueteUdp.
               // SACAMOS LOS DATOS DEL PAQUETE, DESPLAZAMIENTO 0, TAMAÑO A LEER Y CODIFICACIÓN
               String lineaRecibida = new String(paqueteUdp.getData(), 0, paqueteUdp.getLength(), CODTEXTO);
               System.out.println("Linea recibida --->"+ lineaRecibida);
               ipCliente = paqueteUdp.getAddress(); // Sacamos la ip del cliente.
               numPuertoCliente = paqueteUdp.getPort(); // Sacamos el puerto origen del cliente.
               String lineaReplicar = "#"+lineaRecibida+"#"; // Modificamos la línea que hemos recibido desde el cliente.

               // Ahora debemos mandar la respuesta en un nuevo datagrama al Cliente
               // Preparamos otro buffer para enviar los datos al cliente.
               // El mismo que hemos creado anteriormente nos serviría, pero mejor crearemos otro.
               byte[] bufferSalida = new byte[MAXBYTES];
               bufferSalida = lineaReplicar.getBytes(); // Ponemos en el buffer el mensaje que le vamos a enviar.
               paqueteUdp = new DatagramPacket(bufferSalida, bufferSalida.length, ipCliente, numPuertoCliente);
               // Ahora, solo debemos enviarlo a nuetro socket.
               socket.send(paqueteUdp);
           }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
