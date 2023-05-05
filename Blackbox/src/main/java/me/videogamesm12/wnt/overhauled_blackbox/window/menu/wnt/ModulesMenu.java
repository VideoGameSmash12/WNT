package me.videogamesm12.wnt.overhauled_blackbox.window.menu.wnt;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.events.ModuleRegisteredEvent;
import me.videogamesm12.wnt.events.ModuleToggledEvent;
import me.videogamesm12.wnt.events.ModuleUnregisteredEvent;
import me.videogamesm12.wnt.module.Module;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModulesMenu extends JMenu
{
    private final List<Class<? extends Module>> inList = new ArrayList<>();

    public ModulesMenu()
    {
        super("Modules");
        //--
        WNT.getModuleManager().getModules().values().forEach(module -> add(new ModuleMenu<>(module)));
        WNT.getEventBus().register(this);
    }

    @Subscribe
    public <T extends Module> void onModuleRegistered(ModuleRegisteredEvent<T> event)
    {
        if (!inList.contains(event.getModuleClass()))
        {
            add(new ModuleMenu<>(WNT.getModuleManager().getModule(event.getModuleClass())));
            inList.add(event.getModuleClass());
        }
    }

    @Subscribe
    public void onModuleUnregistered(ModuleUnregisteredEvent<?> event)
    {
        if (inList.contains(event.getModuleClass()))
        {
            Arrays.stream(getMenuComponents()).filter(menu -> menu instanceof ModuleMenu<?> mmenu &&
                    mmenu.getModuleClass().equals(event.getModuleClass())).forEach(this::remove);
            inList.remove(event.getModuleClass());
        }
    }

    public static class ModuleMenu<T extends Module> extends JMenu
    {
        @Getter
        private final Class<T> moduleClass;
        @Getter
        private final T module;
        private final JCheckBoxMenuItem enabledItem;

        public ModuleMenu(T module)
        {
            super(module.getMeta().name());
            setToolTipText(module.getMeta().description());
            //--
            this.moduleClass = (Class<T>) module.getClass();
            this.module = module;
            this.enabledItem = new JCheckBoxMenuItem("Enabled", module.isEnabled());
            this.enabledItem.addActionListener(action ->
            {
                if (module.isEnabled())
                {
                    module.disable();
                }
                else
                {
                    module.enable();
                }
            });
            //--
            add(enabledItem);
            //--
            WNT.getEventBus().register(this);
        }

        @Subscribe
        public void onModuleToggled(ModuleToggledEvent event)
        {
            if (event.getModuleClass().equals(module.getClass()))
                enabledItem.setSelected(event.isEnabledNow());
        }
    }
}
