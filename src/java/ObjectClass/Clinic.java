/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectClass;

import java.io.FileNotFoundException;
import java.io.*;
import java.io.IOException;
import java.util.*;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author baran
 */

@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public class Clinic implements Serializable {

    private int ID;
    private ArrayList<Visit> visits = new ArrayList();

    public Clinic() {

    }

    public Clinic(int ID) {
        setID(ID);
    }

    public int getLengthArray() {
        return visits.size();
    }

    public void addElement(int i, Visit visit) {
        visits.add(i, visit);
        visit.setClinicID(this.ID);
    }

    public void removeElment(int i) {
        visits.get(i).setClinicID(0);
        visits.remove(i);
    }

    public void sort() {

        try {
            Set<Visit> set = new LinkedHashSet<Visit>(visits);
            visits.clear();
            visits.addAll(set);

            Collections.sort(visits);
        } catch (IndexOutOfBoundsException q) {
            System.out.println("пустой или некорректный");
        }
    }

    public Visit getElement(int i) {
        try {
            return visits.get(i);
        } catch (IndexOutOfBoundsException e) {
            System.out.print("Выход за предел массив");
            return null;
        }
    }

    public void outputList(String Name) {
        PrintWriter Pw = null;

        try {
            Pw = new PrintWriter(Name);
            Pw.println(this.visits.size());
            for (int i = 0; i < visits.size(); i++) {
                Visit.outputObject(visits.get(i), Pw);
            }
        } catch (FileNotFoundException exp) {
            System.out.print("При выводе не найден файл");
        } finally {
            Pw.flush();
            Pw.close();
        }
    }

    public void inputList(String Name) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(Name));
            int size = Integer.parseInt(br.readLine());

            for (int i = 0; i < size; i++) {
                this.addElement(i, Visit.inputObject(br));
            }
        } catch (FileNotFoundException exp) {
            System.out.print("При чтении не найден файл");
        } catch (IOException exp) {
            System.out.print("Ошибка при чтении ");
        } catch (NumberFormatException excp) {
            System.out.print("Нарушен формат файла");
        }

    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setVisits(ArrayList<Visit> visits) {
        this.visits = visits;
    }

    public ArrayList<Visit> getVisits() {
        return visits;
    }

}
