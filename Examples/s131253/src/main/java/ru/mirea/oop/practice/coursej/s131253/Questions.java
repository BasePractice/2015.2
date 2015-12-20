package ru.mirea.oop.practice.coursej.s131253;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

final class Questions {

    private static final Logger logger = LoggerFactory.getLogger(Questions.class);
    private final List<Question> baseOfQuestions = new ArrayList<>();

    Questions() {
        reload();
    }

    void reload() {
        baseOfQuestions.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Questions.class.getResourceAsStream("/questions.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {  //Каждая строка в документе разбивается символом "=" на 2 строковых элемента.
                    String[] elements = line.split("=");
                    baseOfQuestions.add(new Question(elements[0], elements[1]));  /*Текст вопроса и ответ на него передаются
                    в конструктор класса Question, а затем экземпляр класса добавляется в динамический массив */
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Файл с вопросами не найден: ", e);
        } catch (IOException e) {
            logger.error("Ошибка чтения файла с вопросами: ", e);
        }
    }

    int getQuestionsCount() {
        return getBaseOfQuestions().size();
    }

    List<Question> getBaseOfQuestions() {
        return baseOfQuestions;
    }

    Question getQuestionForNumber(int i) {
        return baseOfQuestions.get(i);
    } //Метод выдаёт вопрос по номеру i.

}
