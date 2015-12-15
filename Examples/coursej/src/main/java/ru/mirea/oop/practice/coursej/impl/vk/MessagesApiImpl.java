package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.LongPollData;

import java.io.IOException;

final class MessagesApiImpl implements MessagesApi {
    private final Messages inter;

    MessagesApiImpl(Messages inter) {
        this.inter = inter;
    }

    @Override
    public synchronized LongPollData getLongPollServer(Boolean useSsl, Boolean needPts) throws IOException {
        Call<Result<LongPollData>> call = inter.getLongPollServer(useSsl, needPts);
        return Result.call(call);
    }

    @Override
    public synchronized int send(Long idUser,
                    String domain,
                    Integer idChat,
                    String idUsers,
                    String message,
                    Integer guid,
                    Float latitude,
                    Float longitude,
                    String attachment,
                    String forwardMessages,
                    Integer isSticker) throws IOException {
        Call<Result<Integer>> call = inter.send(idUser, domain, idChat, idUsers, message, guid,
                latitude, longitude, attachment, forwardMessages, isSticker);
        return Result.call(call);
    }

    @Override
    public synchronized int setActivity(Long idUser, String type) throws IOException {
        Call<Result<Integer>> call = inter.setActivity(idUser, type);
        return Result.call(call);
    }
}
