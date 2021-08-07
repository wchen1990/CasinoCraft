package mod.casinocraft.menu.chip;

import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.menu.MenuCasino;
import mod.casinocraft.blockentity.BlockEntityMachine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MenuChipLightGray extends MenuCasino {

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor **/
    public MenuChipLightGray(int windowID, Inventory playerInventory, BlockEntityMachine board) {
        super(CasinoKeeper.CONTAINER_CHIP_LIGHT_GRAY.get(), windowID, playerInventory, board);
    }

    /** Forge Registry Constructor **/
    public MenuChipLightGray(int windowID, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(CasinoKeeper.CONTAINER_CHIP_LIGHT_GRAY.get(), windowID, playerInventory, packetBuffer);
    }




    //----------------------------------------SUPPORT----------------------------------------//

    @Override
    public int getID(){
        return 23;
    }

}