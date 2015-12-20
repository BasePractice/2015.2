package ru.mirea.oop.practice.coursej.s131245;

import java.time.Duration;
import java.time.LocalDateTime;

/*
  Первоначальный класс, отвечающий за хранения статистки о пользователях, один объект класса является одни посещением
  какого-либо пользователя, одно посещение имеет: вход - поле begin, выход - end и продолжительность пребывания в
  социальной сети - session в минутах.

 */
final class Session {
    private final LocalDateTime begin;
    private LocalDateTime end;
    private Duration session;

    Session(LocalDateTime begin) {
        this.begin = begin;
    }

    //Заменено на LocalDateTime и Duration
    void setEnd(LocalDateTime end) {
        this.end = end;
        this.session = Duration.between(begin, end);
    }

    LocalDateTime getBegin() {
        return begin;
    }

    LocalDateTime getEnd() {
        return end;
    }

    Duration getSession() {
        return session;
    }

}