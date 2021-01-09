package mod.casinocraft.screen.chip;

import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.logic.chip.LogicChipYellow;
import mod.casinocraft.screen.ScreenCasino;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenChipYellow extends ScreenCasino {   // -----

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public ScreenChipYellow(ContainerCasino container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicChipYellow logic(){
        return (LogicChipYellow) CONTAINER.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClicked2(double mouseX, double mouseY, int mouseButton){

    }

    protected void keyTyped2(int keyCode){

    }




    //----------------------------------------DRAW----------------------------------------//

    protected void drawGuiContainerForegroundLayer2(int mouseX, int mouseY){

    }

    protected void drawGuiContainerBackgroundLayer2(float partialTicks, int mouseX, int mouseY){

    }

    protected void drawGuiContainerBackgroundLayer3(float partialTicks, int mouseX, int mouseY) {

    }




    //----------------------------------------CUSTOM----------------------------------------//

    // ...




    //----------------------------------------SUPPORT----------------------------------------//

    protected String getGameName() {
        return "";
    }

}