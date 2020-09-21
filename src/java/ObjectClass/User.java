package ObjectClass;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "User")

public class User {

    private int ID;
    private String name;
    private String login;
    private String password;
    private ArrayList<Clinic> clinics = new ArrayList();

    public int getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public void setLogin(String login) {
        this.login = login;
    }

    @XmlTransient
    public void setPassword(String password) {
        this.password = password;
    }

    public Clinic getClinic(int i) {
        try {
            return clinics.get(i);
        } catch (IndexOutOfBoundsException exp) {
            System.out.print("Выход за пределы массива пользователей");
        }
        return null;
    }

    public void addClinic(int i, Clinic cars) {
        clinics.add(i, cars);
    }

    public void removeClinic(int i) {
        clinics.remove(i);
    }

    public int getClinicSize() {
        return clinics.size();
    }

    public void setClinics(ArrayList<Clinic> clinics) {
        this.clinics = clinics;
    }

    public ArrayList<Clinic> getClinics() {
        return clinics;
    }
}
