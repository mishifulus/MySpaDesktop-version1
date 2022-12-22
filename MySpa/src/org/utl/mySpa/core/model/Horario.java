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
public class Horario {
    private int id;
    private String horaI;
    private String horaF;

    public Horario() {}

    public Horario(String horaI, String horaF) {
        this.horaI = horaI;
        this.horaF = horaF;
    }

    public Horario(int id, String horaI, String horaF) {
        this.id = id;
        this.horaI = horaI;
        this.horaF = horaF;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraI() {
        return horaI;
    }

    public void setHoraI(String horaI) {
        this.horaI = horaI;
    }

    public String getHoraF() {
        return horaF;
    }

    public void setHoraF(String horaF) {
        this.horaF = horaF;
    }

    @Override
    public String toString() {
        return "Horario{" + "id=" + id + ", horaI=" + horaI + ", horaF=" + horaF + '}';
    }
}
