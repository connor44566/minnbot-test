package minn.minnbot.util;

import minn.minnbot.entities.IgnoreListener;
import minn.minnbot.events.ignore.*;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IgnoreUtil {

    private static final List<User> users = new LinkedList<>();
    private static final List<Guild> guilds = new LinkedList<>();
    private static final List<TextChannel> channels = new LinkedList<>();
    private static final List<IgnoreListener> listeners = new LinkedList<>();

    public static List<IgnoreListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    public static void removeListener(IgnoreListener... listeners) {
        Arrays.stream(listeners).forEachOrdered(IgnoreUtil.listeners::remove);
    }

    public static void addListener(IgnoreListener... listeners) {
        Arrays.stream(listeners).forEachOrdered(IgnoreUtil.listeners::add);
    }

    public static String listAll() {
        return String.format("**__Guilds:__** %s\n**__TextChannels:__** %s\n**__Users:__** %s", listGuilds().isEmpty() ? "none" : listGuilds(), listChannels().isEmpty() ? "none" : listChannels(), listUsers().isEmpty() ? "none" : listUsers());
    }

    public static String listUsers() {
        if (users.isEmpty())
            return "";
        final String[] s = {""};
        users.parallelStream().forEach((u) -> s[0] += "`" + u.getUsername().replace("`", "") + "#" + u.getDiscriminator() + "`, ");
        return s[0];
    }

    public static String listChannels() {
        if (channels.isEmpty())
            return "";
        final String[] s = {""};
        channels.parallelStream().forEach((c) -> s[0] += "`" + c.getName() + "<" + c.getId() + ">`, ");
        return s[0];
    }

    public static String listGuilds() {
        if (guilds.isEmpty())
            return "";
        final String[] s = {""};
        guilds.parallelStream().forEach((g) -> s[0] += "`" + g.getName().replace("`", "") + "<" + g.getId() + ">`, ");
        return s[0];
    }

    public static boolean isIgnored(User user, Guild guild, TextChannel channel) {
        return isIgnored(user) || isIgnored(guild) || isIgnored(channel);
    }

    public static boolean isIgnored(User u) {
        return u != null && users.contains(u);
    }

    public static boolean isIgnored(Guild g) {
        return g != null && guilds.contains(g);
    }

    public static boolean isIgnored(TextChannel c) {
        return c != null && channels.contains(c);
    }

    public static boolean toggleIgnore(User u) {
        if (u == null)
            throw new UnsupportedOperationException("User can not be null!");
        if (users.contains(u)) {
            users.remove(u);
            listeners.forEach(ignoreListener -> ignoreListener.onEvent(new UnignoreUserEvent(u)));
            return false;
        }
        ignore(u);
        return true;
    }

    public static boolean toggleIgnore(Guild g) {
        if (g == null)
            throw new UnsupportedOperationException("Guild can not be null!");
        if (guilds.contains(g)) {
            guilds.remove(g);
            listeners.forEach(ignoreListener -> ignoreListener.onEvent(new UnignoreGuildEvent(g)));
            return false;
        }
        ignore(g);
        return true;
    }

    public static boolean toggleIgnore(TextChannel c) {
        if (c == null)
            throw new UnsupportedOperationException("TextChannel can not be null!");
        if (channels.contains(c)) {
            channels.remove(c);
            listeners.forEach(ignoreListener -> ignoreListener.onEvent(new UnignoreChannelEvent(c)));
            return false;
        }
        ignore(c);
        return true;
    }

    public static void ignore(User u) {
        if (u == null || users.contains(u))
            return;
        users.add(u);
        listeners.forEach(ignoreListener -> ignoreListener.onEvent(new IgnoreUserEvent(u)));
    }

    public static void ignore(Guild g) {
        if (g == null || guilds.contains(g))
            return;
        guilds.add(g);
        listeners.forEach(ignoreListener -> ignoreListener.onEvent(new IgnoreGuildEvent(g)));
    }

    public static void ignore(TextChannel c) {
        if (c == null || channels.contains(c))
            return;
        channels.add(c);
        listeners.forEach(ignoreListener -> ignoreListener.onEvent(new IgnoreChannelEvent(c)));
    }
}
