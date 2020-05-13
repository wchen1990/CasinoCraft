package mod.casinocraft.screen.mino;

import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.logic.LogicBase;
import mod.casinocraft.logic.mino.LogicMinoCyan;
import mod.casinocraft.screen.ScreenCasino;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenMinoCyan extends ScreenCasino {   // Halma

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public ScreenMinoCyan(ContainerCasino container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicMinoCyan logic(){
        return (LogicMinoCyan) CONTAINER.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClicked2(double mouseX, double mouseY, int mouseButton){
        if (mouseButton == 0){
            for(int y = 0; y < 9; y++) {
                for(int x = 0; x < 17; x++) {
                    if(mouseRect(-4 + x*24-24*3, -4+24 + y*24, 24, 24, mouseX, mouseY)){ action(y*17 + x); }
                }
            }
        }
    }

    protected void keyTyped2(int keyCode){

    }




    //----------------------------------------DRAW----------------------------------------//

    protected void drawGuiContainerForegroundLayer2(int mouseX, int mouseY){
        if(logic().tableID == 1) {
            drawFont("POINTS",           24, 24);
            drawFont("" + logic().scorePoint, 34, 34);
        } else {
            drawFont("POINTS",           24-76-16  , 24);
            drawFont("" + logic().scorePoint, 34-76-16  , 34);
        }
    }

    protected void drawGuiContainerBackgroundLayer2(float partialTicks, int mouseX, int mouseY){
        this.minecraft.getTextureManager().bindTexture(CasinoKeeper.TEXTURE_MINOS);
        for(int y = 0; y < 9; y++) {
            for(int x = 0; x < 17; x++) {
                if(logic().grid[x][y] == 0) drawMino(-4-24*3 + 24*x, -4+24 + 24*y, 9, 0);
                if(logic().grid[x][y] == 1) drawMino(-4-24*3 + 24*x, -4+24 + 24*y, 0, 0);
            }
        }
        drawMino(-4-24*3 + 24*logic().selector.X, -4+24 + 24*logic().selector.Y, 3, 0);
    }

    protected void drawGuiContainerBackgroundLayer3(float partialTicks, int mouseX, int mouseY) {

    }




    //----------------------------------------CUSTOM----------------------------------------//

    // ...




    //----------------------------------------SUPPORT----------------------------------------//

    protected String getGameName() {
        return "halma";
    }

}
