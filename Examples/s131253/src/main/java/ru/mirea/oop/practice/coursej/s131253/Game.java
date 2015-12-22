package ru.mirea.oop.practice.coursej.s131253;

final class Game {
    private Questions questions;
    private Question currentQuestion; //Текущий вопрос
    private int score = 0;  //Кол-во правильных ответов пользователя
    private int countOfSanded = 0; //Кол-во отправленных вопросов
    private boolean isStarted = false; //Начата ли игра
    private long idPlayer;
    private long startTime = System.currentTimeMillis() / 1000L; //Записывается время начала игры


    Game() {
        this.questions = new Questions();
    }

    void restart() {
        this.questions.reload();
    }

    public int getCountOfSanded() {
        return countOfSanded;
    }

    public void plusCount() {
        countOfSanded++;
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
            int number = (int) (Math.random() * questions.getQuestionsCount());
            this.currentQuestion = questions.getQuestionForNumber(number);
        }
        while (currentQuestion.isSanded());

        return currentQuestion;
    }

    public Question currentQuest() {
        return currentQuestion;
    }

    public void setQuestionsFalse() { //Сброс флага "Был отправлен" на false у всех вопросов.
        for (int i = 0; i < questions.getQuestionsCount(); i++) {
            questions.getQuestionForNumber(i).setSanded(false);
        }
        System.out.println("Вопросы сброшены");
    }

    public int getQuestionsCount() {
        return questions.getBaseOfQuestions().size();
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
