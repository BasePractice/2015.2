package ru.mirea.oop.practice.coursej.s131253;

import java.io.*;
import java.util.ArrayList;
import ru.mirea.oop.practice.coursej.Configuration;


/**
 * Created by Александр on 08.12.2015.
 */

public class Questions {

    private ArrayList<Question> baseOfQuestons = new ArrayList<>();

    public Questions() {

        BufferedReader reader = null;
        try {  //Производится считывание вопросов для викторины из текстового файла
            reader = new BufferedReader(new FileReader(new File(Configuration.getFileName("Questions.txt"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            assert reader != null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {  //Каждая строка в документе разбивается символом "=" на 2 строковых элемента.
                    String[] elements = line.split("=");
                    baseOfQuestons.add(new Question(elements[0], elements[1])); /*Текст вопроса и ответ на него передаются
                    в конструктор класса Question, а затем экземпляр класса добавляется в динамический массив */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Question> getBaseOfQuestons() {
        return baseOfQuestons;
    }

    public Question getQuesForNumber(int i) {
     return baseOfQuestons.get(i);
    } //Метод выдаёт вопрос по номеру i.

}
