package com.github.tartaricacid.i18nupdatemod;

import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static com.github.tartaricacid.i18nupdatemod.I18nUpdateMod.LANGUAGE_PACK;

@ParametersAreNonnullByDefault
public class LanguagePackFinder implements RepositorySource {
    @Override
    public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor iFactory) {
        Pack packInfo = Pack.create("Minecraft-Mod-Language-Modpack-1-16.zip",
            true, () -> new FilePackResources(LANGUAGE_PACK.toFile()),
            iFactory, Pack.Position.TOP, PackSource.DEFAULT);
        if (packInfo != null) {
            consumer.accept(packInfo);
        }
    }
}
