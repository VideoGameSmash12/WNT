package me.videogamesm12.wnt.blackbox.window.tool.console;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Getter;
import me.videogamesm12.wnt.blackbox.Blackbox;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.ISession;
import net.minecraft.text.Text;

public class FantasiaTab extends AbstractTab
{
    private final BlackboxSession session;

    public FantasiaTab()
    {
        super();
        session = new BlackboxSession(this);
        //--
        Fantasia.getInstance().getServer().addSession(session);
    }

    @Override
    public boolean shouldDisplay(Text message)
    {
        return false;
    }

    @Override
    public String name()
    {
        return "Fantasia";
    }

    @Override
    public void send(String command)
    {
        final CommandSender sender = session.getSender();

        try
        {
            Fantasia.getServerLogger().info(session.getConnectionIdentifier() + " issued client command '" + command + "'");
            Fantasia.getInstance().getServer().getDispatcher().execute(command, sender);
        }
        catch (CommandSyntaxException ignored)
        {
            showMessage("Unknown command: " + command.split(" ")[0]);
        }
        catch (Throwable ex)
        {
            Fantasia.getServerLogger().error("An error occurred whilst attempting to execute command " + command, ex);
            showMessage("Command error: " + ex.getMessage());
        }
    }

    public static class BlackboxSession implements ISession
    {
        @Getter
        private final FantasiaTab tab;
        @Getter
        private final CommandSender sender;

        public BlackboxSession(FantasiaTab tab)
        {
            this.tab = tab;
            this.sender = new CommandSender(this);
        }

        @Override
        public String getConnectionIdentifier()
        {
            return "Blackbox";
        }

        @Override
        public boolean isConnected()
        {
            return Blackbox.getInstance().getMainWindow() != null;
        }

        @Override
        public void disconnect(boolean quiet)
        {
            // Silently do nothing because you can't disconnect like this
        }

        @Override
        public void sendMessage(String message)
        {
            tab.showMessage(message);
        }
    }
}
