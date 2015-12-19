package ru.mirea.oop.practice.coursej.s131253;

final class Question {
    private boolean isSanded = false; //Отправлен ли уже вопрос. По умолчанию - нет.
    private String text;
    private String answer;

    Question(String text, String answer) {   //Конструктор вопросов в форме "текст вопроса-ответ на вопрос"
        this.text = text;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setSanded(boolean isSanded) {
        this.isSanded = isSanded;
    }

    public boolean isSanded() {
        return isSanded;
    }

    public String getText() {
        return text;
    }
}
