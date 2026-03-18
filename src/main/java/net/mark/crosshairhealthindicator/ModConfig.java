package net.mark.crosshairhealthindicator;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = CrosshairHealthIndicatorClient.MOD_ID)
public class ModConfig implements ConfigData {

    public boolean enableMod = true;
    public boolean displayHealthInHearts = false;

    public boolean alwaysShow = false;


    @ConfigEntry.Gui.Excluded
    public static final int TEXT_COLOR_DEFAULT = 0xFFFFFF;
    @ConfigEntry.ColorPicker
    public int textColor = TEXT_COLOR_DEFAULT;

    public boolean preciseHealth = true;
}
