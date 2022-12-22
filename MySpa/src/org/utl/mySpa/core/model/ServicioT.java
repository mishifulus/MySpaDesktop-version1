/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.core.model;

import java.util.List;

/**
 *
 * @author marti
 */
public class ServicioT {
    private int id;
    private List<Tratamiento> tratamientos;
    private Servicio servicio;

    public ServicioT() {}

    public ServicioT(List<Tratamiento> tratamientos, Servicio servicio) {
        this.tratamientos = tratamientos;
        this.servicio = servicio;
    }

    public ServicioT(int id, List<Tratamiento> tratamientos, Servicio servicio) {
        this.id = id;
        this.tratamientos = tratamientos;
        this.servicio = servicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    @Override
    public String toString() {
        return "ServicioT{" + "id=" + id + ", tratamientos=" + tratamientos.toString() + ", servicio=" + servicio.toString() + '}';
    }
}
