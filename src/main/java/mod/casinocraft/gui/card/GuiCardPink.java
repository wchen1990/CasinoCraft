package mod.casinocraft.gui.card;

import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.container.card.ContainerCardBlack;
import mod.casinocraft.container.card.ContainerCardPink;
import mod.casinocraft.gui.GuiCasino;
import mod.casinocraft.logic.card.LogicCardPink;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.io.IOException;

public class GuiCardPink extends GuiCasino {   // -----

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public GuiCardPink(InventoryPlayer playerInv, IInventory furnaceInv, BlockPos pos, World world){
        super(new ContainerCardPink(playerInv, furnaceInv, pos, world), playerInv);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicCardPink logic(){
        return (LogicCardPink) CONTAINER.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClicked2(double mouseX, double mouseY, int mouseButton){

    }

    protected void keyTyped2(char typedChar, int keyCode) throws IOException {

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
