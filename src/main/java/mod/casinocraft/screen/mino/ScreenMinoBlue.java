package mod.casinocraft.screen.mino;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.logic.LogicBase;
import mod.casinocraft.logic.mino.LogicMinoBlue;
import mod.casinocraft.logic.other.LogicDummy;
import mod.casinocraft.screen.ScreenCasino;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenMinoBlue extends ScreenCasino {   // Memory

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public ScreenMinoBlue(ContainerCasino container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicMinoBlue logic(){
        return (LogicMinoBlue) CONTAINER.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClicked2(double mouseX, double mouseY, int mouseButton){
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        if (mouseButton == 0){
            for(int y = 0; y < 9; y++) {
                for(int x = 0; x < 17; x++) {
                    if(mouseRect(-4 + x*24-24*3, -4+24 + y*24, 24, 24, mouseX, mouseY)){ action(y*17 + x); }
                }
            }
            if(logic().turnstate == 3 && mouseButton == 0){
                if(mouseRect( 24, 204, 92, 26, mouseX, mouseY)){ action(-1); }
                if(mouseRect(140, 204, 92, 26, mouseX, mouseY)){ action(-2); }
            }
        }
    }

    protected void keyTyped2(int keyCode){

    }




    //----------------------------------------DRAW----------------------------------------//

    protected void drawGuiContainerForegroundLayer2(MatrixStack matrixstack, int mouseX, int mouseY){
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        if(logic().tableID == 1) {
            drawFont(matrixstack, "POINTS",           24, 24);
            drawFont(matrixstack, "" + logic().scorePoint, 34, 34);
            drawFont(matrixstack, "LIVES",            204, 24);
            drawFont(matrixstack, "" + logic().scoreLives, 214, 34);
        } else {
            drawFont(matrixstack, "POINTS",           24-76-16, 24);
            drawFont(matrixstack, "" + logic().scorePoint, 34-76-16, 34);
            drawFont(matrixstack, "LIVES",            204+76+16, 24);
            drawFont(matrixstack, "" + logic().scoreLives, 214+76+16, 34);
        }
    }

    protected void drawGuiContainerBackgroundLayer2(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY){
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        if(logic().turnstate >= 2){
            this.minecraft.getTextureManager().bindTexture(CasinoKeeper.TEXTURE_MINOS);
            for(int y = 0; y < 9; y++){
                for(int x = 0; x < 17; x++){
                    if(logic().grid[x][y] != -1){
                        if(logic().positionA.matches(x, y)){
                            drawMino(matrixstack, -76 + 24*x, 20 + 24*y, logic().grid[x][y]+1, 0);
                        } else
                        if(logic().positionB.matches(x, y)){
                            drawMino(matrixstack, -76 + 24*x, 20 + 24*y, logic().grid[x][y]+1, 0);
                        } else {
                            drawMino(matrixstack, -76 + 24*x, 20 + 24*y, 0, 0);
                        }
                    }
                }
            }

        }
    }

    protected void drawGuiContainerBackgroundLayer3(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY) {
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        this.minecraft.getTextureManager().bindTexture(CasinoKeeper.TEXTURE_BUTTONS);
        if(logic().turnstate == 3){
            blit(matrixstack, guiLeft+24+7,  guiTop+204+2,  0, 0, 78, 22); // Button Hit
            blit(matrixstack, guiLeft+140+7, guiTop+204+2, 78, 0, 78, 22); // Button Stand
        }
    }




    //----------------------------------------CUSTOM----------------------------------------//

    // ...




    //----------------------------------------SUPPORT----------------------------------------//

    protected String getGameName() {
        return "memory";
    }

}
