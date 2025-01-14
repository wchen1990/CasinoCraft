package mod.casinocraft.logic.chip;

import mod.casinocraft.logic.LogicModule;
import mod.lucky77.util.Vector2;
import net.minecraft.nbt.CompoundNBT;
import java.util.ArrayList;
import java.util.List;
import static mod.casinocraft.util.KeyMap.*;
import static mod.casinocraft.util.SoundMap.SOUND_IMPACT;
import static mod.casinocraft.util.SoundMap.SOUND_TETRIS;

public class LogicChipLightBlue extends LogicModule {   // Puyo Puyo

    public boolean active_hold;
    public int[] container_next    = new int[2];
    public int[] container_hold    = new int[2];
    public int[] container_current = new int[2];
    public double time_last;
    public double time_break;
    public int timer;
    public Vector2[] domino = new Vector2[]{new Vector2(0, 0), new Vector2(0, 0)};
    public List<Vector2> clear = new ArrayList<Vector2>();
    public int alpha;
    private List<Vector2> clear_temp = new ArrayList<Vector2>();




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public LogicChipLightBlue(int tableID){
        super(tableID, 6, 15);
    }




    //----------------------------------------START/RESTART----------------------------------------//

    public void start2(){
        active_hold = true;
        container_next[0] = dominoRoll();
        container_next[1] = dominoRoll();
        container_hold[0] = -1;
        container_hold[1] = -1;
        container_current[0] = dominoRoll();
        container_current[1] = dominoRoll();
        dominoCreate();
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 15; j++) {
                grid[i][j] = -1;
            }
        }
        time_break = 200;
        time_last = 0;
        clear.clear();
        alpha = 255;
    }




    //----------------------------------------COMMAND----------------------------------------//

    public void command(int action){
        if(action == KEY_UP){    commandTurn(true);    } // UP
        if(action == KEY_DOWN){  dominoDrop();                 } // DOWN
        if(action == KEY_LEFT){  commandStrafe(true);  } // LEFT
        if(action == KEY_RIGHT){ commandStrafe(false); } // RIGHT
        if(action == KEY_ENTER){ commandHold();                } // HOLD
    }




    //----------------------------------------UPDATE----------------------------------------//

    public void updateLogic(){
        timer+=15;
        if(alpha == 255) {
            if(timer > time_last + time_break && turnstate == 2) {
                dominoFall();
                time_last = timer;
            }
        } else {
            alpha -= 10;
            if(alpha <= 0) {
                alpha = 255;
                for(int y = 0; y < 15; y++) {
                    for(int x = 0; x < 10; x++) {
                        if(isCleared(x, y)) {
                            grid[x][y] = -1;
                        }
                    }
                }
                setJingle(SOUND_TETRIS);
                commandCollapse();
            }
        }
    }

    public void updateMotion(){

    }




    //----------------------------------------SAVE/LOAD----------------------------------------//

    public void load2(CompoundNBT compound){
        active_hold = compound.getBoolean("activehold");
        container_next[0] = compound.getInt("container_next0");
        container_next[1] = compound.getInt("container_next1");

        container_hold[0] = compound.getInt("container_hold0");
        container_hold[1] = compound.getInt("container_hold1");

        container_current[0] = compound.getInt("container_current0");
        container_current[1] = compound.getInt("container_current1");

        time_last = compound.getDouble("timelast");
        time_break = compound.getDouble("timebreak");
        timer = compound.getInt("timer");

        domino[0].set(compound.getInt("domino0x"), compound.getInt("domino0y"));
        domino[1].set(compound.getInt("domino1x"), compound.getInt("domino1y"));

        alpha = compound.getInt("alpha");
    }

    public CompoundNBT save2(CompoundNBT compound){
        compound.putBoolean("activehold", active_hold);
        compound.putInt("container_next0", container_next[0]);
        compound.putInt("container_next1", container_next[1]);

        compound.putInt("container_hold0", container_hold[0]);
        compound.putInt("container_hold1", container_hold[1]);

        compound.putInt("container_current0", container_current[0]);
        compound.putInt("container_current1", container_current[1]);

        compound.putDouble("timelast", time_last);
        compound.putDouble("timebreak", time_break);
        compound.putInt("timer", timer);
        compound.putInt("domino0x", domino[0].X);
        compound.putInt("domino0y", domino[0].Y);
        compound.putInt("domino1x", domino[1].X);
        compound.putInt("domino1y", domino[1].Y);
        compound.putInt("alpha", alpha);
        return compound;
    }




    //----------------------------------------CUSTOM----------------------------------------//

    public boolean inLine(int x, int y){
        for(Vector2 v : clear) {
            if(v.matches(x, y)) return true;
        }
        return false;
    }

    private void dominoDrop() {
        int tempPoint = scorePoint;
        while(scorePoint == tempPoint) {
            dominoFall();
        }
    }

    private boolean isCleared(int x, int y) {
        for(int i = 0; i < clear.size(); i++){
            if(clear.get(i).X == x && clear.get(i).Y == y) return true;
        }
        return false;
    }

    private void commandCollapse() {
        // Gravity after match found and cleared
        int temp = 0;
        for(int y = 13; y >= 0; y--) {
            for(int x = 0; x < 6; x++) {
                if(grid[x][y] != -1) {
                    temp = 0;
                    while(y + temp + 1 < 15 && grid[x][y + temp + 1] == -1) {
                        temp++;
                    }
                    if(temp != 0) {
                        grid[x][y + temp] = grid[x][y];
                        grid[x][y] = -1;
                    }
                }
            }
        }
        clear.clear();
        alpha = 255;
        checkField();
    }

    private void commandStrafe(boolean totheleft) {
        int dir = 0;
        if(totheleft) {
            dir = -1;
            if(domino[0].X > 0) {
                if(domino[1].X > 0) {
                    if(grid[domino[0].X + dir][domino[0].Y] == -1) {
                        if(grid[domino[1].X + dir][domino[1].Y] == -1) {
                            domino[0].X = domino[0].X + dir;
                            domino[1].X = domino[1].X + dir;
                        }
                    }
                }
            }
        } else {
            dir = 1;
            if(domino[0].X < 5) {
                if(domino[1].X < 5) {
                    if(grid[domino[0].X + dir][domino[0].Y] == -1) {
                        if(grid[domino[1].X + dir][domino[1].Y] == -1) {
                            domino[0].X = domino[0].X + dir;
                            domino[1].X = domino[1].X + dir;
                        }
                    }
                }
            }
        }
    }

    private void commandTurn(boolean totheleft) {

        int pos = 0; // Position of the rotatable Mino
        if(domino[0].Y > domino[1].Y) pos = 1; // Up
        if(domino[0].X > domino[1].X) pos = 2; // Left
        if(domino[0].Y < domino[1].Y) pos = 3; // Down
        if(domino[0].X < domino[1].X) pos = 4; // Right



        if(totheleft) {
            if(pos == 1) { if(domino[0].X - 1 >=  0 && grid[domino[0].X - 1][domino[0].Y] == -1) { domino[1] = new Vector2(domino[0].X - 1, domino[0].Y    ); } }
            if(pos == 2) { if(domino[0].Y + 1 <= 14 && grid[domino[0].X][domino[0].Y + 1] == -1) { domino[1] = new Vector2(domino[0].X    , domino[0].Y + 1); } }
            if(pos == 3) { if(domino[0].X + 1 <=  5 && grid[domino[0].X + 1][domino[0].Y] == -1) { domino[1] = new Vector2(domino[0].X + 1, domino[0].Y    ); } }
            if(pos == 4) { if(domino[0].Y - 1 >=  0 && grid[domino[0].X][domino[0].Y - 1] == -1) { domino[1] = new Vector2(domino[0].X    , domino[0].Y - 1); } }
        } else {
            if(pos == 1) { if(domino[0].X + 1 <=  5 && grid[domino[0].X + 1][domino[0].Y] == -1) { domino[1] = new Vector2(domino[0].X + 1, domino[0].Y    ); } }
            if(pos == 2) { if(domino[0].Y - 1 >=  0 && grid[domino[0].X][domino[0].Y - 1] == -1) { domino[1] = new Vector2(domino[0].X    , domino[0].Y - 1); } }
            if(pos == 3) { if(domino[0].X - 1 >=  0 && grid[domino[0].X - 1][domino[0].Y] == -1) { domino[1] = new Vector2(domino[0].X - 1, domino[0].Y    ); } }
            if(pos == 4) { if(domino[0].Y + 1 <= 14 && grid[domino[0].X][domino[0].Y + 1] == -1) { domino[1] = new Vector2(domino[0].X    , domino[0].Y + 1); } }
        }

    }

    private void commandHold() {
        if(active_hold) {
            active_hold = false;
            if(container_hold[0] == -1) {
                container_hold[0] = container_current[0];
                container_hold[1] = container_current[1];
                container_current[0] = container_next[0];
                container_current[1] = container_next[1];
                container_next[0] = dominoRoll();
                container_next[1] = dominoRoll();
            } else {
                int[] temp = new int[2];
                temp[0] = container_hold[0];
                temp[1] = container_hold[1];
                container_hold[0] = container_current[0];
                container_hold[1] = container_current[1];
                container_current[0] = temp[0];
                container_current[1] = temp[1];
            }
            dominoCreate();
        }
    }

    private int dominoRoll() {
        int r = RANDOM.nextInt(6)+1;
        return r >= 3 ? r+1 : r;
    }

    private void dominoCreate() {
        domino[0] = new Vector2(2, 1);
        domino[1] = new Vector2(2, 0);
        setJingle(SOUND_IMPACT);
    }

    private void dominoFall() {
        if(domino[0].Y < 14 && domino[1].Y < 14) {
            if(grid[domino[0].X][domino[0].Y + 1] == -1 && grid[domino[1].X][domino[1].Y + 1] == -1) {
                domino[0].Y = domino[0].Y + 1;
                domino[1].Y = domino[1].Y + 1;
            } else {
                dominoPlace();
            }
        } else {
            dominoPlace();
        }
    }

    private void dominoPlace() {
        active_hold = true;
        grid[domino[0].X][domino[0].Y] = container_current[0];
        grid[domino[1].X][domino[1].Y] = container_current[1];
        scorePoint += 2 * 2;
        if(domino[1].Y == 0) turnstate = 4;
        container_current[0] = container_next[0];
        container_current[1] = container_next[1];
        container_next[0] = dominoRoll();
        container_next[1] = dominoRoll();
        commandCollapse();
        dominoCreate();
        checkField();
    }

    private void checkField() {
        int points = 0;
        int bonus = 0;
        clear_temp.clear();
        for(int y = 14; y >= 0; y--) {
            for(int x = 0; x < 6; x++) {
                if(!isCleared(x, y)) {
                    pathfinder(x, y);
                    if(clear_temp.size() >= 4) {
                        points += (clear_temp.size() * 10);
                        bonus++;
                        clear.addAll(clear_temp);
                    }
                    clear_temp.clear();
                }
            }
        }

        if(points > 0) {
            alpha -= 5;
            scorePoint += (points * 2 * bonus * (scoreLevel + 1));
            scoreLives++;
            if(scoreLives > (1 + scoreLevel) * 10) {
                scoreLevel++;
                time_break -= (time_break / 10);
            }
            //Command_Collapse();
        }
    }

    private void pathfinder(int x, int y) {
        clear_temp.add(new Vector2(x, y));
        if(y - 1 >=  0 && grid[x][y] != -1 && grid[x][y] == grid[x    ][y - 1] && !isClearedTemp(x    , y - 1)) pathfinder(x    , y - 1);
        if(y + 1 <= 14 && grid[x][y] != -1 && grid[x][y] == grid[x    ][y + 1] && !isClearedTemp(x    , y + 1)) pathfinder(x    , y + 1);
        if(x - 1 >=  0 && grid[x][y] != -1 && grid[x][y] == grid[x - 1][y    ] && !isClearedTemp(x - 1, y    )) pathfinder(x - 1, y    );
        if(x + 1 <=  5 && grid[x][y] != -1 && grid[x][y] == grid[x + 1][y    ] && !isClearedTemp(x + 1, y    )) pathfinder(x + 1, y    );
    }

    private boolean isClearedTemp(int x, int y) {
        for (Vector2 vector2 : clear_temp) {
            if (vector2.X == x && vector2.Y == y) return true;
        }
        return false;
    }




    //----------------------------------------SUPPORT----------------------------------------//

    public boolean hasHighscore(){
        return true;
    }

    public boolean isMultiplayer(){
        return false;
    }

    public int getID(){
        return 22;
    }

}
