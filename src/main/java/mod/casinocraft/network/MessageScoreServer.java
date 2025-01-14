package mod.casinocraft.network;

import mod.casinocraft.system.CasinoPacketHandler;
import mod.casinocraft.tileentities.TileEntityMachine;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageScoreServer {

    static int points;
    static String names;
    static BlockPos pos;




    //----------------------------------------CONSTRUCTOR----------------------------------------//

    public MessageScoreServer(String scoreName, int scorePoints, BlockPos pos) {
        this.points = scorePoints;
        this.names  = scoreName;
        this.pos = pos;
    }




    //----------------------------------------ENCODE/DECODE----------------------------------------//

    public static void encode (MessageScoreServer msg, PacketBuffer buf) {
        buf.writeInt(msg.points);
        buf.writeUtf(msg.names, 24);
        buf.writeBlockPos(msg.pos);
    }

    public static MessageScoreServer decode (PacketBuffer buf) {
        int _points = buf.readInt();
        String _names = buf.readUtf(24);
        BlockPos _pos = buf.readBlockPos();
        return new MessageScoreServer(_names, _points, _pos);
    }




    //----------------------------------------HANDLER----------------------------------------//

    public static class Handler {
        public static void handle (final MessageScoreServer message, Supplier<NetworkEvent.Context> context) {
            TileEntityMachine te = (TileEntityMachine) context.get().getSender().level.getBlockEntity(message.pos);
            context.get().enqueueWork(() ->{
                te.logic.addScore(message.names, message.points);
                te.logic.resetPlayers();
            });
            CasinoPacketHandler.sendToChunk(new MessageScoreClient(message.names, message.points, message.pos), context.get().getSender().level.getChunkAt(message.pos));
            context.get().setPacketHandled(true);
        }
    }

}
