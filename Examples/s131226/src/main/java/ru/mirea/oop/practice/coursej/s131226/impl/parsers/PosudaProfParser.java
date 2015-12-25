package ru.mirea.oop.practice.coursej.s131226.impl.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.entities.Item;
import ru.mirea.oop.practice.coursej.s131226.impl.Parser;
import ru.mirea.oop.practice.coursej.s131226.entities.Snapshot;

import java.io.IOException;


final class PosudaProfParser implements Parser {
    public static final String TABLE_NAME = "PosudaProf";
    private static final Logger logger = LoggerFactory.getLogger(PosudaProfParser.class);

    @Override
    public Snapshot parsePrices() {
        Document document;
        Snapshot snapshot = new Snapshot(TABLE_NAME);
        try {
            document = Jsoup.connect("http://posuda-prof.ru/brand/fissman/?PAGE_CNT=3000").timeout(25000).get();
            Elements divs = document.select(".products__items").select("li");
            for (Element div : divs) {
                int price = formatPrice(div.select(".line_price").text());
                int article = formatArticle(div.select(".name_category").text());
                snapshot.add(new Item(article, price));
            }
        } catch (IOException e) {
            logger.error("Ошибка при получении данных с сайта.");
        }
        return snapshot;
    }


    private static int formatArticle(String articleStr) { // TODO переделать эту гадость
        articleStr = articleStr.replaceAll(".*Арт.", "");
        if (articleStr.equals("")) {
            articleStr = "0";
        }
        articleStr = articleStr.replaceAll(" ", "");
        if (articleStr.length() > 5) {
            articleStr = articleStr.substring(articleStr.length() - 4);
        }
        return Integer.parseInt(articleStr);
    }

    private static int formatPrice(String priceStr) {
        if (priceStr.equals("")) {
            priceStr = "0";
        }
        priceStr = priceStr.replaceAll(" ", "");
        priceStr = priceStr.replaceAll("руб.", "");
        priceStr = priceStr.replaceAll("\\D", "");
        return Integer.parseInt(priceStr);

    }
}
