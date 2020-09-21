package webClasses;

import ObjectClass.User;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

@ManagedBean(name = "CustomerBean", eager = true)
@SessionScoped
public class CustomerBean implements Serializable {

    @EJB
    private CustomerEJB customerEJB;
    private String login;
    private String password;
    private User user;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passord) {
        this.password = passord;
    }

    public CustomerEJB getCustomerEJB() {
        return customerEJB;
    }

    public void setCustomerEJB(CustomerEJB customerEJB) {
        this.customerEJB = customerEJB;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String sessUser() {

        this.user = customerEJB.validateUserLogin(this.login, this.password);
        this.flag = "true";
        if (this.user == null) {
            return "index?faces-redirect=true";
        } else {
            return "result?faces-redirect=true";
        }
    }

    public void loadXML() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset();
        ec.getSession(true);
        ec.setResponseContentType("text/xml");
        ec.setResponseHeader("Content-Disposition", "attachment;filename = result.xml");

        OutputStream out = ec.getResponseOutputStream();

        try {

            JAXBContext context = JAXBContext.newInstance(User.class);
            Marshaller marsh = context.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marsh.marshal(user, out);
            fc.responseComplete();
        } catch (JAXBException exp) {
            System.out.print("Ошибка маппинга");
        }
    }
}
