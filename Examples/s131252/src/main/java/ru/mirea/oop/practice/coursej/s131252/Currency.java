package ru.mirea.oop.practice.coursej.s131252;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
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
    private final String charCode;
    private final double value;

    private Currency(String charCode, double value) {
        this.charCode = charCode;
        this.value = value;
    }

    public static List<Currency> getCurrencyList() {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        InputSource inputSource = new InputSource();
        try {
            Response response = client.newCall(request).execute();
            inputSource.setCharacterStream(response.body().charStream());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
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

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
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
        Currency currency = new Currency(charCode, value / nominal);
        return currency;
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
