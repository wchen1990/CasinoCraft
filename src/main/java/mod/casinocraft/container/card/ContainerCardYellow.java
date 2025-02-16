package mod.casinocraft.container.card;

import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.tileentities.TileEntityMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerCardYellow extends ContainerCasino {

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor **/
    public ContainerCardYellow(int windowID, PlayerInventory playerInventory, TileEntityMachine board) {
        super(CasinoKeeper.CONTAINER_CARD_YELLOW.get(), windowID, playerInventory, board);
    }

    /** Forge Registry Constructor **/
    public ContainerCardYellow(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        super(CasinoKeeper.CONTAINER_CARD_YELLOW.get(), windowID, playerInventory, packetBuffer);
    }




    //----------------------------------------SUPPORT----------------------------------------//

    @Override
    public int getID(){
        return 15;
    }


}