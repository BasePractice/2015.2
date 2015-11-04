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
import java.util.Collections;
import java.util.Date;
import java.util.Map;


//FIXME: Разобраться в логике.
//SimpleDateFormat не потокобезопасен - сделать его таковым.
//Разбить на классы в соответствии с логикой
public class Attachment {
    private static final Logger logger = LoggerFactory.getLogger(Attachment.class);
    private static final String REPORTS_DIRECTORY = System.getProperty("user.home") + "/reports";
    private static int WIDTH = 4;

    private static final SimpleDateFormat dateFormat_input = new SimpleDateFormat("dd/MM HH:mm");
    private static final SimpleDateFormat dateFormat_output = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat_forFile = new SimpleDateFormat("dd-MM");

    private  boolean isDate = false;
    private Map<Long, ArrayList<Session>> mapSession;
    private Map<Long, Contact> friendsMap;
    private VkontakteApi api;
    private String msg;

    public Attachment(Map<Long, ArrayList<Session>> mapSession, Map<Long, Contact> friendsMap, VkontakteApi api, String msg) {
        this.mapSession = mapSession;
        this.friendsMap = friendsMap;
        this.api = api;
        this.msg = msg;
    }

    public String getAttachmentName() throws Exception {
        DocumentsApi documents = api.getDocuments();
        File file = createFile();
        boolean document = documents.uploadDocument(file);

        return null;
    }

    public File createFile() throws FileNotFoundException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(11);

        sheet.createRow(0);
        sheet.createRow(1);

        Date date = parseDate(msg);
        ArrayList<String> arrayOfPeople = new ArrayList<>();
            if (!msg.split(": ")[1].equals("всех")) {
                arrayOfPeople = queryOfPeople(msg);
            }

        int index = 2;
        int column = 0;

        for (Map.Entry<Long, ArrayList<Session>> currentMan : mapSession.entrySet()) {
            String key = friendsMap.get(currentMan.getKey()).firstName + " " + friendsMap.get(currentMan.getKey()).lastName;
            if (msg.contains("bot get: всех") || arrayOfPeople.contains(key)) {
                Row row;

                row = sheet.getRow(0);
                row.createCell(column).setCellValue(currentMan.getKey());
                row.createCell(column + 1).setCellValue(friendsMap.get(currentMan.getKey()).firstName);
                row.createCell(column + 2).setCellValue(friendsMap.get(currentMan.getKey()).lastName);

                row = sheet.getRow(1);
                row.createCell(column).setCellValue("Вход");
                row.createCell(column + 1).setCellValue("Выход");
                row.createCell(column + 2).setCellValue("Сессия");
                for (int i = 0; i < currentMan.getValue().size(); i++) {

                    if (sheet.getLastRowNum() >= index) {
                        row = sheet.getRow(index);
                    } else {
                        row = sheet.createRow(index);
                    }
                    try {
                        if((!isDate) || (isDate && (date.getDay() == currentMan.getValue().get(i).getBegin().getDay() || date.getDay() == currentMan.getValue().get(i).getEnd().getDay())))
                        {
                            row.createCell(column).setCellValue(dateFormat_input.format(currentMan.getValue().get(i).getBegin()));
                            row.createCell(column + 1).setCellValue(dateFormat_output.format(currentMan.getValue().get(i).getEnd()));
                            row.createCell(column + 2).setCellValue(dateFormat_output.format(currentMan.getValue().get(i).getSession()));
                            index++;
                        }
                    } catch (NullPointerException e) {
                        break;
                    }


                }
                column += WIDTH;
                index = 2;
            }
        }
        isDate = false;

        File file = writeFile(workbook);


        return file;
    }

    public static File writeFile(Workbook workbook) {
        String fileName = dateFormat_forFile.format(new Date()) + ".xlsx";
        File file = new File(REPORTS_DIRECTORY, fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            logger.debug("Ошибка записи");
        }
        return file;
    }


    private  Date parseDate(String msg) {
        Date date = null;
        try {
            String[] str = msg.split(" ")[msg.split(" ").length -1].split("/");
            date = new Date(Integer.parseInt(str[2]) - 1900 ,Integer.parseInt(str[1]) - 1,Integer.parseInt(str[0]));
            msg = msg.substring(0,msg.lastIndexOf(" "));
            isDate = true;
        }
        catch (Exception e) {
            logger.error("Даты нет в запросе");
        }
        return date;
    }

    public static ArrayList<String> queryOfPeople(String msg) {
        String withoutDate = msg.substring(0, msg.lastIndexOf(" "));
        String[] arr = withoutDate.split(": ")[1].split(", ");
        ArrayList<String> listOfPeople = new ArrayList<>();
        Collections.addAll(listOfPeople, arr);
        return listOfPeople;
    }



}
