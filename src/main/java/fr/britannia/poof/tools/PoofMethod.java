package fr.britannia.poof.tools;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;

public class PoofMethod
{
    // <------------------ VECTOR -------------------->
    
    public static Vector getVectorLeft(Vector vector)
    {
        double x = vector.getX();
        double z = vector.getZ();
        return new Vector(z, 0, -x);
    }
    
    // <------------------ VECTOR -------------------->
    
    public static Vector getVectorRight(Vector vector)
    {
        double x = vector.getX();
        double z = vector.getZ();
        return new Vector(-z, 0, x);
    }
    
    
    // <----------------- FIREWORK ------------------->
    
    public static FireworkMeta getRandomMeta(Firework fire)
    {
        FireworkMeta meta = fire.getFireworkMeta();
        meta.setPower(new Random().nextInt(1));
        for(int i = 0; i < new Random().nextInt(5)+1; i++)
        {
            
            FireworkEffect.Builder effect = FireworkEffect.builder();
            effect.with(Arrays.asList(FireworkEffect.Type.values()).get(new Random().nextInt(FireworkEffect.Type.values().length-1)));
            Color[] colors = {Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED,  Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW};
            effect.withColor(colors[new Random().nextInt(colors.length-1)]);
            effect.withFade(colors[new Random().nextInt(colors.length-1)]);
            meta.addEffect(effect.build());
        }
        
        return meta;
    }
    
    
}
