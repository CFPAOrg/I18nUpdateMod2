package com.github.tartaricacid.i18nupdatemod.mixin;

import com.github.tartaricacid.i18nupdatemod.I18nUpdateMod;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.client.book.BookContentResourceDirectLoader;
import vazkii.patchouli.client.book.BookContentLoader;
import vazkii.patchouli.common.book.Book;

import java.io.IOException;
import java.io.InputStream;

@Mixin(BookContentResourceDirectLoader.class)
public class MixinBookContentResourceDirectLoader {
    @Inject(at = @At("HEAD"), method = "loadJson", remap = false, cancellable = true)
    public void loadJson(Book book, ResourceLocation location, ResourceLocation fallback, CallbackInfoReturnable<JsonElement> cir)
    {
        I18nUpdateMod.LOGGER.info("loading json from {}.", location);
        try {
            InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
            cir.setReturnValue(BookContentLoader.streamToJson(stream));
        } catch (IOException ignore) {
        }
    }
}
