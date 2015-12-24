package ru.mirea.oop.practice.coursej.s131226.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.Parser;
import ru.mirea.oop.practice.coursej.s131226.entities.Item;
import ru.mirea.oop.practice.coursej.s131226.entities.Snapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Superpovar implements Parser {
    public static final String TABLE_NAME = "superpovar";
    private static final Logger logger = LoggerFactory.getLogger(Superpovar.class);

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://superpovar.ru/catalog/25-FISSMAN.htm").timeout(15000).get();
            Elements elements = document.select(".open").select("li");
            for (Element a : elements) {
                String link = a.select("a").attr("href");
                links.add(link);
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении данных с сайта.");
        }
        return links;

    }

    @Override
    public Snapshot parsePrices() {
        Snapshot snapshot = new Snapshot(TABLE_NAME);
        for (String link : parseLinks()) {
            try {
                Document document = Jsoup.connect(link).timeout(15000).get();
                Elements elements = document.select(".item");
                for (Element element : elements) {
                    int article = formatArticle(element.select(".art").text());
                    int price = formatPrice(element.select(".price").text());
                    snapshot.add(new Item(article, price));
                }
            } catch (IOException e) {
                logger.error("Ошибка при получении данных с сайта.");
            }
        }
        return snapshot;
    }

    private static int formatArticle(String articleStr) { // TODO переделать
        if (articleStr.equals("")) {
            return 0;
        } else {
            articleStr = articleStr.replaceAll("\\D", "");
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
        priceStr = priceStr.replaceAll("руб.*", "");
        priceStr = priceStr.replaceAll("\\D", "");
        if (priceStr.equals("")) {
            return 0;
        }
        return Integer.parseInt(priceStr);
    }
}
