package mod.casinocraft.screen.chip;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.logic.chip.LogicChipPink;
import mod.casinocraft.screen.ScreenCasino;
import mod.casinocraft.util.Ship;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenChipPink extends ScreenCasino {   // Sokoban

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public ScreenChipPink(ContainerCasino container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicChipPink logic(){
        return (LogicChipPink) menu.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClickedSUB(double mouseX, double mouseY, int mouseButton){

    }




    //----------------------------------------DRAW----------------------------------------//

    protected void drawGuiContainerForegroundLayerSUB(MatrixStack matrixstack, int mouseX, int mouseY){

    }

    protected void drawGuiContainerBackgroundLayerSUB(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY){
        if(logic().turnstate == 2){
            this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_SOKOBAN);
            this.blit(matrixstack, leftPos, tableID, 0, 0, this.imageWidth, this.imageHeight); // Background
        }

        if(logic().turnstate >= 2) {
            if(logic().turnstate == 2){
                this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_FONT_ARCADE);
                for(int x = 0; x < 4; x++){
                    for(int y = 0; y < 5; y++){
                        int number = y * 4 + x + 1;
                        drawNumber(matrixstack, leftPos+34+6 + 48 * x, topPos+12+2 + 34 * y, (number / 10), (number % 10), hasUnlocked(number), logic().mapID == number - 1);
                    }
                }
            }
            if(logic().turnstate >= 3){
                this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_ARCADE);
                for(int x = 0; x < 12; x++){
                    for(int y = 0; y < 15; y++){
                        if(logic().grid[x][y] > 0) drawDigi(matrixstack, 32 + x*16, 8 + y*16, 0, 0);
                    }
                }
                for(Ship e : logic().cross){ drawShip(matrixstack, e, 4, 2, false); }
                for(Ship e : logic().crate){ drawShip(matrixstack, e, 4, 1, false); }
                drawShip(matrixstack, logic().octanom, 2, -1, true);

            }
        }
    }

    protected void drawGuiContainerBackgroundLayerGUI(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY) {

    }




    //----------------------------------------CUSTOM----------------------------------------//

    private boolean hasUnlocked(int index){
        for(int i = 0; i < 20; i++){
            if(logic().scoreHigh[i] == index){
                return true;
            }
        }
        return false;
    }

    private void drawNumber(MatrixStack matrixstack, int x, int y, int left, int right, boolean colored, boolean highlighted){

        int vOffset = 160 + (colored ? 48 : 0) + (highlighted ? 24 : 0);
        blit(matrixstack, x      , y, 16 * left,  vOffset, 16, 24);
        blit(matrixstack, x+16, y, 16 * right, vOffset, 16, 24);
    }




    //----------------------------------------SUPPORT----------------------------------------//

    protected String getGameName() {
        return "sokoban";
    }

}
