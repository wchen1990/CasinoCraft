package mod.casinocraft.logic.dust;

import mod.casinocraft.logic.LogicBase;

public class Logic2048 extends LogicBase {

    public     int[][] gridI = new     int[4][4];
    public boolean[][] gridB = new boolean[4][4];

    public boolean placing = false;
    public boolean timerActive = false;

    public int timer = 0;
    public int direction = 0; // 0 - null, 1 - up, 2 - down, 3 - left, 4 - right



    //--------------------CONSTRUCTOR--------------------

    public Logic2048(){
        super(false, "a_2048");
    }



    //--------------------BASIC--------------------

    public void start2(){
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                gridI[x][y] = 0;
            }
        }
        placing = false;
        gridI[0][0] = RANDOM.nextInt(1) + 1;
        gridI[3][0] = RANDOM.nextInt(1) + 1;
        gridI[0][3] = RANDOM.nextInt(1) + 1;
        gridI[3][3] = RANDOM.nextInt(1) + 1;
        timerActive = false;
        timer = 0;
        direction = 0;
    }

    public void actionTouch(int action){
        if(action == 0) { Move(1); }
        if(action == 1) { Move(2); }
        if(action == 2) { Move(3); }
        if(action == 3) { Move(4); }
    }

    public void updateLogic(){
        if(timerActive) {
            timer += 6;
            if(timer == 48) {
                timerActive = false;
                timer = 0;
                Change();
                Move(direction);
                placing = true;
            }
        } else {
            if(placing) {
                Place();
                placing = false;
            }
            direction = 0;
        }
    }

    public void updateMotion() {

    }



    //--------------------CUSTOM--------------------

    private void Move(int s) {
        if(s == 1) { // up
            for(int y = 1; y < 4; y++) {
                for(int x = 0; x < 4; x++) {
                    if(gridI[x][y] != 0) {
                        if(gridI[x][y - 1] == 0 || gridI[x][y - 1] == gridI[x][y]) {
                            gridB[x][y] = true;
                            timerActive = true;
                            direction = s;
                        }
                    }
                }
            }
        }
        if(s == 2) { // down
            for(int y = 2; y > -1; y--) {
                for(int x = 3; x > -1; x--) {
                    if(gridI[x][y] != 0) {
                        if(gridI[x][y + 1] == 0 || gridI[x][y + 1] == gridI[x][y]) {
                            gridB[x][y] = true;
                            timerActive = true;
                            direction = s;
                        }
                    }
                }
            }
        }
        if(s == 3) { // left
            for(int x = 1; x < 4; x++) {
                for(int y = 0; y < 4; y++) {
                    if(gridI[x][y] != 0) {
                        if(gridI[x - 1][y] == 0 || gridI[x - 1][y] == gridI[x][y]) {
                            gridB[x][y] = true;
                            timerActive = true;
                            direction = s;
                        }
                    }
                }
            }
        }
        if(s == 4) { // right
            for(int x = 2; x > -1; x--) {
                for(int y = 3; y > -1; y--) {
                    if(gridI[x][y] != 0) {
                        if(gridI[x + 1][y] == 0 || gridI[x + 1][y] == gridI[x][y]) {
                            gridB[x][y] = true;
                            timerActive = true;
                            direction = s;
                        }
                    }
                }
            }
        }
    }

    private void Change() {
        if(direction == 1) { // up
            for(int y = 1; y < 4; y++) {
                for(int x = 0; x < 4; x++) {
                    if(gridB[x][y]) {
                        if(gridI[x][y - 1] == 0) {
                            gridI[x][y - 1] = gridI[x][y];
                        } else {
                            gridI[x][y - 1] = gridI[x][y - 1] + 1;
                            Add_Points(gridI[x][y - 1]);
                        }
                        gridI[x][y] = 0; // sets FLAG automatically to FALSE
                    }
                }
            }
        }
        if(direction == 2) { // down
            for(int y = 2; y > -1; y--) {
                for(int x = 3; x > -1; x--) {
                    if(gridB[x][y]) {
                        if(gridI[x][y + 1] == 0) {
                            gridI[x][y + 1] = gridI[x][y];
                        } else {
                            gridI[x][y + 1] = gridI[x][y+1] + 1;
                            Add_Points(gridI[x][y + 1]);
                        }
                        gridI[x][y] = 0;
                    }
                }
            }
        }
        if(direction == 3) { // left
            for(int x = 1; x < 4; x++) {
                for(int y = 0; y < 4; y++) {
                    if(gridB[x][y]) {
                        if(gridI[x - 1][y] == 0) {
                            gridI[x - 1][y] = gridI[x][y];
                        } else {
                            gridI[x - 1][y] = gridI[x - 1][y] + 1;
                            Add_Points(gridI[x - 1][y]);
                        }
                        gridI[x][y] = 0;
                    }
                }
            }
        }
        if(direction == 4) { // right
            for(int x = 2; x > -1; x--) {
                for(int y = 3; y > -1; y--) {
                    if(gridB[x][y]) {
                        if(gridI[x + 1][y] == 0) {
                            gridI[x + 1][y] = gridI[x][y];
                        } else {
                            gridI[x + 1][y] = gridI[x + 1][y] + 1;
                            Add_Points(gridI[x + 1][y]);
                        }
                        gridI[x][y] = 0;
                    }
                }
            }
        }
    }

    private void Place() {
        for(int i = 0; i < 24; i++) {
            int x = RANDOM.nextInt(4);
            int y = RANDOM.nextInt(4);
            if(gridI[x][y] == 0) { // FLAGGING ???
                gridI[x][y] = 1;
                break;
            }
        }
        Check();
    }

    private void Check() {
        boolean b = false;
        for(int y = 1; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                if(gridI[x][y] != 0) {
                    if(gridI[x][y - 1] == 0 || gridI[x][y - 1] == gridI[x][y]) {
                        b = true;
                        break;
                    }
                }
            }
            if(b) break;
        }
        for(int y = 2; y > -1; y--) {
            for(int x = 3; x > -1; x--) {
                if(gridI[x][y] != 0) {
                    if(gridI[x][y + 1] == 0 || gridI[x][y + 1] == gridI[x][y]) {
                        b = true;
                        break;
                    }
                }
            }
            if(b) break;
        }
        for(int x = 1; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                if(gridI[x][y] != 0) {
                    if(gridI[x - 1][y] == 0 || gridI[x - 1][y] == gridI[x][y]) {
                        b = true;
                        break;
                    }
                }
            }
            if(b) break;
        }
        for(int x = 2; x > -1; x--) {
            for(int y = 3; y > -1; y--) {
                if(gridI[x][y] != 0) {
                    if(gridI[x + 1][y] == 0 || gridI[x + 1][y] == gridI[x][y]) {
                        b = true;
                        break;
                    }
                }
            }
            if(b) break;
        }
        if(!b) {
            turnstate = 4;
        }
    }

    private int Get_Direction(boolean horizontal, int x, int y) {
        if(direction == 0)
            return 0;
        if( horizontal && direction == 3) if(gridB[x][y]) return -timer; // left
        if( horizontal && direction == 4) if(gridB[x][y]) return  timer; // right
        if(!horizontal && direction == 1) if(gridB[x][y]) return -timer; // up
        if(!horizontal && direction == 2) if(gridB[x][y]) return  timer; // down
        return 0;
    }

    private void Add_Points(int i) {
        if(i ==  1) scorePoint =     2;
        if(i ==  2) scorePoint =     4;
        if(i ==  3) scorePoint =     8;
        if(i ==  4) scorePoint =    16;
        if(i ==  5) scorePoint =    32;
        if(i ==  6) scorePoint =    64;
        if(i ==  7) scorePoint =   128;
        if(i ==  8) scorePoint =   256;
        if(i ==  9) scorePoint =   512;
        if(i == 10) scorePoint =  1024;
        if(i == 11) scorePoint =  2048;
        if(i == 12) scorePoint =  4096;
        if(i == 13) scorePoint =  8192;
        if(i == 14) scorePoint = 16384;
        if(i == 15) scorePoint = 32768;
        if(i == 16) scorePoint = 65536;
    }

}