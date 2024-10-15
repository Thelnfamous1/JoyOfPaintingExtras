package me.Thelnfamous1.joyofpaintingextras.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import me.Thelnfamous1.joyofpaintingextras.JOPExtrasMod;
import me.Thelnfamous1.joyofpaintingextras.network.CustomCanvasMiniUpdatePacket;
import me.Thelnfamous1.joyofpaintingextras.network.CustomCanvasUpdatePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.simple.SimpleChannel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xerca.xercapaint.client.GuiCanvasEdit;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.entity.EntityEasel;
import xerca.xercapaint.common.packets.CanvasMiniUpdatePacket;
import xerca.xercapaint.common.packets.CanvasUpdatePacket;

import java.util.Arrays;
import java.util.UUID;

@Mixin(GuiCanvasEdit.class)
public abstract class GuiCanvasEditMixin {
    @Shadow(remap = false) @Final private CanvasType canvasType;

    @Mutable
    @Shadow(remap = false) @Final private int canvasPixelWidth;

    @Mutable
    @Shadow(remap = false) @Final private int canvasPixelHeight;

    @Mutable
    @Shadow(remap = false) @Final private int canvasWidth;

    @Mutable
    @Shadow(remap = false) @Final private int canvasHeight;

    @Mutable
    @Shadow(remap = false) @Final private int canvasPixelScale;

    @Shadow(remap = false) private int[] pixels;

    @Mutable
    @Shadow(remap = false) @Final private static double[] canvasXs;
    @Mutable
    @Shadow(remap = false) @Final private static double[] canvasYs;

    @Shadow(remap = false) private String name;

    @Shadow(remap = false) @Final private EntityEasel easel;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void modifyCanvasDimensions_init(Player player, CompoundTag canvasTag, CompoundTag paletteTag, Component title, CanvasType canvasType, EntityEasel easel, CallbackInfo ci){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            this.canvasPixelWidth = CustomCanvasType.getCustomWidth(canvasTag);
            this.canvasPixelHeight = CustomCanvasType.getCustomHeight(canvasTag);
            this.canvasPixelScale = CustomCanvasType.getPixelScale(this.canvasPixelWidth, this.canvasPixelHeight);
            int canvasPixelArea = this.canvasPixelHeight * this.canvasPixelWidth;
            this.canvasWidth = this.canvasPixelWidth * this.canvasPixelScale;
            this.canvasHeight = this.canvasPixelHeight * this.canvasPixelScale;
            if (canvasTag != null && !canvasTag.isEmpty()) {
                if(canvasTag.contains("pixels")){
                    int[] nbtPixels = canvasTag.getIntArray("pixels");
                    this.pixels = Arrays.copyOfRange(nbtPixels, 0, canvasPixelArea);
                } else{
                    this.pixels = null;
                }
            }
            if (this.pixels == null) {
                this.pixels = new int[canvasPixelArea];
                Arrays.fill(this.pixels, BasePaletteAccessor.joyofpaintingextras$getbasicColors()[15].rgbVal());
                long secs = System.currentTimeMillis() / 1000L;
                UUID playerUUID = player.getUUID();
                this.name = playerUUID + "_" + secs;
            }
        }
    }

    @WrapOperation(method = "updateCanvas", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/simple/SimpleChannel;sendToServer(Ljava/lang/Object;)V", remap = false, ordinal = 0))
    private void wrap_sendCanvasUpdatePacket_updateCanvas(SimpleChannel instance, Object message, Operation<Void> original){
        if(this.canvasType == CustomCanvasType.CUSTOM && message instanceof CanvasUpdatePacket pack){
            original.call(JOPExtrasMod.NETWORK_HANDLER, new CustomCanvasUpdatePacket(this.pixels, pack.getSigned(), pack.getTitle(), pack.getName(), pack.getVersion(), this.easel, pack.getPaletteColors(), this.canvasType, this.canvasPixelWidth, this.canvasPixelHeight));
        } else{
            original.call(instance, message);
        }
    }

    @WrapOperation(method = "updateCanvas", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/simple/SimpleChannel;sendToServer(Ljava/lang/Object;)V", remap = false, ordinal = 3))
    private void wrap_sendCanvasMiniUpdatePacket_updateCanvas(SimpleChannel instance, Object message, Operation<Void> original){
        if(this.canvasType == CustomCanvasType.CUSTOM && message instanceof CanvasMiniUpdatePacket pack){
            original.call(JOPExtrasMod.NETWORK_HANDLER, new CustomCanvasMiniUpdatePacket(this.pixels, pack.getName(), pack.getVersion(), this.easel, this.canvasType, this.canvasPixelWidth, this.canvasPixelHeight));
        } else{
            original.call(instance, message);
        }
    }

    static {
        int values = CanvasType.values().length;
        assert values > 4;
        canvasXs = new double[values]; // Added 1 for the CUSTOM CanvasType
        Arrays.fill(canvasXs, -1000.0D);
        canvasYs = new double[values]; // Added 1 for the CUSTOM CanvasType
        Arrays.fill(canvasYs, -1000.0D);
    }

}
