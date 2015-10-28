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

public class FissmanPosudaParser implements Parser {
    public static final String TABLE_NAME = "FissmanPosuda";
    public static final String ADRESS = "http://www.fissmanposuda.ru";


    private static int formatArticle(String articleStr) {
        if (articleStr != null) {


            if (articleStr.equals("")) {
                return 0;
            } else {
                articleStr = articleStr.substring(0, 4);
            }
            return Integer.parseInt(articleStr);
        }
        return 0;

    }

    private static int formatPrice(String priceStr) {
        if (priceStr != null) {


            if (priceStr.equals("")) {
                return 0;
            }
            priceStr = priceStr.replaceAll("\\..*руб.*", "");
            priceStr = priceStr.replaceAll("\\D", "");

            return Integer.parseInt(priceStr);
        }
        return 0;
    }

    @Override
    public List<String> parseLinks() {
        List<String> catLinks = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://www.fissmanposuda.ru/").timeout(15000).get();
            Elements elements = document.select(".cpt_category_tree").select(".parent");
            for (Element div : elements) {
                String link = div.select("a").attr("href");
                link = ADRESS + link;
                catLinks.add(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> links = new ArrayList<>();
        for (String catLink : catLinks) {
            try {
                Document document = Jsoup.connect(catLink).timeout(15000).get();
                Elements elements = document.select("a.no_underline");
                int maxOffset = 0;
                for (Element element : elements) {
                    String offset = element.select("a").attr("href");
                    offset = offset.replaceAll(".*all/", "");
                    offset = offset.replaceAll(".*/offset", "");
                    offset = offset.replaceAll("\\D", "");
                    if (!offset.equals("")) {
                        if (Integer.parseInt(offset) > maxOffset) {
                            maxOffset = Integer.parseInt(offset);
                        }
                    }
                }
                for (int i = 20; i <= maxOffset; i = 20 + i) {
                    String link = catLink + "offset" + i + "/";
                    links.add(link);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                Elements elements = document.select(".product_brief_block");
                for (Element element : elements) {
                    int article = formatArticle(element.select(".prdbrief_name").select("a").text());
                    int price = formatPrice(element.select(".totalPrice").text());
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
