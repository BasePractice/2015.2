package ru.mirea.oop.practice.coursej.s131072;

final class Parser {
    private Parser() {

    }

    static String parserMessage(String message) {
        String[] messageArray = message.split(" ");
        StringBuilder answer = new StringBuilder();
        boolean flag = false;
        for (String aMessageArray : messageArray) {
            String key = aMessageArray.toLowerCase();
            if (Keywords.isKeyword(key)) {
                answer.append(Keywords.getAnswer(key)).append(" ");
                flag = true;
            }
        }
        if (!flag) {
            answer.append("Меня нет на месте");
        }
        return answer.toString();
    }
}