package Data;

import java.sql.*;
import java.util.Locale;
import ObjectClass.*;
import java.util.*;

public class Database {

    private String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private String login = "irinab";
    private String password = "111";

    public void Init() throws SQLException {
        Locale.setDefault(Locale.ENGLISH);
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException exp) {
            System.out.println("класс не найден");
        }
        return DriverManager.getConnection(url, login, password);
    }

    public void createModel() throws SQLException //метод создания базы данных
    {
        //создание пользователей
        Clinic clinic1 = new Clinic(1);
        clinic1.inputList("Clinic1");
        Clinic clinic2 = new Clinic(2);
        clinic2.inputList("Clinic2");
        Clinic clinic3 = new Clinic(3);
        clinic3.inputList("Clinic3");
        Clinic clinic4 = new Clinic(4);
        clinic4.inputList("Clinic4");
        Clinic clinic5 = new Clinic(5);
        clinic5.inputList("Clinic5");
        Clinic clinic6 = new Clinic(6);
        clinic6.inputList("Clinic6");

        User user1, user2, user3, user4, user5;
        user1 = new User();
        user1.setID(1);
        user1.setName("Ivan");
        user1.setLogin("visittel");
        user1.setPassword("1111");

        user2 = new User();
        user2.setID(2);
        user2.setName("Alex");
        user2.setLogin("box");
        user2.setPassword("aa10");

        user3 = new User();
        user3.setID(3);
        user3.setName("Sergei");
        user3.setLogin("krest");
        user3.setPassword("1aq1");

        user4 = new User();
        user4.setID(4);
        user4.setName("Irina");
        user4.setLogin("sqrt");
        user4.setPassword("131s");

        user5 = new User();
        user5.setID(5);
        user5.setName("Julia");
        user5.setLogin("reka");
        user5.setPassword("sqla1");

        user1.addClinic(0, clinic1);
        user1.addClinic(1, clinic2);
        user2.addClinic(0, clinic1);
        user2.addClinic(1, clinic3);
        user3.addClinic(0, clinic2);
        user3.addClinic(1, clinic6);
        user3.addClinic(2, clinic4);
        user4.addClinic(0, clinic5);
        user4.addClinic(1, clinic3);
        user5.addClinic(0, clinic2);
        user5.addClinic(1, clinic4);
        user5.addClinic(2, clinic6);

        //соединение с Oracle при помощи JDBC
        Connection connection = getConnection();
        connection.setAutoCommit(false);//отключаем автотранзакции 
        Statement statement = connection.createStatement();
        // Для Insert, Update, Delete
        try {
            statement.executeUpdate("create table Clinics(id int primary key)");
            statement.executeUpdate("create table visits(ID int Primary key check(ID>0),"
                    + "type varchar(100),"
                    + "unitPrice int check(unitPrice>0),"
                    + "quantity int check(quantity>0),"
                    + "cost int,"
                    + "clinicID int check(clinicID>=0),"
                    + "foreign key (clinicID) references Clinics(id))");
            //statement.executeUpdate("ALTER TABLE visits ADD CONSTRAINT <CNAME> CHECK (cost = 5)");
            statement.executeUpdate("create table Users(id int primary key,name varchar(100),login varchar(100),password varchar(100))");
            statement.executeUpdate("create table UserClinic(id_user int  references Users(id),id_clinic int references Clinics(id), primary key(id_user,id_clinic))");
            this.addUser(user1);
            this.addUser(user2);
            this.addUser(user3);
            this.addUser(user4);
            this.addUser(user5);
        } catch (SQLException exp) {
            System.out.println("Ошибка при создании таблиц " + exp.getLocalizedMessage());
        }
        connection.commit();//завершаем запросы только в случае успеха всех запросов
        connection.close();
    }

    public void addVisit(Visit visit) throws SQLException //Добавление визита в базу данных
    {
        //Statement statement = connection.createStatement();
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into visits(ID,type,unitPrice,quantity,cost,clinicID) values(?,?,?,?,?,?)");
            statement.setInt(1, visit.getID());
            statement.setString(2, visit.getType());
            statement.setInt(3, visit.getUnitPrice());
            statement.setInt(4, visit.getQuantity());
            statement.setInt(5, visit.getCost());
            statement.setInt(6, visit.getClinicID());
            statement.executeUpdate();
        } catch (SQLException exp) {

            if (exp.getErrorCode() == 955 || exp.getErrorCode() == 1) {
                System.out.println("визит с таким ID уже существует");
            } else if (exp.getErrorCode() == 2291) {
                System.out.println("визит не принадлежит ни одной клинике");
            } else {
                System.out.println("SQLexception при добавлении визит " + exp.getErrorCode());
            }
        }
    }

    public void addClinic(Clinic clinic) throws SQLException {//добавление клиники в базу данных
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into clinics values(?)");
            statement.setInt(1, clinic.getID());
            statement.executeUpdate();
            for (int i = 0; i < clinic.getLengthArray(); i++) {
                this.addVisit(clinic.getElement(i));
            }
        } catch (SQLException exp) {
            if (exp.getErrorCode() == 955 || exp.getErrorCode() == 1) {
                System.out.println("Салон с таким ID уже существует");
            }
        }

    }

    public void addUser(User user) throws SQLException {//добавление пользователя в базу данных

        try {
            Connection connection = getConnection();
            PreparedStatement statementUser = connection.prepareStatement("insert into users values(?,?,?,?)");
            PreparedStatement statementUserclinic = connection.prepareStatement("insert into userclinic values(?,?)");
            statementUser.setInt(1, user.getID());
            statementUser.setString(2, user.getName());
            statementUser.setString(3, user.getLogin());
            statementUser.setString(4, user.getPassword());
            statementUser.executeUpdate();

            for (int i = 0; i < user.getClinicSize(); i++) {
                this.addClinic(user.getClinic(i));
                statementUserclinic.setInt(1, user.getID());
                statementUserclinic.setInt(2, user.getClinic(i).getID());
                statementUserclinic.executeUpdate();
            }
        } catch (SQLException exp) {
            if (exp.getErrorCode() == 955 || exp.getErrorCode() == 1) {
                System.out.println("Пользователь с таким ID уже существует");
            }
        }

    }

    public void deleteVisit(Visit visit) {
        try {
            Connection connection = getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("delete from visits where id = ?");
            statement.setInt(1, visit.getID());
            statement.executeUpdate();
            connection.commit();
            connection.close();
        } catch (SQLException exp) {
            System.out.println("Ошибка при удалении");
        }

    }

    public void deleteUser(User user) throws SQLException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statementUser = connection.prepareStatement("delete from users where id = ?");
        PreparedStatement statementUserclinic = connection.prepareStatement("delete from userclinic where id_user = ?");
        statementUserclinic.setInt(1, user.getID());
        statementUserclinic.executeUpdate();
        statementUser.setInt(1, user.getID());
        statementUser.executeUpdate();
        connection.commit();
        connection.close();

    }

    public void deleteClinic(Clinic clinic) throws SQLException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statementVisits = connection.prepareStatement("delete from visits where clinic = ?");
        PreparedStatement statementUserclinic = connection.prepareStatement("delete from userclinic where id_clinic = ?");
        PreparedStatement statementClinic = connection.prepareStatement("delete from clinics where id =?");

        statementVisits.setInt(1, clinic.getID());
        statementVisits.executeUpdate();
        statementUserclinic.setInt(1, clinic.getID());
        statementUserclinic.executeUpdate();
        statementClinic.setInt(1, clinic.getID());
        statementClinic.executeUpdate();
        connection.commit();
        connection.close();
    }

    public User getUserClinics(User user)//возвращает пользователя с его клиниками
    {
        User getUser = null;

        try {
            Connection connection = getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from users where login = '" + user.getLogin() + "'");
            rs.next();
            getUser = new User();
            getUser.setID(rs.getInt("id"));
            getUser.setName(rs.getString("name"));
            getUser.setLogin(rs.getString("login"));
            getUser.setPassword(rs.getString("password"));

            ResultSet rs2 = statement.executeQuery("select id_clinic from userclinic where id_user= ' " + getUser.getID() + "'");
            int i = 0;
            while (rs2.next()) {
                //Retrieve by column name
                int id = rs2.getInt("id_clinic");
                getUser.addClinic(i, getClinicVisits(id));
                i++;
            }
            connection.commit();
            connection.close();
            rs.close();
        } catch (SQLException exp) {
            System.out.print(exp.getErrorCode());
            System.out.print("Данного пользователя нет в базе");
        }

        return getUser;
    }

    public Clinic getClinicVisits(int ID) throws SQLException//возврщает клинику с её визитами из базы данных
    {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from visits where clinicID=" + ID);
        Clinic clinic = new Clinic(ID);
        int i = 0;
        while (rs.next()) {

            clinic.addElement(i, new Visit(rs.getInt("id"), rs.getString("type"), rs.getInt("unitPrice"), rs.getInt("quantity"), rs.getInt("cost")));
            i++;
        }
        rs.close();
        connection.commit();
        connection.close();
        return clinic;
    }

    public boolean existUser(String login, String password) throws SQLException//проверяет существование пользователя
    {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from users where login = ? and password=?");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet rs = statement.executeQuery();
        return (rs.next());
    }

    public ArrayList<Visit> getCheapVisit() throws SQLException//выводит массив самых дешёвых визитов
    {
        ArrayList<Visit> visit = new ArrayList<Visit>();
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from Visits where cost=(select distinct min(cost) from visits)");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            visit.add(new Visit(rs.getInt("id"), rs.getString("type"), rs.getInt("unitPrice"), rs.getInt("quantity"), rs.getInt("cost")));
        }
        return (visit);
    }

}
