package ru.mirea.oop.practice.coursej.s131226.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.mirea.oop.practice.coursej.s131226.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class KupitfissmanParser implements Parser {
    public static final String TABLE_NAME = "kupitfissman";
    public static final String ADRESS = "http://kupitfissman.ru";

    private static int formatArticle(String articleStr) {
        if (articleStr.equals("")) {
            return 0;
        } else {
            articleStr = articleStr.replaceAll("FISSMAN\\D", "");
            articleStr = articleStr.replaceAll("\\D", "");
            articleStr = articleStr.substring(0, 4);
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
    public List<String> parseLinks() {
        List<String> catLinks = new ArrayList<>();
        List<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://kupitfissman.ru/").timeout(15000).get();
            Elements elements = document.select(".btn-group").select("a");
            for (Element a : elements) {
                String link = ADRESS + a.attr("href");
                catLinks.add(link);
            }
            for (String catLink : catLinks) {
                document = Jsoup.connect(catLink).timeout(15000).get();
                elements = document.select("a.page-numbers").select("a");
                elements.removeAll(document.select("a.next"));
                int maxPage = 0;
                for (Element element : elements) {
                    String link = element.attr("href");
                    links.add(link);

                    maxPage = Integer.parseInt(link.replaceAll("\\D", ""));
                }
                if (maxPage > 2) {
                    for (int i = 3; i < maxPage; i++) {

                        links.add(catLink + "page/" + i + "/");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        links.addAll(catLinks);
        System.out.println("количество запросов для " + this.getClass().getName() + " " + links.size());
        return links;

    }

    @Override
    public Prices parsePrices() {
        Map<Integer, Integer> pairs = new HashMap<>();
        for (String link : parseLinks()) {
            try {
                Document document = Jsoup.connect(link).timeout(15000).get();
                Elements elements = document.select(".product");

                for (Element element : elements) {
                    int article = formatArticle(element.select("a").text());
                    int price = formatPrice(element.select(".price").select(".amount").text());
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
