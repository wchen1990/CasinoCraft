package mod.casinocraft.container.chip;

import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.tileentities.TileEntityMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerChipCyan extends ContainerCasino {

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor **/
    public ContainerChipCyan(int windowID, PlayerInventory playerInventory, TileEntityMachine board) {
        super(CasinoKeeper.CONTAINER_CHIP_CYAN.get(), windowID, playerInventory, board);
    }

    /** Forge Registry Constructor **/
    public ContainerChipCyan(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        super(CasinoKeeper.CONTAINER_CHIP_CYAN.get(), windowID, playerInventory, packetBuffer);
    }




    //----------------------------------------SUPPORT----------------------------------------//

    @Override
    public int getID(){
        return 19;
    }

}
