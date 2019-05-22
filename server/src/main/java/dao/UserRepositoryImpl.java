package dao;

import model.User;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository {
    private static final String URL = "your database url";
    private static final String USER = "your user";
    private static final String PASSWORD = "your password";
    private Connection conn = null;
    private static int count = 0;

    @Override
    public User getUserByUsername(String user_name) {
        try{
            String sqlStatement = "SELECT password From user WHERE user_name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setString(1, user_name);
            ResultSet rs = preparedStatement.executeQuery();
            String password = "";
            while(rs.next()){
                password = rs.getString("password");
                User user = new User(user_name, password);
                return user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String saveUser(User user) {
        try{
            String sqlStatement = "INSERT INTO user(user_name, password) VALUES(?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setString(1, user.getUser_name());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
        }catch(SQLException e){
            e.printStackTrace();
            return "failure";
        }

        return "success";
    }

    @Override
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openConncetion() {
        try{
            // initialize database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
