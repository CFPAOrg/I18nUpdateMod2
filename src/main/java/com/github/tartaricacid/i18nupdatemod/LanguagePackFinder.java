package com.github.tartaricacid.i18nupdatemod;

import net.minecraft.resources.FilePack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;

import java.util.function.Consumer;

import static com.github.tartaricacid.i18nupdatemod.I18nUpdateMod.LANGUAGE_PACK;

public class LanguagePackFinder implements IPackFinder {
    @Override
    public void loadPacks(Consumer<ResourcePackInfo> consumer, ResourcePackInfo.IFactory iFactory) {
        ResourcePackInfo packInfo = ResourcePackInfo.create("Minecraft-Mod-Language-Modpack-1-16.zip",
                true, () -> new FilePack(LANGUAGE_PACK.toFile()),
                iFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.DEFAULT);
        if (packInfo != null) {
            consumer.accept(packInfo);
        }
    }
}
