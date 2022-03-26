package me.videogamesm12.w95;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public interface Notifiable
{
    InGameNotifiable ingame = new InGameNotifiable();

    void sendNotification(Text text, NotificationType type);

    enum NotificationType
    {
        ERROR("Title"),
        INFORMATION("Information");

        private String title;

        NotificationType(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return title;
        }
    }

    static InGameNotifiable inGame()
    {
        return ingame;
    }

    class InGameNotifiable implements Notifiable
    {
        @Override
        public void sendNotification(Text text, NotificationType type)
        {
            if (MinecraftClient.getInstance().player != null)
                MinecraftClient.getInstance().player.sendMessage(text, false);
        }
    }
}
