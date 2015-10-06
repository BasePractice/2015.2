package ru.mirea.oop.practice.coursej;

import com.squareup.okhttp.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class FormAction {
    private final String url;
    private final String method;
    private final Map<String, String> params = new HashMap<>();

    interface FormData {
        void push(Map<String, String> data);
    }

    private FormAction(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public Response process(OkHttpClient ok, FormData data) throws IOException {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Map<String, String> params = new HashMap<>(this.params);
        if (data != null)
            data.push(params);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).method(method.toUpperCase(), body).build();
        return ok.newCall(request).execute();
    }

    public static FormAction create(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("form");
        Element form = elements.first();
        if (form != null) {
            FormAction formAction = new FormAction(form.attr("action"), form.attr("method"));
            findInputs(form.children(), formAction.params);
            return formAction;
        }
        return null;
    }

    private static void findInputs(Elements elements, Map<String, String> map) {
        if (elements.size() > 0) {
            for (Element element : elements) {
                String type = element.attr("type");
                if ("input".equalsIgnoreCase(element.tagName()) &&
                        ("text".equalsIgnoreCase(type) ||
                                "hidden".equalsIgnoreCase(type) ||
                                "password".equalsIgnoreCase(type))) {
                    map.put(element.attr("name"), element.attr("value"));
                }
                if (element.children().size() > 0) {
                    findInputs(element.children(), map);
                }
            }
        }
    }
}
