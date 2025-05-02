package com.allthemodiumcraft.withernether.event;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ResourceKey<Level> targetDimension = event.getDimension();
            if (targetDimension.equals(Level.NETHER)) {
                var server = player.server;
                var holder = server.getAdvancements().get(ResourceLocation.parse("withersnether:consumed_seal"));
                if (holder != null) {
                    Advancement advancement = holder.value();
                    AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
                    if (!progress.isDone()) {
                        // Deny portal entry
                        event.setCanceled(true);

                        // Dramatic denial message
                        player.sendSystemMessage(Component.literal("§l§cA burning force rejects you from the portal!").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));

                        // Hint message
                        player.sendSystemMessage(Component.literal("§6Seek out and consume the §eSeal of the Nether§6 to pass.").withStyle(ChatFormatting.GOLD));

                        // Play ominous sound
                        player.level().playSound(
                                null,
                                player.getX(), player.getY(), player.getZ(),
                                SoundEvents.ENDERMAN_STARE,
                                SoundSource.PLAYERS,
                                1.2F,
                                0.5F
                        );

                        // Spawn smoke and portal particles
                        if (player.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(
                                    ParticleTypes.SMOKE,
                                    player.getX(), player.getY() + 1, player.getZ(),
                                    20, 0.6, 0.6, 0.6, 0.03
                            );
                            serverLevel.sendParticles(
                                    ParticleTypes.PORTAL,
                                    player.getX(), player.getY() + 1, player.getZ(),
                                    30, 0.7, 0.7, 0.7, 0.05
                            );
                        }

                        // Brief nausea and slowness effects
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0)); // 3 seconds nausea
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1)); // 3 seconds slowness
                    }
                }
            }
        }
    }
}
