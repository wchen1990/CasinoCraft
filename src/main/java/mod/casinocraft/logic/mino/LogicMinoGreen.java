package mod.casinocraft.logic.mino;

import mod.casinocraft.logic.LogicBase;
import net.minecraft.nbt.NBTTagCompound;

public class LogicMinoGreen extends LogicBase {   // Mystic Square

    // ...




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public LogicMinoGreen(int tableID){
        super(tableID, 4, 4);
    }




    //----------------------------------------START/RESTART----------------------------------------//

    public void start2(){
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                grid[x][y] = -1;
            }
        }
        int i = 0;
        while(i < 15) {
            int x = RANDOM.nextInt(4);
            int y = RANDOM.nextInt(4);
            if(grid[x][y] == -1) {
                grid[x][y] = i;
                i++;
            }
        }
    }




    //----------------------------------------COMMAND----------------------------------------//

    public void command(int action){
        move(action);
    }




    //----------------------------------------UPDATE----------------------------------------//

    public void updateMotion(){

    }

    public void updateLogic(){

    }




    //----------------------------------------SAVE/LOAD----------------------------------------//

    public void load2(NBTTagCompound compound){

    }

    public NBTTagCompound save2(NBTTagCompound compound){
        return compound;
    }




    //----------------------------------------CUSTOM----------------------------------------//

    private void move(int direction) {
        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                if(direction == 0 && y > 0) if(grid[x][y - 1] == -1) grid[x][y] += 20; // UP
                if(direction == 1 && y < 3) if(grid[x][y + 1] == -1) grid[x][y] += 20; // DOWN
                if(direction == 2 && x > 0) if(grid[x - 1][y] == -1) grid[x][y] += 20; // LEFT
                if(direction == 3 && x < 3) if(grid[x + 1][y] == -1) grid[x][y] += 20; // RIGHT
            }
        }
        change(direction);
    }

    private void change(int direction) {
        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                if(direction == 0 && grid[x][y] >= 20) { grid[x][y - 1] = grid[x][y] % 20; grid[x][y] = -1; } // UP
                if(direction == 1 && grid[x][y] >= 20) { grid[x][y + 1] = grid[x][y] % 20; grid[x][y] = -1; } // DOWN
                if(direction == 2 && grid[x][y] >= 20) { grid[x - 1][y] = grid[x][y] % 20; grid[x][y] = -1; } // LEFT
                if(direction == 3 && grid[x][y] >= 20) { grid[x + 1][y] = grid[x][y] % 20; grid[x][y] = -1; } // RIGHT
            }
        }
    }




    //----------------------------------------SUPPORT----------------------------------------//

    public boolean hasHighscore(){
        return false;
    }

    public boolean isMultiplayer(){
        return false;
    }

    public int getID(){
        return 37;
    }

}
