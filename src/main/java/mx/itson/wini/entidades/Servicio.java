/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.wini.entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.itson.wini.persistencia.Conexion;

/**
 *
 * @author pedrizquierdo
 */
public class Servicio {

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the fechaRealizacion
     */
    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    /**
     * @param fechaRealizacion the fechaRealizacion to set
     */
    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    /**
     * @return the responsable
     */
    public Responsable getResponsable() {
        return responsable;
    }

    /**
     * @param responsable the responsable to set
     */
    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    /**
     * @return the descripcionProblema
     */
    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    /**
     * @param descripcionProblema the descripcionProblema to set
     */
    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    /**
     * @return the actividades
     */
    public List<Actividad> getActividades() {
        return actividades;
    }

    /**
     * @param actividades the actividades to set
     */
    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }
    
    private int id;
    private Date fechaRealizacion;
    private Responsable responsable;
    private String descripcionProblema;
    private List<Actividad> actividades;
    
    public static Servicio getById(int id){
            Servicio s = new Servicio ();
        try {
            Connection conexion = Conexion.obtener();
            
            String query = "SELECT id, fecha_realizacion, descripcion_problema FROM servicio WHERE id = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setInt(1, id);
            
            ResultSet rs = rs = statement.executeQuery();
            while(rs.next()) {
 
                s.setId(rs.getInt(1));
                s.setFechaRealizacion(rs.getDate(2));
                s.setDescripcionProblema(rs.getString(3));

            }
        } catch (Exception ex) {
            System.err.println("Ocurrió un error " + ex.getMessage());
        }
        return s;
    }
    
    public static boolean save(Date fecha, int idResponsable, String descripcionProblema) {
        boolean resultado = false;
        try{
                Connection conexion = Conexion.obtener();
                String consulta = "INSERT INTO servicio (fecha_realizacion, id_responsable, descripcion_problema) VALUES (?, ?, ?)";
                PreparedStatement statement = conexion.prepareStatement(consulta);
                statement.setDate(1, new java.sql.Date(fecha.getTime()));
                statement.setInt(2, idResponsable);
                statement.setString(3, descripcionProblema);
                statement.execute();
                resultado = statement.getUpdateCount() == 1;
                conexion.close();
        }catch(Exception ex) {
            System.err.println("Ocurrió un error: " + ex.getMessage());
        }return resultado;
        
        
    }
    
    public static boolean edit(Date fecha, int idResponsable, int idServicio, String descripcionProblema) {
        boolean resultado = false;
        try{
                Connection conexion = Conexion.obtener();
                String consulta = "UPDATE servicio SET fecha_realizacion = ?, descripcion_problema = ?, id_responsable = ? WHERE id = ?";
                PreparedStatement statement = conexion.prepareStatement(consulta);
                statement.setDate(1, new java.sql.Date(fecha.getTime()));
                statement.setString(2, descripcionProblema);
                statement.setInt(3, idResponsable);
                statement.setInt(4, idServicio);
                
                statement.execute();
                resultado = statement.getUpdateCount() == 1;
                conexion.close();
        }catch(Exception ex) {
            System.err.println("Ocurrió un error: " + ex.getMessage());
        }return resultado;
    } 
    
    public static boolean delete(int idServicio) {
        boolean resultado = false;
        try{
                Connection conexion = Conexion.obtener();
                String consulta = "DELETE FROM Servicio WHERE id = ?";
                PreparedStatement statement = conexion.prepareStatement(consulta);
                
                statement.setInt(1, idServicio);
                
                statement.execute();
                resultado = statement.getUpdateCount() == 1;
                conexion.close();
        }catch(Exception ex) {
            System.err.println("Ocurrió un error: " + ex.getMessage());
        }return resultado;
    } 
    
    public static List<Servicio> getAll (){
        List<Servicio> servicios = new ArrayList<>();
        try {
            Connection conexion = Conexion.obtener();
            Statement statement = conexion.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id, fecha_realizacion, id_responsable, descripcion_problema FROM servicio");
            while(rs.next()) {
                Servicio s = new Servicio ();
                s.setId(rs.getInt(1));
                s.setFechaRealizacion(rs.getDate(2));
                
                //Obtenemos un ejemplo de tipo responsable 
                Responsable r = Responsable.getById(rs.getInt(3));
                s.setResponsable(r);
                
                s.setDescripcionProblema(rs.getString(4));
                
                //Obtenemos una lista de tipo Actividad
                List<Actividad> actividades = Actividad.getList(rs.getInt(1));
                s.setActividades(actividades);
                
                servicios.add(s);
                
            }
            
        } catch(Exception ex){
            System.err.println("Ocurrió un error: " + ex.getMessage());
        } return servicios; 
    } 
}
