package me.Thelnfamous1.joyofpaintingextras.network;

import java.util.Arrays;
import net.minecraft.network.FriendlyByteBuf;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.PaletteUtil;
import xerca.xercapaint.common.entity.EntityEasel;

public class CustomCanvasUpdatePacket {
    private PaletteUtil.CustomColor[] paletteColors;
    private int[] pixels;
    private boolean signed;
    private String title;
    private CanvasType canvasType;
    private int width;
    private int height;
    private String name;
    private int version;
    private int easelId;
    private boolean messageIsValid;

    public CustomCanvasUpdatePacket(int[] pixels, boolean signed, String title, String name, int version, EntityEasel easel, PaletteUtil.CustomColor[] paletteColors, CanvasType canvasType, int width, int height) {
        this.paletteColors = Arrays.copyOfRange(paletteColors, 0, 12);
        this.signed = signed;
        this.title = title;
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

    public CustomCanvasUpdatePacket() {
        this.messageIsValid = false;
    }

    public static void encode(CustomCanvasUpdatePacket pkt, FriendlyByteBuf buf) {
        PaletteUtil.CustomColor[] var2 = pkt.paletteColors;

        for (PaletteUtil.CustomColor color : var2) {
            color.writeToBuffer(buf);
        }

        buf.writeInt(pkt.easelId);
        buf.writeByte(pkt.canvasType.ordinal());
        buf.writeInt(pkt.width);
        buf.writeInt(pkt.height);
        buf.writeInt(pkt.version);
        buf.writeUtf(pkt.name);
        buf.writeUtf(pkt.title);
        buf.writeBoolean(pkt.signed);
        buf.writeVarIntArray(pkt.pixels);
    }

    public static CustomCanvasUpdatePacket decode(FriendlyByteBuf buf) {
        CustomCanvasUpdatePacket result = new CustomCanvasUpdatePacket();

        try {
            result.paletteColors = new PaletteUtil.CustomColor[12];
            int area = 0;

            while(true) {
                if (area >= result.paletteColors.length) {
                    result.easelId = buf.readInt();
                    result.canvasType = CanvasType.fromByte(buf.readByte());
                    result.width = buf.readInt();
                    result.height = buf.readInt();
                    area = result.width * result.height;
                    result.version = buf.readInt();
                    result.name = buf.readUtf(64);
                    result.title = buf.readUtf(32);
                    result.signed = buf.readBoolean();
                    result.pixels = buf.readVarIntArray(area);
                    break;
                }

                result.paletteColors[area] = new PaletteUtil.CustomColor(buf);
                ++area;
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Exception while reading CustomCanvasUpdatePacket: " + e);
            return null;
        }

        result.messageIsValid = true;
        return result;
    }

    public int[] getPixels() {
        return this.pixels;
    }

    public PaletteUtil.CustomColor[] getPaletteColors() {
        return this.paletteColors;
    }

    public boolean getSigned() {
        return this.signed;
    }

    public String getTitle() {
        return this.title;
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