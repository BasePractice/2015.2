package ru.mirea.oop.practice.coursej.s131253;

import java.io.*;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import com.sun.media.jfxmedia.logging.Logger;
import ru.mirea.oop.practice.coursej.Configuration;


/**
 * Created by Александр on 08.12.2015.
 */

public class Questions {

    private static final Logger logger = LoggerFactory.getLogger(Questions.class);
    private ArrayList<Question> baseOfQuestons = new ArrayList<>();

    public Questions() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Questions.class.getResourceAsStream("/questions.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {  //Каждая строка в документе разбивается символом "=" на 2 строковых элемента.
                    String[] elements = line.split("=");
                    baseOfQuestons.add(new Question(elements[0], elements[1]));  /*Текст вопроса и ответ на него передаются
                    в конструктор класса Question, а затем экземпляр класса добавляется в динамический массив */
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Файл с вопросами не найден: ", e);
        } catch (IOException e) {
            logger.error("Ошибка чтения файла с вопросами: ", e);
        }
    }

    public int getQuestionsCount () {
        return getBaseOfQuestons().size();
    }

    public ArrayList<Question> getBaseOfQuestons() {
        return baseOfQuestons;
    }

    public Question getQuestionForNumber(int i) {
     return baseOfQuestons.get(i);
    } //Метод выдаёт вопрос по номеру i.

}
