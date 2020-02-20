/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Norath
 */
public class HiloConexion extends Thread {

    private ArrayList<Socket> conexiones;
    private ArrayList<String> clientes;

    private Socket connCliente;
    private String nick;

    public HiloConexion(ArrayList<Socket> conexiones, Socket connCliente, ArrayList<String> clientes) {
        this.conexiones = conexiones;
        this.connCliente = connCliente;
        this.clientes = clientes;
    }

    public void run() {
        Scanner entrada = null;
        PrintWriter salida = null;
        try {
            entrada = new Scanner(connCliente.getInputStream());

            nick = entrada.nextLine();
            System.out.println(nick);
            if (clientes.contains(nick)) {

                salida = new PrintWriter(connCliente.getOutputStream());
                salida.print("Usuario " + nick + " ya existe, introduzca otro" + "\r\n");
                salida.flush();
                connCliente.close();
                return;
            }

            for (Socket conexion : conexiones) {
                salida = new PrintWriter(conexion.getOutputStream());

                salida.print(nick + " se ha unido al chat" + "\r\n");
                salida.flush();
            }

            String mensaje = null;
            do {
                try {
                    mensaje = entrada.nextLine();
                    System.out.println(mensaje);
                    for (Socket conexion : conexiones) {
                        salida = new PrintWriter(conexion.getOutputStream());

                        salida.print(nick + ":" + mensaje + "\r\n");
                        salida.flush();
                    }
                } catch (NoSuchElementException nsee) {
                    System.out.println("El servidor ha cerrado las conexiones");
                }

            } while (!"!q".equals(mensaje));

        } catch (IOException ex) {
            System.out.println("No se ha podido recuperar la I/O del cliente");
            return;
        }

    }
}
