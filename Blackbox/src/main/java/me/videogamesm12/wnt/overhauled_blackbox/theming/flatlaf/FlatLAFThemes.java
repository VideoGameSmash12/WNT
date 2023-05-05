package me.videogamesm12.wnt.overhauled_blackbox.theming.flatlaf;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.overhauled_blackbox.theming.ITheme;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public enum FlatLAFThemes implements ITheme
{
    ARC_DARK("Arc Dark", FLThemeType.INTELLIJ, FlatArcDarkIJTheme.class, true),
    ARC_DARK_HC("Arc Dark Contrast", "A variant of Arc Dark with better text box contrast.", FLThemeType.INTELLIJ, FlatArcDarkIJTheme.class, true),
    CARBON("Carbon", FLThemeType.INTELLIJ, FlatCarbonIJTheme.class, true),
    COBALT_2("Cobalt 2", FLThemeType.INTELLIJ, FlatCobalt2IJTheme.class, true),
    DARK("Material Darker", "A nice flat dark theme.", FLThemeType.INTELLIJ, FlatMaterialDarkerIJTheme.class, true),
    DARK_HC("Material Darker Contrast", "A variant of Material Darker with better text box contrast.", FLThemeType.INTELLIJ, FlatMaterialDarkerContrastIJTheme.class, true),
    LIGHT("Material Lighter", FLThemeType.INTELLIJ, FlatMaterialLighterIJTheme.class, true),
    LIGHT_HC("Material Lighter Contrast", "A variant of Material Lighter with better text box contrast.", FLThemeType.INTELLIJ, FlatMaterialLighterContrastIJTheme.class, true),
    DEEP_OCEAN("Material Deep Ocean", FLThemeType.INTELLIJ, FlatMaterialDeepOceanIJTheme.class, true),
    DEEP_OCEAN_HC("Material Deep Ocean Contrast", "A variant of Material Deep Ocean with better text box contrast.", FLThemeType.INTELLIJ, FlatMaterialDeepOceanContrastIJTheme.class, true),
    NORD("Nord", FLThemeType.INTELLIJ, FlatNordIJTheme.class, true),
    ONE_DARK("One Dark", FLThemeType.INTELLIJ, FlatOneDarkIJTheme.class, true),
    PURPLE("Dark Purple", FLThemeType.INTELLIJ, FlatDarkPurpleIJTheme.class, true),
    //--
    MAC_DARK("Mac OS Dark", "A theme that imitates the Mac OS dark theme.", FLThemeType.FLATLAF, FlatMacDarkLaf.class, true),
    MAC_LIGHT("Mac OS Light", "A theme that imitates the Mac OS light theme.", FLThemeType.FLATLAF, FlatMacLightLaf.class, true);

    private final String themeName;
    private String themeDescription;
    private final FLThemeType type;
    private final Class<? extends FlatLaf> themeClass;
    private final boolean supposedToShow;

    @Override
    public String getInternalName()
    {
        return name();
    }

    @Override
    public String getThemeClass()
    {
        return themeClass.getName();
    }

    @Override
    public void apply()
    {
        try
        {
            themeClass.getMethod("setup").invoke(this);
        }
        catch (Throwable ex)
        {
            WNT.getLogger().error("Failed to apply theme", ex);
            FlatMaterialDarkerContrastIJTheme.setup();
        }
    }

    @Override
    public void showOptionalMessage()
    {

    }
}
