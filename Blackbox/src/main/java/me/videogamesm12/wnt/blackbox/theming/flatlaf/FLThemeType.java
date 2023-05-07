package me.videogamesm12.wnt.blackbox.theming.flatlaf;

import com.formdev.flatlaf.FlatLaf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.videogamesm12.wnt.blackbox.theming.IThemeType;

@AllArgsConstructor
@Getter
public enum FLThemeType implements IThemeType
{
    FLATLAF("Built into FlatLAF", 38737),
    INTELLIJ("From Intellij Theme Pack", 18284);

    private final String label;
    private final int id;

    @Override
    public void update()
    {
        FlatLaf.updateUI();
    }
}
