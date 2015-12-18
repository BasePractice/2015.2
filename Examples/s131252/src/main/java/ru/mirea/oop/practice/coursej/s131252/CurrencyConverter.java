package ru.mirea.oop.practice.coursej.s131252;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrencyConverter extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverter.class);
    private static final String HELP = "ожидается запрос следующего формата: дробное или целое " +
            "число(дробная часть отделяется точкой), пробел,символьный код валюты," +
            " ИЗ которой нужно конвертировать, пробел, символьный код валюты В которую нужно конвертировать.\n" +
            "Пример входных даннх: 12.34 USD RUB";
    private final MessagesApi msgApi;

    public CurrencyConverter() throws Exception {
        super("vk.services.CurrencyConverter");
        this.msgApi = api.getMessages();
    }

    @Override
    protected void doEvent(Event event) {
        switch (event.type) {
            case MESSAGE_RECEIVE: {
                Message msg = (Message) event.object;
                Contact contact = msg.contact;
                if (msg.isOutbox()) {
                    break;
                }
                String answer = getAnswer(msg.text);

                try {
                    Integer idMessage = msgApi.send(
                            contact.id,
                            null,
                            null,
                            null,
                            answer,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    logger.debug("Сообщение отправлено " + idMessage);
                } catch (IOException ex) {
                    logger.error("Ошибка отправки сообщения", ex);
                }
                break;
            }
        }
    }

    public static String getAnswer(String request) {
        String answer;
        List<String> req = new ArrayList<>(Arrays.asList(request.split(" ")));
        try {
            if (req.size() == 3 && req.get(0).matches("[0-9]+[\\.][0-9]+|[0-9]+")) {
                List<Currency> currencyList = Currency.getCurrencyList();
                Currency from = null;
                Currency to = null;
                for (Currency currency : currencyList) {
                    if (currency.getCharCode().equals(req.get(1))) {
                        from = currency;
                    }
                    if (currency.getCharCode().equals(req.get(2))) {
                        to = currency;
                    }
                }
                if (from == null) {
                    throw new IllegalArgumentException("неизвестная валюта " + req.get(1));
                } else if (to == null) {
                    throw new IllegalArgumentException("неизвестная валюта " + req.get(2));
                }

                double amount = Double.parseDouble(req.get(0));
                MoneyAmount beforeConversion = new MoneyAmount(from, amount);
                MoneyAmount afterConversion = beforeConversion.convertTo(to);
                answer = beforeConversion.toString() + " = " + afterConversion.toString();
            } else {
                throw new IllegalArgumentException("введенная вами строка не состоит из трех ожидаемых частей " +
                        " разделенных пробелами, либо первая её часть не является числом");
            }
        } catch (IllegalArgumentException e) {
            answer = " Не корректный ввод, \n" + e.getMessage() + ",\n" + HELP + "\n Список доступных валют:";
            for (Currency currency : Currency.getCurrencyList()) {
                answer += currency.getCharCode() + ", ";
            }
            answer = answer.substring(0, answer.length() - 2);
        }
        return answer;
    }

    @Override
    public String description() {
        return null;
    }
}
