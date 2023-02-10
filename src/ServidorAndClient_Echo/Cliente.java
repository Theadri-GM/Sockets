package ServidorAndClient_Echo;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private static final int MAXBYTES=1400;

    private static final String CODTEXTO = "UTF-8";
    public static void main(String[] args) {
        Scanner in;
        /*
            - Comprobar parámetros ip server + puerto server
        */
        if (args.length <2){
            System.out.println("Debes pasar host y puerto");
            System.exit(1);
        }
        /*
            - Crear un buffer para la lectura de los datos.
        */
        byte[] buffer = new byte[MAXBYTES];
        /*
            - Creamos un socket.
            - Creamos un flujo entrada para lectura de datos.
            - Leer una cadena desde flujo.
            - Pasar al buffer de bytes.
            - Crear un InetAdress con la ip del servidor.
            - Crear un DatagramPacket con el buffer salida, longitud, ip del servidor y puerto del servidor.
            - CONECTAR NUESTRO SOCKET CON IP DEL SERVIDOR Y PUERTO DEL SERVIDOR.
            - Mandar el DatagramPacket. En el comento que se manda, se marca la ip y puerto del cliente que somos nosotros.
            - Crear buffer para la recepción de la respuesta del servidor.
            - Crearemos un DatagramPacket para la recepción del paquete.
            - Recibir de nuestro socket cliente, pasándole nuestro DatagramPacket anterior.
            - Sacar la respuesta del paquete con el constructor de String (paquete.getData(), 0, paquete.longitud, CODTEXTO)
            - Mostrar en pantalla la respuesta que nos ha mandado el servidor.
         */
        int puertoServidor = Integer.parseInt(args[1]);
        String hostServidor = args[0];
        in = new Scanner(System.in);

        try(DatagramSocket socketCliente = new DatagramSocket()){
            while(true){
                System.out.print("Msg--->");
                String linea = in.nextLine();
                buffer = linea.getBytes();
                InetAddress ipServidor = InetAddress.getByName(hostServidor);
                DatagramPacket datagramaUdp = new DatagramPacket(buffer, buffer.length, ipServidor, puertoServidor);
                socketCliente.connect(ipServidor, puertoServidor);
                socketCliente.send(datagramaUdp);

                // Ahora esperamos a recibir la respuesta del servidor.
                byte[] bufferRespuesta = new byte[MAXBYTES];
                System.out.println("Esperando respuesta...");
                // Preparamos el paquete para respuesta. Lo preparamos con el tamaño del buffer.
                datagramaUdp = new DatagramPacket(bufferRespuesta, bufferRespuesta.length);
                socketCliente.receive(datagramaUdp);
                // Ahora recogemos el mensaje del datagrama.
                String msgRespuesta = new String(datagramaUdp.getData(), 0, datagramaUdp.getLength(), CODTEXTO);
                System.out.println("Mensaje--->" + msgRespuesta);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
