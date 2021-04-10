package com.github.tartaricacid.i18nupdatemod.mixin;

import java.io.IOException;
import java.io.InputStream;

import com.github.tartaricacid.i18nupdatemod.I18nUpdateMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.client.book.BookContents;



@Mixin(BookContents.class)
public class MixinBookContents {
    @Inject(at = @At("HEAD"), method = "loadJson", cancellable = true, remap = false)
    private void loadJson(ResourceLocation resloc, ResourceLocation fallback,  CallbackInfoReturnable<InputStream> callback) {
        I18nUpdateMod.LOGGER.debug("loading json from {}.",resloc);
        try {
            callback.setReturnValue(Minecraft.getInstance().getResourceManager().getResource(resloc).getInputStream());
        } catch (IOException e) {
            //no-op
        }
    }
}
