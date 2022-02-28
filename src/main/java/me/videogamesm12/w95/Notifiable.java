package me.videogamesm12.w95;

import net.minecraft.text.Text;

public interface Notifiable
{
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
}
