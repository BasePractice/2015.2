package ru.mirea.oop.practice.coursej.s131250;


import ru.mirea.oop.practice.coursej.Configuration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BotUsersAction {
    public static List<Long> users = new LinkedList<>();

    public static boolean isRegistered(long uid) {
        if (users.contains(uid))
            return true;
        else {
            boolean isRegistered = false;
            Scanner in;
            try {
                in = new Scanner(Configuration.loadFrom(".users"));
                while (in.hasNextLine())
                    if (uid == in.nextLong()) {
                        isRegistered = true;
                    }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return isRegistered;
        }
    }

    public static int getCount() {
        int count = 0;
        Scanner in;
        try {
            in = new Scanner(Configuration.loadFrom(".users"));
            while (in.hasNextLine()) {
                in.nextLine();
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return count + users.size();
    }

    public static void register(long uid) {
        users.add(uid);
        PrintWriter file;
        try {
            file = new PrintWriter(new FileOutputStream(Configuration.getFileName(".users"), true));
            file.println();
            file.print(uid);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
