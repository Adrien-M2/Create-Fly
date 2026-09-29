package com.zurrtum.create.content.logistics.tunnel;

import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.content.kinetics.belt.BeltBlockEntity;
import com.zurrtum.create.content.kinetics.belt.BeltBlockEntity.CasingType;
import com.zurrtum.create.content.kinetics.belt.BeltHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jspecify.annotations.Nullable;

public class BeltTunnelItem extends BlockItem {

    public BeltTunnelItem(Block p_i48527_1_, Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext ctx, BlockState state) {
        Player playerentity = ctx.getPlayer();
        CollisionContext iselectioncontext =
            playerentity == null ? CollisionContext.empty() : CollisionContext.of(playerentity);
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        return (!mustSurvive() || AllBlocks.ANDESITE_TUNNEL.isValidPositionForPlacement(
            state,
            world,
            pos
        )) && world.isUnobstructed(state, pos, iselectioncontext);
    }

    // TODO(26.3): BlockItem.updateCustomBlockEntityTag est devenu static -> plus appelée. Logique à rebrancher sur un autre point d\'entrée.
    protected boolean legacyUpdateCustomBlockEntityTag(
        Level world,
        @Nullable Player p_195943_3_,
        BlockPos pos,
        ItemStack p_195943_4_
    ) {
        BlockState state = world.getBlockState(pos);
        boolean flag = BlockItem.updateCustomBlockEntityTag(world, p_195943_3_, pos, p_195943_4_);
        if (!world.isClientSide()) {
            BeltBlockEntity belt = BeltHelper.getSegmentBE(world, pos.below());
            if (belt != null && belt.casing == CasingType.NONE) {
                belt.setCasingType(state.is(AllBlocks.ANDESITE_TUNNEL) ? CasingType.ANDESITE : CasingType.BRASS);
            }
        }
        return flag;
    }

}
