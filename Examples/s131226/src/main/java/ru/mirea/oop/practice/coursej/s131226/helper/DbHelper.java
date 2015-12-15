package ru.mirea.oop.practice.coursej.s131226.helper;

import ru.mirea.oop.practice.coursej.s131226.parsers.Prices;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


/** FIXME: Переписать в соответствии с хорошими манерами использования JDBC */
//ЗЫ: Использовать Repository конструкцию
public class DbHelper {
    private Connection conn;
    private Statement statmt;
    private ResultSet resSet;

    public DbHelper() {
    }


    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public synchronized void conn() throws ClassNotFoundException, SQLException {
        this.conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:TEST2.db");

        System.out.println("База Подключена!");
    }

    public List<String> getDBState() throws SQLException, ClassNotFoundException {
        conn();
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table';");
        List<String> tables = new ArrayList<>();
        while (resSet.next()) {
            tables.add(resSet.getObject("name").toString() + "\n");
        }
        return tables;
    }



    // --------Создание таблицы--------


    public synchronized String createTable(String[] sites) throws SQLException {
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
    public synchronized void writeSumDB(List<Prices> pricesList) throws SQLException {

        String siteNames = "";
        String[] sites = new String[pricesList.size()];
        for (int i = 0; i < pricesList.size(); i++) {
            sites[i] = pricesList.get(i).getSitename();
            siteNames = siteNames + ", " + pricesList.get(i).getSitename();
        }
        String tableName = createTable(sites);

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

    public String getLastTableName() throws SQLException, ClassNotFoundException {
        List<String> tables = getDBState();
        return tables.get(tables.size() - 1);
    }
    public List<Prices> getDifferences() throws SQLException, ClassNotFoundException {
        return getDifferences(getLastTableName());
    }

    public List<Prices> getDifferences(String tableName) throws SQLException, ClassNotFoundException {
        List<Prices> pricesList = getPrices(tableName);
        List<Prices> differences = new ArrayList<>();
        Prices referencePrices = new Prices(null, null);

        for (Prices prices : pricesList) {
            if (prices.getSitename().equals("FissmanPosuda")) {
                referencePrices = prices;
                pricesList.remove(referencePrices);
                break;
            }
        }
        for (Prices prices : pricesList) {
            Prices difference = new Prices(prices.getSitename(), new HashMap<>());
            for (Map.Entry<Integer, Integer> entry : prices.getPricesMap().entrySet()) {
                if (entry.getValue() - referencePrices.getPricesMap().getOrDefault(entry.getKey(), 0) < -1 && entry.getValue() != 0) {
                    difference.addValue(entry.getKey(), entry.getValue());
                }
            }
            differences.add(difference);
        }
        differences.add(0, referencePrices);
        return differences;
    }
    public List<Prices> getPrices() throws SQLException, ClassNotFoundException {
        String lastDBName = getLastTableName();

        return getPrices(lastDBName);
    }


    public List<Prices> getPrices(String tableName) throws SQLException, ClassNotFoundException {

        conn();
        List<String> sites = new ArrayList<>();
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("pragma table_info(" + tableName + ");");

        while (resSet.next()) {
            if (!resSet.getObject("name").toString().equals("article")) {
                sites.add(resSet.getObject("name").toString());
            }
        }
        List<Prices> pricesList = new ArrayList<>();
        for (String site : sites) {
            pricesList.add(new Prices(site, new HashMap<Integer, Integer>()));
        }

        resSet = statmt.executeQuery("SELECT * FROM " + tableName + ";");
        while (resSet.next()) {
            for (Prices prices : pricesList) {
                prices.addValue(resSet.getInt("article"), resSet.getInt(prices.getSitename()));
            }
        }
        return pricesList;
    }


    // --------Закрытие--------

    public void closeDB() throws ClassNotFoundException, SQLException {

        statmt.close();
        resSet.close();
        conn.close();
        System.out.println("Соединения закрыты");
    }

}
