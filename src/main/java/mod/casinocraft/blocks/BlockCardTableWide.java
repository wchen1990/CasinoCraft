package mod.casinocraft.blocks;

import mod.casinocraft.container.ContainerProvider;
import mod.casinocraft.tileentities.TileEntityBoard;
import mod.casinocraft.tileentities.TileEntityCardTableWide;
import mod.shared.blocks.MachinaDoubleWide;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockCardTableWide extends MachinaDoubleWide {

    DyeColor color;

    public static final VoxelShape AABB0 = Block.makeCuboidShape(1, 0, 1, 15, 16, 16);
    public static final VoxelShape AABB1 = Block.makeCuboidShape(0, 0, 1, 15, 16, 15);
    public static final VoxelShape AABB2 = Block.makeCuboidShape(1, 0, 0, 15, 16, 15);
    public static final VoxelShape AABB3 = Block.makeCuboidShape(1, 0, 1, 16, 16, 15);

    /** Contructor with predefined BlockProperty */
    public BlockCardTableWide(String modid, String name, Block block, DyeColor color) {
        super(modid, name, block);
        this.color = color;
    }

    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction enumfacing = state.get(FACING);
        boolean offset = state.get(OFFSET);
        switch(enumfacing) {
            case NORTH: return offset ? AABB1 : AABB3;
            case SOUTH: return offset ? AABB3 : AABB1;
            case EAST:  return offset ? AABB2 : AABB0;
            case WEST:  return offset ? AABB0 : AABB2;
            default:
                return VoxelShapes.fullCube();
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        if(state.get(OFFSET)){
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(OFFSET)){
            return new TileEntityCardTableWide(color, 2);
        }
        return null;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isRemote) {
            return ActionResultType.PASS;
        } else {
            if (!world.isRemote() && player instanceof ServerPlayerEntity) {
                boolean isPrimary = world.getBlockState(pos).get(OFFSET);
                final BlockPos pos2 = offset(state.get(FACING), isPrimary, pos);
                Item item = Items.FLINT;
                TileEntityBoard tileEntity = (TileEntityBoard) world.getTileEntity(pos2);
                if (tileEntity instanceof TileEntityCardTableWide) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new ContainerProvider((TileEntityCardTableWide) tileEntity), buf -> buf.writeBlockPos(pos2));
                }
            }
            return ActionResultType.SUCCESS;
        }
    }

    private BlockPos offset(Direction facing, boolean isPrimary, BlockPos pos){
        if(isPrimary) return pos;
        if(facing == Direction.NORTH) return pos.east();
        if(facing == Direction.SOUTH) return pos.west();
        if(facing == Direction.EAST ) return pos.south();
        if(facing == Direction.WEST ) return pos.north();
        return pos;
    }

}
