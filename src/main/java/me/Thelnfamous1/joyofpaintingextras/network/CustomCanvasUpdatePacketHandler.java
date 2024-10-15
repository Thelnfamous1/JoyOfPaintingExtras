package me.Thelnfamous1.joyofpaintingextras.network;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import xerca.xercapaint.common.PaletteUtil;
import xerca.xercapaint.common.XercaPaint;
import xerca.xercapaint.common.entity.EntityEasel;
import xerca.xercapaint.common.item.ItemCanvas;
import xerca.xercapaint.common.item.ItemPalette;
import xerca.xercapaint.common.item.Items;

public class CustomCanvasUpdatePacketHandler {
    public CustomCanvasUpdatePacketHandler() {
    }

    public static void handle(CustomCanvasUpdatePacket message, Supplier<NetworkEvent.Context> ctx) {
        if (!message.isMessageValid()) {
            System.err.println("Packet was invalid");
        } else {
            ServerPlayer sendingPlayer = ctx.get().getSender();
            if (sendingPlayer == null) {
                System.err.println("EntityPlayerMP was null when CustomCanvasUpdatePacket was received");
            } else {
                ctx.get().enqueueWork(() -> processMessage(message, sendingPlayer));
                ctx.get().setPacketHandled(true);
            }
        }
    }

    private static void processMessage(CustomCanvasUpdatePacket msg, ServerPlayer pl) {
        Entity entityEasel = null;
        ItemStack canvas;
        ItemStack palette;
        if (msg.getEaselId() > -1) {
            entityEasel = pl.level.getEntity(msg.getEaselId());
            if (entityEasel == null) {
                XercaPaint.LOGGER.error("CustomCanvasUpdatePacketHandler: Easel entity not found! easelId: " + msg.getEaselId());
                return;
            }

            if (!(entityEasel instanceof EntityEasel easel)) {
                XercaPaint.LOGGER.error("CustomCanvasUpdatePacketHandler: Entity found is not an easel! easelId: " + msg.getEaselId());
                return;
            }

            canvas = easel.getItem();
            if (!(canvas.getItem() instanceof ItemCanvas)) {
                XercaPaint.LOGGER.error("CustomCanvasUpdatePacketHandler: Canvas not found inside easel!");
                return;
            }

            ItemStack mainHandItem = pl.getMainHandItem();
            ItemStack offHandItem = pl.getOffhandItem();
            if (mainHandItem.getItem() instanceof ItemPalette) {
                palette = mainHandItem;
            } else {
                if (!(offHandItem.getItem() instanceof ItemPalette)) {
                    XercaPaint.LOGGER.error("CustomCanvasUpdatePacketHandler: Palette not found on player's hands!");
                    return;
                }

                palette = offHandItem;
            }
        } else {
            canvas = pl.getMainHandItem();
            palette = pl.getOffhandItem();
            if (canvas.getItem() instanceof ItemPalette) {
                ItemStack temp = canvas;
                canvas = palette;
                palette = temp;
            }
        }

        if (!canvas.isEmpty() && canvas.getItem() instanceof ItemCanvas) {
            CompoundTag comp = canvas.getOrCreateTag();
            comp.putIntArray("pixels", msg.getPixels());
            comp.putString("name", msg.getName());
            comp.putInt("v", msg.getVersion());
            comp.putInt("generation", 0);
            if (msg.getSigned()) {
                comp.putString("author", pl.getName().getString());
                comp.putString("title", msg.getTitle().trim());
                comp.putInt("generation", 1);
            }

            if (!palette.isEmpty() && palette.getItem() == Items.ITEM_PALETTE.get()) {
                CompoundTag paletteComp = palette.getOrCreateTag();
                PaletteUtil.writeCustomColorArrayToNBT(paletteComp, msg.getPaletteColors());
            }

            if (entityEasel instanceof EntityEasel easel) {
                easel.setItem(canvas, false);
                easel.setPainter(null);
            }

            XercaPaint.LOGGER.debug("Handling canvas update: Name: {} V: {}", msg.getName(), msg.getVersion());
        }

    }
}