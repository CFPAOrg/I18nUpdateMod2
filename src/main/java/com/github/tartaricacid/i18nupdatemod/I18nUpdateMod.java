package com.github.tartaricacid.i18nupdatemod;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourcePackList;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Mod(I18nUpdateMod.MOD_ID)
public class I18nUpdateMod {
    public final static String MOD_ID = "i18nupdatemod";
    public final static Path CACHE_DIR = Paths.get(System.getProperty("user.home"), "." + MOD_ID, "1.16.5");
    public final static Path RESOURCE_FOLDER = Paths.get(Minecraft.getInstance().gameDirectory.getPath(), "resourcepacks");
    public final static Path LOCAL_LANGUAGE_PACK = RESOURCE_FOLDER.resolve("Minecraft-Mod-Language-Modpack-1-16.zip");
    public final static Path LANGUAGE_PACK = CACHE_DIR.resolve("Minecraft-Mod-Language-Modpack-1-16.zip");
    public final static Path LANGUAGE_MD5 = CACHE_DIR.resolve("1.16.md5");
    public final static String LINK = "http://downloader1.meitangdehulu.com:22943/Minecraft-Mod-Language-Modpack-1-16.zip";
    public final static String MD5 = "http://downloader1.meitangdehulu.com:22943/1.16.md5";
    public final static long MAX_INTERVAL_DAYS = 7;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static String MD5String = "";

    public I18nUpdateMod() {
        Minecraft.getInstance().options.languageCode = "zh_cn";

        // 检查主资源包目录是否存在
        if (!Files.isDirectory(CACHE_DIR)) {
            try {
                Files.createDirectories(CACHE_DIR);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        //检查游戏下资源包目录
        if (!Files.isDirectory(RESOURCE_FOLDER)) {
            try {
                Files.createDirectories(RESOURCE_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        //尝试加载MD5文件
        try {
            FileUtils.copyURLToFile(new URL(MD5), LANGUAGE_MD5.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Download MD5 failed.");
            return;
        }
        try {
            StringBuilder stringBuffer = new StringBuilder();
            List<String> lines = Files.readAllLines(LANGUAGE_MD5);
            for (String line : lines) {
                stringBuffer.append(line);
                MD5String = stringBuffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        if (Files.exists(LANGUAGE_PACK)) {
            String md5;
            try {
                InputStream is = Files.newInputStream(LANGUAGE_PACK);
                md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is).toUpperCase();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Error when compute md5.");
                return;
            }
            try {
                if (!md5.equals(MD5String)) {
                    FileUtils.copyURLToFile(new URL(LINK), LANGUAGE_PACK.toFile());
                    Files.delete(LOCAL_LANGUAGE_PACK);
                    Files.copy(LANGUAGE_PACK, LOCAL_LANGUAGE_PACK);
                }
            } catch (MalformedURLException e) {
                LOGGER.error("Download Langpack failed.");
                e.printStackTrace();
                return;
            } catch (IOException e) {
                LOGGER.error("Error when copy file.");
                e.printStackTrace();
                return;
            }
            setResourcesRepository();
        } else {
            try {
                FileUtils.copyURLToFile(new URL(LINK), LANGUAGE_PACK.toFile());
                Files.copy(LANGUAGE_PACK, LOCAL_LANGUAGE_PACK);
            } catch (IOException e) {
                LOGGER.error("Download Langpack failed.");
                e.printStackTrace();
                return;
            }
            try {
                setResourcesRepository();
                //Minecraft.getInstance().getResourcePackRepository().addPackFinder(new LanguagePackFinder());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        if(!Files.exists(LOCAL_LANGUAGE_PACK)){
            try {
                Files.copy(LANGUAGE_PACK, LOCAL_LANGUAGE_PACK);
                //InputStream is1 = Files.newInputStream(LOCAL_LANGUAGE_PACK);
                //InputStream is2 = Files.newInputStream(LANGUAGE_PACK);
                //String md51 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is1).toUpperCase();
                //String md52 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is2).toUpperCase();
                //if (!md51.equals(md52)){
                    //Files.delete(LOCAL_LANGUAGE_PACK);
                    //Files.copy(LANGUAGE_PACK, LOCAL_LANGUAGE_PACK);
                //}
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Error when copy file.");
                return;
            }
            try {
                setResourcesRepository();
                //Minecraft.getInstance().getResourcePackRepository().addPackFinder(new LanguagePackFinder());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public static String getMD5(String path) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len;
            MessageDigest md = MessageDigest.getInstance("MD5");
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return "";
        }
        return bi.toString(16);
    }

    public static void setResourcesRepository() {
        Minecraft mc = Minecraft.getInstance();
        GameSettings gameSettings = mc.options;
        // 在gameSetting中加载资源包
        if (!gameSettings.resourcePacks.contains("Minecraft-Mod-Language-Modpack-1-16.zip")) {
            mc.options.resourcePacks.add("Minecraft-Mod-Language-Modpack-1-16.zip");
        } else {
            List<String> packs = new ArrayList<>(10);
            packs.add("Minecraft-Mod-Language-Modpack-1-16.zip"); // 资源包的 index 越小优先级越低(在资源包 gui 中置于更低层)
            packs.addAll(gameSettings.resourcePacks);
            gameSettings.resourcePacks = packs;
        }
        reloadResources();
    }

    public static void reloadResources() {
        Minecraft mc = Minecraft.getInstance();
        // 因为这时候资源包已经加载了，所以需要重新读取，重新加载
        ResourcePackList resourcePackRepository = mc.getResourcePackRepository();
        resourcePackRepository.reload();
    }
}
