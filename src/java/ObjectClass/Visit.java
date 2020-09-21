/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectClass;

import java.io.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//import com.fasterxml.jackson.databind;
/**
 *
 * @author baran
 */
@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public class Visit implements Serializable, Comparable<Visit> {

    private int ID;
    private String type;
    private int unitPrice;
    private int quantity;
    private int cost;
    private int clinicID = 0;

    public Visit() {

    }

    public Visit(int ID, String type, int unitPrice, int quantity, int cost) {

        setID(ID);
        setType(type);
        setUnitPrice(unitPrice);
        setQuantity(quantity);
        setCost(cost);
        this.clinicID = 0;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Visit guest = (Visit) obj;

        return this.cost == guest.cost && this.type.equals(guest.type) && this.unitPrice == guest.unitPrice && this.quantity == guest.quantity;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.cost;
        //result = prime * result + this.;
        return result;
    }

    public int compareTo(Visit visit) {

        return this.type.compareTo(visit.getType());

    }

    public String getType() {
        return type;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCost() {

        return cost;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUnitPrice(int unitPrice) {
        if (unitPrice < 0) {
            try {
                throw new IllegalArgumentException();
            } catch (IllegalArgumentException excp) {
                System.out.print("Неверно заданы значения");
            }
        }
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            try {
                throw new IllegalArgumentException();
            } catch (IllegalArgumentException excp) {
                System.out.print("Неверно заданы значения");
            }
        }
        this.quantity = quantity;
    }

    public void setCost(int cost) {
        if (cost != quantity * unitPrice) {
            try {
                throw new IllegalArgumentException();
            } catch (IllegalArgumentException excp) {
                System.out.print("Неверно заданы значения");
            }
        }
        this.cost = cost;
    }

    public static void outputObject(Visit visit, PrintWriter pW) {
        pW.println(visit.ID);
        pW.println(visit.type);
        pW.println(visit.unitPrice);
        pW.println(visit.quantity);
        pW.println(visit.cost);
        pW.println(visit.clinicID);
    }

    public static Visit inputObject(BufferedReader br) {

        String type = null;
        int unitPrice = 0;
        int quantity = 0;
        int ID = 0;
        int cost = 0;
        int clinicID = 0;
        try {
            ID = Integer.parseInt(br.readLine());
            type = br.readLine();
            unitPrice = Integer.parseInt(br.readLine());
            quantity = Integer.parseInt(br.readLine());
            cost = Integer.parseInt(br.readLine());
            clinicID = Integer.parseInt(br.readLine());
        } catch (IOException ioe) {
            System.out.print("Ошибка при чтении из файла");
        }

        return new Visit(ID, type, unitPrice, quantity, cost);

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getClinicID() {
        return this.clinicID;
    }

    public void setClinicID(int clinicID) {
        this.clinicID = clinicID;
    }

}
