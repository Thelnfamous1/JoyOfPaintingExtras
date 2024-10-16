package me.Thelnfamous1.joyofpaintingextras.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xerca.xercapaint.common.entity.EntityEasel;

@Mixin(EntityEasel.class)
public abstract class EntityEaselMixin extends Entity {

    public EntityEaselMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
    private void post_init_0(EntityType entityCanvasEntityType, Level world, CallbackInfo ci){
        this.noCulling = true;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
    private void post_init_1(Level world, CallbackInfo ci){
        this.noCulling = true;
    }

    @Inject(method = "<init>(Lnet/minecraftforge/network/PlayMessages$SpawnEntity;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
    private void post_init_2(PlayMessages.SpawnEntity ignoredSpawnEntity, Level world, CallbackInfo ci){
        this.noCulling = true;
    }
}
