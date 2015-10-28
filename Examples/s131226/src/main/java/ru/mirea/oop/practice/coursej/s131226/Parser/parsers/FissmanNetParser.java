package ru.mirea.oop.practice.coursej.s131226.parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FissmanNetParser implements Parser {
    public static final String TABLE_NAME = "FissmanNet";
    public static final String ADRESS = "http://www.fissman.net";

    private static int formatArticle(String articleStr) {
        if (articleStr.equals("")) {
            articleStr = "0";
        }
        if (articleStr.length() > 3) {
            articleStr = articleStr.substring(0, 4);

        }
        return Integer.parseInt(articleStr);


    }

    private static int formatPrice(String priceStr) {
        if (priceStr.equals("")) {
            priceStr = "0";
        }
        priceStr = priceStr.replaceAll(" ", "");
        priceStr = priceStr.replaceAll("руб", "");
        return Integer.parseInt(priceStr);

    }

    @Override
    public ArrayList<String> parseLinks() {
        ArrayList<String> links = new ArrayList<>();

        try {
            Document document = Jsoup.connect("http://www.fissman.net/shop/CID_43.html").timeout(15000).get();
            Elements elements = document.select("div.podcatalog_forma").select("td.pod_c");
            for (Element div : elements) {
                String link = div.select("a").attr("href");
                link = link.replaceAll(".html", "_ALL.html");
                link = ADRESS + link;
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
                Elements elements = document.select(".panel_r");
                Elements elements1 = document.select(".panel_l");
                elements.addAll(elements1);
                for (Element element : elements) {
                    int price = formatPrice(element.select(".price").text());
                    int article = formatArticle(element.select(".product_name").attr("title"));
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
