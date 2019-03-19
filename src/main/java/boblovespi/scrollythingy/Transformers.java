package boblovespi.scrollythingy;

import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 1/22/2019.
 */
public class Transformers
{
	public static ItemStack transform(IItemStack stack)
	{
		return (ItemStack) stack.getInternal();
	}
}
