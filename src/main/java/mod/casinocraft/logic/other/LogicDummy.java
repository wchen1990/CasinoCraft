package mod.casinocraft.logic.other;

import mod.casinocraft.logic.LogicBase;
import net.minecraft.nbt.CompoundNBT;

public class LogicDummy extends LogicBase {

    public LogicDummy() {
        super(false, 0, "x_dummy");
    }

    @Override
    public void actionTouch(int action) {

    }

    @Override
    public void updateMotion() {

    }

    @Override
    public void updateLogic() {

    }

    @Override
    public void start2() {

    }

    @Override
    public void load2(CompoundNBT compound){

    }

    @Override
    public CompoundNBT save2(CompoundNBT compound){
        return compound;
    }
}
