package ru.mirea.oop.practice.coursej.s131226.impl.parsers;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.impl.Parser;
import ru.mirea.oop.practice.coursej.s131226.entities.item;
import ru.mirea.oop.practice.coursej.s131226.entities.Snapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class KazanchikParser implements Parser {
    public static final String TABLE_NAME = "kazanchik";
    private static final Logger logger = LoggerFactory.getLogger(KazanchikParser.class);

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        for (int i = 1; i < 42; i++) {
            String link = "http://www.kazanchik.ru/category/fissman/" + i + "/";
            links.add(link);
        }
        return links;
    }

    @Override
    public Snapshot parsePrices() {
        Snapshot snapshot = new Snapshot(TABLE_NAME);
        for (String link : parseLinks()) {
            try {
                Document document = Jsoup.connect(link).timeout(15000).get();
                Elements elements = document.select(".vitrine").select(".product");
                for (Element element : elements) {
                    int article = formatArticle(element.select(".product-name-link").attr("href"));
                    int price = formatPrice(element.select(".price").text());
                    snapshot.add(new item(article, price));
                }
            } catch (HttpStatusException e404) {
                logger.error("Ошибка при получении данных с сайта.(404)");
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
        priceStr = priceStr.replaceAll("\\D", "");
        if (priceStr.equals("")) {
            return 0;
        }

        return Integer.parseInt(priceStr);
    }

}
