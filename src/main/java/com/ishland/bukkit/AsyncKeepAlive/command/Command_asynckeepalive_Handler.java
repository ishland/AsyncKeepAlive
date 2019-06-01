package com.ishland.bukkit.AsyncKeepAlive.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.ishland.bukkit.AsyncKeepAlive.main.Launcher;

/**
 * @author ishland
 *
 */
public class Command_asynckeepalive_Handler implements CommandExecutor {
    private Plugin plugin;

    public Command_asynckeepalive_Handler(Plugin plugin) {
	setPlugin(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (args.length > 0) {
	    if (args[0].equals("help") || args[0].equals("h"))
		sender.sendMessage(getUsage());
	    else if (args[0].equals("statistics") || args[0].equals("s")) {
		sender.sendMessage(getStatus(sender));
	    } else {
		sender.sendMessage(getUsage());
	    }
	    return true;
	}
	sender.sendMessage(getUsage());
	return true;
    }

    protected String[] getUsage() {
	String usage[] = { "Usage of this command:", "/asynckeepalive help(h): You are currently looking at this!",
		"/asynckeepalive statistics(s): Get the status of this plugin" };
	return usage;
    }

    protected String[] getStatus(CommandSender sender) {
	try {
	    String returnval[] = { "Status: ",
		    "Received packets / Sent packets: "
			    + String.valueOf(((Launcher) getPlugin()).getPacketListener().getObject().getCount())
			    + " / " + String.valueOf(((Launcher) getPlugin()).getPacketThread().getObject().getCount()),
		    "Packet loss: "
			    + (String.valueOf(100L - ((Launcher) getPlugin()).getPacketListener().getObject().getCount()
				    / ((Launcher) getPlugin()).getPacketListener().getObject().getCount() * 100))
			    + "%",
		    "GC tasks: " + String.valueOf(
			    ((Launcher) getPlugin()).getPacketThread().getObject().getGarbargeCleanList().size()),
		    "Packets in database: "
			    + String.valueOf(((Launcher) getPlugin()).getPacketThread().getObject().getPing().size()) };
	    return returnval;
	} catch (ArithmeticException e) {
	    String returnval[] = {
		    "There is currently no statistics. Let some players join the server and see again." };
	    return returnval;
	}
    }

    /**
     * @return the plugin
     */
    public Plugin getPlugin() {
	return plugin;
    }

    /**
     * @param plugin the plugin to set
     */
    public void setPlugin(Plugin plugin) {
	this.plugin = plugin;
    }

}
