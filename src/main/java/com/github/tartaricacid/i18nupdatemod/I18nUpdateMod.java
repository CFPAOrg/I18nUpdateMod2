package com.github.tartaricacid.i18nupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Mod(I18nUpdateMod.MOD_ID)
public class I18nUpdateMod {
    public final static String MOD_ID = "i18nupdatemod";
    public final static Path CACHE_DIR = Paths.get(System.getProperty("user.home"), MOD_ID, "1.18.1");
    public final static Path LANGUAGE_PACK = CACHE_DIR.resolve("Minecraft-Mod-Language-Modpack-1-18.zip");
    public final static String LINK = "http://downloader1.meitangdehulu.com:22943/Minecraft-Mod-Language-Modpack-1-18.zip";
    public final static long MAX_INTERVAL_DAYS = 7;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public I18nUpdateMod() {
        Minecraft.getInstance().options.languageCode = "zh_cn";

        // 检查资源包目录是否存在
        if (!Files.isDirectory(CACHE_DIR)) {
            try {
                Files.createDirectories(CACHE_DIR);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        if (Files.exists(LANGUAGE_PACK)) {
            try {
                long fileTime = Files.getLastModifiedTime(LANGUAGE_PACK).toMillis();
                long nowTime = System.currentTimeMillis();
                if (TimeUnit.MILLISECONDS.toDays(nowTime - fileTime) < MAX_INTERVAL_DAYS) {
                    Minecraft.getInstance().getResourcePackRepository().addPackFinder((RepositorySource) new LanguagePackFinder());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileUtils.copyURLToFile(new URL(LINK), LANGUAGE_PACK.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Minecraft.getInstance().getResourcePackRepository().addPackFinder((RepositorySource) new LanguagePackFinder());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
