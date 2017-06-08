package fr.syst3ms.skuared;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.Operator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Skuared extends JavaPlugin {
    private static Skuared instance;
    private SkriptAddon addon;

    @Override
    public void onEnable() {
        startupChecks();
        getLogger().log(Level.INFO, "Starting up Skuared...");
        setupSyntax();
        getLogger().log(Level.INFO, "Successfully started up Skuared !");
    }

    @Override
    public void onDisable() {
    }

    private void startupChecks() {
        if (instance == null) {
            instance = this;
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot create two instances of Skuared !");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (!Skript.isRunningMinecraft(1, 8)) {
            getLogger().log(Level.SEVERE, "Outdated minecraft version ! Please update to 1.8 or newer !");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void setupSyntax() {
        addon = Skript.registerAddon(this);
        getLogger().log(Level.INFO, "Registering Skuared's syntax !");
        Algorithms.registerOperator(">>", (BinaryOperator<Number>) (a, b) -> a.longValue() >> b.longValue(), Operator.Associativity.LEFT, 4);
        Algorithms.registerOperator("<<", (BinaryOperator<Number>) (a, b) -> a.longValue() << b.longValue(), Operator.Associativity.LEFT, 4);
        Algorithms.registerOperator(">>>", (BinaryOperator<Number>) (a, b) -> a.longValue() >>> b.longValue(), Operator.Associativity.LEFT, 4);
        Algorithms.registerOperator("+", (BinaryOperator<Number>) (a, b) -> a.doubleValue() + b.doubleValue(), Operator.Associativity.LEFT, 3);
        Algorithms.registerOperator("-", (BinaryOperator<Number>) (a, b) -> a.doubleValue() - b.doubleValue(), Operator.Associativity.LEFT, 3);
        Algorithms.registerOperator("*", (BinaryOperator<Number>) (a, b) -> a.doubleValue() * b.doubleValue(), Operator.Associativity.LEFT, 2);
        Algorithms.registerOperator("/", (BinaryOperator<Number>) (a, b) -> a.doubleValue() / b.doubleValue(), Operator.Associativity.LEFT, 2);
        Algorithms.registerOperator("^", (BinaryOperator<Number>) (a, b) -> Math.pow(a.doubleValue(), b.doubleValue()), Operator.Associativity.RIGHT, 1);
        try {
            addon.loadClasses("fr.syst3ms.skuared", "expressions");
            getLogger().log(Level.INFO, "Successfully registered Skuared's syntaxes !");
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Something went wrong while registering syntax, so some syntaxes may not work");
        }
    }

    public static Skuared getInstance() {
        return instance;
    }

    public static Logger logger() {
        return getInstance().getLogger();
    }
}
