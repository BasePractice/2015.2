package ru.mirea.oop.practice.coursej.s131252;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class Currency {
    private static final Logger logger = LoggerFactory.getLogger(Currency.class);
    final String code;
    final double value;
    private static final String URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    private Currency(String code, double value) {
        this.code = code;
        this.value = value;
    }

    static List<Currency> getCurrencyList() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();
        InputSource inputSource = new InputSource();
        try {
            Response response = client.newCall(request).execute();
            inputSource.setCharacterStream(response.body().charStream());
        } catch (IOException e) {
            logger.error("Ошибка при выполнении запроса к сайту ЦБ РФ по url {}", URL);
        }
        List<Currency> currencyList = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputSource);
            NodeList nodeList = document.getElementsByTagName("Valute");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Currency currency = getCurrencyFromNode(nodeList.item(i));
                if (currency != null) {
                    currencyList.add(currency);
                }
            }
        } catch (ParserConfigurationException e) {
            logger.error("Ошибка чтения парамтеров конфигурации при разборке XML документа.");
        } catch (SAXException e) {
            logger.error("Ошибка при разборе XML документа полученного с сайта ЦБ РФ по url {}", URL);
        } catch (IOException e) {
            logger.error("Ошибка при получении XML документа с сайта ЦБ РФ по url {}", URL);
        }
        currencyList.add(new Currency("RUB", 1));
        return currencyList;
    }

    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }

    private static Currency getCurrencyFromNode(Node node) {
        Element element = (Element) node;
        String charCode = getCharacterDataFromElement((Element) element.getElementsByTagName("CharCode").item(0));
        int nominal = Integer.parseInt(getCharacterDataFromElement((Element) element.getElementsByTagName("Nominal").item(0)));
        double value = Double.parseDouble((getCharacterDataFromElement((Element) element.getElementsByTagName("Value").item(0))).replaceAll(",", "."));
        if (nominal != 0 && value != 0) {
            return new Currency(charCode, value / nominal);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                ", value=" + value +
                '}';
    }
}
