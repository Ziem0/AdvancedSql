package com.sql.advanced;

import com.sql.advanced.Checker.DateInputChecker;
import com.sql.advanced.Dao.ConnectDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Controller {

    private static Controller controller = null;
    private Connection conn;
    private PreparedStatement ps;

    private Controller() {
        conn = ConnectDB.getConn();
    }

    public static Controller getController() {
        if (controller == null) {
            synchronized (Controller.class) {
                if (controller == null) {
                    controller = new Controller();
                }
            }
        }
        return controller;
    }

    private void getPrep(String sql) {
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closePrep() {
        try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void calcAvgAmountOrders() {
        String query = "select sum(amount)/count(id) from orders;";
        getPrep(query);
        try {
            ResultSet rs = ps.executeQuery();
            System.out.println(rs.getInt(1));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closePrep();
    }

    public void selectOrdersByMonths() {
        Scanner sc = new Scanner(System.in);
        int firstMonth = sc.nextInt();
        int lastMonth = sc.nextInt();
        while (firstMonth > lastMonth || DateInputChecker.isMonthCorrect(firstMonth) || DateInputChecker.isMonthCorrect(lastMonth)) {
            System.out.println("Incorrect input!");
            firstMonth = sc.nextInt();
            lastMonth = sc.nextInt();
        }

        String firstDate = getDate(firstMonth);
        String lastDate = getDate(lastMonth);


        String query = "select * from orders where placed_at >= ? and placed_at <= ?;";
        getPrep(query);
        try {
            ps.setString(1, firstDate);
            ps.setString(2, lastDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String date = rs.getString(2);
                int amount = rs.getInt(3);
                int customer_id = rs.getInt(4);
                int salesmen_id = rs.getInt(5);
                System.out.printf("id:%d date:%s amount:%d customerID:%d salesmenID:%d\n", id, date, amount, customer_id, salesmen_id);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closePrep();
    }

    private String getDate(int month) {
        LocalDate date = LocalDate.from(LocalDate.of(2016, month, 1));
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private String getDay(int month, int day) {
        LocalDate date = LocalDate.from(LocalDate.of(2016, month, day));
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void amountOfOrdersOnIndicatedDay() {
        Scanner sc = new Scanner(System.in);
        int month = sc.nextInt();
        int day = sc.nextInt();
        while (DateInputChecker.isMonthCorrect(month) || DateInputChecker.isDayCorrect(day)) {
            System.out.println("Incorrect input!");
            month = sc.nextInt();
            day = sc.nextInt();
        }
        String selectedDate = getDay(month,day);

        String query = "select count(id) from orders where placed_at like '"+selectedDate+"%';";
        getPrep(query);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int amount = rs.getInt(1);
                System.out.println(amount);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closePrep();
    }

    public void selectIdWithHighestAmount() {
        String query = "select id, max(amount) from orders;";
        getPrep(query);
        try {
            ResultSet rs = ps.executeQuery();
            System.out.println(rs.getInt(1));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closePrep();
    }

    public void selectCustomersWithoutOrders () {
        String query = "select last_name from customers where customers.id not in (select orders.customer_id from orders);";
        getPrep(query);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                System.out.println(name);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closePrep();
    }

    public void selectAmountForEachCustomer() {
        String query = "select sum(orders.amount), customers.last_name from orders join customers on orders.customer_id=customers.id group by customers.last_name;";
        getPrep(query);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int amount = rs.getInt(1);
                String name = rs.getString(2);
                System.out.printf("amount:%d name:%s\n", amount, name);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closePrep();
    }


//7    select count(orders.id), customers.last_name from orders join customers on customers.id=orders.customer_id group by orders.customer_id;
//8    select customers.last_name,count(salesmen.name) from orders join customers on customers.id=orders.customer_id join salesmen on salesmen.id=orders.salesmen_id group by customers.last_name;
//9    select orders.id from orders join customers on customers.id=orders.customer_id join salesmen on salesmen.id=orders.salesmen_id where customers.city <> salesmen.city;
//10   select max(orders.amount), customers.city from orders join customers on customers.id=orders.customer_id group by city;


    public static void main(String[] args) {
        Controller controller = Controller.getController();
//        controller.calcAvgAmountOrders();
//        controller.selectOrdersByMonths();
//        controller.amountOfOrdersOnIndicatedDay();
//        controller.selectAmountForEachCustomer();

    }

}
