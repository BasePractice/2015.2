package ru.mirea.oop.practice.coursej.s131226.parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FissmanInfoParser implements Parser {
    public static final String TABLE_NAME = "fissmaninfo";

    private static int formatArticle(String articleStr) {

        if (!articleStr.equals("") && articleStr.length() < 200) {
            articleStr = articleStr.substring(0, 4);
            articleStr = articleStr.replaceAll("\\D", "");
            if (!articleStr.equals("")) {
                return Integer.parseInt(articleStr);
            } else return 0;
        } else {
            return 0;
        }

    }

    private static int formatPrice(String priceStr) {

        if (!priceStr.equals("") && priceStr.length() < 20) {

            priceStr = priceStr.replaceAll(" ", "");
            priceStr = priceStr.replaceAll("...Руб.", "");
            priceStr = priceStr.replaceAll("\\D", "");

            return Integer.parseInt(priceStr);

        } else {
            return 0;
        }

    }

    @Override
    public List<String> parseLinks() {
        List<String> categoryLinks = new ArrayList<>();
        Document document = null;
        try {
            document = Jsoup.connect("http://fissman.info/shop/index.php?route=common/home").timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements catLinksElem;
        if (document != null) {
            catLinksElem = document.select("#menu").select("a");

            for (Element catLink : catLinksElem) {
                categoryLinks.add(catLink.attr("href") + "#limit=500");

            }
        }
        System.out.println("количество запросов для " + this.getClass().getName() + " " + categoryLinks.size());
        return categoryLinks;
    }

    @Override
    public Prices parsePrices() {
        Map<Integer, Integer> pairs = new HashMap<>();
        for (String link : parseLinks()) {
            try {
                Document documentForCategory = Jsoup.connect(link).timeout(15000).get();
                Elements elements = documentForCategory.select("div");
                for (Element element : elements) {
                    try {
                        int price = formatPrice(element.select(".price").text());
                        int article = formatArticle(element.select(".name").text());
                        if (article != 0 && price != 0 && price < 80000) {
                            pairs.put(article, price);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
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
