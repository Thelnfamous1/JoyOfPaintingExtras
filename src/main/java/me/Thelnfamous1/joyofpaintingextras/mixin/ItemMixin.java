package me.Thelnfamous1.joyofpaintingextras.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow protected abstract boolean allowedIn(CreativeModeTab pCategory);

    @Inject(method = "fillItemCategory", at = @At("HEAD"), cancellable = true)
    private void pre_fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems, CallbackInfo ci){
        if(this.allowedIn(pCategory) && this.joyofpaintingextras$fillCustomItems(pCategory, pItems)){
            ci.cancel();
        }
    }

    @Unique
    protected boolean joyofpaintingextras$fillCustomItems(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        return false;
    }
}
