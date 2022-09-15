package me.videogamesm12.poker;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Poker
{
    @Getter
    private static Map<Identifier, EventBus> neighborhoods = new HashMap<>();
    @Getter
    private static Logger logger = LogManager.getLogger("Poker");

    public static EventBus getHouse(Identifier mod)
    {
        if (!neighborhoods.containsKey(mod)) neighborhoods.put(mod, new EventBus());

        return neighborhoods.get(mod);
    }
}
