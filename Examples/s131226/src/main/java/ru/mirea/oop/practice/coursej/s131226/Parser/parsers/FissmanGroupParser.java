package ru.mirea.oop.practice.coursej.s131226.Parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FissmanGroupParser implements Parser {
    public static final String SHOWALL = "?SHOWALL_3=1";
    public static final String ADRESS = "http://fissman-group.ru";
    public static final String TABLE_NAME = "fissmangroup";

    @Override
    public ArrayList<String> parseLinks() {
        ArrayList<String> links = new ArrayList<>();
        Document document;
        try {

            document = Jsoup.connect(ADRESS + "/catalog/nabory_posudy_kastryuli/" + SHOWALL).timeout(15000).get();
            Elements linksInHtml = document.select("ul.menu").select(".menu_item_link");
            for (Element link : linksInHtml) {
                links.add(ADRESS + link.attr("data-link"));
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
                Document documentForCategory = Jsoup.connect(link + SHOWALL).timeout(15000).get();
                Elements elements = documentForCategory.select("div.catalog_item_grid_short");
                elements.select("div.catalog_item_title").remove();
                elements.select("div.product_big_photo").remove();
                for (Element element : elements) {
                    int price = formatPrice(element.select(".product_price").text());
                    int article = formatArticle(element.select("a").attr("data-artnum"));
                    pairs.put(article, price);
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

    private static int formatArticle(String articleStr) {
        if (articleStr.equals("")) {
            articleStr = "0";
        }
        articleStr = articleStr.replaceAll("\\D", "");
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
}

