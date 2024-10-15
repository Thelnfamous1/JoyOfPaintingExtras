package me.Thelnfamous1.joyofpaintingextras.network;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import xerca.xercapaint.common.XercaPaint;
import xerca.xercapaint.common.entity.EntityEasel;
import xerca.xercapaint.common.item.ItemCanvas;
import xerca.xercapaint.common.item.ItemPalette;

public class CustomCanvasMiniUpdatePacketHandler {
    public CustomCanvasMiniUpdatePacketHandler() {
    }

    public static void handle(CustomCanvasMiniUpdatePacket message, Supplier<NetworkEvent.Context> ctx) {
        if (!message.isMessageValid()) {
            System.err.println("Packet was invalid");
        } else {
            ServerPlayer sendingPlayer = ctx.get().getSender();
            if (sendingPlayer == null) {
                System.err.println("EntityPlayerMP was null when CustomCanvasMiniUpdatePacket was received");
            } else {
                ctx.get().enqueueWork(() -> processMessage(message, sendingPlayer));
                ctx.get().setPacketHandled(true);
            }
        }
    }

    private static void processMessage(CustomCanvasMiniUpdatePacket msg, ServerPlayer pl) {
        Entity entityEasel = null;
        ItemStack canvas;
        if (msg.getEaselId() > -1) {
            entityEasel = pl.level.getEntity(msg.getEaselId());
            if (entityEasel == null) {
                XercaPaint.LOGGER.error("CustomCanvasMiniUpdatePacketHandler: Easel entity not found! easelId: " + msg.getEaselId());
                return;
            }

            if (!(entityEasel instanceof EntityEasel easel)) {
                XercaPaint.LOGGER.error("CustomCanvasMiniUpdatePacketHandler: Entity found is not an easel! easelId: " + msg.getEaselId());
                return;
            }

            canvas = easel.getItem();
            if (!(canvas.getItem() instanceof ItemCanvas)) {
                XercaPaint.LOGGER.error("CustomCanvasMiniUpdatePacketHandler: Canvas not found inside easel!");
                return;
            }
        } else {
            canvas = pl.getMainHandItem();
            ItemStack palette = pl.getOffhandItem();
            if (canvas.getItem() instanceof ItemPalette) {
                canvas = palette;
            }
        }

        if (!canvas.isEmpty() && canvas.getItem() instanceof ItemCanvas) {
            CompoundTag comp = canvas.getOrCreateTag();
            comp.putIntArray("pixels", msg.getPixels());
            comp.putString("name", msg.getName());
            comp.putInt("v", msg.getVersion());
            comp.putInt("generation", 0);
            if (entityEasel instanceof EntityEasel easel) {
                easel.setItem(canvas, false);
            }

            XercaPaint.LOGGER.debug("Handling canvas update: Name: {} V: {}", msg.getName(), msg.getVersion());
        }

    }
}