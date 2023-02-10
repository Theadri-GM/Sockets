package ServidorYCliente_Ejercicio1_Formato_Fecha;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Utilizo el mismo cliente que en el ejemplo de la práctica.
public class Servidor_Hora {
    // Máximo de caracteres que almacenará.
    private static final int MAXBYTES = 1024;
    // Cifrado de nuestro texto.
    private static final String CODTEXTO = "UTF-8";
    // Ejecución de nuestro programa. (MAIN).
    public static void main(String[] args) {
        // Nuestros puertos.
        int numPuertoServidor, numPuertoCliente;
        // DatagramPacket.
        DatagramPacket paqueteUdp;
        // InetAddress (ip de nuestro servidor).
        InetAddress ipCliente ;
        // Comprobación valor nulo argumentos.
        if (args.length < 1){
            System.out.println("Error, debe pasar el puerto del servidor.");
            System.exit(1);
        }
        // Nuestro puerto.
        numPuertoServidor = Integer.parseInt(args[0]);
        // Creamos nuestro socket.
        try(DatagramSocket socket = new DatagramSocket(numPuertoServidor)) {
            while(true){
                System.out.println("Esperando algún datagrama");
                byte[] bufferEntrada = new byte[MAXBYTES];
                paqueteUdp = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                socket.receive(paqueteUdp);
                // Sacamos los datos del paquete.
                String lineaRecibida = new String(paqueteUdp.getData(), 0, paqueteUdp.getLength(), CODTEXTO );
                String fechaDelCliente = new SimpleDateFormat(lineaRecibida).format(new Date());
                System.out.println("Linea a dar --->"+fechaDelCliente);
                String lineaReplicar = "#"+fechaDelCliente+"#";
                ipCliente = paqueteUdp.getAddress();
                numPuertoCliente = paqueteUdp.getPort();
                // Sacamos los datos del paquete.
                byte[] bufferSalida = new byte[MAXBYTES];
                bufferSalida = lineaReplicar.getBytes();
                paqueteUdp = new DatagramPacket(bufferSalida, bufferSalida.length, ipCliente, numPuertoCliente);
                socket.send(paqueteUdp);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}