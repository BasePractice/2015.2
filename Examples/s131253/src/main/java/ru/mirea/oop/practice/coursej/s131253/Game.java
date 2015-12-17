package ru.mirea.oop.practice.coursej.s131253;

/**
 * Created by Александр on 08.12.2015.
 */
public class Game {
    private Questions questions;
    private Question currentQuestion; //Текущий вопрос
    private int score = 0;  //Кол-во правильных ответов пользователя
    private int countOfSended = 0; //Кол-во отправленных вопросов
    private boolean isStarted = false; //Начата ли игра
    private long idPlayer;
    private long startTime = System.currentTimeMillis() / 1000L; //Записывается время начала игры


    public Game() {
        this.questions = new Questions();
    }

    public int getCountOfSended() {
        return countOfSended;
    }

    public void plusCount() {
        countOfSended++;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public int getScore() {
        return score;
    }

    public void scorePlus() {
        score++;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public Question RandomQuestion() { //Метод, выдающий случайный вопрос из тех, которые ещё не были отправлены
        do {
            int number = (int) (Math.random() * questions.getBaseOfQuestons().size());
            Question question = questions.getQuestionForNumber(number);
            this.currentQuestion = question;
        }
        while (currentQuestion.isSended());

        return currentQuestion;
    }


    public Question currentQuest() {
        return currentQuestion;
    }

    public void setQuestionsFalse() { //Сброс флага "Был отправлен" на false у всех вопросов.
        for (int i = 0; i < questions.getBaseOfQuestons().size(); i++) {
            questions.getQuestionForNumber(i).setSended(false);
        }
        System.out.println("Вопросы сброшены");
    }

    public void putPlayerId(long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public long getIdPlayer() {
        return idPlayer;
    }

    public long getStartTime() {
        return startTime;
    }

}
