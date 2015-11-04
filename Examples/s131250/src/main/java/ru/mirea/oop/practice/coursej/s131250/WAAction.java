package ru.mirea.oop.practice.coursej.s131250;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.mirea.oop.practice.coursej.Configuration;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.vk.PhotosApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class WAAction {
    private static final Logger logger = LoggerFactory.getLogger(WAAction.class);
    private ImageBuilder currentImage;
    private VkontakteApi api;
    private String vkPhotoOptions;
    private WAMessage waMessage;

    public WAAction(VkontakteApi api) {
        this.currentImage = new ImageBuilder();
        this.api = api;
    }


    public WAMessage getWAMessage(String input) {
        try {
            createImageFromWA(input);
            uploadPhotoToVk();
            generateWAMessage();
            return waMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new WAMessage("WA parse error", null);
    }



    private void createImageFromWA(String input) throws IOException {
        ResponseBody waResponse = WARequestAction.getResponsefromWA(input);
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
                    currentImage.writeTextOnImage(eElement.getAttribute("title") + "\n");
                    NodeList subnods = eElement.getElementsByTagName("subpod");
                    for (int inod = 0; inod < subnods.getLength(); inod++) {
                        Node nSubNode = subnods.item(inod);
                        if (nSubNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element subElem = (Element) nSubNode;
                            Element imgElem = (Element) subElem.getElementsByTagName("img").item(0);
                            currentImage.pasteImageFromURL(imgElem.getAttribute("src"));
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void uploadPhotoToVk() throws Exception {
        PhotosApi photosApi = api.getPhotos();
        UploadServer uploadServer = photosApi.getMessagesUploadServer();
        OkHttpClient client = api.createClient();
        File file = new File(currentImage.getFullFileName());
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("photo", file.getName(),
                        RequestBody.create(MediaType.parse("image/gif"), file))
                .build();
        Request request = new Request.Builder()
                .url(uploadServer.urlUpload)
                .post(requestBody)
                .build();
        Response resp2 = client.newCall(request).execute();

        if (!file.delete()) {logger.error("Ошибка удаления файла "+ Configuration.getFileName(file.getName()));}
        if (!resp2.isSuccessful()) {logger.error("Unexpected code "+ resp2);}

        Gson gson = new Gson();
        VkImageGson gsonObject = gson.fromJson(resp2.body().string(), VkImageGson.class);
        Integer serverS = gsonObject.getServer();
        String photoS = gsonObject.getPhoto();
        String hash = gsonObject.getHash();
        vkPhotoOptions = photosApi.saveMessagesPhoto(serverS, photoS, photoS, hash).toString();
    }

    private class VkImageGson {
        private Integer server;
        private String photo;
        private String hash;

        public Integer getServer() {
            return server;
        }

        public String getPhoto() {
            return photo;
        }

        public String getHash() {
            return hash;
        }

    }

    private void generateWAMessage() throws IOException {
        String mediaId = vkPhotoOptions.split(", id=")[1].split(", aid")[0];
        String srcXxBig = "";
        String srcXxxBig = "";
        String messageText = "";
        try {
            srcXxBig = vkPhotoOptions.split(", src_xxbig=")[1].split(", ")[0];
            srcXxxBig = vkPhotoOptions.split(", src_xxxbig=")[1].split(", ")[0];
        } catch (Exception ignored) {
        }
        if (!srcXxBig.equals("")) {
            messageText = srcXxBig;
        }
        if (!srcXxxBig.equals("")) {
            messageText = srcXxxBig;
        }
        if (!messageText.equals("")) {
            messageText = "Original image: " + messageText;
        }
        waMessage = new WAMessage(messageText, mediaId);
    }
}
