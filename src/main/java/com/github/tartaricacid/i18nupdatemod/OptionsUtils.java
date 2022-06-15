package com.github.tartaricacid.i18nupdatemod;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public final class OptionsUtils {
    private static final String DEFAULT_LANG = "lang:zh_cn";
    private static final String OPTION_PACK_NAME = "file/" + I18nUpdateMod.LANG_PACK_FILE_NAME;
    private static final String DEFAULT_RES = "resourcePacks:[\"" + OPTION_PACK_NAME + "\"]";
    private static final String INIT_OPTIONS = DEFAULT_LANG + "\n" + DEFAULT_RES;
    private static final Gson GSON = new Gson();


    public static void createInitFile(File optionsFile) throws IOException {
        if (!optionsFile.exists()) {
            FileUtils.write(optionsFile, INIT_OPTIONS, StandardCharsets.UTF_8);
        }
    }

    public static void changeFile(File optionsFile) throws IOException {
        List<String> options = FileUtils.readLines(optionsFile, StandardCharsets.UTF_8);
        List<String> output = Lists.newArrayList();
        boolean hasLang = false;
        boolean hasResourcePacks = false;

        for (String line : options) {
            if (line.startsWith("lang:")) {
                line = DEFAULT_LANG;
                hasLang = true;
            }
            if (line.startsWith("resourcePacks:")) {
                String packs = line.substring(14);
                String[] packsItems = GSON.fromJson(packs, String[].class);
                if (Arrays.stream(packsItems).noneMatch(OPTION_PACK_NAME::equals)) {
                    String[] newPacksItem = new String[packsItems.length + 1];
                    newPacksItem[0] = OPTION_PACK_NAME;
                    System.arraycopy(packsItems, 0, newPacksItem, 1, packsItems.length);
                    line = "resourcePacks:" + GSON.toJson(newPacksItem);
                }
                hasResourcePacks = true;
            }
            output.add(line);
        }

        if (!hasLang) {
            output.add(DEFAULT_LANG);
        }
        if (!hasResourcePacks) {
            output.add(DEFAULT_RES);
        }

        FileUtils.writeLines(optionsFile, StandardCharsets.UTF_8.name(), output, "\n");
    }
}
