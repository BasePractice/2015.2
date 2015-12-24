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

final class ChashkaLozhkaPraser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(ChashkaLozhkaPraser.class);
    public static final String TABLE_NAME = "chashkalozhka";

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        try {
            Document documentForPage = Jsoup.connect("http://xn--80aaaxscfy0fl.xn--p1ai/fissman?limit=2000&page=1").timeout(15000).get();
            Elements elements = documentForPage.select(".name");
            for (Element div : elements) {
                String link = div.select("a").attr("href");
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
        for (String productLink : parseLinks()) {
            Document documentForProduct;
            try {
                documentForProduct = Jsoup.connect(productLink).timeout(15000).get();
                Elements elements = documentForProduct.select("div.product-info");
                for (Element div : elements) {
                    int price = formatPrice(div.select("div.price").text());
                    int article = formatArticle(div.select("div.infoleft").text());
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
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {
        priceStr = priceStr.replaceAll(".00.р.", "");
        priceStr = priceStr.replaceAll("\\D", "");
        return Integer.parseInt(priceStr);
    }
}
