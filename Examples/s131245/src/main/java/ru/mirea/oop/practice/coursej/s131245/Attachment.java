package ru.mirea.oop.practice.coursej.s131245;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/*
   Создание документа по запросу, загрузка на сервер
 */

final class Attachment {
    public static int WIDTH = 4;
    private static final Logger logger = LoggerFactory.getLogger(Attachment.class);
    private static final String REPORTS_DIRECTORY = System.getProperty("user.home") + "/reports";
    //DateTimeFormatter - thread-safe
    private static final DateTimeFormatter dateFormatInput = DateTimeFormatter.ofPattern("dd/MM HH:mm");
    private static final DateTimeFormatter dateFormatOutput = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter dateFormatForFile = DateTimeFormatter.ofPattern("dd-MM");


    private final Map<Long, List<Session>> mapSession;
    private final Map<Long, Contact> friendsMap;
    private final VkontakteApi api;
    private final Parser parser;

    Attachment(Map<Long, List<Session>> mapSession, Map<Long, Contact> friendsMap, VkontakteApi api, Parser parser) {
        this.mapSession = mapSession;
        this.friendsMap = friendsMap;
        this.api = api;
        this.parser = parser;
    }

    String getAttachmentName() throws Exception {
        DocumentsApi documents = api.getDocuments();
        File file = createFile();
        documents.uploadDocument(file);
        Document document = documents.list(1, 0, api.idOwner())[0];


        return "doc" + document.idOwner + "_" + document.id;
    }

    File createFile() throws FileNotFoundException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        sheet.setDefaultColumnWidth(11);
        sheet.createRow(0);
        sheet.createRow(1);
        //Вызов из Parser возможнной даты и возможный список людей
        LocalDate date = parser.getDate();
        List<String> arrayOfPeople = parser.getQueryOfPeople();


        int index = 2;
        int column = 0;

        for (Map.Entry<Long, List<Session>> currentMan : mapSession.entrySet()) {
            if (!friendsMap.containsKey(currentMan.getKey())) {
                continue;
            }
            //Проход по списку контактов и заполнение каждого в отдельности по столбцам
            String key = friendsMap.get(currentMan.getKey()).firstName + " " + friendsMap.get(currentMan.getKey()).lastName;
            //Либо часть людей заполняется, либо все
            if (parser.getMsg().contains("Статистика всех пользователей") || arrayOfPeople.contains(key)) {
                Row row;

                row = sheet.getRow(0);
                row.createCell(column).setCellValue(currentMan.getKey());
                row.createCell(column + 1).setCellValue(friendsMap.get(currentMan.getKey()).firstName);
                row.createCell(column + 2).setCellValue(friendsMap.get(currentMan.getKey()).lastName);

                row = sheet.getRow(1);
                row.createCell(column).setCellValue("Вход");
                row.createCell(column + 1).setCellValue("Выход");
                row.createCell(column + 2).setCellValue("Сессия, мин");
                //Проверка на наличие следующей строки
                for (int i = 0; i < currentMan.getValue().size(); i++) {

                    if (sheet.getLastRowNum() >= index) {
                        row = sheet.getRow(index);
                    } else {
                        row = sheet.createRow(index);
                    }
                    try {
                        //При отсутствие даты, проверяем конкретную сессию на совпадение с заданной датой
                        if ((!parser.isDate()) || (parser.isDate() && (date.getDayOfMonth() == currentMan.getValue().get(i).getBegin().getDayOfMonth() || date.getDayOfMonth() == currentMan.getValue().get(i).getEnd().getDayOfMonth()))) {
                            row.createCell(column).setCellValue(dateFormatInput.format(currentMan.getValue().get(i).getBegin()));
                            row.createCell(column + 1).setCellValue(dateFormatOutput.format(currentMan.getValue().get(i).getEnd()));
                            row.createCell(column + 2).setCellValue(currentMan.getValue().get(i).getSession().toMinutes());
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
        return writeFile(workbook);
    }

    File writeFile(Workbook workbook) {
        String fileName = dateFormatForFile.format(LocalDate.now()) + ".xlsx";
        File file = new File(REPORTS_DIRECTORY, fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            logger.debug("Ошибка записи");
        }
        return file;
    }


    void deleteFile(String attachmentName) {
        DocumentsApi documentsApi;
        try {
            documentsApi = api.getDocuments();
            Document[] documents = documentsApi.list(10, 0, api.idOwner());

            for (Document existDoc : documents) {
                if (("doc" + existDoc.idOwner + "_" + existDoc.id).equals(attachmentName)) {
                    documentsApi.delete(existDoc);
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка удаления файла");
        }
    }
}
