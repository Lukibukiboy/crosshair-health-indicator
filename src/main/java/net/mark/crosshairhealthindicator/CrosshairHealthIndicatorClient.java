package net.mark.crosshairhealthindicator;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;

public class CrosshairHealthIndicatorClient implements ClientModInitializer {
    public static final String MOD_ID = "crosshair-health-indicator";
    public static ModConfig config;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CROSSHAIR,
                Identifier.fromNamespaceAndPath(MOD_ID, "crosshair"),
                CrosshairHealthIndicatorClient::renderIndicator
        );
    }


    private static void renderIndicator(GuiGraphics graphics, DeltaTracker tracker) {

        Minecraft minecraft = Minecraft.getInstance();
        assert minecraft.player != null;

        if (!shouldRender(minecraft)) return;

        int x = (graphics.guiWidth() - 15) / 2 + 8;
        int y = (graphics.guiHeight() - 15) / 2 + 13;
        int color = 0xFF000000 | config.textColor;

        String text = config.displayHealthInHearts ? getPlayerHeartsAsFormattedString(minecraft.player) : getPlayerHealthAsFormattedString(minecraft.player);

        graphics.drawCenteredString(
                Minecraft.getInstance().font,
                text,
                x, y,
                color
        );
    }

    private static boolean shouldRender(Minecraft minecraft) {
        if (!config.enableMod) return false;
        if (minecraft.player == null) return false;
        if (!minecraft.options.getCameraType().isFirstPerson()) return false;
        if (config.alwaysShow) return true;
            else return minecraft.player.gameMode() == GameType.SURVIVAL || minecraft.player.gameMode() == GameType.ADVENTURE;
    }

    public static String getPlayerHealthAsFormattedString(LocalPlayer player) {
        float health = player.getHealth();

        if (health >= 10) {
            return String.valueOf(Math.round(health));
        }
        return String.valueOf((float) (Math.round(health * 10)) / 10); // strips health down to a single decimal digit

    }

    public static String getPlayerHeartsAsFormattedString(LocalPlayer player) {
        float hearts = player.getHealth() / 2; // / 2 to get playerHealth in hearts

        if (hearts >= 10) {
            return String.valueOf(Math.round(hearts));
        }
        return String.valueOf((float) (Math.round(hearts * 10)) / 10); // strips hearts down to a single decimal digit

    }
}