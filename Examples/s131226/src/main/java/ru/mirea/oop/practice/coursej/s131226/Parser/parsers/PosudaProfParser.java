package ru.mirea.oop.practice.coursej.s131226.parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PosudaProfParser implements Parser {
    public static final String TABLE_NAME = "PosudaProf";

    private static int formatArticle(String articleStr) {
        articleStr = articleStr.replaceAll(".*Арт.", "");
        if (articleStr.equals("")) {
            articleStr = "0";
        }
        articleStr = articleStr.replaceAll(" ", "");
        if (articleStr.length() > 5) {
            articleStr = articleStr.substring(articleStr.length() - 4);
        }
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {
        if (priceStr.equals("")) {
            priceStr = "0";
        }
        priceStr = priceStr.replaceAll(" ", "");
        priceStr = priceStr.replaceAll("руб.", "");
        priceStr = priceStr.replaceAll("\\D", "");
        return Integer.parseInt(priceStr);

    }

    @Override
    public ArrayList parseLinks() {
        return null;
    }

    @Override
    public Prices parsePrices() {

        Document document;
        Map<Integer, Integer> pairs = new HashMap<>();
        try {
            document = Jsoup.connect("http://posuda-prof.ru/brand/fissman/?PAGE_CNT=3000").timeout(15000).get();
            Elements divs = document.select(".products__items").select("li");

            for (Element div : divs) {
                int price = formatPrice(div.select(".line_price").text());
                int article = formatArticle(div.select(".name_category").text());
                pairs.put(article, price);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Prices(TABLE_NAME, pairs);
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }
}
