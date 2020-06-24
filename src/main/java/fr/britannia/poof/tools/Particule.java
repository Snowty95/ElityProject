package fr.britannia.poof.tools;

import fr.britannia.poof.Poof;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Particule
{
    private EnumParticle particle;
    public Particule(EnumParticle particle)
    {
        this.particle = particle;
    }

    public void sendCircle(Location location, double radius,  double loop, double incHeight, double incLarge)
    {
        final double angle = 360;
        final double height = incHeight / angle;
        final double large = incLarge / angle;
        
        float y = (float) location.getY();
        
        for(double i = 0; i<(angle*loop); i++)
        {
            double valeur = Math.toRadians(i);
            float x = (float) (location.getX() + (radius * Math.sin(valeur)));
            float z = (float) (location.getZ() + (radius * Math.cos(valeur)));
    
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particle, true, x, y, z, 0, 0, 0, 0, 1);
            Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet));
            
            radius += large;
            y += height;
        }
        
    }
    
    public void sendRay(Location location, Vector vector, double lenght, double interval)
    {
        final World world = location.getWorld();
        RayTrace rayTrace = new RayTrace(location.toVector(), vector);
        
        for(Vector v : rayTrace.traverse(lenght, interval))
        {
            Location loc = v.toLocation(world);
            float x = (float) loc.getX();
            float y = (float) loc.getY();
            float z = (float) loc.getZ();
    
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particle, true, x, y, z, 0, 0, 0, 0, 1);
            Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet));
        }
    }
    
    public void sendDragged(Location location)
    {
        float x = (float) location.getX();
        float y = (float) location.getY();
        float z = (float) location.getZ();
    
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particle, true, x, y, z, 0, 0, 0, 0, 1);
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet));
    }
    
    public void sendDragged(Player player, final int time)
    {
        final EnumParticle particle = this.particle;
        new BukkitRunnable()
        {
            private int timer = time*10;
            
            @Override
            public void run()
            {
                if(timer == 0) cancel();
                float x = (float) player.getLocation().getX();
                float y = (float) player.getLocation().getY();
                float z = (float) player.getLocation().getZ();
    
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, x, y, z, 0, 0, 0, 0, 1);
                Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet));
             
                timer--;
            }
        }.runTaskTimerAsynchronously(Poof.getInstance(), 0, 2);
    }
    
    public static void sendAllParticle(Location location)
    {
        new BukkitRunnable()
        {
            int indexCurrent = 0;
            List<EnumParticle> enumPass = new ArrayList<>();
    
            @Override
            public void run()
            {
                int index = 0;
                for(EnumParticle particle : EnumParticle.values())
                {
                    if(!this.enumPass.contains(particle)) new Particule(particle).sendCircle(location, 1, 3, 0, 0);
                    if(indexCurrent <= index)
                    {
                        this.enumPass.add(particle);
                        indexCurrent++;
                        break;
                    }
                    index++;
                }
            }
        }.runTaskTimerAsynchronously(Poof.getInstance(), 0, 40);
    }
    
    public static class RayTrace {
        
        //origin = start position
        //direction = direction in which the raytrace will go
        Vector origin, direction;
        
        RayTrace(Vector origin, Vector direction) {
            this.origin = origin;
            this.direction = direction;
        }
        
        //get a point on the raytrace at X blocks away
        public Vector getPostion(double blocksAway) {
            return origin.clone().add(direction.clone().multiply(blocksAway));
        }
        
        //checks if a position is on contained within the position
        public boolean isOnLine(Vector position) {
            double t = (position.getX() - origin.getX()) / direction.getX();
            ;
            if (position.getBlockY() == origin.getY() + (t * direction.getY()) && position.getBlockZ() == origin.getZ() + (t * direction.getZ())) {
                return true;
            }
            return false;
        }
        
        //get all postions on a raytrace
        public ArrayList<Vector> traverse(double blocksAway, double accuracy) {
            ArrayList<Vector> positions = new ArrayList<>();
            for (double d = 0; d <= blocksAway; d += accuracy) {
                positions.add(getPostion(d));
            }
            return positions;
        }
        
        //intersection detection for current raytrace with return
        public Vector positionOfIntersection(Vector min, Vector max, double blocksAway, double accuracy) {
            ArrayList<Vector> positions = traverse(blocksAway, accuracy);
            for (Vector position : positions) {
                if (intersects(position, min, max)) {
                    return position;
                }
            }
            return null;
        }
        
        //intersection detection for current raytrace
        public boolean intersects(Vector min, Vector max, double blocksAway, double accuracy) {
            ArrayList<Vector> positions = traverse(blocksAway, accuracy);
            for (Vector position : positions) {
                if (intersects(position, min, max)) {
                    return true;
                }
            }
            return false;
        }
        
        //general intersection detection
        public static boolean intersects(Vector position, Vector min, Vector max) {
            if (position.getX() < min.getX() || position.getX() > max.getX()) {
                return false;
            } else if (position.getY() < min.getY() || position.getY() > max.getY()) {
                return false;
            } else if (position.getZ() < min.getZ() || position.getZ() > max.getZ()) {
                return false;
            }
            return true;
        }
        
        //debug / effects
        public void highlight(World world, double blocksAway, double accuracy){
            for(Vector position : traverse(blocksAway,accuracy)){
                world.playEffect(position.toLocation(world), Effect.COLOURED_DUST,0);
            }
        }
        
    }
    
    
}

