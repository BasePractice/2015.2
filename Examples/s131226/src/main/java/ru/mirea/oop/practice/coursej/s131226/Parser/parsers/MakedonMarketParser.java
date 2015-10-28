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

final class MakedonMarketParser implements Parser {
    public static final String TABLE_NAME = "makedonshop";

    private static int formatArticle(String articleStr) {
        articleStr = articleStr.replaceAll("\\D", "");
        if (articleStr.length() > 3) {
            articleStr = articleStr.substring(0, 4);
        }
        if (articleStr.equals("")) {
            articleStr = "0";
        }
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {

        priceStr = priceStr.replaceAll("\\D", "");
        if (priceStr.equals("")) {
            priceStr = "0";
        }
        return Integer.parseInt(priceStr);
    }

    @Override

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://www.makedon-market.ru/search/?search_field=FISSMAN&page=1").timeout(15000).get();
            Elements elementsLinks = document.select("div.navigation-pages").select("a");
            String lastPage = "anylink";
            for (Element e : elementsLinks) {
                lastPage = e.attr("abs:href");
            }
            lastPage = lastPage.replaceAll("http://www.makedon-market.ru/page=", "");
            int lastPageIndex = Integer.valueOf(lastPage);
            for (int i = 1; i <= lastPageIndex; i++) {
                String link = "http://www.makedon-market.ru/search/?search_field=FISSMAN&page=" + i;
                links.add(link);

            }
        } catch (IOException e) {
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
                Elements li = document.select(".products");
                Elements div = li.select("div.line_stars_warehouse");
                li.removeAll(div);
                li = li.select("li").attr("itemprop", "itemListElement");
                for (Element element : li) {
                    int price = formatPrice(element.select(".price").text());
                    int article = formatArticle(element.select(".sku").text());
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
