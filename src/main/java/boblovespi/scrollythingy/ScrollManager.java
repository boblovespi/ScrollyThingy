package boblovespi.scrollythingy;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Willi on 3/17/2019.
 */
@Mod.EventBusSubscriber
@ZenClass("scrollythingy.ScrollManager")
@ZenRegister
public class ScrollManager
{
	private static HashMap<Item, ArrayList<ListKey>> ITEM_TO_LIST_MAP = new HashMap<>();
	private static ArrayList<ListKey[]> SCROLL_GROUP_MAP = new ArrayList<>();
	private static int scrollGroupCounter = 0;

	@ZenMethod
	public static void addScrollGroup(IIngredient[] items)
	{
		ArrayList<IItemStack> stacks = new ArrayList<>();
		for (IIngredient item : items)
		{
			stacks.addAll(Arrays.asList(item.getItemArray()));
		}
		addScrollGroup(stacks.toArray(new IItemStack[stacks.size()]));
	}

	@ZenMethod
	public static void addScrollGroup(IItemStack[] items)
	{
		ArrayList<ListKey> tempList = new ArrayList<>(items.length);
		int counter = 0;
		for (IItemStack item : items)
		{
			ItemStack stack = Transformers.transform(item);
			if (findListKey(stack) != null)
			{
				CraftTweakerAPI.logError(
						"Attempting to bind an item to a scroll group that already has a scroll group! Item: " + item);
				continue;
			}

			if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
			{
				NonNullList<ItemStack> list = NonNullList.create();
				stack.getItem().getSubItems(CreativeTabs.SEARCH, list);
				for (ItemStack stack1 : list)
				{
					ListKey key = new ListKey(scrollGroupCounter, counter, stack1);
					tempList.add(key);
					if (ITEM_TO_LIST_MAP.containsKey(stack1.getItem()))
					{
						ITEM_TO_LIST_MAP.get(stack1.getItem()).add(key);
					} else
					{
						ITEM_TO_LIST_MAP.put(stack1.getItem(), new ArrayList<>());
						ITEM_TO_LIST_MAP.get(stack1.getItem()).add(key);
					}
					++counter;
				}
			} else
			{
				ListKey key = new ListKey(scrollGroupCounter, counter, stack);
				tempList.add(key);
				if (ITEM_TO_LIST_MAP.containsKey(stack.getItem()))
				{
					ITEM_TO_LIST_MAP.get(stack.getItem()).add(key);
				} else
				{
					ITEM_TO_LIST_MAP.put(stack.getItem(), new ArrayList<>());
					ITEM_TO_LIST_MAP.get(stack.getItem()).add(key);
				}
				++counter;
			}
		}

		SCROLL_GROUP_MAP.add(tempList.toArray(new ListKey[tempList.size()]));
		++scrollGroupCounter;
	}

	@Nullable
	public static ListKey findListKey(ItemStack stack)
	{
		stack = stack.copy();
		stack.setCount(1);
		ArrayList<ListKey> keys = ITEM_TO_LIST_MAP.get(stack.getItem());
		if (keys == null)
			return null;
		for (ListKey key : keys)
		{
			if (key.item.isItemEqual(stack))
			{
				if (key.item.getTagCompound() == null || stack.getTagCompound() == null || key.item.getTagCompound()
																								   .equals(stack
																										   .getTagCompound()))
					return key;
			}
		}
		return null;
	}

	@Nullable
	public static ListKey getListKey(int id, int item)
	{
		if (id < 0 || SCROLL_GROUP_MAP.size() <= id)
			return null;
		ListKey[] keys = SCROLL_GROUP_MAP.get(id);
		if (item < 0 || keys.length <= item)
			return null;
		return keys[item];
	}

	public static int getListSize(int id)
	{
		if (id < 0 || SCROLL_GROUP_MAP.size() <= id)
			return -1;
		return SCROLL_GROUP_MAP.get(id).length;
	}

	public static boolean stacksAreEqual(ItemStack s1, ItemStack s2)
	{
		if (!s1.isItemEqual(s2))
			return false;
		if (s1.getTagCompound() == null)
			return s2.getTagCompound() == null;
		else
			return s1.getTagCompound().equals(s2.getTagCompound());
	}

	public static class ListKey
	{
		public final int scrollGroupId;
		public final int scrollGroupPos;
		public final ItemStack item;

		public ListKey(int scrollGroupId, int scrollGroupPos, ItemStack item)
		{
			this.scrollGroupId = scrollGroupId;
			this.scrollGroupPos = scrollGroupPos;
			this.item = item;
		}
	}

}
