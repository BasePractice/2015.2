package ru.mirea.oop.practice.coursej;

import java.io.File;

public class HomeDirLoader {
    public static final String HOMEDIR = "C:\\Users\\Максим\\IdeaProjects\\files\\";
    public static void CheckHomeDir(){
        File file = new File(HOMEDIR + ".bottest");
        if (!file.exists()) {
            System.out.println("HOMEDIR check error (file \".bottest\" not found in " + HOMEDIR + ").");
            System.exit(0);
        }
    }}
