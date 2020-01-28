package mod.casinocraft.tileentities.minigames;

import java.util.ArrayList;
import java.util.List;

import mod.shared.util.Vector2;
import mod.casinocraft.tileentities.TileEntityBoard;
import mod.casinocraft.tileentities.TileEntityCasino;
import net.minecraft.util.math.BlockPos;

public class TileEntityMinesweeper extends TileEntityCasino {

    List<Vector2> FieldList = new ArrayList<Vector2>();

    //boolean[][] grid_cover = new boolean[14][14];
    boolean[][] grid_flag  = new boolean[30][14];
    //    int[][] grid_base  = new     int[14][14];
    
    int bombs;
	
    
    
	//--------------------CONSTRUCTOR--------------------
        
	public TileEntityMinesweeper(TileEntityBoard te, BlockPos bp){
    	super(te, bp);
    	gridI = new     int[26][14];
        gridB = new boolean[26][14];
	}
	
	
	
	//--------------------BASIC--------------------
	
	public void start2(){
		bombs = 0;
        selector = new Vector2(5, 5);
        FieldList.clear();
        scoreLevel = 1;
        Create_Field();
	}
	
	public void Restart() {
		turnstate = 2;
        FieldList.clear();
        scoreLevel++;
        Create_Field();
    }
	
    public void actionTouch(int action){
    	if(action == -1) {
    		Restart();
    	} else if(action == -2) {
    		turnstate = 4;
    	} else {
    		selector = new Vector2(action%26, action/26);
        	Command_Grid_Enter();
    	}
    }
    
	public void update(){
		
	}
	
	
	
	//--------------------GETTER--------------------
	
	public int getValue(int index){
		return gridI[index%26][index/26];
	}
	
	public boolean getFlag(int index){
		return gridB[index%26][index/26];
	}
	
	
	
	//--------------------CUSTOM--------------------
	
	private void Create_Field() {
        for(int i = 0; i < 14; i++) {
            for(int j = 0; j < 26; j++) {
                gridI[j][i] = 0;
                gridB[j][i] = false;
                grid_flag[j][i] = false;
            }
        }
        bombs = scoreLevel*difficulty;
        int max = difficulty == 2 ? 14*14 : 14*26;
        max -= max/4;
        if(bombs > max) bombs = max;
        for(int i = 0; i < bombs; i++) {
            int x = rand.nextInt(difficulty == 2 ? 14 : 26) + (difficulty == 2 ? 6 : 0);
            int y = rand.nextInt(14);
            if(gridI[x][y] != 9) {
                gridI[x][y] = 9;
            } else {
                i--;
            }
        }
        for(int y = 0; y < 14; y++) {
            for(int x = difficulty == 2 ? 6 : 0; x < (difficulty == 2 ? 20 : 26); x++) {
            	gridB[x][y] = true;
                if(gridI[x][y] != 9) {
                    int count = 0;
                    if(x >  0 && y >  0) if(gridI[x - 1][y - 1] == 9) count++; // -X -Y
                    if(          y >  0) if(gridI[x    ][y - 1] == 9) count++; //    -Y
                    if(x < 25 && y >  0) if(gridI[x + 1][y - 1] == 9) count++; // +X -Y
                    if(x < 25          ) if(gridI[x + 1][y    ] == 9) count++; // +X
                    if(x < 25 && y < 13) if(gridI[x + 1][y + 1] == 9) count++; // +X +Y
                    if(          y < 13) if(gridI[x    ][y + 1] == 9) count++; //    +Y
                    if(x >  0 && y < 13) if(gridI[x - 1][y + 1] == 9) count++; // -X +Y
                    if(x >  0          ) if(gridI[x - 1][y    ] == 9) count++; // -X
                    gridI[x][y] = count;
                }
            }
        }
        if(difficulty == 2) {
        	for(int y = 0; y < 6; y++) {
        		for(int x = 0; x < 6; x++) {
            		
            	}
        	}
        }
    }

