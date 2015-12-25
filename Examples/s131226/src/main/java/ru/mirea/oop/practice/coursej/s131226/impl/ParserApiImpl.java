package ru.mirea.oop.practice.coursej.s131226.impl;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.DBUpdater;
import ru.mirea.oop.practice.coursej.s131226.ParserApi;
import ru.mirea.oop.practice.coursej.s131226.ReportsRepository;
import ru.mirea.oop.practice.coursej.s131226.entities.Report;
import ru.mirea.oop.practice.coursej.s131226.entities.ReportItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParserApiImpl implements ParserApi {
    private static final Logger logger = LoggerFactory.getLogger(ParserApiImpl.class);
    public static final String REPORTS_DIRECTORY = System.getProperty("user.home") + System.getProperty("file.separator") + "reports";

    public ParserApiImpl() {
    }

    @Override
    public File getReport() {
        ReportsRepository reportsRepository = new ReportsRepositoryImpl();
        List<Report> reports = reportsRepository.getReports();
        int sCount = 0;
        java.util.Date utilDate = new java.util.Date();
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            for (Report report : reports) {
                if (report.getDate().equals(new Date(utilDate.getYear(), utilDate.getMonth(), utilDate.getDate()))) {
                    sCount++;
                    Sheet sheet = wb.createSheet(report.getShopName());
                    Row row = sheet.createRow(0);
                    Cell cell = row.createCell(0);
                    cell.setCellValue("Артикул");
                    cell = row.createCell(1);
                    cell.setCellValue("Цена в " + report.getShopName());
                    cell = row.createCell(2);
                    cell.setCellValue("Цена в FissmanPosuda");
                    int rowNum = 1;
                    for (ReportItem reportItem : report.getItems()) {
                        row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(reportItem.getArticle());
                        row.createCell(1).setCellValue(reportItem.getPrice());
                        row.createCell(2).setCellValue(reportItem.getReferencePrice());
                    }
                }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
            String date = dateFormat.format(new java.util.Date());
            String fileName = sCount + "_sites_" + date + ".xlsx";
            File file = new File(REPORTS_DIRECTORY, fileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                wb.write(fileOutputStream);
                return file;
            } catch (IOException e) {
                logger.debug("Ошибка записи");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String getChanges() {
        ReportsRepository reportsRepository = new ReportsRepositoryImpl();
        List<Report> reports = reportsRepository.getReports();
        Collections.sort(reports, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        Set<String> shops = new HashSet<>();
        Set<Date> dates = new HashSet<>();
        for (Report report : reports) {
            shops.add(report.getShopName());
            dates.add(report.getDate());
        }

        List<Date> dateList = Arrays.asList(dates.toArray(new Date[dates.size()]));
        Collections.sort(dateList);
        StringBuilder changes = new StringBuilder();
        changes.append(" Изменения по датам :\n");

        for (Date date : dateList) {
            changes.append("->").append(date.toString());
        }
        changes.append("\n");
        for (String shopName : shops) {
            changes.append(shopName).append(":");
            for (Date date : dateList) {
                boolean contains = false;
                for (Report report : reports) {
                    if (report.getShopName().equals(shopName) && report.getDate().equals(date)) {
                        changes.append("->").append(report.getItems().size());
                        contains = true;
                    }
                }
                if (!contains) {
                    changes.append("->0");
                }
            }
            changes.append("\n");
        }
        return changes.toString();

    }


    @Override
    public synchronized void updateDB() {
        DBUpdater updater = new DBUpdaterImpl();
        Thread update = new Thread(updater);
        update.start();
        try {
            update.join();
        } catch (InterruptedException e) {
            logger.error("Ошибка при работе с потоком обновления БД.");
        }
    }
}
