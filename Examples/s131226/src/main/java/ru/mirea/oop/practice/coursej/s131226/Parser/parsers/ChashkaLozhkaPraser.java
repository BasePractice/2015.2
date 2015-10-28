package ru.mirea.oop.practice.coursej.s131226.parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChashkaLozhkaPraser implements Parser {
    public static final String TABLE_NAME = "chashkalozhka";

    @Override
    public ArrayList<String> parseLinks() {
        ArrayList<String> links = new ArrayList<>();
        try {
            Document documentForPage = Jsoup.connect("http://xn--80aaaxscfy0fl.xn--p1ai/fissman?limit=2000&page=1").timeout(15000).get();
            Elements elements = documentForPage.select(".name");

            for (Element div : elements) {
                String link = div.select("a").attr("href");
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
        for (String productLink : parseLinks()) {
            Document documentForProduct;
            try {
                documentForProduct = Jsoup.connect(productLink).timeout(15000).get();
                Elements elements = documentForProduct.select("div.product-info");
                for (Element div : elements) {
                    int price = formatPrice(div.select("div.price").text());
                    int article = formatArticle(div.select("div.infoleft").text());
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
        articleStr = articleStr.replaceAll("Производитель: Fissman Модель: ", "");
        articleStr = articleStr.replaceAll(" Наличие(.*)", "");
        articleStr = articleStr.replaceAll("\\D", "");
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {

        priceStr = priceStr.replaceAll(".00.р.", "");
        priceStr = priceStr.replaceAll("\\D", "");

        return Integer.parseInt(priceStr);
    }

}
