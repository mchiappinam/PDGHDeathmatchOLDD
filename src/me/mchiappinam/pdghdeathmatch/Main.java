/**
 * Copyright PDGH Minecraft Servers & HostLoad © 2013-XXXX
 * Todos os direitos reservados
 * Uso apenas para a PDGH.com.br e https://HostLoad.com.br
 * Caso você tenha acesso a esse sistema, você é privilegiado!
*/

package me.mchiappinam.pdghdeathmatch;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;

import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class Main extends JavaPlugin {
	public boolean vault = false;
	protected SCCore core;
	protected SimpleClans core2;
	protected int version = 0;
	int ttempoFim;
	int ttempoParaLiberarNaArena;
	public int status=0;//1=entrar(/dm), 2=conhecer arena, 3=andamento, 0=nenhum evento ocorrendo
	List<Player> participantes = new ArrayList<Player>();

	protected static Economy econ = null;
	
	ItemStack[] inventario=null;
	ItemStack[] armadura=null;

	public void onEnable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2ativando... - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2verificando config... - Plugin by: mchiappinam");
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2salvando config pela primeira vez... - Plugin by: mchiappinam");
				saveResource("config_template.yml", false);
				File file2 = new File(getDataFolder(), "config_template.yml");
				file2.renameTo(new File(getDataFolder(), "config.yml"));
				getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2config salva... - Plugin by: mchiappinam");
			} catch (Exception e) {
			}
		}
		getServer().getPluginCommand("cf").setExecutor(new Comando(this));
		if (!setupEconomy()) {
			getLogger().warning("ERRO: Vault nao encontrado!");
			vault = false;
		} else {
			getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2Sucesso: Vault encontrado.");
			vault = true;
		}

		if (hookSimpleClans()) {
			getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2Sucesso: SimpleClans2 encontrado.");
			version = 2;
		}else if (getServer().getPluginManager().getPlugin("SimpleClans") != null) {
			getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2Sucesso: SimpleClans1 encontrado.");
			core2 = ((SimpleClans) getServer().getPluginManager().getPlugin("SimpleClans"));
			version = 1;
		}else {
			version = 0;
			getLogger().warning("ERRO: SimpleClans1 ou SimpleClans2 nao encontrado!");
		}
		
		if (getServer().getPluginManager().getPlugin("Legendchat") == null) {
			getLogger().warning("ERRO: Legendchat nao encontrado!");
		}else{
			getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2Sucesso: Legendchat encontrado.");
			getServer().getPluginManager().registerEvents(new ListenerLegendchat(this), this);
		}
		getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2ativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2Acesse: http://pdgh.com.br/");
		getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2Plugin manipulado por HostLoad e pode ser desativado a qualquer momento para quem nao hospeda na HostLoad!");
	}

	public void onDisable() {
		if((status>=1)&&(status<=4)) {
			getServer().broadcastMessage("§3[PDGHClanFronto] §2Desativando...");
			for(Player p : participantes) {
				World w = getServer().getWorld(getConfig().getString("mundoPrincipal"));
		        if (w != null)
		        	p.teleport(w.getSpawnLocation());
		        else
		        	p.sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
			}
			resetarEvento();
		}
		getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2desativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHClanFronto] §2Acesse: http://pdgh.com.br/");
	}
	
	public void tempoFim() {
		ttempoFim = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			int timer;
    		public void run() {
				if(status!=4) {
					ctempoFim();
					return;
				}sdsadsdasdasda
				if((timer==3s00)||(timer==600)||(timer==900)||(timer==960)||(timer==1020)||(timer==1080)||(timer==1140)) {
					for(Player p : participantes) {
						getServer().getPlayerExact(p.getName()).playSound(getServer().getPlayerExact(p.getName()).getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
						if(timer==1140)
							p.sendMessage("§9[ⒸⒻ] §aFalta §c§l"+(1200-timer)/60+"§a minuto para encerrar automaticamente a partida.");
						else
							p.sendMessage("§9[ⒸⒻ] §aFaltam §c§l"+(1200-timer)/60+"§a minutos para encerrar automaticamente a partida.");
					}
				}else if(timer==1200) {
	                getServer().broadcastMessage("§9[ⒸⒻ] §a"+jogadores+"x"+jogadores+"§f - "+getClanTAGColorida(desafiador)+" §fvs §a"+getClanTAGColorida(desafiado));
					getServer().broadcastMessage("§9[ⒸⒻ] §aClanFronto cancelado por exceder o tempo limite de "+dasasdsad+" minutos! Ninguém venceu!");
					for(Player p : participantes) {
						World w = getServer().getWorld(getConfig().getString("mundoPrincipal"));
				        if (w != null)
				        	p.teleport(w.getSpawnLocation());
				        else
				        	p.sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
					}
					resetarEvento();
				}
				timer++;
    		}
    	}, 0, 20L);
	}
	
	public void prepararArena() {
		List<String> arenas = new ArrayList<String>();
		for(String r : getConfig().getConfigurationSection("arenas").getKeys(false))
			arenas.add(r);
		final Random r=new Random();
        int randomNum = r.nextInt(arenas.size());
        String arenaNome = arenas.get(randomNum);
		String ent1[] = getConfig().getString("arenas."+arenaNome+".1").split(";");
		lado1 = new Location(getServer().getWorld(ent1[0]),Double.parseDouble(ent1[1]),Double.parseDouble(ent1[2]),Double.parseDouble(ent1[3]),Float.parseFloat(ent1[4]),Float.parseFloat(ent1[5]));

		String ent2[] = getConfig().getString("arenas."+arenaNome+".2").split(";");
		lado2 = new Location(getServer().getWorld(ent2[0]),Double.parseDouble(ent2[1]),Double.parseDouble(ent2[2]),Double.parseDouble(ent2[3]),Float.parseFloat(ent2[4]),Float.parseFloat(ent2[5]));
		
		getServer().broadcastMessage("§9[ⒸⒻ] §fArena escolhida: §e"+arenaNome);
	}
	
	public void teleportarForaArena() {
		tteleportarForaArena = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			int timer;
    		public void run() {
				if(status!=5) {
					cteleportarForaArena();
					return;
				}
				if((timer==10)||(timer==20)||(timer==25)) {
					for(Player p : participantesDesafiador)
						p.sendMessage("§9[ⒸⒻ] §fVocê tem "+(30-timer)+" segundos para ser teleportado.");
					for(Player p : participantesDesafiado)
						p.sendMessage("§9[ⒸⒻ] §fVocê tem "+(30-timer)+" segundos para ser teleportado.");
				}else if(timer==30) {
					for(Player p : participantesDesafiador) {
						World w = getServer().getWorld(getConfig().getString("mundoPrincipal"));
				        if (w != null)
				        	p.teleport(w.getSpawnLocation());
				        else
				        	p.sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
					}
					for(Player p : participantesDesafiado) {
						World w = getServer().getWorld(getConfig().getString("mundoPrincipal"));
				        if (w != null)
				        	p.teleport(w.getSpawnLocation());
				        else
				        	p.sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
					}
					getServer().broadcastMessage("§9[ⒸⒻ] §fFim do ClanFronto...");
					resetarEvento();
				}
				timer++;
    		}
    	}, 0, 20L);
	}
	
	public void checkFim() {
        int clan=0; //1=desafiador, 2=desafiado
		if(participantesDesafiador.size()==0)
			clan=1;
		else if(participantesDesafiado.size()==0)
			clan=2;
		
		if(clan!=0) {
			cAllTasks();
			status=5;
			teleportarForaArena();
			if(clan==1) {
                getServer().broadcastMessage("§9[ⒸⒻ] §a"+jogadores+"x"+jogadores+"§f - "+getClanTAGColorida(desafiador)+" §fvs §a"+getClanTAGColorida(desafiado));
                getServer().broadcastMessage("§9[ⒸⒻ] §aClan "+getClanTAGColorida(desafiado)+"§a venceu o evento ClanFronto!");
                getServer().broadcastMessage("§9[ⒸⒻ] §aClan "+getClanTAGColorida(desafiado)+"§a MITOU!");
                getServer().broadcastMessage("§9[ⒸⒻ] §aClan "+getClanTAGColorida(desafiado)+"§a não perde para ninguém!");
                if(participantesDesafiado.size()==1)
                	getServer().broadcastMessage("§9[ⒸⒻ] §aÚnico sobrevivente: "+participantesDesafiado.get(0).getName());
                else {
                	getServer().broadcastMessage("§9[ⒸⒻ] §aSobreviventes:");
                	for(Player p : participantesDesafiado)
                    	getServer().broadcastMessage("§9[ⒸⒻ] §e"+p.getName());
                }
                getConfig().set("vencedor", getClanTAG(desafiado));
                getConfig().options().copyDefaults(true);
                saveConfig();
                getServer().broadcastMessage("§9[ⒸⒻ] §aParabéns clan "+getClanTAGColorida(desafiado));
			}else if(clan==2) {
                getServer().broadcastMessage("§9[ⒸⒻ] §a"+jogadores+"x"+jogadores+"§f - "+getClanTAGColorida(desafiador)+" §fvs §a"+getClanTAGColorida(desafiado));
                getServer().broadcastMessage("§9[ⒸⒻ] §aClan "+getClanTAGColorida(desafiador)+"§a venceu o evento ClanFronto!");
                getServer().broadcastMessage("§9[ⒸⒻ] §aClan "+getClanTAGColorida(desafiador)+"§a MITOU!");
                getServer().broadcastMessage("§9[ⒸⒻ] §aClan "+getClanTAGColorida(desafiador)+"§a não perde para ninguém!");
                if(participantesDesafiador.size()==1)
                	getServer().broadcastMessage("§9[ⒸⒻ] §aÚnico sobrevivente: "+participantesDesafiador.get(0).getName());
                else {
                	getServer().broadcastMessage("§9[ⒸⒻ] §aSobreviventes:");
                	for(Player p : participantesDesafiador)
                    	getServer().broadcastMessage("§9[ⒸⒻ] §e"+p.getName());
                }
                getConfig().set("vencedor", getClanTAG(desafiador));
                saveConfig();
                getServer().broadcastMessage("§9[ⒸⒻ] §aParabéns clan "+getClanTAGColorida(desafiador));
			}
		}
	}
	
	public void resetarEvento() {
		cAllTasks();
		jogadores=0;
        status=0;
        lado1=null;
        lado2=null;
        desafiador=null;
        desafiado=null;
        participantesDesafiador.clear();
        participantesDesafiador.clear();
	}
	
	public void help(Player p) {
		double taxa = getConfig().getDouble("taxa");
		p.sendMessage("§3§lPDGH ClanFronto - Comandos:");
		p.sendMessage("§2/cf desafiar <nick-do-lider-do-clan-adversário> <2-5 jogadores> -§a- Desafia alguém para o CF.");
		p.sendMessage("§2/cf aceitar -§a- Aceita o CF desafiado.");
		p.sendMessage("§2/cf negar -§a- Nega o CF desafiado.");
		p.sendMessage("§2/cf participar -§a- Participa do ClanFronto.");
		if(p.hasPermission("pdgh.admin")) {
			p.sendMessage("§c/cf admin setpos <1-2> <nome-da-arena>");
			p.sendMessage("§c/cf admin cancelar");
		}
		p.sendMessage("§cTaxa do CF de §a§l$"+taxa+"§c.");
		p.sendMessage("§cLimite de 1 CF por vez.");
	}
	
	public boolean isMesmoClan(Player p1, Player p2) {
		if(getClanTAG(p1).contains(getClanTAG(p2)))
			return true;
		return false;
	}

	public void cAllTasks() {
		ctempoResposta();
		ctempoParaLiberarNaArena();
		cteleportarForaArena();
		ctempoFim();
	}

	public void ctempoResposta() {
		getServer().getScheduler().cancelTask(ttempoResposta);
	}

	public void ctempoParaLiberarNaArena() {
		getServer().getScheduler().cancelTask(ttempoParaLiberarNaArena);
	}

	public void cteleportarForaArena() {
		getServer().getScheduler().cancelTask(tteleportarForaArena);
	}

	public void ctempoFim() {
		getServer().getScheduler().cancelTask(ttempoFim);
	}
	
	public String getClanTAG(Player p) {
		if(version!=0)
			if(version==1)
				if(core2.getClanManager().getClanPlayer(p) != null)
					return core2.getClanManager().getClanPlayer(p).getClan().getTag().toLowerCase();
			else if(version==2)
				if(core.getClanPlayerManager().getClanPlayer(p) != null)
					return core.getClanPlayerManager().getClanPlayer(p).getClan().getCleanTag().toLowerCase();
		return null;
	}
	
	public String getClanTAGColorida(Player p) {
		if(version!=0)
			if(version==1)
				if(core2.getClanManager().getClanPlayer(p) != null)
					return core2.getClanManager().getClanPlayer(p).getClan().getColorTag();
			else if(version==2)
				if(core.getClanPlayerManager().getClanPlayer(p) != null)
					return core.getClanPlayerManager().getClanPlayer(p).getClan().getTag();
		return null;
	}
	
	public boolean hasClan(Player p) {
		if(version!=0)
			if(version==1)
				if(core2.getClanManager().getClanPlayer(p) != null)
					return true;
			else if(version==2)
				if(core.getClanPlayerManager().getClanPlayer(p) != null)
					return true;
		return false;
	}
	
	public boolean isLeadder(Player p) {
		if(version!=0)
			if(version==1)
				if(core2.getClanManager().getClanPlayer(p) != null)
					return core2.getClanManager().getClanPlayer(p).isLeader();
			else if(version==2)
				if(core.getClanPlayerManager().getClanPlayer(p) != null)
					return core.getClanPlayerManager().getClanPlayer(p).isLeader();
		return false;
	}
	
	public void soundClan(String tag, Sound s) {
		if(version!=0)
			if(version==1) {
				if(core2.getClanManager().getClan(tag) != null)
					for(ClanPlayer cp : core2.getClanManager().getClan(tag).getMembers())
						if(getServer().getPlayerExact(cp.getName())!=null)
							getServer().getPlayerExact(cp.getName()).playSound(getServer().getPlayerExact(cp.getName()).getLocation(), s, 1.0F, 1.0F);
			}else if(version==2) {
				if(core.getClanManager().getClan(tag) != null)
					for(com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer cp : core.getClanManager().getClan(tag).getAllMembers())
						if(getServer().getPlayerExact(cp.getName())!=null)
							getServer().getPlayerExact(cp.getName()).playSound(getServer().getPlayerExact(cp.getName()).getLocation(), s, 1.0F, 1.0F);
			}
	}
	
	public void notifyClan(String tag, String msg) {
		if(version!=0)
			if(version==1) {
				if(core2.getClanManager().getClan(tag) != null)
					for(ClanPlayer cp : core2.getClanManager().getClan(tag).getMembers())
						if(getServer().getPlayerExact(cp.getName())!=null)
							getServer().getPlayerExact(cp.getName()).sendMessage(msg);
			}else if(version==2) {
				if(core.getClanManager().getClan(tag) != null)
					for(com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer cp : core.getClanManager().getClan(tag).getAllMembers())
						if(getServer().getPlayerExact(cp.getName())!=null)
							getServer().getPlayerExact(cp.getName()).sendMessage(msg);
			}
	}
	
	public int clanOnlinePlayers(String tag) {
		int quantidade=0;
		if(version!=0)
			if(version==1) {
				if(core2.getClanManager().getClan(tag) != null) {
					for(ClanPlayer cp : core2.getClanManager().getClan(tag).getMembers())
						if(getServer().getPlayerExact(cp.getName())!=null)
							quantidade++;
					return quantidade;
				}
			}else if(version==2) {
				if(core.getClanManager().getClan(tag) != null) {
					for(com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer cp : core.getClanManager().getClan(tag).getAllMembers())
						if(getServer().getPlayerExact(cp.getName())!=null)
							quantidade++;
					return quantidade;
				}
			}
		return quantidade;
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean hookSimpleClans() {
		try {
			for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
				if ((plugin instanceof SCCore)) {
					core = ((SCCore) plugin);
					return true;
				}
			}
		} catch (NoClassDefFoundError e) {
			return false;
		}
		return false;
	}

	public ClanPlayerManager getClanPlayerManager() {
		return core.getClanPlayerManager();
	}
}