    private void Command_Grid_Enter() {
        if(gridB[selector.X][selector.Y]) {
            //if(active_flag) {
            //    if(grid_flag[(int)selector.X][(int)selector.Y]) {
            //        grid_flag[(int)selector.X][(int)selector.Y] = false;
            //    } else {
            //        grid_flag[(int)selector.X][(int)selector.Y] = true;
            //    }
            //} else {
                gridB[selector.X][selector.Y] = false;
                if(gridI[selector.X][selector.Y] == 9) {
                    gridI[selector.X][selector.Y] = 10;
                    Uncover_Bombs();
                    turnstate = 4;
                    scorePoints /= 2;
                } else {
                    if(gridI[selector.X][selector.Y] == 0) {
                        FieldList.add(selector);
                        Uncover_Tiles();
                    }
                    boolean temp = false;
                    for(int i = 0; i < 14; i++) {
                        for(int j = 0; j < 26; j++) {
                            if(gridI[j][i] != 9) {
                                if(gridB[j][i]) {
                                    temp = true;
                                }
                            }
                        }
                    }
                    if(!temp) {
                    	turnstate = 3;
                    	scorePoints += bombs;
                    }
                }
            //}
        }
    }

    private void Uncover_Tiles() {
        while(FieldList.size() > 0) {
            boolean temp0 = false;
            for(Vector2 v : FieldList) {
                if(v.X >  0 && v.Y >  0) { if(gridB[v.X - 1][v.Y - 1] && !grid_flag[v.X - 1][v.Y - 1]) { gridB[v.X - 1][v.Y - 1] = false; if(gridI[v.X - 1][v.Y - 1] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X - 1, v.Y - 1)) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X - 1, v.Y - 1)); temp0 = true; break; } } } } } // -X -Y
                if(            v.Y >  0) { if(gridB[v.X    ][v.Y - 1] && !grid_flag[v.X    ][v.Y - 1]) { gridB[v.X    ][v.Y - 1] = false; if(gridI[v.X    ][v.Y - 1] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X    , v.Y - 1)) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X    , v.Y - 1)); temp0 = true; break; } } } } } //    -Y
                if(v.X < 25 && v.Y >  0) { if(gridB[v.X + 1][v.Y - 1] && !grid_flag[v.X + 1][v.Y - 1]) { gridB[v.X + 1][v.Y - 1] = false; if(gridI[v.X + 1][v.Y - 1] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X + 1, v.Y - 1)) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X + 1, v.Y - 1)); temp0 = true; break; } } } } } // +X -Y
                if(v.X < 25            ) { if(gridB[v.X + 1][v.Y    ] && !grid_flag[v.X + 1][v.Y    ]) { gridB[v.X + 1][v.Y    ] = false; if(gridI[v.X + 1][v.Y    ] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X + 1, v.Y    )) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X + 1, v.Y    )); temp0 = true; break; } } } } } // +X
                if(v.X < 25 && v.Y < 13) { if(gridB[v.X + 1][v.Y + 1] && !grid_flag[v.X + 1][v.Y + 1]) { gridB[v.X + 1][v.Y + 1] = false; if(gridI[v.X + 1][v.Y + 1] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X + 1, v.Y + 1)) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X + 1, v.Y + 1)); temp0 = true; break; } } } } } // +X +Y
                if(            v.Y < 13) { if(gridB[v.X    ][v.Y + 1] && !grid_flag[v.X    ][v.Y + 1]) { gridB[v.X    ][v.Y + 1] = false; if(gridI[v.X    ][v.Y + 1] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X    , v.Y + 1)) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X    , v.Y + 1)); temp0 = true; break; } } } } } //    +Y
                if(v.X >  0 && v.Y < 13) { if(gridB[v.X - 1][v.Y + 1] && !grid_flag[v.X - 1][v.Y + 1]) { gridB[v.X - 1][v.Y + 1] = false; if(gridI[v.X - 1][v.Y + 1] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X - 1, v.Y + 1)) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X - 1, v.Y + 1)); temp0 = true; break; } } } } } // -X +Y
                if(v.X >  0            ) { if(gridB[v.X - 1][v.Y    ] && !grid_flag[v.X - 1][v.Y    ]) { gridB[v.X - 1][v.Y    ] = false; if(gridI[v.X - 1][v.Y    ] == 0) { boolean temp = false; for(Vector2 v2 : FieldList) { if(v2.matches(v.X - 1, v.Y    )) { temp = true; break; } if(!temp) { FieldList.add(new Vector2(v.X - 1, v.Y    )); temp0 = true; break; } } } } } // -X
                break;
            }
            if(!temp0) FieldList.remove(0);
        }
    }

    private void Uncover_Bombs() {
        for(int y = 0; y < 14; y++) {
            for(int x = 0; x < 26; x++) {
                if(gridI[x][y] == 9) {
                    gridB[x][y] = false;
                }
            }
        }
    }
}