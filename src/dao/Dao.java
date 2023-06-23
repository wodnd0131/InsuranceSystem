package dao;

import exception.DaoException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;

public class Dao {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public void connect() throws Exception{
        String url = "jdbc:mysql://localhost:3306/Insurance?characterEncoding=UTF-8&useSSL=false";
        connect = DriverManager.getConnection(url,  "root", "");
    }

    public void create(String query) {
        try {
            statement = connect.createStatement();
            if (!statement.execute(query)) {
//                System.out.println("insert OK!!");
            }
        } catch (Exception e) {
            throw new DaoException("저장에 실패했습니다.", e);
        }
    }

    public ResultSet retrieve(String query) {
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            throw new DaoException("저장에 실패했습니다.", e);
        }
        return resultSet;
    }

    public void update(String query) {
        try {
            statement = connect.createStatement();
            if (!statement.execute(query)) {
//                System.out.println("update OK!!");
            }
        } catch (Exception e) {
            throw new DaoException("저장에 실패했습니다.", e);
        }
    }

    public void delete(String query) {
        try {
            statement = connect.createStatement();
            if (!statement.execute(query)) {
//                System.out.println("delete OK!!");
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public <T> T enumNullCheck(String value, Supplier<T> supplier) {
        return value.contains("null") ? null : supplier.get();
    }
    public LocalDate dateNullCheck(Date value) {
        return value == null ? null : value.toLocalDate();
    }
    public LocalDateTime dateTimeNullCheck(Timestamp value) {
        return value == null ? null : value.toLocalDateTime();
    }
}
