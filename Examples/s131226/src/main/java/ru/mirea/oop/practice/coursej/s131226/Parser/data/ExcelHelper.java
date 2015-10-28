package ru.mirea.oop.practice.coursej.s131226.parser.data;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExcelHelper {
    public static final String REPORTS_DIRECTORY = System.getProperty("user.home") + "/reports";
    private DbHelper DbHelper;

    public ExcelHelper(DbHelper DbHelper) {
        this.DbHelper = DbHelper;
    }

    public File exportReport() {
        XSSFWorkbook wb = new XSSFWorkbook();
        String lastDBName = null;
        try {
            DbHelper.Conn();
            DbHelper.statmt = DbHelper.conn.createStatement();
            DbHelper.resSet = DbHelper.statmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table';");

            while (DbHelper.resSet.next()) {
                lastDBName = DbHelper.resSet.getObject("name").toString();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<String> sites = new ArrayList<>();

        try {
            DbHelper.statmt = DbHelper.conn.createStatement();
            DbHelper.resSet = DbHelper.statmt.executeQuery("pragma table_info(" + lastDBName + ");");

            while (DbHelper.resSet.next()) {
                sites.add(DbHelper.resSet.getObject("name").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String site : sites) {
            if (!site.equals("FissmanPosuda") && !site.equals("article")) {
                XSSFSheet sheet = wb.createSheet(site);
                try {
                    DbHelper.statmt = DbHelper.conn.createStatement();
                    DbHelper.resSet = DbHelper.statmt.executeQuery("SELECT article, " + site + ",FissmanPosuda FROM " + lastDBName + " WHERE (" + site + " - FissmanPosuda)<-1  AND " + site + "!=0  AND FissmanPosuda!=0;");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                XSSFRow row = sheet.createRow(0);
                XSSFCell cell = row.createCell(0);
                cell.setCellValue("Артикул");
                cell = row.createCell(1);
                cell.setCellValue("Цена в" + site);
                cell = row.createCell(2);
                cell.setCellValue("Цена в FissmanPosuda");
                int rowNum = 1;
                try {
                    while (DbHelper.resSet.next()) {
                        row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(DbHelper.resSet.getInt("article"));
                        row.createCell(1).setCellValue(DbHelper.resSet.getInt(site));
                        row.createCell(2).setCellValue(DbHelper.resSet.getInt("FissmanPosuda"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        }
        FileOutputStream fileOut;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
            String date = dateFormat.format(new Date());

            String fileName = "ReportFor(" + sites.size() + ")Sites_" + date + ".xlsx";
            fileOut = new FileOutputStream(REPORTS_DIRECTORY + "/" + fileName);

            wb.write(fileOut);
            fileOut.close();
            return new File(REPORTS_DIRECTORY, fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public File exportChanges() {

        XSSFWorkbook wb = new XSSFWorkbook();
        ArrayList<String> tables = new ArrayList<>();
        try {
            DbHelper.statmt = DbHelper.conn.createStatement();
            DbHelper.resSet = DbHelper.statmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table';");

            while (DbHelper.resSet.next()) {
                tables.add(DbHelper.resSet.getObject("name").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.sort(tables);
        List<String> sites = new ArrayList<>();

        try {
            DbHelper.statmt = DbHelper.conn.createStatement();
            DbHelper.resSet = DbHelper.statmt.executeQuery("pragma table_info(" + tables.get(tables.size() - 1) + ");");

            while (DbHelper.resSet.next()) {
                sites.add(DbHelper.resSet.getObject("name").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        for (String site : sites) {
            if (!site.equals("FissmanPosuda")) {
                int rowNum = 1;
                XSSFSheet sheet = wb.createSheet(site);

                for (int i = 1; i < tables.size(); i++) {
                    try {
                        SimpleDateFormat inputData = new SimpleDateFormat("yyyyMMdd");
                        SimpleDateFormat correctData = new SimpleDateFormat("dd.MM.yyyy");
                        String dateCur = tables.get(i).replaceAll("\\D", "");
                        String datePrev = tables.get(i - 1).replaceAll("\\D", "");
                        try {

                            dateCur = correctData.format(inputData.parse(dateCur));
                            datePrev = correctData.format(inputData.parse(datePrev));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DbHelper.statmt = DbHelper.conn.createStatement();
                        DbHelper.resSet = DbHelper.statmt.executeQuery("SELECT * FROM (SELECT\n" +
                                "  article AS 'art',\n" +
                                "  " + site + " AS 'oldPrice'\n" +
                                "FROM " + tables.get(i - 1) + "\n" +
                                "EXCEPT\n" +
                                "SELECT\n" +
                                "  article,\n" +
                                "  " + site + "\n" +
                                "FROM " + tables.get(i) + ")\n" +
                                "LEFT JOIN (SELECT article," + site + " AS 'newPrice' FROM " + tables.get(i) + ") ON art=article;");

                        XSSFRow row = sheet.createRow(rowNum++);
                        XSSFCell cell = row.createCell(0);
                        cell.setCellValue("Артикул");
                        cell = row.createCell(1);
                        cell.setCellValue("Цена " + datePrev);
                        cell = row.createCell(2);
                        cell.setCellValue("Цена " + dateCur);


                        while (DbHelper.resSet.next()) {
                            row = sheet.createRow(rowNum++);
                            row.createCell(0).setCellValue(DbHelper.resSet.getInt("article"));
                            row.createCell(1).setCellValue(DbHelper.resSet.getInt("oldPrice"));
                            row.createCell(2).setCellValue(DbHelper.resSet.getInt("newPrice"));
                        }
                        rowNum++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


            }


        }
        FileOutputStream fileOut;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
            String date = dateFormat.format(new Date());

            String fileName = "ChangesFor(" + sites.size() + ")Sites_" + date + ".xlsx";
            fileOut = new FileOutputStream(fileName);

            wb.write(fileOut);
            fileOut.close();
            return new File(REPORTS_DIRECTORY, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
