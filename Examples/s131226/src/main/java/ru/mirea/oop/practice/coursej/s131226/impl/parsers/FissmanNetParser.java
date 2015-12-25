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

final class FissmanNetParser implements Parser {
    public static final String TABLE_NAME = "FissmanNet";
    public static final String ADRESS = "http://www.fissman.net";
    private static final Logger logger = LoggerFactory.getLogger(FissmanNetParser.class);

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
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
                    int price = formatPrice(element.select(".price").text());
                    int article = formatArticle(element.select(".product_name").attr("title"));
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
        }
        if (articleStr.length() > 3) {
            articleStr = articleStr.substring(0, 4);
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
