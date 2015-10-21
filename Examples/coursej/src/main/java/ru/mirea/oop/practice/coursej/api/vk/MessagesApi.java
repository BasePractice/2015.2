package ru.mirea.oop.practice.coursej.api.vk;


import ru.mirea.oop.practice.coursej.api.vk.entities.LongPollData;

import java.io.IOException;

public interface MessagesApi extends ExternalCall {
    LongPollData getLongPollServer(Boolean useSsl,
                                   Boolean needPts) throws IOException;

    int send(Long idUser,
             String domain,
             Integer idChat,
             String idUsers,
             String message,
             Integer guid,
             Float latitude,
             Float longitude,
             String attachment,
             String forwardMessages,
             Integer isSticker) throws IOException;

    int setActivity(Long idUser,
                    String type) throws IOException;
}
