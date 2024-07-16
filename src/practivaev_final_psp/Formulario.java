package practivaev_final_psp;

import com.google.gson.Gson;
import com.google.gson.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import org.json.*;

public class Formulario extends JFrame implements ActionListener {

    JPanel panel;
    JSONObject json;
    Gson miGson = new Gson();
    String jsonEnviar;
    String clave = "Hola";
    Key clavePublica;
    String mensajeEncriptado;

    JComboBox<String> ciudadesCB;
    String[] listaCiudades = {"valencia", "madrid", "barcelona", "leon", "malaga", "sevilla", "bilbao", "zaragoza", "lugo", "badajoz"};

    JLabel tempL;
    JLabel presionL;
    JLabel humedadL;
    JLabel vientoL;
    JLabel nubesL;

    JButton selecionarB;

    URL url;
    HttpURLConnection conexion;

    public Formulario() {
        this.setTitle("APP Tiempo");
        // Tamaño en ancho y alto
        this.setSize(900, 500);
        // Colocamos la ventana en el centro de la pantalla
        this.setLocationRelativeTo(null);
        // Evitamos que se pueda redimensionar desde el borde
        this.setResizable(false);
        // Damos la operativa al boton X de cerrar
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);

        ciudadesCB = new JComboBox<String>();
        ciudadesCB.setBounds(10, 20, 100, 30);
        panel.add(ciudadesCB);
        for (int i = 0; i < listaCiudades.length; i++) {
            ciudadesCB.addItem(listaCiudades[i]);
        }

        selecionarB = new JButton("Seleccionar");
        selecionarB.setBounds(130, 20, 150, 30);
        panel.add(selecionarB);
        selecionarB.addActionListener(this);

        tempL = new JLabel("Temperatura: ");
        tempL.setBounds(10, 250, 180, 30);
        panel.add(tempL);

        presionL = new JLabel("Presion: ");
        presionL.setBounds(200, 250, 100, 30);
        panel.add(presionL);

        humedadL = new JLabel("Humedad: ");
        humedadL.setBounds(390, 250, 100, 30);
        panel.add(humedadL);

        vientoL = new JLabel("Viento: ");
        vientoL.setBounds(580, 250, 100, 30);
        panel.add(vientoL);

        nubesL = new JLabel("Nubes: ");
        nubesL.setBounds(770, 250, 100, 30);
        panel.add(nubesL);

        this.add(panel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                conexion.disconnect();
                System.out.println("Salimos de la aplicación");
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selecionarB) {
            String q = (String) ciudadesCB.getSelectedItem();
            try {
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + q + "&units=metric&APPID=a77a241542b9ed8ac5b8e533f10c2f0a");
                conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");
                conexion.setRequestProperty("Accept", "aplication/json");
                int codigoRespuesta = conexion.getResponseCode();
                System.out.println("Respuesta: " + codigoRespuesta);

                BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = "";
                String jsonString = "";
                while ((linea = br.readLine()) != null) {
                    //System.out.println(linea);
                    jsonString += linea;
                }
                json = new JSONObject(jsonString);
                //System.out.println(json);
                double temp = json.getJSONObject("main").getDouble("temp");
                int presion = json.getJSONObject("main").getInt("pressure");
                double humedad = json.getJSONObject("main").getDouble("humidity");
                double viento = json.getJSONObject("wind").getDouble("speed");
                int nubes = json.getJSONObject("clouds").getInt("all");

                Tiempo tiempo = new Tiempo(temp, presion, humedad, viento, nubes);
                tempL.setText("Temperatura: " + tiempo.getTemperatura());
                presionL.setText("Presión: " + tiempo.getPresion());
                humedadL.setText("Humedad: " + tiempo.getHumedad());
                vientoL.setText("Viento: " + tiempo.getViento());
                nubesL.setText("Nubes: " + tiempo.getNubes());

                String clave = "Hola";
                jsonEnviar = miGson.toJson(tiempo);
                String jsonEncriptado = encriptar(jsonEnviar, clave);

                Socket cliente = new Socket("localhost", 6000);
                System.out.println("Conectando cliente");
                DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
                salida.writeUTF(jsonEncriptado);
                System.out.println("Mensaje Enviado");
                
                
                br.close();
                salida.close();
                cliente.close();
                conexion.disconnect();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // Convertimos la clave String en un array de bytes
        byte[] claveBytes = clave.getBytes("UTF-8");
        // Encriptamos la clave en SHA-1
        // Indicamos el algoritmo de encriptacion
        MessageDigest algSHA = MessageDigest.getInstance("SHA-1");
        // Pasamos la clave al algoritmo de encirptacion
        algSHA.update(claveBytes);
        // Encriptamos la clave
        byte[] claveEncrptada = algSHA.digest();
        // Algoritmo AES utiliza los priomeros bytes de la clave
        claveEncrptada = Arrays.copyOf(claveEncrptada, 16);
        // Generamos la clave privada de tipo secretKeySpec
        // Para poder encriptar y desencriptar
        SecretKeySpec clavePrivada = new SecretKeySpec(claveEncrptada, "AES");
        return clavePrivada;
    }

    public static String encriptar(String mensaje, String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec claveSecreta = crearClave(clave);
        // Creamos instgancia de tipo Cipher para encriptar
        Cipher cifrar = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        // Inicializamos el cifrado indicando el modo (Encriptar) y la clave del tipo SecretKeySpec
        cifrar.init(Cipher.ENCRYPT_MODE, claveSecreta);
        // Convertir el mensaje en un array de Bytes
        byte[] mensajeBytes = mensaje.getBytes();
        // Metodo doFinal encripta el mensaje y devuelve un array de bytes
        byte[] mensajeBytesEncriptado = cifrar.doFinal(mensajeBytes);
        // Codificamos el mensajeen bytes encriptado en Base64
        String mensajeEncriptado = Base64.getEncoder().encodeToString(mensajeBytesEncriptado);
        // Retornamos el mensaje encriptado
        return mensajeEncriptado;

    }

}
