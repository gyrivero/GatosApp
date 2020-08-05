/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gatosapp;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Gonza
 */
public class GatoService {
    public static void verGatos() throws IOException {    
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search")
                                            .get()
                                            .build();
            Response response = client.newCall(request).execute();

            String json = response.body().string();

            json = json.substring(1,json.length());
            json = json.substring(0,json.length()-1);

            Gson gson = new Gson();
            Gato gato = gson.fromJson(json, Gato.class); 
            URL url = new URL(gato.getUrl());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(http.getInputStream());            
            
            ImageIcon fondoGato = new ImageIcon(bufferedImage);
            
            if (fondoGato.getIconWidth() > 800) {
                Image imagen = fondoGato.getImage();
                fondoGato.setImage(imagen.getScaledInstance(700, 525, java.awt.Image.SCALE_SMOOTH));              
            }
            
            String menu = "Elige una opcion";
            String[] botones = {"Ver otra imagen","Favorito","Volver"};            
            String idGato = String.valueOf(gato.getId());
            String opcion = (String) JOptionPane.showInputDialog(null,menu,idGato,JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);            
                       
            if (opcion.equals(botones[0])) {
                verGatos();
            }
            else if(opcion.equals(botones[1])) {
                favorito(gato);
            }                
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }    

    public static void favorito(Gato gato) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n  \"image_id\": \""+gato.getId()+"\"\r\n}");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites")
              .method("POST", body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", ApiKey.getKey())
              .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verFavoritos(String apiKey) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites")
              .method("GET", null)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", apiKey)
              .build();
            Response response = client.newCall(request).execute();

            String json = response.body().string();

            Gson gson = new Gson();
            GatoFavoritos[] gatosFavoritos = gson.fromJson(json, GatoFavoritos[].class);

            if (gatosFavoritos.length>0) {
                int min = 1;
                int max = gatosFavoritos.length;
                int aleatorio = (int) ((Math.random() * ((max-min) + 1)) + min);
                int indice = aleatorio - 1;

                GatoFavoritos gatoFav = gatosFavoritos[indice];
                
                URL url = new URL(gatoFav.getImage().getUrl());
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.addRequestProperty("User-Agent", "");
                BufferedImage bufferedImage = ImageIO.read(http.getInputStream());            

                ImageIcon fondoGato = new ImageIcon(bufferedImage);

                if (fondoGato.getIconWidth() > 800) {
                    Image imagen = fondoGato.getImage();
                    fondoGato.setImage(imagen.getScaledInstance(700, 525, java.awt.Image.SCALE_SMOOTH));              
                }

                String menu = "Elige una opcion";
                String[] botones = {"Ver otra imagen","Eliminar favorito","Volver"};            
                String idGato = String.valueOf(gatoFav.getId());
                String opcion = (String) JOptionPane.showInputDialog(null,menu,idGato,JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);            

                if (opcion.equals(botones[0])) {
                    verFavoritos(apiKey);
                }
                else if(opcion.equals(botones[1])) {
                    borrarFavorito(gatoFav);                    
                    return;
                }                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    private static void borrarFavorito(GatoFavoritos gatoFav) throws IOException {        
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+gatoFav.getId()+"")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", ApiKey.getKey())
                    .build();
            Response response = client.newCall(request).execute();   
        } catch (Exception e) {
            e.printStackTrace();
        }       
    }
}
