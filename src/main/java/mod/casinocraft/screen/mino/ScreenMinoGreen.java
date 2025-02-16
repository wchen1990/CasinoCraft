package mod.casinocraft.screen.mino;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.logic.mino.LogicMinoGreen;
import mod.casinocraft.screen.ScreenCasino;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenMinoGreen extends ScreenCasino {   // Mystic Square

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public ScreenMinoGreen(ContainerCasino container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicMinoGreen logic(){
        return (LogicMinoGreen) menu.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClickedSUB(double mouseX, double mouseY, int mouseButton){
        if (mouseButton == 0){
            for(int y = 0; y < 4; y++){
                for(int x = 0; x < 4; x++){
                    if(mouseRect(32 + 48*x, 32 + 48*y, 48, 48, mouseX, mouseY)){
                        if(y > 0) if(logic().grid[x    ][y - 1] == -1) action(0);
                        if(y < 3) if(logic().grid[x    ][y + 1] == -1) action(1);
                        if(x > 0) if(logic().grid[x - 1][y    ] == -1) action(2);
                        if(x < 3) if(logic().grid[x + 1][y    ] == -1) action(3);
                    }
                }
            }
        }
    }




    //----------------------------------------DRAW----------------------------------------//

    protected void drawGuiContainerForegroundLayerSUB(MatrixStack matrixstack, int mouseX, int mouseY){

    }

    protected void drawGuiContainerBackgroundLayerSUB(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY){
        if(logic().turnstate >= 2){
            this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_MYSTICSQUARE);
            for(int y = 0; y < 4; y++) {
                for(int x = 0; x < 4; x++) {
                    int i = logic().grid[x][y] % 20;
                    if(i != -1) {
                        this.blit(matrixstack, leftPos + 32 + 48*x, topPos + 32 + 48*y, (i % 4)*48, (i / 4)*48, 48, 48);
                    }
                }
            }
        }
    }

    protected void drawGuiContainerBackgroundLayerGUI(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY) {

    }




    //----------------------------------------CUSTOM----------------------------------------//

    // ...




    //----------------------------------------SUPPORT----------------------------------------//

    protected String getGameName() {
        return "mystic_square";
    }

}
