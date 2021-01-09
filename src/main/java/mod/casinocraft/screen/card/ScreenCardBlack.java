package mod.casinocraft.screen.card;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.casinocraft.CasinoKeeper;
import mod.casinocraft.container.ContainerCasino;
import mod.casinocraft.logic.card.LogicCardBlack;
import mod.casinocraft.logic.other.LogicDummy;
import mod.casinocraft.screen.ScreenCasino;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenCardBlack extends ScreenCasino {   // Black Jack

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public ScreenCardBlack(ContainerCasino container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
    }




    //----------------------------------------LOGIC----------------------------------------//

    public LogicCardBlack logic(){
        return (LogicCardBlack) CONTAINER.logic();
    }




    //----------------------------------------INPUT----------------------------------------//

    protected void mouseClicked2(double mouseX, double mouseY, int mouseButton){
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        if(logic().turnstate == 2 && mouseRect( 24, 204, 92, 26, mouseX, mouseY)){ action(0); }
        if(logic().turnstate == 2 && mouseRect(140, 204, 92, 26, mouseX, mouseY)){ action(1); }
        if(playerToken >= bet || !CONTAINER.hasToken())
            if(logic().turnstate == 2 && mouseRect( 24, 164, 92, 26, mouseX, mouseY)){ collectBet(); action(2); }
        if(playerToken >= bet)
            if(logic().turnstate == 2 && mouseRect(140, 164, 92, 26, mouseX, mouseY)){ collectBet(); action(3); }
    }

    protected void keyTyped2(int keyCode){

    }




    //----------------------------------------DRAW----------------------------------------//

    protected void drawGuiContainerForegroundLayer2(MatrixStack matrixstack, int mouseX, int mouseY){
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        if(logic().split == 0){
            drawFont(matrixstack, "PLAYER:  "   + logic().value_player1,  24, 24);
            if(logic().turnstate >= 4) drawFont(matrixstack, logic().hand, 80, 190);
        } else {
            drawFont(matrixstack, "PLAYER L:  " + logic().value_player1,  24, 24);
            drawFont(matrixstack, "PLAYER R:  " + logic().value_player2,  24, 40);
            if(logic().turnstate >= 4) drawFont(matrixstack, logic().hand, 18, 190);
        }
        if(logic().turnstate >= 3) drawFont(matrixstack, "DEALER:  " + logic().value_dealer, 24, 56);
    }

    protected void drawGuiContainerBackgroundLayer2(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY){
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        if(logic().turnstate >= 2){
            for(int z = 0; z < logic().cards_player1.size(); z++){ if(logic().cards_player1.get(z).idletimer == 0) drawCard(matrixstack,  24 + 16*z, 100 + 4*z, logic().cards_player1.get(z)); if(logic().split == 1) drawCardBack(matrixstack,  24 + 16*z, 100 + 4*z, 10); }
            for(int z = 0; z < logic().cards_player2.size(); z++){ if(logic().cards_player2.get(z).idletimer == 0) drawCard(matrixstack, 144 + 16*z, 100 + 4*z, logic().cards_player2.get(z)); if(logic().split == 2) drawCardBack(matrixstack, 144 + 16*z, 100 + 4*z, 10); }
            for(int z = 0; z < logic().cards_dealer.size();  z++){ if(logic().cards_dealer.get(z).idletimer == 0)  drawCard(matrixstack, 144 + 16*z,  24 + 4*z, logic().cards_dealer.get(z)); }
        }
    }

    protected void drawGuiContainerBackgroundLayer3(MatrixStack matrixstack, float partialTicks, int mouseX, int mouseY) {
        if(CONTAINER.logic() instanceof LogicDummy){ return; }
        this.minecraft.getTextureManager().bindTexture(CasinoKeeper.TEXTURE_BUTTONS);
        if(logic().turnstate == 2){
            if(playerToken == -1) validateBet();
            blit(matrixstack, guiLeft+24+7,  guiTop+204+2,  0, 0, 78, 22); // Button Hit
            blit(matrixstack, guiLeft+140+7, guiTop+204+2, 78, 0, 78, 22); // Button Stand
            if((logic().cards_player1.get(0).number >= 9 && logic().cards_player1.get(1).number >= 9) || (logic().cards_player1.get(0).number == logic().cards_player1.get(1).number)){
                if(playerToken >= bet || !CONTAINER.hasToken()){
                    blit(matrixstack, guiLeft+24+7, guiTop+204-40+2, 78*2, 0, 78, 22); // Button Split
                }
            }
            if(playerToken >= bet){
                blit(matrixstack, guiLeft+140+7, guiTop+204-40+2, 0, 44, 78, 22); // Button DoubleDown
            }

        }
    }




    //----------------------------------------CUSTOM----------------------------------------//

    // ...




    //----------------------------------------SUPPORT----------------------------------------//

    protected String getGameName() {
        return "black_jack";
    }

}
