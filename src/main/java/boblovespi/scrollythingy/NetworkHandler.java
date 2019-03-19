package boblovespi.scrollythingy;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import static boblovespi.scrollythingy.ScrollManager.*;

/**
 * Created by Willi on 3/18/2019.
 */
public class NetworkHandler
{
	public static SimpleNetworkWrapper INSTANCE = null;

	public static void init()
	{
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ScrollyThingy.MODID);
		INSTANCE.registerMessage(PacketHandler.class, Packet.class, 0, Side.SERVER);
	}

	public static class Packet implements IMessage
	{
		private int scrollId;
		private int scrollFrom;
		private int scrollTo;

		public Packet()
		{
			this(0, 0, 0);
		}

		public Packet(int scrollId, int scrollFrom, int scrollTo)
		{
			this.scrollId = scrollId;
			this.scrollFrom = scrollFrom;
			this.scrollTo = scrollTo;
		}

		/**
		 * Convert from the supplied buffer into your specific message type
		 */
		@Override
		public void fromBytes(ByteBuf buf)
		{
			scrollId = buf.readInt();
			scrollFrom = buf.readInt();
			scrollTo = buf.readInt();
		}

		/**
		 * Deconstruct your message into the supplied byte buffer
		 */
		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(scrollId);
			buf.writeInt(scrollFrom);
			buf.writeInt(scrollTo);
		}
	}

	public static class PacketHandler implements IMessageHandler<Packet, IMessage>
	{

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 */
		@Override
		public IMessage onMessage(Packet message, MessageContext ctx)
		{
			EntityPlayerMP player = ctx.getServerHandler().player;
			WorldServer world = player.getServerWorld();

			world.addScheduledTask(() ->
			{
				ItemStack stack = player.getHeldItemMainhand();
				ListKey key = getListKey(message.scrollId, message.scrollFrom);
				if (key == null)
					return;
				if (stacksAreEqual(key.item, stack))
				{
					ListKey to = getListKey(message.scrollId, message.scrollTo);
					if (to == null)
						return;
					ItemStack stack1 = to.item.copy();
					stack1.setCount(stack.getCount());
					player.setHeldItem(EnumHand.MAIN_HAND, stack1);
				}
			});
			return null;
		}
	}
}
