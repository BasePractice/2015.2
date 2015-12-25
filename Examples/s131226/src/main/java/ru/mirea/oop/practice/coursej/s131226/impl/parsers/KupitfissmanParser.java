package ru.mirea.oop.practice.coursej.s131226.impl.parsers;

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

final class KupitfissmanParser implements Parser {
    public static final String TABLE_NAME = "kupitfissman";
    public static final String ADRESS = "http://kupitfissman.ru/";
    private static final Logger logger = LoggerFactory.getLogger(KupitfissmanParser.class);

    public List<String> parseLinks() {
        List<String> catLinks = new ArrayList<>();
        List<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect(ADRESS).timeout(15000).get();
            Elements elements = document.select(".col-sm-3").select("a");
            for (Element a : elements) {
                String link = a.attr("href");
                if (link.contains("http")) {
                    catLinks.add(link);
                }
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
        } catch (IOException e) {
            logger.error("Ошибка при получении данных с сайта.");
        }
        links.addAll(catLinks);
        return links;

    }

    @Override
    public Snapshot parsePrices() {
        Snapshot snapshot = new Snapshot(TABLE_NAME);
        for (String link : parseLinks()) {
            try {
                Document document = Jsoup.connect(link).timeout(15000).get();
                Elements elements = document.select(".product");
                for (Element element : elements) {
                    int article = formatArticle(element.select("a").text());
                    int price = formatPrice(element.select(".price").select(".amount").text());
                    snapshot.add(new item(article, price));
                }
            } catch (IOException e) {
                logger.error("Ошибка при получении данных с сайта.");
            }
        }
        return snapshot;
    }

    private static int formatArticle(String articleStr) {
        articleStr = articleStr.replaceAll("\\D", "");
        if (articleStr.length() > 4) {
            articleStr = articleStr.substring(0, 4);
            return Integer.parseInt(articleStr);
        }
        return 0;
    }

    private static int formatPrice(String priceStr) {
        priceStr = priceStr.replaceAll("\\D", "");
        if (priceStr.equals("")) {
            return 0;
        }
        return Integer.parseInt(priceStr);
    }
}
