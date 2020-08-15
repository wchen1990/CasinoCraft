package mod.casinocraft.blocks;

import java.util.List;

import mod.casinocraft.CasinoCraft;
import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.system.CasinoPacketHandler;
import mod.casinocraft.tileentities.TileEntityArcade;
import mod.casinocraft.tileentities.TileEntityBoard;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockArcade extends BlockContainer {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool PRIMARY = PropertyBool.create("primary");
	public static final PropertyEnum<EnumModule> MODULE = PropertyEnum.create("module", EnumModule.class);

	final EnumDyeColor color;
	
	public BlockArcade(String name, EnumDyeColor colorIn) {
		super(Material.ANVIL);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setHardness(2);
		this.setResistance(2);
		this.setSoundType(SoundType.ANVIL);
		this.setHarvestLevel("pickaxe", 0);
		this.setTickRandomly(false);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MODULE, EnumModule.OFF).withProperty(PRIMARY, true));
		this.color = colorIn;
	}

    /** ??? */
	public boolean isFullCube(IBlockState state){
        return false;
    }
	
	/** ??? */
	public boolean isOpaqueCube(IBlockState state){
        return false;
    }
	
	/** The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only, LIQUID for vanilla liquids, INVISIBLE to skip all rendering */
    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
	
	/** Called by ItemBlocks after a block is set in the world, to allow post-place logic */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
    	TileEntityBoard te = (TileEntityBoard) worldIn.getTileEntity(pos);
    	worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(PRIMARY, Boolean.valueOf(true)).withProperty(MODULE, EnumModule.OFF));
        if(worldIn.isAirBlock(pos.up())) {
        	worldIn.setBlockState(pos.up(), state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(PRIMARY, Boolean.valueOf(false)).withProperty(MODULE, EnumModule.OFF));
        } else {
     	   worldIn.destroyBlock(pos, true);
        }
    }
    
    /** Spawns this Block's drops into the World as EntityItems. */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune){
 	   boolean isPrimary = state.getValue(PRIMARY);
 	   if(isPrimary) {
 		   if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots){ // do not drop items while restoring blockstates, prevents item dupe
 	           List<ItemStack> drops = getDrops(worldIn, pos, state, fortune); // use the old method until it gets removed, for backward compatibility
 	           chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, worldIn, pos, state, fortune, chance, false, harvesters.get());
 	           for (ItemStack drop : drops){
 	               if (worldIn.rand.nextFloat() <= chance){
 	                   spawnAsEntity(worldIn, pos, drop);
 	               }
 	           }
 	       }
 	   }
    }
    
    /** Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect this block */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player){
 	   boolean isPrimary = state.getValue(PRIMARY);
 	   if(isPrimary) {
 		   worldIn.destroyBlock(pos.up(),  true);
 	   } else {
 		   worldIn.destroyBlock(pos.down(),  true);
 	   }
    }
    
    /** Called when the block is destroyed by an explosion. */
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion){
 	   boolean isPrimary = world.getBlockState(pos).getValue(PRIMARY);
 	   if(isPrimary) {
 		   world.destroyBlock(pos.up(),  true);
 	   } else {
 		   world.destroyBlock(pos.down(),  true);
 	   }
 	   world.setBlockToAir(pos);
        onBlockDestroyedByExplosion(world, pos, explosion);
    }
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if (world.isRemote){
            return true;
        } else {
        	BlockPos pos2 = pos;
        	boolean isPrimary = world.getBlockState(pos).getValue(PRIMARY);
        	Item item = Items.FLINT;
        	if(isPrimary) {
        		//pos2 = pos.up();
        	} else {
        		pos2 = pos.down();
        	}
        	if(world.getTileEntity(pos2) instanceof TileEntityBoard){
        		TileEntityBoard te = (TileEntityBoard) world.getTileEntity(pos2);
                if(te.getStackInSlot(0).isEmpty() || (te.getStackInSlot(0).getItem() == player.getHeldItem(hand).getItem())){
					player.openGui(CasinoCraft.instance, 48, world, pos2.getX(), pos2.getY(), pos2.getZ());
            	} else {
				    if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_WHITE)     player.openGui(CasinoCraft.instance,  0, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_ORANGE)    player.openGui(CasinoCraft.instance,  1, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_MAGENTA)   player.openGui(CasinoCraft.instance,  2, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_LIGHTBLUE) player.openGui(CasinoCraft.instance,  3, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_YELLOW)    player.openGui(CasinoCraft.instance,  4, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_LIME)      player.openGui(CasinoCraft.instance,  5, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_PINK)      player.openGui(CasinoCraft.instance,  6, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_GRAY)      player.openGui(CasinoCraft.instance,  7, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_SILVER)    player.openGui(CasinoCraft.instance,  8, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_CYAN)      player.openGui(CasinoCraft.instance,  9, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_PURPLE)    player.openGui(CasinoCraft.instance, 10, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_BLUE)      player.openGui(CasinoCraft.instance, 11, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_BROWN)     player.openGui(CasinoCraft.instance, 12, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_GREEN)     player.openGui(CasinoCraft.instance, 13, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_RED)       player.openGui(CasinoCraft.instance, 14, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else if(te.inventory.get(1).getItem() == CasinoKeeper.MODULE_CHIP_BLACK)     player.openGui(CasinoCraft.instance, 15, world, pos2.getX(), pos2.getY(), pos2.getZ());
                    else { player.openGui(CasinoCraft.instance, 52, world, pos2.getX(), pos2.getY(), pos2.getZ()); }
            	}
    			
    			te.markDirty();
    		}
            return true;
        }
    }
	
	public TileEntity createNewTileEntity(World worldIn, int meta){
        return meta < 8 ? null : new TileEntityArcade(color, 0);
    }
	
	/** ??? */
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, PRIMARY, MODULE);
    }
	
    /** Convert the given metadata into a BlockState for this Block */
    public IBlockState getStateFromMeta(int meta){
    	EnumFacing enumfacing = EnumFacing.getFront(meta%8);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y){
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(PRIMARY, Boolean.valueOf((meta / 8) > 0)).withProperty(MODULE, EnumModule.OFF);
    }
    
    /** Convert the BlockState into the correct metadata value */
    public int getMetaFromState(IBlockState state){
    	int i = state.getValue(FACING).getIndex();
        if (state.getValue(PRIMARY).booleanValue()){
            i += 8;
        }
        return i;
    }

    public static void setModuleState(World world, BlockPos pos){
        IBlockState iblockstate = world.getBlockState(pos);
        TileEntityBoard tileentity = (TileEntityBoard) world.getTileEntity(pos);
        if (tileentity != null){
            if(tileentity.inventory.get(0).isEmpty()){
                world.setBlockState(pos,      iblockstate.withProperty(                             MODULE, EnumModule.OFF), 3);
                world.setBlockState(pos.up(), iblockstate.withProperty(PRIMARY, false).withProperty(MODULE, EnumModule.OFF), 3);
                tileentity.validate();
                world.setTileEntity(pos, tileentity);
            }
            else {
                world.setBlockState(pos,      iblockstate.withProperty(                             MODULE, EnumModule.byItem(tileentity.inventory.get(1).getItem())), 3);
                world.setBlockState(pos.up(), iblockstate.withProperty(PRIMARY, false).withProperty(MODULE, EnumModule.byItem(tileentity.inventory.get(1).getItem())), 3);
                tileentity.validate();
                world.setTileEntity(pos, tileentity);
            }
        }
    }
	
	public enum EnumModule implements IStringSerializable{
        BLACK    ( 0, "black"),
        RED      ( 1, "red"),
        GREEN    ( 2, "green"),
        BROWN    ( 3, "brown"),
        BLUE     ( 4, "blue"),
        PURPLE   ( 5, "purple"),
        CYAN     ( 6, "cyan"),
        SILVER   ( 7, "silver"),
        GRAY     ( 8, "gray"),
        PINK     ( 9, "pink"),
        LIME     (10, "lime"),
        YELLOW   (11, "yellow"),
        LIGHTBLUE(12, "lightblue"),
        MAGENTA  (13, "magenta"),
        ORANGE   (14, "orange"),
        WHITE    (15, "white"),
        EMPTY    (16, "empty"),
        OFF      (17, "off");
    	
        public final String name;
        public final int meta;

        EnumModule(int meta, String name){
        	this.meta = meta;
            this.name = name;
        }
        
        public int getMetadata(){
            return this.meta;
        }
        
        public String toString(){
            return this.name;
        }
        
        public String getName(){
            return this.name;
        }
        
        public static EnumModule byMetadata(int meta){
        	//if(meta == 0) return EnumModule.EMPTY;
        	//if(meta == 1) return EnumModule.TETRIS;
        	//if(meta == 2) return EnumModule.COLUMNS;
        	//if(meta == 3) return EnumModule.MEANMINOS;
        	//if(meta == 4) return EnumModule.SNAKE;
        	//if(meta == 5) return EnumModule.SOKOBAN;
        	//if(meta == 6) return EnumModule._2048;
        	return EnumModule.EMPTY;
        }
        
        public static EnumModule byItem(Item item){
        	if(item == CasinoKeeper.MODULE_CHIP_BLACK)     return EnumModule.BLACK;
            if(item == CasinoKeeper.MODULE_CHIP_RED)       return EnumModule.RED;
            if(item == CasinoKeeper.MODULE_CHIP_GREEN)     return EnumModule.GREEN;
            if(item == CasinoKeeper.MODULE_CHIP_BROWN)     return EnumModule.BROWN;
            if(item == CasinoKeeper.MODULE_CHIP_BLUE)      return EnumModule.BLUE;
            if(item == CasinoKeeper.MODULE_CHIP_PURPLE)    return EnumModule.PURPLE;
            if(item == CasinoKeeper.MODULE_CHIP_CYAN)      return EnumModule.CYAN;
            if(item == CasinoKeeper.MODULE_CHIP_SILVER)    return EnumModule.SILVER;
            if(item == CasinoKeeper.MODULE_CHIP_GRAY)      return EnumModule.GRAY;
            if(item == CasinoKeeper.MODULE_CHIP_PINK)      return EnumModule.PINK;
            if(item == CasinoKeeper.MODULE_CHIP_LIME)      return EnumModule.LIME;
            if(item == CasinoKeeper.MODULE_CHIP_YELLOW)    return EnumModule.YELLOW;
            if(item == CasinoKeeper.MODULE_CHIP_LIGHTBLUE) return EnumModule.LIGHTBLUE;
            if(item == CasinoKeeper.MODULE_CHIP_MAGENTA)   return EnumModule.MAGENTA;
            if(item == CasinoKeeper.MODULE_CHIP_ORANGE)    return EnumModule.ORANGE;
            if(item == CasinoKeeper.MODULE_CHIP_WHITE)     return EnumModule.WHITE;
        	return EnumModule.EMPTY;
        }
        
    }
	
}
