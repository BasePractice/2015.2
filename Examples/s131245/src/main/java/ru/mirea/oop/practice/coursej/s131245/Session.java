package ru.mirea.oop.practice.coursej.s131245;

import java.time.LocalDateTime;


/**FIXME: Разобраться в логике. Сделать все поля final */
public final class Session {
    private final LocalDateTime begin;
    private LocalDateTime end;
    private LocalDateTime session;

    public Session(LocalDateTime begin) {
        this.begin = begin;
    }

    /** FIXME: Избавится от deprecated методов */
    public void setEnd(LocalDateTime end) {
        this.end = end;
        this.session =  LocalDateTime.of(begin.getYear(), begin.getMonthValue(), begin.getDayOfMonth() , end.getHour() - begin.getHour(), end.getMinute() - begin.getMinute());
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDateTime getSession() {
        return session;
    }

}