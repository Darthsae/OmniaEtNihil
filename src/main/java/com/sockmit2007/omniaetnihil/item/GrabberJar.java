package com.sockmit2007.omniaetnihil.item;

import java.util.List;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GrabberJar extends Item {
    public GrabberJar(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        if (world.isClientSide || player == null) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);

        if (!state.isEmpty()) {
            ItemStack stack = player.getMainHandItem();
            Integer manaHandler = stack.get(OmniaEtNihil.MANA_DATA_COMPONENT);
            manaHandler += 10;
            stack.set(OmniaEtNihil.MANA_DATA_COMPONENT, manaHandler);
            OmniaEtNihil.LOGGER.info(String.valueOf(manaHandler));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents,
            TooltipFlag pTooltipFlag) {

        pTooltipComponents
                .add(Component.literal("Mana: " + String.valueOf(pStack.get(OmniaEtNihil.MANA_DATA_COMPONENT))));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pStack.get(OmniaEtNihil.MANA_DATA_COMPONENT) <= 0) {
            return false;
        }

        pTarget.hurt(pAttacker.damageSources().generic(),
                (float) pStack.get(OmniaEtNihil.MANA_DATA_COMPONENT) / 100);
        return true;
    }
}
