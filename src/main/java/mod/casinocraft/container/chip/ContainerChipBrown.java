package mod.casinocraft.container.chip;

import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.tileentities.TileEntityMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerChipBrown extends ContainerCasino {

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor **/
    public ContainerChipBrown(int windowID, PlayerInventory playerInventory, TileEntityMachine board) {
        super(CasinoKeeper.CONTAINER_CHIP_BROWN.get(), windowID, playerInventory, board);
    }

    /** Forge Registry Constructor **/
    public ContainerChipBrown(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        super(CasinoKeeper.CONTAINER_CHIP_BROWN.get(), windowID, playerInventory, packetBuffer);
    }




    //----------------------------------------SUPPORT----------------------------------------//

    @Override
    public int getID(){
        return 18;
    }

}
