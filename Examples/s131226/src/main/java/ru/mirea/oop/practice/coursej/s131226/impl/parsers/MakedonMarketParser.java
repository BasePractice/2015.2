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

final class MakedonMarketParser implements Parser {
    public static final String TABLE_NAME = "makedonshop";
    private static final Logger logger = LoggerFactory.getLogger(MakedonMarketParser.class);

    public List<String> parseLinks() {
        List<String> links = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://www.makedon-market.ru/search/?search_field=FISSMAN&page=1").timeout(15000).get();
            Elements elementsLinks = document.select("div.navigation-pages").select("a");
            String lastPage = "1";
            for (Element e : elementsLinks) {
                lastPage = e.attr("abs:href");
            }
            lastPage = lastPage.replaceAll("http://www.makedon-market.ru/page=", "");
            int lastPageIndex = Integer.valueOf(lastPage);
            for (int i = 1; i <= lastPageIndex; i++) {
                String link = "http://www.makedon-market.ru/search/?search_field=FISSMAN&page=" + i;
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
                Elements li = document.select(".products");
                Elements div = li.select("div.line_stars_warehouse");
                li.removeAll(div);
                li = li.select("li").attr("itemprop", "itemListElement");
                for (Element element : li) {
                    int price = formatPrice(element.select(".price").text());
                    int article = formatArticle(element.select(".sku").text());
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
        if (articleStr.length() > 3) {
            articleStr = articleStr.substring(0, 4);
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
