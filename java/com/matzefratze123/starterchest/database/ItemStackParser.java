package com.matzefratze123.starterchest.database;

import java.util.Map;

import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.NBTTagList;
import net.minecraft.server.v1_4_6.NBTTagString;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Includes methods for parsing and converting ItemStacks to Strings and back
 * 
 * @author matzefratze123
 */
public class ItemStackParser {

	public static String convertItemStacktoString(ItemStack item) {
		
		String s;
		int amount = item.getAmount();
		int durability = item.getDurability();
		int id = item.getTypeId();
		int data = item.getData().getData();
		Map<Enchantment, Integer> ent = item.getEnchantments();
		StringBuilder builder = new StringBuilder();
		for (Enchantment ench : ent.keySet()) {
			builder.append(ench.getName()).append("=").append(ent.get(ench)).append(":");
		}
		if (item.getType() == Material.WRITTEN_BOOK) {
			
			//Variables begin
			String author;
			String title;
			String[] pages;
			//Variables end
			
			NBTTagCompound bookData = CraftItemStack.asNMSCopy(item).tag;
			author = bookData.getString("author");
			title = bookData.getString("title");
			
			NBTTagList nPages = bookData.getList("pages");
			String[] sPages = new String[nPages.size()];
			for (int i = 0; i < nPages.size(); i++) {
				sPages[i] = nPages.get(i).toString();
			}
			pages = sPages;
			StringBuilder bookStringBuilder = new StringBuilder();
			bookStringBuilder.append(author).append("/&").append(replaceToString(title)).append("/&");
			for (String page : pages) {
				bookStringBuilder.append(replaceToString(page)).append("/!");
			}
			
			s = id + "," + data + "," + amount + "," + durability + "," + builder.toString() + "," + bookStringBuilder.toString();
			return s;
		} else if (item.getType() == Material.BOOK_AND_QUILL) {
			
			NBTTagCompound bookData = CraftItemStack.asNMSCopy(item).tag;
			NBTTagList nPages = bookData.getList("pages");
			
			//Variable pages
			String[] pages = new String[nPages.size()];
			
			for (int i = 0; i < nPages.size(); i++) {
				pages[i] = nPages.get(i).toString();
			}
			StringBuilder bookStringBuilder = new StringBuilder();
			for (String page : pages)
				bookStringBuilder.append(replaceToString(page)).append("/!");
			s = id + "," + data + "," + amount + "," + durability + "," + builder.toString() + "," + bookStringBuilder.toString();
			return s;
		}
		s = id + "," + data + "," + amount + "," + durability + "," + builder.toString();
		return s;
	}
	
	private static String replaceToString(String str) {
		str = str.replaceAll("\n", "/n");
		str = str.replaceAll(":", "//colon");
		str = str.replaceAll(",", "//comma");
		return str;
	}
	
	private static String replaceToItemStack(String str) {
		str = str.replaceAll("/n", "\n");
		str = str.replaceAll("//colon", ":");
		str = str.replaceAll("//comma", ",");
		return str;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack convertStringtoItemStack(String s) {
    	String[] split = s.split(",", -5);
    	int id = Integer.valueOf(split[0]);
    	int itemdata = Integer.valueOf(split[1]);
    	int amount = Integer.valueOf(split[2]);
    	int durability = Short.valueOf(split[3]);
    	String ent = split[4];
    	ItemStack item = new ItemStack(id, amount, (short) durability, (byte) itemdata);
    	if (!ent.equalsIgnoreCase("")) {
    		String[] enchantments = ent.split(":", -10);
    		for (String enchantmentString : enchantments) {
    			
    			String[] levelAndEnchantment = enchantmentString.split("=", -2);
    			if (levelAndEnchantment.length > 1) {
    				Enchantment ench = Enchantment.getByName(levelAndEnchantment[0]);
    				int level = Integer.valueOf(levelAndEnchantment[1]);
    			
    				item.addUnsafeEnchantment(ench, level);
    			}
    		}
    	}
    	
    	Material mat = Material.getMaterial(id);
    	if (mat == Material.WRITTEN_BOOK) {
    		String data = split[5];
    		
    		//Variables
    		String author;
    		String title;
    		
    		String[] bookData = data.split("/&");
    		
    		author = bookData[0];
    		title = bookData[1];
    		
    		String[] sPages = bookData[2].split("/!");
    		//Variable
    		String[] pages = new String[sPages.length];
    		
    		for (int i = 0; i < sPages.length; i++) {
    			if (!sPages[i].equalsIgnoreCase("")) {
    				pages[i] = replaceToItemStack(sPages[i]);
    			}
    		}
    		net.minecraft.server.v1_4_6.ItemStack cis = new net.minecraft.server.v1_4_6.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
    		cis.setData(itemdata);
    		
    		NBTTagCompound newBookData = new NBTTagCompound();
    		
    		newBookData.setString("author", author);
    		newBookData.setString("title", replaceToItemStack(title));
    		
    		NBTTagList list = new NBTTagList();
    		for (int i = 0; i < pages.length; i++)
    			list.add(new NBTTagString(pages[i], pages[i]));
    		
    		newBookData.set("pages", list);
    		cis.setTag(newBookData);
    		return CraftItemStack.asBukkitCopy(cis);
    	} else if (mat == Material.BOOK_AND_QUILL) {
    		String data = split[5];
    		
    		String[] sPages = data.split("/!");
    		String[] pages = new String[sPages.length];
    		
    		for (int i = 0; i < sPages.length; i++) {
    			if (!sPages[i].equalsIgnoreCase("")) {
    				pages[i] = replaceToItemStack(sPages[i]);
    			}
    		}
    		net.minecraft.server.v1_4_6.ItemStack cis = CraftItemStack.asNMSCopy(item);
    		
    		NBTTagCompound newBookData = new NBTTagCompound();
    		
    		NBTTagList list = new NBTTagList();
    		for (int i = 0; i < pages.length; i++)
    			list.add(new NBTTagString(pages[i], pages[i]));
    		
    		newBookData.set("pages", list);
    		cis.tag = newBookData;
    		return CraftItemStack.asBukkitCopy(cis);
    	}
    	
    	return item;
    }
	
}
