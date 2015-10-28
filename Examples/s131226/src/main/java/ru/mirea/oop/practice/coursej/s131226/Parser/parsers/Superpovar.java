package ru.mirea.oop.practice.coursej.s131226.Parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Superpovar implements Parser {
    public static final String TABLE_NAME = "superpovar";

    private static int formatArticle(String articleStr) {
        if (articleStr.equals("")) {
            return 0;
        } else {
            articleStr = articleStr.replaceAll("\\D", "");
        }
        if (articleStr.equals("")) {
            return 0;
        }
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {
        if (priceStr.equals("")) {
            return 0;
        }
        priceStr = priceStr.replaceAll("руб.*", "");
        priceStr = priceStr.replaceAll("\\D", "");
        if (priceStr.equals("")) {
            return 0;
        }
        return Integer.parseInt(priceStr);
    }

    @Override
    public ArrayList<String> parseLinks() {

        ArrayList<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://superpovar.ru/catalog/25-FISSMAN.htm").timeout(15000).get();
            Elements elements = document.select(".open").select("li");
            for (Element a : elements) {
                String link = a.select("a").attr("href");
                links.add(link);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("количество запросов для " + this.getClass().getName() + " " + links.size());
        return links;

    }

    @Override
    public Prices parsePrices() {

        Map<Integer, Integer> pairs = new HashMap<>();
        for (String link : parseLinks()) {
            try {
                Document document = Jsoup.connect(link).timeout(15000).get();
                Elements elements = document.select(".item");
                for (Element element : elements) {
                    int article = formatArticle(element.select(".art").text());
                    int price = formatPrice(element.select(".price").text());
                    pairs.put(article, price);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Prices(TABLE_NAME, pairs);

    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }
}
