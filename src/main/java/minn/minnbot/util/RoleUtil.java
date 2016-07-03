package minn.minnbot.util;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.managers.RoleManager;

import java.util.List;

public class RoleUtil {


    /**
     * Returns last role that has the given name or null if no role applies.
     *
     * @param name
     * @param guild
     * @return
     */
    public static Role getRoleByName(String name, Guild guild) {
        final Role[] r = new Role[1];
        guild.getRoles().parallelStream().forEach((Role role) -> {
            if (role.getName().equalsIgnoreCase(name)) {
                r[0] = role;
            }
        });
        return r[0];
    }

    /**
     * Copies given role and applies name (can be null).
     *
     * @param r1   Role to copy.
     * @param name Name to give to the newly created role.
     * @return the copied role.
     * @throws net.dv8tion.jda.exceptions.PermissionException if the bot does not have the permission to manage roles.
     */
    public static Role copyRole(Role r1, String name) {
        List<Permission> perms = r1.getPermissions();

        RoleManager manager = r1.getGuild().createRole();
        manager.setName(((name.isEmpty()) ? r1.getName() + "_copy" : name));
        manager.setColor(r1.getColor());
        manager.setGrouped(r1.isGrouped());

        manager.revoke(Permission.values());
        perms.parallelStream().forEach(manager::give);

        manager.update();
        return manager.getRole();
    }
}
