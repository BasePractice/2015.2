package ru.mirea.oop.practice.coursej.api.vk;

import java.io.IOException;

public interface AccountApi extends ExternalCall {
    int setOnline(Integer voip) throws IOException;
}
