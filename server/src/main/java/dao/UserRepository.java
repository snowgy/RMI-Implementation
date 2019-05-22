package dao;

import model.User;

public interface UserRepository {
    User getUserByUsername(String user_name);

    /**
     * insert user into the table and save
     * return "success" if operation succeeds
     * return "failure" if operation fails
     * @param user
     * @return
     */

    String saveUser(User user);
    void closeConnection();
    void openConncetion();
}
