package me.Thelnfamous1.joyofpaintingextras.mixin;

import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import me.Thelnfamous1.joyofpaintingextras.JOPExtrasMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.item.ItemCanvas;

import java.util.List;

@Mixin(ItemCanvas.class)
public abstract class ItemCanvasMixin extends ItemMixin{

    @Shadow(remap = false) public abstract CanvasType getCanvasType();



    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void post_appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci){
        if(this.getCanvasType() == CustomCanvasType.CUSTOM){
            if(CustomCanvasType.isUnmodifiedCustomCanvas(stack)){
                tooltip.add(Component.translatable("canvas.empty").withStyle(ChatFormatting.GRAY));
            }
            int customWidth = CustomCanvasType.getCustomWidth(stack);
            int customHeight = CustomCanvasType.getCustomHeight(stack);
            if(customWidth > 0 && customHeight > 0){
                tooltip.add(Component.translatable("canvas.custom_dimensions", customWidth, customHeight).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    protected boolean joyofpaintingextras$fillCustomItems(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if((Item) (Object) this == JOPExtrasMod.CUSTOM_CANVAS.get()){
            for(int i = 1; i <= 5; i++){
                ItemStack itemStack = new ItemStack(JOPExtrasMod.CUSTOM_CANVAS.get());
                CustomCanvasType.setCustomWidth(itemStack, i * 10);
                CustomCanvasType.setCustomHeight(itemStack, i * 10);
                pItems.add(itemStack);
            }
            return true;
        }
        return false;
    }
}
