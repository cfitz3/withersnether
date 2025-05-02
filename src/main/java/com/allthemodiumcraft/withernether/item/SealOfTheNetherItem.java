package com.allthemodiumcraft.withernether.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class SealOfTheNetherItem extends Item {

    public SealOfTheNetherItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // If the player is in the Nether, deny use with dramatic effects
        if (level.dimension().equals(Level.NETHER)) {
            if (!level.isClientSide) {
                // Dramatic denial message
                player.sendSystemMessage(Component.literal("A mysterious force repels you from using the Seal here!")
                        .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));

                // Play ominous Nether sound
                level.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT, // You can use another sound if you wish
                    SoundSource.PLAYERS,
                    1.0F,
                    0.7F
                );

                // Spawn smoke particles around the player (server side)
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        player.getX(), player.getY() + 1, player.getZ(),
                        20, 0.5, 0.5, 0.5, 0.02
                    );
                }

                // Brief nausea effect
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0)); // 3 seconds
            }
            return InteractionResultHolder.fail(itemStack);
        }

        // Only consume and award advancement on server side
        if (!level.isClientSide) {
            player.sendSystemMessage(Component.literal("The Seal of the Nether has been consumed!"));
            itemStack.shrink(1); // Reduce the item count by 1

            // Play a sound when consumed
            level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
            );

            // Award advancement
            if (player instanceof ServerPlayer serverPlayer) {
                AdvancementHolder advancement = serverPlayer.server.getAdvancements()
                        .get(ResourceLocation.parse("withersnether:consumed_seal"));
                if (advancement != null) {
                    serverPlayer.getAdvancements().award(advancement, "consume_seal");
                }
            }
        }

        return InteractionResultHolder.success(itemStack);
    }
}
