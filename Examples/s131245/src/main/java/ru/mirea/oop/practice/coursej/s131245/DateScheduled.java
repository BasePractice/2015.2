package ru.mirea.oop.practice.coursej.s131245;


import java.time.LocalDateTime;



//Отправка сообщения по расписанию
public class DateScheduled {

    private boolean alreadySend = false;


    DateScheduled() {

    }


    public boolean isScheduled() {
        boolean sendMessage = false;
        LocalDateTime dateTime = LocalDateTime.now();

        if(alreadySend) {
            if(dateTime.getMinute() >= 2) {
                alreadySend = false;

            } else {
                return false;
            }
        }
        if(dateTime.getHour() == 0 && (dateTime.getMinute() == 0 || dateTime.getMinute() == 1)) {
            alreadySend = true;
            sendMessage =  true;
        }
        return sendMessage;
    }
}
