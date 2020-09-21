package webClasses;

import ObjectClass.User;
import Data.Database;
import java.sql.SQLException;
import javax.ejb.Stateless;

@Stateless
public class CustomerEJB {

    public User validateUserLogin(String login, String password) {

        try {
            Database base = new Database();
            base.Init();

            if (base.existUser(login, password)) {

                User user = new User();
                user.setLogin(login);
                user.setPassword(password);
                return base.getUserClinics(user);
            }
        } catch (SQLException exp) {
            System.out.print(("SQL error"));
        }

        return null;

    }
}
