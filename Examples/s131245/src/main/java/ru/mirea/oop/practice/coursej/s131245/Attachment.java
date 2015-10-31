package ru.mirea.oop.practice.coursej.s131245;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.vk.DocumentsApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


/**
 * Created by aleksejpluhin on 17.10.15.
 */
public class Attachment {
    private static final Logger logger = LoggerFactory.getLogger(Attachment.class);
    public static final String REPORTS_DIRECTORY = System.getProperty("user.home") + "/reports";
    private static int WIDTH = 4;


    public static String getAttachment(Map<Long, ArrayList<VkStatistic.Session>> listMap, VkontakteApi api, String msg, Map<Long, Contact> friendsMap) throws Exception {
        DocumentsApi documents = api.getDocuments();
        File file = createFile(listMap, msg, friendsMap);
        boolean document = documents.uploadDocument(file);

        return null;
    }

    public static File createFile(Map<Long, ArrayList<VkStatistic.Session>> listMap, String msg, Map<Long, Contact> friendsMap) throws FileNotFoundException {
        SimpleDateFormat dateFormat_input = new SimpleDateFormat("dd/MM HH:mm");
        SimpleDateFormat dateFormat_output = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat_forFile = new SimpleDateFormat("dd-MM");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        boolean isDate = false;
        CreationHelper createHelper = workbook.getCreationHelper();
        sheet.setDefaultColumnWidth(11);

        ArrayList<String> arrayOfPeople = new ArrayList<>();
        Date date = null;
        try {
            String[] str = msg.split(" ")[msg.split(" ").length -1].split("/");
            date = new Date(Integer.parseInt(str[2]) - 1900 ,Integer.parseInt(str[1]) - 1,Integer.parseInt(str[0]));
            msg = msg.substring(0,msg.lastIndexOf(" "));
            System.out.println(date);
            isDate = true;
        }
        catch (Exception e) {
            logger.error("Даты нет в запросе");
        }
            if (!msg.contains("все")) {
                arrayOfPeople = queryOfPeople(msg);
            }

        int index = 2;
        int column = 0;
        Row zeroRow = sheet.createRow(0);
        sheet.createRow(1);
        sheet.createRow(2);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
        Cell cell = zeroRow.createCell(0);
        cell.setCellValue(new Date());
        cell.setCellStyle(cellStyle);

        for (Map.Entry<Long, ArrayList<VkStatistic.Session>> currentMan : listMap.entrySet()) {
            String key = friendsMap.get(currentMan.getKey()).firstName + " " + friendsMap.get(currentMan.getKey()).lastName;
            if (msg.equals("bot get: всех") || arrayOfPeople.contains(key)) {
                Row row;
                row = sheet.getRow(1);
                row.createCell(column).setCellValue(currentMan.getKey());
                row.createCell(column + 1).setCellValue(friendsMap.get(currentMan.getKey()).firstName);
                row.createCell(column + 2).setCellValue(friendsMap.get(currentMan.getKey()).lastName);
                row = sheet.getRow(2);
                row.createCell(column).setCellValue("Вход");
                row.createCell(column + 1).setCellValue("Выход");
                row.createCell(column + 2).setCellValue("Сессия");
                index++;
                for (int i = 0; i < currentMan.getValue().size(); i++) {
                    Row row1;
                    if (sheet.getLastRowNum() >= index) {
                        row1 = sheet.getRow(index);
                    } else {
                        row1 = sheet.createRow(index);
                    }
                    try {
                        if((!isDate) || (isDate && (date.getDay() == currentMan.getValue().get(i).getBegin().getDay() || date.getDay() == currentMan.getValue().get(i).getEnd().getDay())))
                        {
                            System.out.println("---");
                            row1.createCell(column).setCellValue(dateFormat_input.format(currentMan.getValue().get(i).getBegin()));
                            row1.createCell(column + 1).setCellValue(dateFormat_output.format(currentMan.getValue().get(i).getEnd()));
                            row1.createCell(column + 2).setCellValue(dateFormat_output.format(currentMan.getValue().get(i).getSession()));
                        }
                    } catch (Exception e) {
                        break;
                    }
                    index++;

                }
                column += WIDTH;
                index = 2;
            }
        }

        String fileName = dateFormat_forFile.format(new Date()) + ".xlsx";
        FileOutputStream fileOutputStream = new FileOutputStream(REPORTS_DIRECTORY + "/" +  fileName);
        try {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            logger.debug("Ошибка записи");
        }

        File file = new File(REPORTS_DIRECTORY, fileName);

        return file;
    }
    public static ArrayList<String> queryOfPeople(String msg) {
        String[] arr = msg.split(": ")[1].split(", ");
        ArrayList<String> listOfPeople = new ArrayList<>();
        for(int i = 0; i < arr.length; i++) {
            listOfPeople.add(arr[i]);
        }
        return listOfPeople;
    }

}
