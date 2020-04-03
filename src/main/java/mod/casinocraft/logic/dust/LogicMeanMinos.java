package mod.casinocraft.logic.dust;

import mod.casinocraft.logic.LogicBase;
import mod.shared.util.Vector2;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

public class LogicMeanMinos extends LogicBase {

    public boolean active_hold;

    public int[] container_next    = new int[2];
    public int[] container_hold    = new int[2];
    public int[] container_current = new int[2];

    public double time_last;
    public double time_break;
    public int timer;

    public Vector2[] domino = new Vector2[]{new Vector2(-1, -1), new Vector2(-1, -1)};

    public List<Vector2> clear = new ArrayList<Vector2>();

    public int alpha;



    //--------------------CONSTRUCTOR--------------------

    public LogicMeanMinos(){
        super(true, 0, "a_meanminos", 6, 15);
    }



    //--------------------BASIC--------------------

    public void start2(){
        active_hold = true;
        container_next[0] = Domino_Roll();
        container_next[1] = Domino_Roll();
        container_hold[0] = -1;
        container_hold[1] = -1;
        container_current[0] = Domino_Roll();
        container_current[1] = Domino_Roll();
        Domino_Create();
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

    public void actionTouch(int action){
        if(action == 0){ Domino_Drop();         } // UP
        if(action == 1){ Domino_Fall();         } // DOWN
        if(action == 2){ Command_Strafe(true);  } // LEFT
        if(action == 3){ Command_Strafe(false); } // RIGHT
        if(action == 4){ Command_Turn(true);    } // ROTATE LEFT
        if(action == 5){ Command_Turn(false);   } // ROTATE RIGHT
        if(action == 6){ Command_Hold();        } // HOLD
    }

    public void updateMotion(){

    }

    public void updateLogic(){
        timer+=15;
        if(alpha == 255) {
            if(timer > time_last + time_break && turnstate == 2) {
                Domino_Fall();
                time_last = timer;
            }
        } else {
            alpha -= 10;
            if(alpha <= 0) {
                alpha = 255;
                for(int y = 0; y < 15; y++) {
                    for(int x = 0; x < 10; x++) {
                        if(IsCleared(x, y)) {
                            grid[x][y] = -1;
                        }
                    }
                }
                Command_Collapse();
            }
        }
    }

    public void load2(CompoundNBT compound){
        active_hold = compound.getBoolean("activehold");
        container_next[0] = compound.getInt("containernext0");
        container_next[1] = compound.getInt("containernext1");

        container_hold[0] = compound.getInt("containerhold0");
        container_hold[1] = compound.getInt("containerhold1");

        container_current[0] = compound.getInt("containercurrent0");
        container_current[1] = compound.getInt("containercurrent1");

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



    //--------------------CUSTOM--------------------

    private boolean inLine(int x, int y){
        for(Vector2 v : clear) {
            if(v.matches(x, y)) return true;
        }
        return false;
    }

    private void Domino_Drop() {
        int tempPoint = scorePoint;
        while(scorePoint == tempPoint) {
            Domino_Fall();
        }
    }

    private boolean IsCleared(int x, int y) {
        for(int i = 0; i < clear.size(); i++){
            if(clear.get(i).X == x && clear.get(i).Y == y) return true;
        }
        return false;
    }

    private void Command_Collapse() {
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
        Check_Field();
    }

    public void Command_Strafe(boolean totheleft) {
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

    public void Command_Turn(boolean totheleft) {

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

    public void Command_Hold() {
        if(active_hold) {
            active_hold = false;
            if(container_hold[0] == -1) {
                container_hold[0] = container_current[0];
                container_hold[1] = container_current[1];
                container_current[0] = container_next[0];
                container_current[1] = container_next[1];
                container_next[0] = Domino_Roll();
                container_next[1] = Domino_Roll();
            } else {
                int[] temp = new int[2];
                temp[0] = container_hold[0];
                temp[1] = container_hold[1];
                container_hold[0] = container_current[0];
                container_hold[1] = container_current[1];
                container_current[0] = temp[0];
                container_current[1] = temp[1];
            }
            Domino_Create();
        }
    }

    private int Domino_Roll() {
        return RANDOM.nextInt(7);
    }

    private void Domino_Create() {
        domino[0] = new Vector2(2, 1);
        domino[1] = new Vector2(2, 0);
    }

    public void Domino_Fall() {
        if(domino[0].Y < 14 && domino[1].Y < 14) {
            if(grid[domino[0].X][domino[0].Y + 1] == -1 && grid[domino[1].X][domino[1].Y + 1] == -1) {
                domino[0].Y = domino[0].Y + 1;
                domino[1].Y = domino[1].Y + 1;
            } else {
                Domino_Place();
            }
        } else {
            Domino_Place();
        }
    }

    private void Domino_Place() {
        active_hold = true;
        grid[domino[0].X][domino[0].Y] = container_current[0];
        grid[domino[1].X][domino[1].Y] = container_current[1];
        scorePoint += 2 * 2;
        if(domino[1].Y == 0) turnstate = 4;
        container_current[0] = container_next[0];
        container_current[1] = container_next[1];
        container_next[0] = Domino_Roll();
        container_next[1] = Domino_Roll();
        Domino_Create();
        Check_Field();
    }

    List<Vector2> clear_temp = new ArrayList<Vector2>();

    private void Check_Field() {
        int points = 0;
        int bonus = 0;
        clear_temp.clear();
        for(int y = 14; y >= 0; y--) {
            for(int x = 0; x < 6; x++) {
                if(!IsCleared(x, y)) {
                    Pathfinder(x, y);
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

    private void Pathfinder(int x, int y) {
        clear_temp.add(new Vector2(x, y));
        if(y - 1 >=  0 && grid[x][y] != -1 && grid[x][y] == grid[x    ][y - 1] && !IsClearedTemp(x    , y - 1)) Pathfinder(x    , y - 1);
        if(y + 1 <= 14 && grid[x][y] != -1 && grid[x][y] == grid[x    ][y + 1] && !IsClearedTemp(x    , y + 1)) Pathfinder(x    , y + 1);
        if(x - 1 >=  0 && grid[x][y] != -1 && grid[x][y] == grid[x - 1][y    ] && !IsClearedTemp(x - 1, y    )) Pathfinder(x - 1, y    );
        if(x + 1 <=  5 && grid[x][y] != -1 && grid[x][y] == grid[x + 1][y    ] && !IsClearedTemp(x + 1, y    )) Pathfinder(x + 1, y    );
    }

    private boolean IsClearedTemp(int x, int y) {
        for(int i = 0; i < clear_temp.size(); i++){
            if(clear_temp.get(i).X == x && clear_temp.get(i).Y == y) return true;
        }
        return false;
    }

}
