package ru.mirea.oop.practice.coursej.s131253;

/**
 * Created by Александр on 08.12.2015.
 */
public class Question {
    private boolean isSended = false; //Отправлен ли уже вопрос. По умолчанию - нет.
    private String text;
    private String answer;

    public Question(String text, String answer) {   //Конструктор вопросов в форме "текст вопроса-ответ на вопрос"
        this.text = text;
        this.answer = answer;
    }

    public String  getAnswer() {
        return answer;
    }

    public void setSended(boolean isSended) {
        this.isSended = isSended;
    }

    public boolean isSended() {
        return isSended;
    }

    public String getText() {
        return text;
    }
}
