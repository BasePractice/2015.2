package ru.mirea.oop.practice.coursej.s131250;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.vk.PhotosApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class WAAction {
    public static WAMessage processWAMessage(String input, VkontakteApi api) throws Exception {
        ResponseBody waResponse = WARequestAction.getResponsefromWA(input);
        String baseImage = ImageBuilder.textWrite("VK Bot /WolframAlpha/ results");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            assert waResponse != null;
            Document doc = dBuilder.parse(waResponse.byteStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("pod");
            for (int ipod = 0; ipod < nList.getLength(); ipod++) {
                Node nNode = nList.item(ipod);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    baseImage = ImageBuilder.textWrite(baseImage, eElement.getAttribute("title") + "\n");
                    NodeList subnods = eElement.getElementsByTagName("subpod");
                    for (int inod = 0; inod < subnods.getLength(); inod++) {
                        Node nSubNode = subnods.item(inod);
                        if (nSubNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element subElem = (Element) nSubNode;
                            Element imgElem = (Element) subElem.getElementsByTagName("img").item(0);
                            baseImage = ImageBuilder.combImagesfromURL(baseImage, imgElem.getAttribute("src"));
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        PhotosApi userver = api.getPhotos();
        UploadServer mu = userver.getMessagesUploadServer();
        MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");
        OkHttpClient client = api.createClient();
        File file = new File(baseImage);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("photo", file.getName(),
                        RequestBody.create(MediaType.parse("image/gif"), file))
                .build();

        Request request = new Request.Builder()
                .url(mu.urlUpload)
                .post(requestBody)
                .build();
        Response resp2 = client.newCall(request).execute();
        if (!resp2.isSuccessful()) throw new IOException("Unexpected code " + resp2);
        String photo = resp2.body().string();

        JSONObject obj2 = new JSONObject(photo);
        Integer serverS = obj2.getInt("server");
        String photoS = obj2.getString("photo");
        String hash = obj2.getString("hash");
        PhotosApi p = api.getPhotos();
        String p1 = userver.saveMessagesPhoto(serverS, photoS, photoS, hash).toString();
        String mediaid = p1.split(", id=")[1].split(", aid")[0];
        String src_xxbig = "";
        String src_xxxbig = "";
        String originalImage = "";
        try {
            src_xxbig = p1.split(", src_xxbig=")[1].split(", ")[0];
            src_xxxbig = p1.split(", src_xxxbig=")[1].split(", ")[0];
        } catch (Exception ignored) {
        }
        if (!src_xxbig.equals("")) {
            originalImage = src_xxbig;
        }
        if (!src_xxxbig.equals("")) {
            originalImage = src_xxxbig;
        }
        if (!originalImage.equals("")) {
            originalImage = "Original image: " + originalImage;
        }
        return new WAMessage(originalImage, mediaid);
    }
}
