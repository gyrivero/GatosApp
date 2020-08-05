/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gatosapp;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Gonza
 */
public class Inicio {
    public static void main(String[] args) throws IOException {        
        String[] botones = {
            "1. Ver gatos",
            "2. Ver favoritos",
            "3. Salir"
        };
        String opcion = "";
        
        while (!opcion.equals(botones[2])) {            
            opcion = (String) JOptionPane.showInputDialog(null,"Gatos Java","Menu Principal",JOptionPane.INFORMATION_MESSAGE,null,botones,botones[0]);
            
            if (opcion.equals(botones[0])) {
                GatoService.verGatos();
            }
            else if (opcion.equals(botones[1])) {
                GatoService.verFavoritos(ApiKey.getKey());
            }
        }
    }    
}
