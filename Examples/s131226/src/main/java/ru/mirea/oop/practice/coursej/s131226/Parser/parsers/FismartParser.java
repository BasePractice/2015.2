package ru.mirea.oop.practice.coursej.s131226.Parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FismartParser implements Parser {
    public static final String ADRESS = "http://fismart.ru";
    public static final String TABLE_NAME = "Fismart";

    @Override
    public ArrayList<String> parseLinks() {
        ArrayList<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://fismart.ru/").timeout(15000).get();
            Elements elements = document.select(".has_inn");
            for (Element a : elements) {
                a = a.removeClass(".head_popup");
                String link = a.select("a").attr("href");
                link = ADRESS + link + "?SHOWALL_1=1";

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
                Elements elements = document.select(".cat_side").select(".cat_item").select(".info");

                for (Element element : elements) {
                    int article = formatArticle(element.select("a").text());
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

    private static int formatArticle(String articleStr) {
        if (articleStr.equals("")) {
            return 0;
        } else {
            articleStr = articleStr.substring(articleStr.length() - 4, articleStr.length());
        }
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {
        if (priceStr.equals("")) {
            return 0;
        }
        priceStr = priceStr.replaceAll("руб.*", "");
        priceStr = priceStr.replaceAll("\\D", "");
        return Integer.parseInt(priceStr);
    }
}