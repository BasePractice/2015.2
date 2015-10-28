package ru.mirea.oop.practice.coursej.s131226.parser.parsers;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KazanchikParser implements Parser {
    public static final String TABLE_NAME = "kazanchik";

    private static int formatArticle(String articleStr) {
        if (articleStr.equals("")) {
            return 0;
        } else {
            articleStr = articleStr.replaceAll("\\D", "");
            if (articleStr.length() > 4) {
                articleStr = articleStr.substring(0, 4);
            }
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

        priceStr = priceStr.replaceAll("\\D", "");

        return Integer.parseInt(priceStr);
    }

    @Override
    public ArrayList<String> parseLinks() {
        ArrayList<String> links = new ArrayList<>();
        for (int i = 1; i < 42; i++) {
            String link = "http://www.kazanchik.ru/category/fissman/" + i + "/";
            links.add(link);
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
                Elements elements = document.select(".vitrine").select(".product");

                for (Element element : elements) {
                    int article = formatArticle(element.select(".product-name-link").attr("href"));
                    int price = formatPrice(element.select(".price").text());
                    pairs.put(article, price);
                }
            } catch (HttpStatusException e404) {
                System.out.println("404 " + link);
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
