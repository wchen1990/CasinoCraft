package mod.casinocraft.container.mino;

import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.tileentities.TileEntityBoard;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerMinoLightBlue extends ContainerCasino {

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor **/
    public ContainerMinoLightBlue(int windowID, PlayerInventory playerInventory, TileEntityBoard board) {
        super(CasinoKeeper.CONTAINER_MINO_LIGHT_BLUE.get(), windowID, playerInventory, board);
    }

    /** Forge Registry Constructor **/
    public ContainerMinoLightBlue(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        super(CasinoKeeper.CONTAINER_MINO_LIGHT_BLUE.get(), windowID, playerInventory, packetBuffer);
    }




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    @Override
    public int getID(){
        return 38;
    }

}