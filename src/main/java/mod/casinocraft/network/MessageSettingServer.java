package mod.casinocraft.network;

import mod.casinocraft.system.CasinoPacketHandler;
import mod.casinocraft.tileentities.TileEntityMachine;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSettingServer {

    static int[] packetData;
    static BlockPos pos;




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public MessageSettingServer(BlockPos _pos, int[] _packetData) {
        this.pos = _pos;
        this.packetData = _packetData;
    }




    //----------------------------------------ENCODE/DECODE----------------------------------------//

    public static void encode (MessageSettingServer msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeVarIntArray(msg.packetData);
    }

    public static MessageSettingServer decode (PacketBuffer buf) {
        BlockPos _pos = buf.readBlockPos();
        int[] _packet = buf.readVarIntArray();
        return new MessageSettingServer(_pos, _packet);
    }




    //----------------------------------------HANDLER----------------------------------------//

    public static class Handler {
        public static void handle (final MessageSettingServer message, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() ->{
                TileEntityMachine te = (TileEntityMachine) context.get().getSender().level.getBlockEntity(message.pos);
                te.bettingLow                  = message.packetData[ 0];
                te.bettingHigh                 = message.packetData[ 1];
                te.rewardScore1                = message.packetData[ 2];
                te.rewardScore2                = message.packetData[ 3];
                te.rewardScore3                = message.packetData[ 4];
                te.rewardAmount1               = message.packetData[ 5];
                te.rewardAmount2               = message.packetData[ 6];
                te.rewardAmount3               = message.packetData[ 7];
                te.prizeMode1                  = message.packetData[ 8] == 1;
                te.prizeMode2                  = message.packetData[ 9] == 1;
                te.prizeMode3                  = message.packetData[10] == 1;
                te.transferTokenIN             = message.packetData[11] == 1;
                te.transferTokenOUT            = message.packetData[12] == 1;
                te.transferRewardIN            = message.packetData[13] == 1;
                te.transferRewardOUT           = message.packetData[14] == 1;
                te.settingInfiniteToken        = message.packetData[15] == 1;
                te.settingInfiniteReward       = message.packetData[16] == 1;
                te.settingDropItemsOnBreak     = message.packetData[17] == 1;
                te.settingIndestructableBlock  = message.packetData[18] == 1;
                te.settingAlternateColor       = message.packetData[19];
            });
            CasinoPacketHandler.sendToChunk(new MessageSettingClient(message.pos, message.packetData), context.get().getSender().level.getChunkAt(message.pos));
            context.get().setPacketHandled(true);
        }
    }

}
