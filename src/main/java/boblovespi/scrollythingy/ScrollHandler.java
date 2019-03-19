package boblovespi.scrollythingy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by Willi on 3/18/2019.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ScrollyThingy.MODID)
public class ScrollHandler
{
	public static final KeyBinding scrollModifier = new KeyBinding(
			"key.scrollthingy.scroll", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_RCONTROL,
			ScrollyThingy.NAME);

	@SubscribeEvent
	public static void onMouseScroll(MouseEvent event)
	{
		if (Minecraft.getMinecraft().currentScreen == null && scrollModifier.isKeyDown() && event.getDwheel() != 0)
		{
			int scrollAmount = event.getDwheel() > 0 ? 1 : -1;
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			ScrollManager.ListKey key = ScrollManager.findListKey(player.getHeldItemMainhand());
			if (key == null)
				return;
			int id = key.scrollGroupId;
			int from = key.scrollGroupPos;
			int size = ScrollManager.getListSize(id);
			int to = (from + scrollAmount + size) % size;
			NetworkHandler.INSTANCE.sendToServer(new NetworkHandler.Packet(id, from, to));
			event.setCanceled(true);
		}
	}
}
