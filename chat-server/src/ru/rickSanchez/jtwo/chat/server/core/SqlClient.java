package ru.rickSanchez.jtwo.chat.server.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlClient {

    private static Connection connection;
    private static Statement statement;

    synchronized static void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat-server/chat-db.sqlite");
            statement = connection.createStatement();
        }catch(ClassNotFoundException |SQLException e){
            throw new RuntimeException();
        }

    }

    synchronized static String getNickName(String login, String password){
        String query = String.format("select nickname from users where login='%s' and password='%s'", login, password);
        try (ResultSet set = statement.executeQuery(query)){
            if(set.next()){
                return set.getString(1);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    synchronized static void disconnect(){
        try{
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
