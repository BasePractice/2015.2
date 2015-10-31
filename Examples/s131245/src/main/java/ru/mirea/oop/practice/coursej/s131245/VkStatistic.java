package ru.mirea.oop.practice.coursej.s131245;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.FriendsApi;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by aleksejpluhin on 13.10.15.
 */
public final class VkStatistic extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkStatistic.class);
    private static Map<Long, ArrayList<Session>> MapSession = new HashMap<>();
    private final Map<Long, Contact> friendsMap = new HashMap<>();
    private final MessagesApi msgApi;
    boolean alreadySend = false;
    private static final String FRIENDS_FIELDS = "nickname, " +
            "domain, " +
            "sex, " +
            "bdate, " +
            "city, " +
            "country, " +
            "timezone, " +
            "photo_50, " +
            "photo_100, " +
            "photo_200_orig, " +
            "has_mobile, " +
            "contacts, " +
            "education, " +
            "online, " +
            "relation, " +
            "last_seen, " +
            "status, " +
            "can_write_private_message, " +
            "can_see_all_posts, " +
            "can_post, " +
            "universities";

    static {
        MapSession.clear();
    }

    public VkStatistic() throws Exception {
        super("vk.services.VkStatistic");
        this.msgApi = api.getMessages();
    }


    @Override
    protected void doEvent(Event event) {

        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (MapSession.isEmpty()) {
            try {
                friendsMap.clear();
                FriendsApi friendsApi = api.getFriends();
                Contact[] contacts = friendsApi.list(null, null, null, null, FRIENDS_FIELDS);
                for (Contact contact : contacts) {
                    friendsMap.put(contact.id, contact);
                }
            } catch (Exception e) {
                logger.error("Ошибка получения списка друзей");
            }

            for (Map.Entry<Long, Contact> friend : friendsMap.entrySet()) {
                if (friend.getValue().online == 1) {
                    Session session = new Session(new Date());
                    ArrayList<Session> arrayList = new ArrayList<>();
                    arrayList.add(session);
                    MapSession.put(friend.getValue().id, arrayList);
                    logger.debug(friend.getValue().lastName + " - онлайн");
                } else {
                    ArrayList<Session> arrayList = new ArrayList<>();
                    MapSession.put(friend.getValue().id, arrayList);
                    logger.debug(friend.getValue().lastName + " - оффлайн");
                }
            }


        }

        switch (event.type) {

            case FRIEND_ONLINE: {
                UserOnline userOnline = (UserOnline) event.object;
                Long key = userOnline.getContact().id;

                Session session = new Session(new Date());
                if (MapSession.get(key).isEmpty()) {
                    ArrayList<Session> sessions = new ArrayList<>();
                    sessions.add(session);
                    MapSession.put(key, sessions);
                } else {
                    try {
                        int index = MapSession.get(key).size() - 1;
                        if (MapSession.get(key).get(index).end == null) {
                            MapSession.get(key).remove(index);
                            MapSession.get(key).add(session);
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        MapSession.get(key).add(session);
                    }

                }
                logger.debug(event.object + " вошел в " + dt.format(MapSession.get(key).get(MapSession.get(key).size() - 1).begin));

                break;

            }

            case FRIEND_OFFLINE: {
                UserOffline userOffline = (UserOffline) event.object;
                Long key = userOffline.getContact().id;
                int index = MapSession.get(key).size();
                index -= 1;
                try {
                    if (MapSession.get(key).get(index).begin == null) {
                        break;
                    }
                } catch (Exception e) {

                }
                MapSession.get(key).get(index).setEnd(new Date());
                logger.debug(((UserOffline) event.object).getContact().firstName +  " " + ((UserOffline) event.object).getContact().lastName + " вышел в " + dt.format(MapSession.get(key).get(MapSession.get(key).size() - 1).end));
                break;
            }


            case MESSAGE_RECEIVE: {

                if (alreadySend) {
                    alreadySend = false;
                    break;
                }
                Message msg = (Message) event.object;
                Contact contact = msg.contact;
                if (contact.id == owner.id && msg.text.contains("bot get")) {
                    String date = "";
                    String message = msg.text;
                    try {
                        Date date1 = new Date(msg.text.split(" ")[msg.text.split(" ").length - 1]);
                        message = message.substring(0, message.lastIndexOf(" "));
                        date = msg.text.split(" ")[msg.text.split(" ").length - 1];
                    } catch (Exception e) {
                        logger.error("даты нет");
                    }
                    String text = null;
                    if (message.split(": ")[1].equals("всех")) {
                        text = "Статисткика всех пользователй " + date;
                    } else {

                        String[] arr = message.split(": ")[1].split(", ");
                        String people = "";
                        for (String humanName : arr) {
                            people += humanName + ", ";
                            text = "Статистика пользователей: " + people.substring(0, people.length() - 2) + " " + date;
                        }
                    }


                    String attachment = null;
                    try {
                        attachment = Attachment.getAttachment(MapSession, api, msg.text, friendsMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                try {
                    Integer idMessage = msgApi.send(
                            contact.id,
                            null,
                            null,
                            null,
                            text,
                            null,
                            null,
                            null,
                            attachment,
                            null,
                            null

                    );
                    logger.debug("Сообщение отправлено " + idMessage);
                    alreadySend = true;
                } catch (IOException ex) {
                    logger.error("Ошибка отправки сообщения", ex);
                }
            }
            else if (msg.text.equals("help")) {
                String text = "Запрос состоит имеет ввид\n1)bot get: Иван Иванов\n2)bog get: всех\n3)bot get: Иванов Иван, Иванова Ивана 10/04/2015\n";
                try {
                    Integer idMessage = msgApi.send(
                            contact.id,
                            null,
                            null,
                            null,
                            text,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null

                    );
                    logger.debug("Сообщение отправлено " + idMessage);
                    alreadySend = true;
                } catch (IOException ex) {
                    logger.error("Ошибка отправки сообщения", ex);
                }
            }
            break;
        }
        case TIMEOUT: {
            try {
                friendsMap.clear();
                FriendsApi friendsApi = api.getFriends();
                Contact[] contacts = friendsApi.list(null, null, null, null, FRIENDS_FIELDS);
                for (Contact contact : contacts) {
                    friendsMap.put(contact.id, contact);
                }
            } catch (Exception e) {
                logger.debug("Ошибка получение списка друзей");
            }
            for (Map.Entry<Long, Contact> current : friendsMap.entrySet()) {
                Long key = current.getValue().id;
                try {
                    if (!MapSession.get(key).isEmpty()) {
                        if ((MapSession.get(key).get(MapSession.get(key).size() - 1).end == null) && current.getValue().online == 0) {
                            MapSession.get(key).get(MapSession.get(key).size() - 1).setEnd(new Date());
                        } else if ((MapSession.get(key).get(MapSession.get(key).size() - 1).begin == null) && current.getValue().online == 1) {
                            Session session = new Session(new Date());
                            MapSession.get(key).add(session);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Ошибка timeout");
                }
            }
        }

        default:
         logger.debug("" + (event.object == null ? event.type : event.type + "|" + event.object));

        break;
    }


    }

    @Override
    public String description() {
        return  "Сервис статистики пользователей \"Вконтакте\"";
    }


    public static class Session {
        private Date begin;
        private Date end;
        private Date session;
        public Session(Date begin) {
            this.begin = begin;
        }


        public void setBegin(Date begin) {
            this.begin = begin;
        }

        public void setEnd(Date end) {
            this.end = end;
            this.session = new Date(begin.getYear(), begin.getMonth(), begin.getDay(), end.getHours() - begin.getHours(), end.getMinutes() - begin.getMinutes(), 0);
        }

        public Date getBegin() {
            return begin;
        }

        public Date getEnd() {
            return end;
        }

        public Date getSession() {
            return session;
        }

    }
}

