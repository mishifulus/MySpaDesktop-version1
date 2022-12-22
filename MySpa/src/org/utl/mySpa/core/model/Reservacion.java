/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.core.model;

/**
 *
 * @author marti
 */
public class Reservacion {
    private int id;
    private String fechaReservacion;
    private int estatus;
    private Sala sala;
    private Cliente cliente;

    public Reservacion() {}

    public Reservacion(String fechaReservacion, int estatus, Sala sala, Cliente cliente) {
        this.fechaReservacion = fechaReservacion;
        this.estatus = estatus;
        this.sala = sala;
        this.cliente = cliente;
    }

    public Reservacion(int id, String fechaReservacion, int estatus, Sala sala, Cliente cliente) {
        this.id = id;
        this.fechaReservacion = fechaReservacion;
        this.estatus = estatus;
        this.sala = sala;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(String fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Reservacion{" + "id=" + id + ", fechaReservacion=" + fechaReservacion + ", estatus=" + estatus + ", sala=" + sala.toString() + ", cliente=" + cliente.toString() + '}';
    }
}
