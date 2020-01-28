package mod.casinocraft.container.minigames;

import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.tileentities.minigames.TileEntity2048;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Container2048 extends ContainerCasino {
	
	public Container2048(InventoryPlayer playerInventory, IInventory blockInventory){
        super(blockInventory);
    }
    
    public void addListener(IContainerListener listener){
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileCasino);
    }
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges(){
        super.detectAndSendChanges();
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data){
    	
    }
    
    public boolean canInteractWith(EntityPlayer playerIn){
    	return true;
    }
	
}
