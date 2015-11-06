package ru.mirea.oop.practice.coursej.s131245;

import java.time.Duration;
import java.time.LocalDateTime;


/**FIXME: Разобраться в логике. Сделать все поля final */
public final class Session {
    private final LocalDateTime begin;
    private LocalDateTime end;
    private Duration session;

    public Session(LocalDateTime begin) {
        this.begin = begin;
    }

    /** FIXME: Избавится от deprecated методов */
    //Заменено на LocalDateTime и Duration
    public void setEnd(LocalDateTime end) {
        this.end = end;
        this.session = Duration.between(begin, end);
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Duration getSession() {
        return session;
    }

}