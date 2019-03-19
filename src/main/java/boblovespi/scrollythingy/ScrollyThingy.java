package boblovespi.scrollythingy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Willi on 1/22/2019.
 */
@Mod(modid = ScrollyThingy.MODID, name = ScrollyThingy.NAME, version = ScrollyThingy.VERSION)
public class ScrollyThingy
{
	public static final String MODID = "scrollythingy";
	public static final String VERSION = "Alpha 0.0.0";
	public static final String NAME = "Scrolly Thingy";
	public static final String SERVER_PROXY_CLASS = "boblovespi.scrollythingy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "boblovespi.scrollythingy.ClientProxy";
	public static final String GUI_FACTORY = "net.minecraftforge.fml.client.DefaultGuiFactory";
	@Mod.Instance(MODID)
	public static ScrollyThingy instance = new ScrollyThingy();
	@SidedProxy(serverSide = SERVER_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
	public static CommonProxy proxy;


	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NetworkHandler.init();
		proxy.preInit(event);
	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{

	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}
}
