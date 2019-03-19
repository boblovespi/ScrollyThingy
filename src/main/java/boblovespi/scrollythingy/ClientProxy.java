package boblovespi.scrollythingy;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Willi on 3/18/2019.
 */
public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		ClientRegistry.registerKeyBinding(ScrollHandler.scrollModifier);
	}
}
