package ru.mirea.oop.practice.coursej.s131226.helper;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.mirea.oop.practice.coursej.s131226.parsers.Prices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelHelper {
    public static final String REPORTS_DIRECTORY = System.getProperty("user.home") + "/reports";
    private DbHelper dbHelper;

    public ExcelHelper(DbHelper DbHelper) {
        this.dbHelper = DbHelper;
    }

    public File exportReport() {
        List<Prices> pricesList = null;
        try {
            pricesList = dbHelper.getDifferences();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Prices referencePrices = new Prices(null, null);


        for (Prices prices : pricesList) {
            if (prices.getSitename().equals("FissmanPosuda")) {
                referencePrices = prices;
                pricesList.remove(referencePrices);
                break;
            }
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        for (Prices prices : pricesList) {
            XSSFSheet sheet = wb.createSheet(prices.getSitename());
            XSSFRow row = sheet.createRow(0);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue("Артикул");
            cell = row.createCell(1);
            cell.setCellValue("Цена в" + prices.getSitename());
            cell = row.createCell(2);
            cell.setCellValue("Цена в FissmanPosuda");
            int rowNum = 1;
            for (Map.Entry<Integer, Integer> entry : prices.getPricesMap().entrySet()) {
                row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
                row.createCell(2).setCellValue(referencePrices.getPricesMap().get(entry.getKey()));

            }
        }

        FileOutputStream fileOut;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
            String date = dateFormat.format(new Date());

            String fileName = "ReportFor(" + pricesList.size() + ")Sites_" + date + ".xlsx";
            fileOut = new FileOutputStream(REPORTS_DIRECTORY + "/" + fileName);

            wb.write(fileOut);
            fileOut.close();
            return new File(REPORTS_DIRECTORY, fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
