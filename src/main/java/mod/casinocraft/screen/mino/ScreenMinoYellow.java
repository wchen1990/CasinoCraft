package mod.casinocraft.screen.mino;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.logic.mino.LogicMinoYellow;
import mod.casinocraft.screen.ScreenCasino;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;

public class ScreenMinoYellow extends ScreenCasino {   // SicBo

    private int diceColor = 0;




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public ScreenMinoYellow(ContainerCasino container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
        Random rand = new Random();
        diceColor = rand.nextInt(8);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicMinoYellow logic(){
        return (LogicMinoYellow) menu.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClickedSUB(double mouseX, double mouseY, int mouseButton){
        if(logic().turnstate == 2 && mouseButton == 0){
            for(int y = 0; y < 6; y++){
                for(int x = 0; x < 12; x++){
                    int posX = tableID == 1 ? 32 + 16*x : -128+64 + 32*x;
                    int posY = 32+32*y;
                    int sizeX = tableID == 1 ? 16 : 32;
                    int sizeY = 32;
                    if(mouseRect(posX, posY, sizeX, sizeY, mouseX, mouseY)){
                        action(x + y*12);
                    }
                }
            }
            if(mouseRect( 24, 251-16, 92, 26, mouseX, mouseY) && playerToken >= bet){
                action(-1);
                collectBet();
                playerToken = -1;
            }
            if(mouseRect(140, 251-16, 92, 26, mouseX, mouseY)){
                action(-2);
            }
        }
        if((logic().turnstate == 3 || logic().turnstate == 4) && mouseRect(82, 251-16, 92, 26, mouseX, mouseY)){
            action(-2);
        }
    }




    //----------------------------------------DRAW----------------------------------------//

    protected void drawGuiContainerForegroundLayerSUB(MatrixStack matrixstack, int mouseX, int mouseY){
        if(logic().turnstate >= 4){
            drawFont(matrixstack, logic().hand, 25, -10);
        }
        if(logic().turnstate == 2){
            if(CasinoKeeper.config_timeout.get() - logic().timeout > 0){
                drawFontInvert(matrixstack, "" + (CasinoKeeper.config_timeout.get() - logic().timeout), tableID == 1 ? 256-18 : 336, 4);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayerSUB(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY){
        if(tableID == 1){
            this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_SICBO_MIDDLE);
            this.blit(matrixstack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight); // Background SMALL
        } else {
            this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_SICBO_LEFT);
            this.blit(matrixstack, leftPos-128, topPos, 0, 0, this.imageWidth, this.imageHeight); // Background Left
            this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_SICBO_RIGHT);
            this.blit(matrixstack, leftPos+128, topPos, 0, 0, this.imageWidth, this.imageHeight); // Background Right
        }

        this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_DICE);

        if(logic().turnstate >= 2){
            int color = 0;
            for(int y = 0; y < 6; y++){
                for(int x = 0; x < 12; x++){
                    color = logic().grid[x][y];
                    int posX = tableID == 1 ? 32-8 + 16*x : -128+64 + 32*x;
                    int posY = 32+32*y;
                    if(color != 0)
                        this.blit(matrixstack, leftPos+posX, topPos+posY, 192, 32 * color, 32, 32);
                    if(logic().selector.matches(x, y))
                        this.blit(matrixstack, leftPos+posX, topPos+posY, 224, 32 * (logic().activePlayer+1), 32, 32);
                }
            }
        }

        if(logic().turnstate == 3){
            this.blit(matrixstack, leftPos + logic().dice[0].posX, topPos + logic().dice[0].posY, logic().dice[0].number*32, diceColor*32, 32, 32);
            this.blit(matrixstack, leftPos + logic().dice[1].posX, topPos + logic().dice[1].posY, logic().dice[1].number*32, diceColor*32, 32, 32);
            this.blit(matrixstack, leftPos + logic().dice[2].posX, topPos + logic().dice[2].posY, logic().dice[2].number*32, diceColor*32, 32, 32);
        }
    }

    protected void drawGuiContainerBackgroundLayerGUI(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(CasinoKeeper.TEXTURE_BUTTONS);
        if(logic().turnstate == 2){
            if(playerToken == -1) validateBet();
            if(playerToken >= bet)
                blit(matrixstack, leftPos+24+7,  topPos+251-16,  0, 0, 78, 22); // Button Hit
            blit(matrixstack, leftPos+140+7, topPos+251-16, 78, 0, 78, 22); // Button Stand
        }
        if(logic().turnstate == 3 && logic().dice[0].shiftX == 0 && logic().dice[1].shiftX == 0 && logic().dice[2].shiftX == 0){
            blit(matrixstack, leftPos+89, topPos+251-16, 78, 44, 78, 22); // Button Spin
        }
    }




    //----------------------------------------CUSTOM----------------------------------------//

    // ...




    //----------------------------------------SUPPORT----------------------------------------//

    protected String getGameName() {
        return "sicbo";
    }

}
