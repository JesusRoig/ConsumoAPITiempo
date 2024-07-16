
package practivaev_final_psp;

import java.time.LocalDateTime;

public class Tiempo {
    private double temperatura;
    private int presion;
    private double humedad;
    private double viento;
    private int nubes;
    private LocalDateTime fechaHora;

    public Tiempo(double temperatura, int presion, double humedad, double viento, int nubes) {
        this.temperatura = temperatura;
        this.presion = presion;
        this.humedad = humedad;
        this.viento = viento;
        this.nubes = nubes;
        this.fechaHora = LocalDateTime.now();
    }

    public double getTemperatura() {
        return temperatura;
    }

    public int getPresion() {
        return presion;
    }

    public double getHumedad() {
        return humedad;
    }

    public double getViento() {
        return viento;
    }

    public int getNubes() {
        return nubes;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public void setPresion(int presion) {
        this.presion = presion;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }

    public void setViento(double viento) {
        this.viento = viento;
    }

    public void setNubes(int nubes) {
        this.nubes = nubes;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        return "Tiempo{" + "temperatura=" + temperatura + ", presion=" + presion + ", humedad=" + humedad + ", viento=" + viento + ", nubes=" + nubes + ", fechaHora=" + fechaHora + '}';
    }
    
    
    
    
    
    
}
