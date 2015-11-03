package ru.mirea.oop.practice.coursej.s131245;

import java.util.Date;

/**
 * Created by aleksejpluhin on 31.10.15.
 */
public  class Session {
    private Date begin;
    private Date end;
    private Date session;
    public Session(Date begin) {
        this.begin = begin;
    }

//method Duration

    public void setEnd(Date end) {
        this.end = end;
        this.session = new Date(begin.getYear(), begin.getMonth(), begin.getDay(), end.getHours() - begin.getHours(), end.getMinutes() - begin.getMinutes(), 0);
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public Date getSession() {
        return session;
    }

}