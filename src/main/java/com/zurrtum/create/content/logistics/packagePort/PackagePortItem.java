package com.zurrtum.create.content.logistics.packagePort;

import com.zurrtum.create.infrastructure.packet.s2c.PackagePortPlacementRequestPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class PackagePortItem extends BlockItem {

    public PackagePortItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    // TODO(26.3): BlockItem.updateCustomBlockEntityTag est devenu static -> plus appelée. Logique à rebrancher sur un autre point d\'entrée.
    protected boolean legacyUpdateCustomBlockEntityTag(
        Level world,
        @Nullable Player player,
        BlockPos pos,
        ItemStack p_195943_4_
    ) {
        if (!world.isClientSide() && player instanceof ServerPlayer sp) {
            sp.connection.send(new PackagePortPlacementRequestPacket(pos));
        }
        return BlockItem.updateCustomBlockEntityTag(world, player, pos, p_195943_4_);
    }

}
