package me.Thelnfamous1.joyofpaintingextras.network;

import java.util.Arrays;
import net.minecraft.network.FriendlyByteBuf;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.entity.EntityEasel;

public class CustomCanvasMiniUpdatePacket {
    private int[] pixels;
    private CanvasType canvasType;
    private String name;
    private int version;
    private int easelId;
    private boolean messageIsValid;
    private int width;
    private int height;

    public CustomCanvasMiniUpdatePacket(int[] pixels, String name, int version, EntityEasel easel, CanvasType canvasType, int width, int height) {
        this.name = name;
        this.version = version;
        this.canvasType = canvasType;
        this.width = width;
        this.height = height;
        int area = width * height;
        this.pixels = Arrays.copyOfRange(pixels, 0, area);
        if (easel == null) {
            this.easelId = -1;
        } else {
            this.easelId = easel.getId();
        }

    }

    public CustomCanvasMiniUpdatePacket() {
        this.messageIsValid = false;
    }

    public static void encode(CustomCanvasMiniUpdatePacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.easelId);
        buf.writeByte(pkt.canvasType.ordinal());
        buf.writeInt(pkt.width);
        buf.writeInt(pkt.height);
        buf.writeInt(pkt.version);
        buf.writeUtf(pkt.name);
        buf.writeVarIntArray(pkt.pixels);
    }

    public static CustomCanvasMiniUpdatePacket decode(FriendlyByteBuf buf) {
        CustomCanvasMiniUpdatePacket result = new CustomCanvasMiniUpdatePacket();

        try {
            result.easelId = buf.readInt();
            result.canvasType = CanvasType.fromByte(buf.readByte());
            result.width = buf.readInt();
            result.height = buf.readInt();
            int area = result.width * result.height;
            result.version = buf.readInt();
            result.name = buf.readUtf(64);
            result.pixels = buf.readVarIntArray(area);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Exception while reading CustomCanvasMiniUpdatePacket: " + e);
            return null;
        }

        result.messageIsValid = true;
        return result;
    }

    public int[] getPixels() {
        return this.pixels;
    }

    public String getName() {
        return this.name;
    }

    public boolean isMessageValid() {
        return this.messageIsValid;
    }

    public int getVersion() {
        return this.version;
    }

    public int getEaselId() {
        return this.easelId;
    }
}