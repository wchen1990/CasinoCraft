package mod.casinocraft.logic.card;

import mod.casinocraft.logic.LogicBase;
import mod.casinocraft.util.Card;

import java.util.ArrayList;
import java.util.List;

public class LogicBaccarat extends LogicBase {

    public List<Card> cards_player = new ArrayList<Card>();
    public List<Card> cards_dealer = new ArrayList<Card>();

    public int value_player = 0;
    public int value_dealer = 0;

    public int status = 0;



    //--------------------CONSTRUCTOR--------------------

    public LogicBaccarat(int table){
        super(false, table, "c_baccarat");
    }


    //--------------------BASIC--------------------

    public void start2(){
        cards_player.clear();
        cards_dealer.clear();
        value_player = 0;
        value_dealer = 0;
        status = 0;

        cards_player.add(new Card(RANDOM));
        cards_player.add(new Card(RANDOM));
        cards_dealer.add(new Card(RANDOM));
        cards_dealer.add(new Card(RANDOM));

        cards_player.get(0).setShift(-32,   0,  8);
        cards_player.get(1).setShift(-48,   0, 32);
        cards_dealer.get(0).setShift(  0, -48,  8);
        cards_dealer.get(1).setShift(  0, -48, 32);

        for(int i = 0; i < cards_player.size(); i++){
            if(cards_player.get(i).number <= 9) {
                value_player += cards_player.get(i).number + 1;
            }
        }

        for(int i = 0; i < cards_dealer.size(); i++){
            if(cards_dealer.get(i).number <= 9) {
                value_dealer += cards_dealer.get(i).number + 1;
            }
        }

        value_player %= 10;
        value_dealer %= 10;

        if(value_player >= 8 || value_dealer >= 8){
            status = 1;
            Result();
        } else {
            status = 2;
        }
    }

    public void actionTouch(int action){
        if(action == 0) Add_Card(true);  // HIT
        if(action == 1) Add_Card(false); // STAND
    }

    public void updateMotion(){
        if(cards_player.size() > 0) for (Card card : cards_player) { card.update(); }
        if(cards_dealer.size() > 0) for (Card card : cards_dealer) { card.update(); }
    }

    public void updateLogic(){

    }



    //--------------------CUSTOM--------------------

    private void Add_Card(boolean player) {
        if(player) {
            value_player = 0;
            cards_player.add(new Card(RANDOM, -48, 0));
            for(int i = 0; i < cards_player.size(); i++){
                if(cards_player.get(i).number <= 9) {
                    value_player += cards_player.get(i).number + 1;
                }
            }
            value_player %= 10;
        }

        boolean temp_draw = false;

        if(cards_player.size() == 2 || value_dealer <= 3) { temp_draw = true; } else if(value_dealer == 4 && value_player <= 7) { temp_draw = true; } else if(value_dealer == 5 && value_player >= 4 && value_player <= 7) { temp_draw = true; } else if(value_dealer == 6 && value_player >= 6 && value_player <= 7) { temp_draw = true; }

        if(temp_draw) {
            value_dealer = 0;
            cards_dealer.add(new Card(RANDOM, -48, 0));
            for(int i = 0; i < cards_dealer.size(); i++){
                if(cards_dealer.get(i).number <= 9) {
                    value_dealer += cards_dealer.get(i).number + 1;
                }
            }
            value_dealer %= 10;
        }
        Result();
    }

    private void Result() {
        turnstate = 4;
        if(status == 2) status = 3;
        if(value_dealer <  value_player){ hand = "The Player Wins!"; reward = 2; }
        if(value_dealer >  value_player){ hand = "The House Wins!";  reward = 0; }
        if(value_dealer == value_player){ hand = "DRAW!";            reward = 1; }
    }

}