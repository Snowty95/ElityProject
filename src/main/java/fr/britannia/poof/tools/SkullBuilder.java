package fr.britannia.poof.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullBuilder extends ItemBuilder
{
    public SkullBuilder(SKULL_TYPE type) { super(type.getMaterial(), (byte)3); }
    public SkullBuilder(SKULL_TYPE type, int amout) { super(type.getMaterial(), amout, (byte)3); }
    
    public SkullBuilder setOwner(String name)
    {
        SkullMeta meta = (SkullMeta) super.getItemMeta();
        meta.setOwner(name);
        super.setItemMeta(meta);
        return this;
    }
    
    public SkullBuilder setTexture(String url)
    {
        if (url == null || url.isEmpty())
            return this;
    
        SkullMeta meta = (SkullMeta) super.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
    
        try {
            profileField = meta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    
        profileField.setAccessible(true);
    
        try {
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.setItemMeta(meta);
        return this;
    }
    
    public enum SKULL_TYPE
    {
        ITEM(Material.SKULL_ITEM),
        BLOCK(Material.SKULL);
        
        private Material material;
        SKULL_TYPE(Material material) { this.material = material; }
        
        public Material getMaterial() { return this.material; }
    }
    
}
