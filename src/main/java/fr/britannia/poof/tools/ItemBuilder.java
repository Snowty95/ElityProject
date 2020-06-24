package fr.britannia.poof.tools;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ItemBuilder extends ItemStack
{
    
    public ItemBuilder(Material material) { super(material); }
    public ItemBuilder(Material material, int amout) { super(material, amout); }
    public ItemBuilder(Material material, int amout, byte data) { super(material, amout); }
    public ItemBuilder(Material material, byte data) { super(material, 1, data); }
    
    public ItemBuilder setName(String name)
    {
        ItemMeta meta = super.getItemMeta();
        meta.setDisplayName(name);
        super.setItemMeta(meta);
        return this;
    }
    public ItemBuilder setLore(String... line)
    {
        ItemMeta meta = super.getItemMeta();
        meta.setLore(Arrays.asList(line));
        super.setItemMeta(meta);
        return this;
    }
    public ItemBuilder addLine(String line)
    {
        ItemMeta meta = super.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(line);
        meta.setLore(lore);
        super.setItemMeta(meta);
        return this;
    }
    public ItemBuilder addLine(int index, String line)
    {
        ItemMeta meta = super.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(index, line);
        meta.setLore(lore);
        super.setItemMeta(meta);
        return this;
    }
    public ItemBuilder removeLine(int index)
    {
        ItemMeta meta = super.getItemMeta();
        List<String> lore = meta.getLore();
        if(index < lore.size()) lore.remove(index);
        meta.setLore(lore);
        super.setItemMeta(meta);
        return this;
    }
    public ItemBuilder removeLine(String line)
    {
        ItemMeta meta = super.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore.contains(line)) lore.remove(line);
        meta.setLore(lore);
        super.setItemMeta(meta);
        return this;
    }
    public ItemBuilder addFlag(ItemFlag... flags)
    {
        ItemMeta meta = super.getItemMeta();
        Arrays.stream(flags).forEach(meta::addItemFlags);
        super.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder addAllFlag()
    {
        ItemMeta meta = super.getItemMeta();
        Arrays.stream(ItemFlag.values()).forEach(meta::addItemFlags);
        super.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder addEnchant(Enchantment enchantment, int level)
    {
        ItemMeta meta = super.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        super.setItemMeta(meta);
        return this;
    }
    public ItemBuilder addEnchant(Enchantment enchantment)
    {
        ItemMeta meta = super.getItemMeta();
        meta.addEnchant(enchantment, 1, true);
        super.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder setEnchant(boolean bool)
    {
        ItemMeta meta = super.getItemMeta();
        if(bool)
        {
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            super.setItemMeta(meta);
        }
        else
        {
            if(meta.hasEnchants())
            {
                for(Map.Entry<Enchantment, Integer> enc : meta.getEnchants().entrySet()) meta.removeEnchant(enc.getKey());
            }
        }
        return this;
    }
    
    
    public ItemBuilder removeEnchant(Enchantment enchantment)
    {
        ItemMeta meta = super.getItemMeta();
        if(meta.hasEnchant(enchantment)) { meta.removeEnchant(enchantment); }
        super.setItemMeta(meta);
        return this;
    }
   
    
    

}

