package mod.casinocraft.menu.mino;

import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.menu.MenuCasino;
import mod.casinocraft.blockentity.BlockEntityMachine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MenuMinoLightBlue extends MenuCasino {

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor **/
    public MenuMinoLightBlue(int windowID, Inventory playerInventory, BlockEntityMachine board) {
        super(CasinoKeeper.CONTAINER_MINO_LIGHT_BLUE.get(), windowID, playerInventory, board);
    }

    /** Forge Registry Constructor **/
    public MenuMinoLightBlue(int windowID, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(CasinoKeeper.CONTAINER_MINO_LIGHT_BLUE.get(), windowID, playerInventory, packetBuffer);
    }




    //----------------------------------------SUPPORT----------------------------------------//

    @Override
    public int getID(){
        return 38;
    }

}