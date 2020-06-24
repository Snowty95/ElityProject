package fr.britannia.poof.game;

import fr.britannia.poof.Poof;
import fr.britannia.poof.game.task.PlayingGame;
import fr.britannia.poof.game.task.SpawnPoint;
import fr.britannia.poof.game.task.StartGame;
import fr.britannia.poof.game.task.TeleportGame;
import fr.britannia.poof.tools.ItemBuilder;
import fr.britannia.poof.tools.PoofPoint;
import fr.britannia.poof.tools.SkullBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PoofGame
{
    private final Player player;
    public GameState state = GameState.STARTING;
    
    private final StartGame startGame;
    private final TeleportGame teleportGame;
    private final PlayingGame playingGame;
    private final SpawnPoint spawnPoint;
    
    private int point = 0;
    
    private Sheep sheep;
    private Rabbit rabbit;
    private final List<PoofPoint> slimes = new ArrayList<>();
    
    public PoofGame(Player player)
    {
        this.player = player;
        
        this.startGame = new StartGame(this);
        this.teleportGame = new TeleportGame(this);
        this.playingGame = new PlayingGame(this);
        this.spawnPoint = new SpawnPoint(this);
        
        this.startTaskStartGame();
    }
    
    public Player getPlayer() { return this.player; }
    
    
    public GameState getState() { return state; }
    public boolean isState(GameState state) { return this.state == state; }
    public void setState(GameState state) { this.state = state; }
    
    public int getPoint() { return this.point; }
    public void addPoint(int point) { this.point += point; }
    public void removePoint(int point) { this.point -= point; }
    
    
    
    // <------------------- TASK ----------------->
    
    
    public StartGame getStartGame() { return this.startGame; }
    public TeleportGame getTeleportGame() { return this.teleportGame; }
    public PlayingGame getPlayingGame() { return this.playingGame; }
    public SpawnPoint getSpawnPoint() { return this.spawnPoint; }
    
    public void startTaskStartGame()
    {
        this.player.getInventory().clear();
        this.spawnSheep();
        this.startGame.runTaskTimerAsynchronously(Poof.getInstance(), 0, 1);
    }
    
    public void startTaskTeleportGame()
    {
        this.setState(GameState.TELEPORT);
        this.teleportGame.runTaskTimer(Poof.getInstance(), 0, 1);
        this.getPlayer().setVelocity(new Vector(0, 5, 0));
    }
    public void startTaskPlayingGame()
    {
        this.setState(GameState.CHOOSE);
        this.spawnRabbit();
        this.getPlayer().getInventory().setItem(0, this.getLeft());
        this.getPlayer().getInventory().setItem(1, this.getRight());
        this.getPlayer().getInventory().setItem(3, this.getFirework());
        this.getPlayer().getInventory().setItem(8, this.getExit());
        this.spawnPoint.runTaskTimer(Poof.getInstance(), 0, 20*5);
        this.playingGame.runTaskTimerAsynchronously(Poof.getInstance(), 0, 1);
    }
    
    
    
    
    
    
    // <---------------- MOB ---------------->
    
    
    public Sheep getSheep() { return this.sheep; }
    public Rabbit getRabbit() { return this.rabbit; }
    public List<PoofPoint> getSlimes() { return this.slimes; }
    public void addSlime(PoofPoint c) { this.slimes.add(c); }
    public void removeSlime(PoofPoint c) { this.slimes.remove(c); }
    public void clearSlimes() { this.slimes.clear(); }
    
    public void spawnSheep()
    {
        this.sheep = (Sheep) this.getPlayer().getWorld().spawnEntity(this.getPlayer().getLocation().add(0, 30, 0), EntityType.SHEEP);
        this.sheep.setColor(DyeColor.PINK);
        this.sheep.setVelocity(new Vector(0, -3, 0));
    }
    public void spawnRabbit()
    {
        Location loc = new Location(this.getPlayer().getWorld(), this.getPlayer().getLocation().getX(), this.getPlayer().getLocation().getY(), this.getPlayer().getLocation().getZ(), -90f, 0);
        this.rabbit = (Rabbit) this.getPlayer().getWorld().spawnEntity(loc, EntityType.RABBIT);
    }
    
    public Optional<PoofPoint> getSlime()
    {
        return this.slimes.stream().filter(point -> point.getLocation().getBlockX() == this.getRabbit().getLocation().getBlockX() && point.getLocation().getBlockZ() == this.getRabbit().getLocation().getBlockZ()).findFirst();
    }
    
    
    
    
    // <-------------------- ITEM --------------------->
    
    public ItemStack getLeft()
    {
        return new SkullBuilder(SkullBuilder.SKULL_TYPE.ITEM)
                .setTexture("http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5")
                .setName("§eGauche")
                .setEnchant(true);
    }
    
    public ItemStack getRight()
    {
        return new SkullBuilder(SkullBuilder.SKULL_TYPE.ITEM)
                .setTexture("http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311")
                .setName("§eDroite")
                .setEnchant(true);
    }
    
    public ItemStack getExit()
    {
        return new ItemBuilder(Material.BED)
                .setName("§cQuitter")
                .setEnchant(true);
    }
    
    public ItemStack getFirework()
    {
        return new ItemBuilder(Material.FIREWORK)
                .setName("§4Feu")
                .setEnchant(true);
    }
    
    
    
    
    // <---------------- STATE ------------------>
    
    public enum GameState
    {
        
        STARTING,
        TELEPORT,
        CHOOSE,
        PLAYING;
        
    }
    
    public static String prefix() { return "§c§lPoof §8» §e"; }
    
}
