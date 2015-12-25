package ru.mirea.oop.practice.coursej.s131226.impl.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.impl.Parser;
import ru.mirea.oop.practice.coursej.s131226.entities.Item;
import ru.mirea.oop.practice.coursej.s131226.entities.Snapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class Fissman4youParser implements Parser {
    public static final String TABLE_NAME = "fissman4you";
    public static final String ADRESS = "http://fissman4you.ru";
    private static final Logger logger = LoggerFactory.getLogger(Fissman4youParser.class);

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://fissman4you.ru/shop/CID_37.html").timeout(10000).get();
            Elements elements = document.select("div.podcatalog_div");
            for (Element div : elements) {
                String link = div.select("a").attr("href");
                link = link.replaceAll(".html", "_ALL.html");
                link = ADRESS + link;
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
                Elements elements = document.select(".panel_r");
                Elements elements1 = document.select(".panel_l");
                elements.addAll(elements1);
                for (Element element : elements) {
                    int price = formatPrice(element.select(".tovarThree_price4").text());
                    int article = formatArticle(element.select(".tovarTwo_name").select("a").attr("title"));
                    if (article != 0 || price != 0) {
                        snapshot.add(new Item(article, price));
                    }
                }
            } catch (IOException e) {
                logger.error("Ошибка при получении данных с сайта.");
            }
        }
        return snapshot;
    }

    private static int formatArticle(String articleStr) {
        if (articleStr.length() > 3) {
            articleStr = articleStr.substring(0, 4);
        }
        if (articleStr.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(articleStr.replaceAll("\\D", ""));
        }

    }

    private static int formatPrice(String priceStr) {
        priceStr = priceStr.replaceAll("\\.00", "");
        priceStr = priceStr.replaceAll("\\D", "");
        if (priceStr.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(priceStr);
        }
    }
}

