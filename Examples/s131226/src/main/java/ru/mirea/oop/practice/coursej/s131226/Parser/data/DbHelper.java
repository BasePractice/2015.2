package ru.mirea.oop.practice.coursej.s131226.parser.data;

import ru.mirea.oop.practice.coursej.s131226.parser.parsers.Prices;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class DbHelper {
    public Connection conn;
    public Statement statmt;
    public ResultSet resSet;

    public DbHelper() {
    }


    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public synchronized void Conn() throws ClassNotFoundException, SQLException {
        this.conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:TEST2.db");

        System.out.println("База Подключена!");
    }

    public String getDBState() throws SQLException {
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table';");
        String dbState = "На данный момент в базе содержатся следующие таблицы: \n";
        while (resSet.next()) {
            dbState += resSet.getObject("name").toString() + "\n";
        }
        return dbState;
    }

    // --------Создание таблицы--------


    public synchronized String CreateSumTable(String[] sites) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());
        String tableName = "sum" + date;
        statmt = conn.createStatement();
        statmt.execute("DROP TABLE IF EXISTS " + tableName);
        String s = "";
        for (String site : sites) {
            s = s + site + " INT,";

        }
        s = s.substring(0, s.length() - 1);
        statmt.execute("CREATE TABLE if not exists '" + tableName + "' ('article' INT, " + s + ");");
        return tableName;
    }

    // --------Заполнение таблицы--------
    public synchronized void WriteSumDB(List<Prices> pricesList) throws SQLException {

        String siteNames = "";
        String[] sites = new String[pricesList.size()];
        for (int i = 0; i < pricesList.size(); i++) {
            sites[i] = pricesList.get(i).getSitename();
            siteNames = siteNames + ", " + pricesList.get(i).getSitename();
        }
        String tableName = CreateSumTable(sites);

        Set<Integer> articles = new HashSet<>();
        for (Prices prices : pricesList) {
            for (Map.Entry<Integer, Integer> e : prices.getPricesMap().entrySet()) {
                articles.add(e.getKey());
            }
        }

        Map<Integer, Integer[]> map = new HashMap<>();
        for (Integer article : articles) {
            Integer[] prices = new Integer[pricesList.size()];
            for (int i = 0; i < pricesList.size(); i++) {
                prices[i] = pricesList.get(i).getPricesMap().getOrDefault(article, 0);
            }
            map.put(article, prices);
        }

        String req = "INSERT INTO " + tableName + " (  article" + siteNames + ") VALUES ";
        for (Map.Entry<Integer, Integer[]> entry : map.entrySet()) {

            Integer prices[] = entry.getValue();
            req += "(" + entry.getKey();

            for (int i = 0; i < prices.length; i++) {
                req += " ," + prices[i];
            }
            req = req + "), ";

        }
        req = req.substring(0, req.length() - 2);
        req += ";";
        System.out.println(req);
        statmt.execute(req);
    }


    // --------Закрытие--------
    public void CloseDB() throws ClassNotFoundException, SQLException {

        statmt.close();
        resSet.close();
        conn.close();
        System.out.println("Соединения закрыты");
    }

}
