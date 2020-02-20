/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Norath
 */
public class HiloAdministrador extends Thread {

    ServerSocket listenerSocket;
    private ArrayList<Socket> conexiones = new ArrayList<>();
    private ArrayList<String> clientes = new ArrayList<>();
    private int puerto;

    public HiloAdministrador(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {
        try {
            listenerSocket = new ServerSocket(puerto);
            do {
                Socket connCliente = listenerSocket.accept();

                conexiones.add(connCliente);
                
                HiloConexion hc = new HiloConexion(conexiones, connCliente,clientes);
                hc.start();
                

            } while (!listenerSocket.isClosed());

        } catch (SocketException se) {
            for (Socket conexion : conexiones) {
                try {
                    conexion.close();
                } catch (IOException ex) {
                    System.err.println("Fallo al cerrar las conexiones de cliente");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(HiloAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ServerSocket getListenerSocket() {
        return listenerSocket;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

}
