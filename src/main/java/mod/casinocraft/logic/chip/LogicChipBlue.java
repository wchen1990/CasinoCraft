package mod.casinocraft.logic.chip;

import mod.casinocraft.logic.LogicBase;
import mod.shared.util.Vector2;
import net.minecraft.nbt.NBTTagCompound;

public class LogicChipBlue extends LogicBase {   // Tetris

    public boolean canHold = false;

    public int container_next = 0;
    public int container_hold = 0;
    public int container_now = 0;
    public int timer_last = 0;
    public int timer_break = 0;
    public int timer = 0;
    public int[] color = new int[7]; // Color of single Tetromino
    public int[] lines = new int[4];
    public int alpha = 0;

    public Vector2[] tetromino = new Vector2[]{new Vector2(0,0), new Vector2(0,0), new Vector2(0,0), new Vector2(0,0)}; // Position of the moving tetromino on the grid




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public LogicChipBlue(int tableID){
        super(tableID, 10, 20);
    }




    //----------------------------------------START/RESTART----------------------------------------//

    public void start2(){
        canHold = true;
        container_next = tetrominoRoll();
        container_hold = -1;
        container_now = tetrominoRoll();
        tetrominoCreate();
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 20; j++) {
                grid[i][j] = -1;
            }
        }
        timer_break = 200;
        timer_last = 0;
        lines[0] = -1;
        lines[1] = -1;
        lines[2] = -1;
        lines[3] = -1;
        alpha = 250;
        timer = 0;
        colorize();
    }




    //----------------------------------------COMMAND----------------------------------------//

    public void command(int action){
        if(action == 0){ commandTurn(true);      } // UP
        if(action == 1){ tetrominoDrop();      } // DOWN
        if(action == 2){ commandStrafe(true);  } // LEFT
        if(action == 3){ commandStrafe(false); } // RIGHT
        if(action == 4){ commandTurn(true);    } // ROTATE LEFT
        if(action == 5){ commandTurn(false);   } // ROTATE RIGHT
        if(action == 6){ commandHold();        } // HOLD
    }




    //----------------------------------------UPDATE----------------------------------------//

    public void updateMotion() {
        timer+=15;
    }

    public void updateLogic(){
        if(alpha == 255) {
            if(timer > timer_last + timer_break - scoreLevel * 5 && turnstate == 2) {
                tetrominoFall();
                timer_last = timer;
            }
        } else {
            alpha -= 10;
            if(alpha <= 0) {
                alpha = 250;
                commandCollapse();
            }
        }
    }




    //----------------------------------------SAVE/LOAD----------------------------------------//

    public void load2(NBTTagCompound compound){
        canHold = compound.getBoolean("canhold");
        container_next = compound.getInteger("container_next");
        container_hold = compound.getInteger("container_hold");
        container_now  = compound.getInteger("container_now");

        timer_last = compound.getInteger("timerlast");
        timer_break = compound.getInteger("timerbreak");
        timer = compound.getInteger("timer");

        tetromino[0].set(compound.getInteger("tetromino0x"), compound.getInteger("tetromino0y"));
        tetromino[1].set(compound.getInteger("tetromino1x"), compound.getInteger("tetromino1y"));
        tetromino[2].set(compound.getInteger("tetromino2x"), compound.getInteger("tetromino2y"));
        tetromino[3].set(compound.getInteger("tetromino3x"), compound.getInteger("tetromino3y"));

        color[0] = compound.getInteger("color0");
        color[1] = compound.getInteger("color1");
        color[2] = compound.getInteger("color2");
        color[3] = compound.getInteger("color3");
        color[4] = compound.getInteger("color4");
        color[5] = compound.getInteger("color5");
        color[6] = compound.getInteger("color6");

        lines[0] = compound.getInteger("lines0");
        lines[1] = compound.getInteger("lines1");
        lines[2] = compound.getInteger("lines2");
        lines[3] = compound.getInteger("lines3");

        alpha = compound.getInteger("alpha");
    }

    public NBTTagCompound save2(NBTTagCompound compound){
        compound.setBoolean("canhold", canHold);
        compound.setInteger("container_next", container_next);
        compound.setInteger("container_last", container_hold);
        compound.setInteger("container_now", container_now);
        compound.setInteger("timerlast", timer_last);
        compound.setInteger("timerbreak", timer_break);
        compound.setInteger("timer", timer);
        compound.setInteger("tetromino0x", tetromino[0].X);
        compound.setInteger("tetromino0y", tetromino[0].Y);
        compound.setInteger("tetromino1x", tetromino[1].X);
        compound.setInteger("tetromino1y", tetromino[1].Y);
        compound.setInteger("tetromino2x", tetromino[2].X);
        compound.setInteger("tetromino2y", tetromino[2].Y);
        compound.setInteger("tetromino3x", tetromino[3].X);
        compound.setInteger("tetromino3y", tetromino[3].Y);
        compound.setInteger("alpha", alpha);
        compound.setInteger("color0", color[0]);
        compound.setInteger("color1", color[1]);
        compound.setInteger("color2", color[2]);
        compound.setInteger("color3", color[3]);
        compound.setInteger("color4", color[4]);
        compound.setInteger("color5", color[5]);
        compound.setInteger("color6", color[6]);
        compound.setInteger("lines0", lines[0]);
        compound.setInteger("lines1", lines[1]);
        compound.setInteger("lines2", lines[2]);
        compound.setInteger("lines3", lines[3]);
        return compound;
    }




    //----------------------------------------CUSTOM----------------------------------------//

    public boolean inLine(int index){
        if(index     == lines[0]) return true;
        if(index     == lines[1]) return true;
        if(index     == lines[2]) return true;
        return index == lines[3];
    }

    private void colorize() {
        boolean[] used = new boolean[10];
        int index = 0;
        for(int i = 0; i < 10; i++) {
            used[i] = false;
            index++;
        }

        color[0] = -1;
        color[1] = -1;
        color[2] = -1;
        color[3] = -1;
        color[4] = -1;
        color[5] = -1;
        color[6] = -1;

        int count = 0;
        while(count < index && count < 7) {
            int r = RANDOM.nextInt(7)+1;
            if(!used[r]) {
                color[count] = r;
                used[r] = true;
                count++;
            }
        }
    }

    private void commandCollapse() {
        for(int z = 0; z < 4; z++){
            if(lines[z] != -1) {
                for(int i = lines[z]; i > 0; i--) {
                    for(int x = 0; x < 10; x++){
                        grid[x][i] = grid[x][i-1];
                    }
                }
            }
            lines[0] = -1;
        }
        alpha = 255;
    }

    private void commandStrafe(boolean totheleft) {
        int dir = 0;
        if(totheleft) {
            dir = -1;
            if(tetromino[0].X > 0) {
                if(tetromino[1].X > 0) {
                    if(tetromino[2].X > 0) {
                        if(tetromino[3].X > 0) {
                            if(grid[tetromino[0].X + dir][tetromino[0].Y] == -1) {
                                if(grid[tetromino[1].X + dir][tetromino[1].Y] == -1) {
                                    if(grid[tetromino[2].X + dir][tetromino[2].Y] == -1) {
                                        if(grid[tetromino[3].X + dir][tetromino[3].Y] == -1) {
                                            tetromino[0].add(dir, 0);
                                            tetromino[1].add(dir, 0);
                                            tetromino[2].add(dir, 0);
                                            tetromino[3].add(dir, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            dir = 1;
            if(tetromino[0].X < 9) {
                if(tetromino[1].X < 9) {
                    if(tetromino[2].X < 9) {
                        if(tetromino[3].X < 9) {
                            if(grid[tetromino[0].X + dir][tetromino[0].Y] == -1) {
                                if(grid[tetromino[1].X + dir][tetromino[1].Y] == -1) {
                                    if(grid[tetromino[2].X + dir][tetromino[2].Y] == -1) {
                                        if(grid[tetromino[3].X + dir][tetromino[3].Y] == -1) {
                                            tetromino[0].add(dir, 0);
                                            tetromino[1].add(dir, 0);
                                            tetromino[2].add(dir, 0);
                                            tetromino[3].add(dir, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void commandTurn(boolean totheleft) {
        Vector2 tempV1 = new Vector2(tetromino[0].X - tetromino[1].X, tetromino[0].Y - tetromino[1].Y);
        Vector2 tempV2 = new Vector2(tetromino[0].X - tetromino[2].X, tetromino[0].Y - tetromino[2].Y);
        Vector2 tempV3 = new Vector2(tetromino[0].X - tetromino[3].X, tetromino[0].Y - tetromino[3].Y);
        if(totheleft) {
            if(tempV1.X == 0 && tempV1.Y  < 0) { tempV1 = new Vector2(tetromino[0].X + tempV1.Y, tetromino[0].Y           ); } else
            if(tempV1.X == 0 && tempV1.Y  > 0) { tempV1 = new Vector2(tetromino[0].X + tempV1.Y, tetromino[0].Y           ); } else
            if(tempV1.X  < 0 && tempV1.Y == 0) { tempV1 = new Vector2(tetromino[0].X,            tetromino[0].Y - tempV1.X); } else
            if(tempV1.X  > 0 && tempV1.Y == 0) { tempV1 = new Vector2(tetromino[0].X,            tetromino[0].Y - tempV1.X); } else
            if(tempV1.X  < 0 && tempV1.Y  < 0) { tempV1 = new Vector2(tetromino[0].X + tempV1.Y, tetromino[0].Y - tempV1.X); } else
            if(tempV1.X  < 0 && tempV1.Y  > 0) { tempV1 = new Vector2(tetromino[0].X + tempV1.Y, tetromino[0].Y - tempV1.X); } else
            if(tempV1.X  > 0 && tempV1.Y  < 0) { tempV1 = new Vector2(tetromino[0].X + tempV1.Y, tetromino[0].Y - tempV1.X); } else
            if(tempV1.X  > 0 && tempV1.Y  > 0) { tempV1 = new Vector2(tetromino[0].X + tempV1.Y, tetromino[0].Y - tempV1.X); }

            if(tempV2.X == 0 && tempV2.Y  < 0) { tempV2 = new Vector2(tetromino[0].X + tempV2.Y, tetromino[0].Y           ); } else
            if(tempV2.X == 0 && tempV2.Y  > 0) { tempV2 = new Vector2(tetromino[0].X + tempV2.Y, tetromino[0].Y           ); } else
            if(tempV2.X  < 0 && tempV2.Y == 0) { tempV2 = new Vector2(tetromino[0].X,            tetromino[0].Y - tempV2.X); } else
            if(tempV2.X  > 0 && tempV2.Y == 0) { tempV2 = new Vector2(tetromino[0].X,            tetromino[0].Y - tempV2.X); } else
            if(tempV2.X  < 0 && tempV2.Y  < 0) { tempV2 = new Vector2(tetromino[0].X + tempV2.Y, tetromino[0].Y - tempV2.X); } else
            if(tempV2.X  < 0 && tempV2.Y  > 0) { tempV2 = new Vector2(tetromino[0].X + tempV2.Y, tetromino[0].Y - tempV2.X); } else
            if(tempV2.X  > 0 && tempV2.Y  < 0) { tempV2 = new Vector2(tetromino[0].X + tempV2.Y, tetromino[0].Y - tempV2.X); } else
            if(tempV2.X  > 0 && tempV2.Y  > 0) { tempV2 = new Vector2(tetromino[0].X + tempV2.Y, tetromino[0].Y - tempV2.X); }

            if(tempV3.X == 0 && tempV3.Y  < 0) { tempV3 = new Vector2(tetromino[0].X + tempV3.Y, tetromino[0].Y           ); } else
            if(tempV3.X == 0 && tempV3.Y  > 0) { tempV3 = new Vector2(tetromino[0].X + tempV3.Y, tetromino[0].Y           ); } else
            if(tempV3.X  < 0 && tempV3.Y == 0) { tempV3 = new Vector2(tetromino[0].X,            tetromino[0].Y - tempV3.X); } else
            if(tempV3.X  > 0 && tempV3.Y == 0) { tempV3 = new Vector2(tetromino[0].X,            tetromino[0].Y - tempV3.X); } else
            if(tempV3.X  < 0 && tempV3.Y  < 0) { tempV3 = new Vector2(tetromino[0].X + tempV3.Y, tetromino[0].Y - tempV3.X); } else
            if(tempV3.X  < 0 && tempV3.Y  > 0) { tempV3 = new Vector2(tetromino[0].X + tempV3.Y, tetromino[0].Y - tempV3.X); } else
            if(tempV3.X  > 0 && tempV3.Y  < 0) { tempV3 = new Vector2(tetromino[0].X + tempV3.Y, tetromino[0].Y - tempV3.X); } else
            if(tempV3.X  > 0 && tempV3.Y  > 0) { tempV3 = new Vector2(tetromino[0].X + tempV3.Y, tetromino[0].Y - tempV3.X); }
        } else {
            if(tempV1.X == 0 && tempV1.Y  < 0) { tempV1 = new Vector2(tetromino[0].X - tempV1.Y, tetromino[0].Y           ); } else
            if(tempV1.X == 0 && tempV1.Y  > 0) { tempV1 = new Vector2(tetromino[0].X - tempV1.Y, tetromino[0].Y           ); } else
            if(tempV1.X  < 0 && tempV1.Y == 0) { tempV1 = new Vector2(tetromino[0].X,            tetromino[0].Y + tempV1.X); } else
            if(tempV1.X  > 0 && tempV1.Y == 0) { tempV1 = new Vector2(tetromino[0].X,            tetromino[0].Y + tempV1.X); } else
            if(tempV1.X  < 0 && tempV1.Y  < 0) { tempV1 = new Vector2(tetromino[0].X - tempV1.Y, tetromino[0].Y + tempV1.X); } else
            if(tempV1.X  < 0 && tempV1.Y  > 0) { tempV1 = new Vector2(tetromino[0].X - tempV1.Y, tetromino[0].Y + tempV1.X); } else
            if(tempV1.X  > 0 && tempV1.Y  < 0) { tempV1 = new Vector2(tetromino[0].X - tempV1.Y, tetromino[0].Y + tempV1.X); } else
            if(tempV1.X  > 0 && tempV1.Y  > 0) { tempV1 = new Vector2(tetromino[0].X - tempV1.Y, tetromino[0].Y + tempV1.X); }

            if(tempV2.X == 0 && tempV2.Y  < 0) { tempV2 = new Vector2(tetromino[0].X - tempV2.Y, tetromino[0].Y           ); } else
            if(tempV2.X == 0 && tempV2.Y  > 0) { tempV2 = new Vector2(tetromino[0].X - tempV2.Y, tetromino[0].Y           ); } else
            if(tempV2.X  < 0 && tempV2.Y == 0) { tempV2 = new Vector2(tetromino[0].X,            tetromino[0].Y + tempV2.X); } else
            if(tempV2.X  > 0 && tempV2.Y == 0) { tempV2 = new Vector2(tetromino[0].X,            tetromino[0].Y + tempV2.X); } else
            if(tempV2.X  < 0 && tempV2.Y  < 0) { tempV2 = new Vector2(tetromino[0].X - tempV2.Y, tetromino[0].Y + tempV2.X); } else
            if(tempV2.X  < 0 && tempV2.Y  > 0) { tempV2 = new Vector2(tetromino[0].X - tempV2.Y, tetromino[0].Y + tempV2.X); } else
            if(tempV2.X  > 0 && tempV2.Y  < 0) { tempV2 = new Vector2(tetromino[0].X - tempV2.Y, tetromino[0].Y + tempV2.X); } else
            if(tempV2.X  > 0 && tempV2.Y  > 0) { tempV2 = new Vector2(tetromino[0].X - tempV2.Y, tetromino[0].Y + tempV2.X); }

            if(tempV3.X == 0 && tempV3.Y  < 0) { tempV3 = new Vector2(tetromino[0].X - tempV3.Y, tetromino[0].Y           ); } else
            if(tempV3.X == 0 && tempV3.Y  > 0) { tempV3 = new Vector2(tetromino[0].X - tempV3.Y, tetromino[0].Y           ); } else
            if(tempV3.X  < 0 && tempV3.Y == 0) { tempV3 = new Vector2(tetromino[0].X,            tetromino[0].Y + tempV3.X); } else
            if(tempV3.X  > 0 && tempV3.Y == 0) { tempV3 = new Vector2(tetromino[0].X,            tetromino[0].Y + tempV3.X); } else
            if(tempV3.X  < 0 && tempV3.Y  < 0) { tempV3 = new Vector2(tetromino[0].X - tempV3.Y, tetromino[0].Y + tempV3.X); } else
            if(tempV3.X  < 0 && tempV3.Y  > 0) { tempV3 = new Vector2(tetromino[0].X - tempV3.Y, tetromino[0].Y + tempV3.X); } else
            if(tempV3.X  > 0 && tempV3.Y  < 0) { tempV3 = new Vector2(tetromino[0].X - tempV3.Y, tetromino[0].Y + tempV3.X); } else
            if(tempV3.X  > 0 && tempV3.Y  > 0) { tempV3 = new Vector2(tetromino[0].X - tempV3.Y, tetromino[0].Y + tempV3.X); }
        }
        if(tempV1.X > -1 && tempV1.X < 10 && tempV1.Y > -1 && tempV1.Y < 20) {
            if(tempV2.X > -1 && tempV2.X < 10 && tempV2.Y > -1 && tempV2.Y < 20) {
                if(tempV3.X > -1 && tempV3.X < 10 && tempV3.Y > -1 && tempV3.Y < 20) {
                    if(grid[tempV1.X][tempV1.Y] == -1) {
                        if(grid[tempV2.X][tempV2.Y] == -1) {
                            if(grid[tempV3.X][tempV3.Y] == -1) {
                                tetromino[1] = tempV1;
                                tetromino[2] = tempV2;
                                tetromino[3] = tempV3;
                            }
                        }
                    }
                }
            }
        }

    }

    private void commandHold() {
        if(canHold) {
            canHold = false;
            if(container_hold == -1) {
                container_hold = container_now;
                container_now = container_next;
                container_next = tetrominoRoll();
            } else {
                int temp;
                temp = container_hold;
                container_hold = container_now;
                container_now = temp;
            }
            tetrominoCreate();
        }
    }

    private int tetrominoRoll() {
        return RANDOM.nextInt(7);
    }

    private void tetrominoCreate() {
        if(container_now == 0) { // I
            tetromino[0].set(4, 1); // OOXO
            tetromino[1].set(4, 0); // OOXO
            tetromino[2].set(4, 2); // OOXO
            tetromino[3].set(4, 3); // OOXO
        }
        if(container_now == 1) { // O
            tetromino[0].set(4, 0); // OOOO
            tetromino[1].set(4, 1); // OXXO
            tetromino[2].set(5, 0); // OXXO
            tetromino[3].set(5, 1); // OOOO
        }
        if(container_now == 2) { // S
            tetromino[0].set(5, 0); // OOOO
            tetromino[1].set(6, 0); // OOXX
            tetromino[2].set(4, 1); // OXXO
            tetromino[3].set(5, 1); // OOOO
        }
        if(container_now == 3) { // Z
            tetromino[0].set(5, 0); // OOOO
            tetromino[1].set(4, 0); // XXOO
            tetromino[2].set(5, 1); // OXXO
            tetromino[3].set(6, 1); // OOOO
        }
        if(container_now == 4) { // L
            tetromino[0].set(4, 2); // OXOO
            tetromino[1].set(4, 0); // OXOO
            tetromino[2].set(4, 1); // OXXO
            tetromino[3].set(5, 2); // OOOO
        }
        if(container_now == 5) { // J
            tetromino[0].set(5, 2); // OOXO
            tetromino[1].set(5, 0); // OOXO
            tetromino[2].set(5, 1); // OXXO
            tetromino[3].set(4, 2); // OOOO
        }
        if(container_now == 6) { // T
            tetromino[0].set(5, 0); // OOOO
            tetromino[1].set(4, 0); // OXXX
            tetromino[2].set(6, 0); // OOXO
            tetromino[3].set(5, 1); // OOOO
        }
    }

    private void tetrominoDrop() {
        int tempPoints = scorePoint;
        while(scorePoint == tempPoints) {
            tetrominoFall();
        }
    }

    private void tetrominoFall() {
        if(tetromino[0].Y < 19 && tetromino[1].Y < 19 && tetromino[2].Y < 19 && tetromino[3].Y < 19) {
            if(grid[tetromino[0].X][tetromino[0].Y + 1] == -1 && grid[tetromino[1].X][tetromino[1].Y + 1] == -1 && grid[tetromino[2].X][tetromino[2].Y + 1] == -1 && grid[tetromino[3].X][tetromino[3].Y + 1] == -1) {
                tetromino[0].add(0, 1);
                tetromino[1].add(0, 1);
                tetromino[2].add(0, 1);
                tetromino[3].add(0, 1);
            } else {
                tetrominoPlace();
            }
        } else {
            tetrominoPlace();
        }
    }

    private void tetrominoPlace() {
        canHold = true;
        grid[tetromino[0].X][tetromino[0].Y] = container_now;
        grid[tetromino[1].X][tetromino[1].Y] = container_now;
        grid[tetromino[2].X][tetromino[2].Y] = container_now;
        grid[tetromino[3].X][tetromino[3].Y] = container_now;
        scorePoint += 2;
        if(tetromino[0].Y == 0) turnstate = 4;
        container_now = container_next;
        int container_temp = container_next;
        container_next = tetrominoRoll();
        if(container_next == container_temp) {
            if(RANDOM.nextInt(2) == 0) {
                container_next = tetrominoRoll();
            }
        }
        tetrominoCreate();
        lines[0] = -1;
        lines[1] = -1;
        lines[2] = -1;
        lines[3] = -1;
        for(int i = 19; i > -1; i--) {
            if(grid[0][i] != -1 && grid[1][i] != -1 && grid[2][i] != -1 && grid[3][i] != -1 && grid[4][i] != -1 && grid[5][i] != -1 && grid[6][i] != -1 && grid[7][i] != -1 && grid[8][i] != -1 && grid[9][i] != -1) {
                scoreLives++;
                scorePoint += 50;
                if(lines[3] == -1) { lines[3] = i; }
                else if(lines[2] == -1) { lines[2] = i; }
                else if(lines[1] == -1) { lines[1] = i; }
                else if(lines[0] == -1) { lines[0] = i; }
                alpha -= 5;
            }
        }
        if(scoreLives + 1 > (1 + scoreLevel) * 10) {
            scoreLevel++;
            timer_break -= timer_break / 10;
        }
        if(lines[0] != -1 && lines[1] != -1 && lines[2] != -1 && lines[3] != -1) {
            scorePoint += 250 * (scoreLevel + 1);
        }
    }




    //----------------------------------------SUPPORT----------------------------------------//

    public boolean hasHighscore(){
        return true;
    }

    public boolean isMultiplayer(){
        return false;
    }

    public int getID(){
        return 17;
    }

}
