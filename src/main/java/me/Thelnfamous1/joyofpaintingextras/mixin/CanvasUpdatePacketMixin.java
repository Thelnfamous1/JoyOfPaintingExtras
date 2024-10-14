package me.Thelnfamous1.joyofpaintingextras.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import me.Thelnfamous1.joyofpaintingextras.CanvasDimensionsHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.PaletteUtil;
import xerca.xercapaint.common.entity.EntityEasel;
import xerca.xercapaint.common.packets.CanvasUpdatePacket;

import java.util.Arrays;

@Mixin(value = CanvasUpdatePacket.class, remap = false)
public abstract class CanvasUpdatePacketMixin implements CanvasDimensionsHolder {

    @Shadow private int[] pixels;
    @Shadow private CanvasType canvasType;
    @Unique
    private int joyofpixelsextras$customWidth;
    @Unique
    private int joyofpixelsextras$customHeight;

    @Inject(method = "<init>([IZLjava/lang/String;Ljava/lang/String;ILxerca/xercapaint/common/entity/EntityEasel;[Lxerca/xercapaint/common/PaletteUtil$CustomColor;Lxerca/xercapaint/common/CanvasType;)V", at = @At("TAIL"))
    private void post_init(int[] pixels, boolean signed, String title, String name, int version, EntityEasel easel, PaletteUtil.CustomColor[] paletteColors, CanvasType canvasType, CallbackInfo ci){
        if(canvasType == CustomCanvasType.CUSTOM){
            ItemStack stack = easel.getItem();
            int customWidth = CustomCanvasType.getCustomWidth(stack);
            int customHeight = CustomCanvasType.getCustomHeight(stack);
            this.joyofpixelsextras$customWidth = customWidth;
            this.joyofpixelsextras$customHeight = customHeight;
            int area = customWidth * customHeight;
            this.pixels = Arrays.copyOfRange(pixels, 0, area);
        }
    }

    @Override
    public CanvasType joyofpixelsextras$getCanvasType() {
        return this.canvasType;
    }

    @Override
    public int joyofpixelsextras$getCustomHeight() {
        return this.joyofpixelsextras$customHeight;
    }

    @Override
    public int joyofpixelsextras$getCustomWidth() {
        return this.joyofpixelsextras$customWidth;
    }

    @Override
    public void joyofpixelsextras$setCustomWidth(int customWidth) {
        this.joyofpixelsextras$customWidth = customWidth;
    }

    @Override
    public void joyofpixelsextras$setCustomHeight(int customHeight) {
        this.joyofpixelsextras$customHeight = customHeight;
    }

    @Inject(method = "encode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeVarIntArray([I)Lnet/minecraft/network/FriendlyByteBuf;"))
    private static void post_encode(CanvasUpdatePacket pkt, FriendlyByteBuf buf, CallbackInfo ci){
        if(((CanvasDimensionsHolder)pkt).joyofpixelsextras$getCanvasType() == CustomCanvasType.CUSTOM){
            buf.writeInt(((CanvasDimensionsHolder)pkt).joyofpixelsextras$getCustomWidth());
            buf.writeInt(((CanvasDimensionsHolder)pkt).joyofpixelsextras$getCustomHeight());
        }
    }

    @ModifyVariable(method = "decode", at = @At(value = "STORE", ordinal = 1))
    private static int modify_pixelsLength_decode(int original, FriendlyByteBuf buf, @Local(ordinal = 0) CanvasUpdatePacket result){
        if(((CanvasDimensionsHolder)result).joyofpixelsextras$getCanvasType() == CustomCanvasType.CUSTOM){
            int customWidth = buf.readInt();
            int customHeight = buf.readInt();
            ((CanvasDimensionsHolder)result).joyofpixelsextras$setCustomWidth(customWidth);
            ((CanvasDimensionsHolder)result).joyofpixelsextras$setCustomHeight(customHeight);
            return customWidth * customHeight;
        }
        return original;
    }
}
