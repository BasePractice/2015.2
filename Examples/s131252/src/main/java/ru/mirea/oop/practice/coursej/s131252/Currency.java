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

class Currency {
    private static final Logger logger = LoggerFactory.getLogger(Currency.class);
    private final String charCode;
    private final double value;
    public static final String URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    private Currency(String charCode, double value) {
        this.charCode = charCode;
        this.value = value;
    }

    public static List<Currency> getCurrencyList() {
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
                currencyList.add(currency);
            }

        } catch (ParserConfigurationException e) {
            // ну не может его тут быть, стандартные же параметры
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
        return new Currency(charCode, value / nominal);
    }

    public String getCharCode() {
        return charCode;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "charCode='" + charCode + '\'' +
                ", value=" + value +
                '}';
    }
}
