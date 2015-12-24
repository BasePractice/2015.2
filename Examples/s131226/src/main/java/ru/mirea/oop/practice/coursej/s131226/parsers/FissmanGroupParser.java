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

final class FissmanGroupParser implements Parser {
    public static final String SHOWALL = "?SHOWALL_3=1";
    public static final String ADRESS = "http://fissman-group.ru";
    public static final String TABLE_NAME = "fissmangroup";
    private static final Logger logger = LoggerFactory.getLogger(FismartParser.class);


    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        Document document;
        try {
            document = Jsoup.connect(ADRESS + "/catalog/nabory_posudy_kastryuli/" + SHOWALL).timeout(15000).get();
            Elements linksInHtml = document.select("ul.menu").select(".menu_item_link");
            for (Element link : linksInHtml) {
                links.add(ADRESS + link.attr("data-link"));
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
                Document documentForCategory = Jsoup.connect(link + SHOWALL).timeout(15000).get();
                Elements elements = documentForCategory.select("div.catalog_item_grid_short");
                elements.select("div.catalog_item_title").remove();
                elements.select("div.product_big_photo").remove();
                for (Element element : elements) {
                    int price = formatPrice(element.select(".product_price").text());
                    int article = formatArticle(element.select("a").attr("data-artnum"));
                    snapshot.add(new Item(article, price));
                }
            } catch (IOException e) {
                logger.error("Ошибка при получении данных с сайта.");
            }
        }
        return snapshot;
    }

    private static int formatArticle(String articleStr) {
        articleStr = articleStr.replaceAll("\\D", "");
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

