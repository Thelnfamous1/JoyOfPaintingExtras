package me.Thelnfamous1.joyofpaintingextras;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.LogUtils;
import me.Thelnfamous1.joyofpaintingextras.mixin.ItemCanvasAccessor;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import xerca.xercapaint.common.item.ItemCanvas;

@Mod(JOPExtrasMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class JOPExtrasMod {
    public static final String MODID = "joyofpaintingextras";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<ItemCanvas> CUSTOM_CANVAS = ITEMS.register("item_canvas_custom", () -> ItemCanvasAccessor.joyofpaintingextras$new(CustomCanvasType.CUSTOM));

    public static final String NOT_HOLDING_CUSTOM_CANVAS_KEY = Util.makeDescriptionId("commands", new ResourceLocation(MODID, "resize/not_holding_custom_canvas"));
    private static final SimpleCommandExceptionType NOT_HOLDING_CUSTOM_CANVAS = new SimpleCommandExceptionType(Component.translatable(NOT_HOLDING_CUSTOM_CANVAS_KEY));
    public static final String RESIZE_SUCCESS = Util.makeDescriptionId("commands", new ResourceLocation(MODID, "resize/success"));

    public JOPExtrasMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            event.getDispatcher().register(
                    Commands.literal(MODID)
                            .then(Commands.literal("resize")
                                    .then(Commands.argument("width", IntegerArgumentType.integer(1))
                                            .then(Commands.argument("height", IntegerArgumentType.integer(1))
                                                    .executes(context -> {
                                                        ServerPlayer player = context.getSource().getPlayerOrException();
                                                        InteractionHand hand = CustomCanvasType.isCustomCanvas(player.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                                                        ItemStack itemInHand = player.getItemInHand(hand);
                                                        if(CustomCanvasType.isCustomCanvas(itemInHand)){
                                                            int width = IntegerArgumentType.getInteger(context, "width");
                                                            int height = IntegerArgumentType.getInteger(context, "height");
                                                            CustomCanvasType.setCustomWidth(itemInHand, width);
                                                            CustomCanvasType.setCustomHeight(itemInHand, height);
                                                            context.getSource().sendSuccess(Component.translatable(RESIZE_SUCCESS,
                                                                            itemInHand.getDisplayName(),
                                                                            width, height
                                                                            ),
                                                                    false);
                                                            return Command.SINGLE_SUCCESS;
                                                        } else{
                                                            throw NOT_HOLDING_CUSTOM_CANVAS.create();
                                                        }
                                                    })))));
        });
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    }
}
