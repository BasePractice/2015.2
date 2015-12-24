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

final class FismartParser implements Parser {
    public static final String ADRESS = "http://fismart.ru";
    public static final String TABLE_NAME = "Fismart";
    private static final Logger logger = LoggerFactory.getLogger(FismartParser.class);

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://fismart.ru/").timeout(15000).get();
            Elements elements = document.select(".has_inn");
            for (Element a : elements) {
                a = a.removeClass(".head_popup");
                String link = a.select("a").attr("href");
                link = ADRESS + link + "?SHOWALL_1=1";

                links.add(link);
            }
        } catch (IOException e) {
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
                Elements elements = document.select(".cat_side").select(".cat_item").select(".info");

                for (Element element : elements) {
                    int article = formatArticle(element.select("a").text());
                    int price = formatPrice(element.select(".price").text());
                    snapshot.add(new Item(article, price));
                }
            } catch (IOException e) {
                logger.error("Ошибка при получении данных с сайта.");
            }
        }
        return snapshot;
    }

    private static int formatArticle(String articleStr) {
        if (articleStr.equals("")) {
            return 0;
        } else {
            articleStr = articleStr.substring(articleStr.length() - 4, articleStr.length());
        }
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {

        priceStr = priceStr.replaceAll("руб.*", "");
        priceStr = priceStr.replaceAll("\\D", "");
        if (priceStr.equals("")) {
            return 0;
        }
        return Integer.parseInt(priceStr);
    }
}