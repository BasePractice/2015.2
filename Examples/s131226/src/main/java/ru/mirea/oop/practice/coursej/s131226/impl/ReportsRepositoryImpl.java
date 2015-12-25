package ru.mirea.oop.practice.coursej.s131226.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.db.Context;
import ru.mirea.oop.practice.coursej.s131226.ReportsRepository;
import ru.mirea.oop.practice.coursej.s131226.entities.Report;
import ru.mirea.oop.practice.coursej.s131226.entities.ReportItem;
import ru.mirea.oop.practice.coursej.s131226.entities.Snapshot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportsRepositoryImpl implements ReportsRepository {
    private static final Logger logger = LoggerFactory.getLogger(ReportsRepositoryImpl.class);
    private final Context ctx = new Context();
    public static final String REFERENCE_SHOP_NAME = "FissmanPosuda";
    public static final int PRICE_DIFFERENCE = -5;// и меньше

    public ReportsRepositoryImpl() {
        init(ctx);
    }

    private static void init(Context ctx) {
        try (Connection connection = ctx.getConnection()) {
            try (final Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS SHOPS(ID SMALLINT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(255) NOT NULL UNIQUE);");
                stmt.execute("CREATE TABLE IF NOT EXISTS SNAPSHOTS(ID INT AUTO_INCREMENT ,SHOP_ID SMALLINT NOT NULL, DATE DATE NOT NULL,PRIMARY KEY (SHOP_ID,DATE));");
                stmt.execute("CREATE TABLE IF NOT EXISTS ITEMS(SNAPSHOT_ID INT NOT NULL,ARTICLE SMALLINT NOT NULL,  PRICE INT,PRIMARY KEY (SNAPSHOT_ID,ARTICLE));");
            }
        } catch (Exception ex) {
            logger.error("Ошибка при создании таблиц {}", ex.getLocalizedMessage());
        }
    }


    @Override
    public synchronized void addSnapshot(Snapshot snapshot) {
        try (Connection connection = ctx.getConnection()) {
            try (final PreparedStatement stmt = connection.prepareStatement("INSERT INTO SHOPS (NAME) VALUES (?);")) {
                stmt.setString(1, snapshot.getShopName());
                stmt.execute();
            } catch (Exception e) {
                logger.debug("Магазин {} уже существует в базе, нет необходимости записывать его еще раз.");
            }
            try (final PreparedStatement stmt = connection.prepareStatement("INSERT INTO SNAPSHOTS (SHOP_ID,DATE) SELECT ID,? FROM SHOPS WHERE NAME=?;")) {
                stmt.setDate(1, snapshot.getDate());
                stmt.setString(2, snapshot.getShopName());
                stmt.execute();
            } catch (Exception e) {
                logger.debug("Запись за {} для магазина {} уже существует.",
                        snapshot.getDate().toString(), snapshot.getShopName());
            }
            for (ru.mirea.oop.practice.coursej.s131226.entities.item item : snapshot.getItems()) {
                try (final PreparedStatement stmt = connection.prepareStatement("INSERT INTO ITEMS SELECT ID,?,? FROM SNAPSHOTS WHERE (DATE =? AND SHOP_ID = (SELECT ID FROM SHOPS WHERE NAME=?));")) {
                    stmt.setInt(1, item.getArticle());
                    stmt.setInt(2, item.getPrice());
                    stmt.setDate(3, snapshot.getDate());
                    stmt.setString(4, snapshot.getShopName());
                    stmt.execute();
                    logger.debug("Товар добавлен артикул {},цена{}, дата: {},магазин:{}",
                            item.getArticle(), item.getPrice(), snapshot.getDate().toString(), snapshot.getShopName());
                } catch (Exception e) {
                    logger.debug("товар повторяется артикул {},цена{}, дата: {},магазин:{}",
                            item.getArticle(), item.getPrice(), snapshot.getDate().toString(), snapshot.getShopName());
                }
            }

        } catch (ClassNotFoundException e) {
            logger.error("Драйвер H2 не подгрузился. Подключение к базе не возможно.");
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе");
        }
    }

    @Override
    public List<Report> getReports() {
        List<Report> reports = new ArrayList<>();
        try (Connection connection = ctx.getConnection()) {
            try (final Statement stmt = connection.createStatement()) {
                try (final ResultSet rs = stmt.executeQuery("SELECT  DISTINCT NAME,DATE\n" +
                        "FROM (SELECT\n" +
                        "        ARTICLE,\n" +
                        "        PRICE,\n" +
                        "        DATE,\n" +
                        "        NAME\n" +
                        "      FROM (SELECT\n" +
                        "              ARTICLE,\n" +
                        "              PRICE,\n" +
                        "              DATE,\n" +
                        "              SHOP_ID\n" +
                        "            FROM ITEMS\n" +
                        "              JOIN SNAPSHOTS ON SNAPSHOT_ID = ID) JOIN SHOPS\n" +
                        "          ON SHOP_ID = ID\n" +
                        "  ) JOIN (\n" +
                        "  SELECT\n" +
                        "    ARTICLE AS RefARTICLE,\n" +
                        "    PRICE   AS RefPRICE,\n" +
                        "    DATE    AS RefDATE,\n" +
                        "    NAME    AS RefNAME\n" +
                        "  FROM (SELECT\n" +
                        "          ARTICLE,\n" +
                        "          PRICE,\n" +
                        "          DATE,\n" +
                        "          SHOP_ID\n" +
                        "        FROM (SELECT *\n" +
                        "              FROM ITEMS\n" +
                        "              WHERE SNAPSHOT_ID IN (SELECT ID\n" +
                        "                                    FROM SNAPSHOTS\n" +
                        "                                    WHERE SHOP_ID IS (SELECT ID\n" +
                        "                                                      FROM SHOPS\n" +
                        "                                                      WHERE NAME = '" + REFERENCE_SHOP_NAME + "'))) JOIN SNAPSHOTS ON SNAPSHOT_ID = ID) JOIN SHOPS\n" +
                        "      ON SHOP_ID = ID) ON ARTICLE = RefARTICLE AND DATE = RefDATE AND PRICE - RefPRICE <= " + PRICE_DIFFERENCE + ";")) {
                    while (rs.next()) {
                        reports.add(new Report(rs.getString("NAME"), rs.getDate("DATE"), new ArrayList<>()));
                    }
                } catch (SQLException e) {
                    logger.error("Ошибка при получении списка отчетов.");
                }
                try (final ResultSet rs = stmt.executeQuery("SELECT ARTICLE,PRICE,RefPRICE,NAME,DATE\n" +
                        "FROM (SELECT\n" +
                        "        ARTICLE,\n" +
                        "        PRICE,\n" +
                        "        DATE,\n" +
                        "        NAME\n" +
                        "      FROM (SELECT\n" +
                        "              ARTICLE,\n" +
                        "              PRICE,\n" +
                        "              DATE,\n" +
                        "              SHOP_ID\n" +
                        "            FROM ITEMS\n" +
                        "              JOIN SNAPSHOTS ON SNAPSHOT_ID = ID) JOIN SHOPS\n" +
                        "          ON SHOP_ID = ID\n" +
                        "  ) JOIN (\n" +
                        "  SELECT\n" +
                        "    ARTICLE AS RefARTICLE,\n" +
                        "    PRICE   AS RefPRICE,\n" +
                        "    DATE    AS RefDATE,\n" +
                        "    NAME    AS RefNAME\n" +
                        "  FROM (SELECT\n" +
                        "          ARTICLE,\n" +
                        "          PRICE,\n" +
                        "          DATE,\n" +
                        "          SHOP_ID\n" +
                        "        FROM (SELECT *\n" +
                        "              FROM ITEMS\n" +
                        "              WHERE SNAPSHOT_ID IN (SELECT ID\n" +
                        "                                    FROM SNAPSHOTS\n" +
                        "                                    WHERE SHOP_ID IS (SELECT ID\n" +
                        "                                                      FROM SHOPS\n" +
                        "                                                      WHERE NAME = '" + REFERENCE_SHOP_NAME + "'))) JOIN SNAPSHOTS ON SNAPSHOT_ID = ID) JOIN SHOPS\n" +
                        "      ON SHOP_ID = ID) ON ARTICLE = RefARTICLE AND DATE = RefDATE AND PRICE - RefPRICE <= " + PRICE_DIFFERENCE + ";")) {
                    while (rs.next()) {
                        String name = rs.getString("NAME");
                        Date date = rs.getDate("DATE");
                        for (Report report : reports) {
                            if (name.equals(report.getShopName()) && date.equals(report.getDate())) {
                                report.add(new ReportItem(rs.getInt("ARTICLE"), rs.getInt("PRICE"), rs.getInt("REFPRICE")));
                                break;
                            }
                        }
                    }
                } catch (SQLException e) {
                    logger.error("Ошибка при получении списка товаров.");
                }
            }

        } catch (ClassNotFoundException e) {
            logger.error("Драйвер H2 не подгрузился. Подключение к базе не возможно.");
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе");
        }
        return reports;
    }
}
