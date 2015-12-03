package ru.mirea.oop.practice.coursej.s131252;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;
import ru.mirea.oop.practice.coursej.s131252.Currency.Currency;
import ru.mirea.oop.practice.coursej.s131252.Currency.MoneyAmount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrencyConverter extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverter.class);
    private final MessagesApi msgApi;
    public static final String HELP = " пример входных даннх: 22.10 USD RUB";

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
                String answer = "";
                List<String> req = new ArrayList<>(Arrays.asList(msg.text.split(" ")));
                try {
                    if (req.size() == 3 && req.get(0).matches("[0-9]+[\\.]+[0-9]+|[0-9]+")) {
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
                        if (from == null || to == null) {
                            throw new IllegalArgumentException("одна из валют запрошенной валютной пары не найдена");
                        }

                        double amount = Double.parseDouble(req.get(0));
                        MoneyAmount beforeConversion = new MoneyAmount(from, amount);
                        MoneyAmount afterConversion = beforeConversion.convertTo(to);
                        answer = beforeConversion.toString() + " = " + afterConversion.toString();
                    } else {
                        throw new IllegalArgumentException("ошибка входных параметров");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    answer = " Не корректный ввод, \n" + HELP+"\n список доступных валют:";
                    for (Currency currency : Currency.getCurrencyList()) {
                        answer += currency.getCharCode() + ", ";
                    }
                    answer = answer.substring(0, answer.length() - 2);
                }
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

    @Override
    public String description() {
        return null;
    }
}
