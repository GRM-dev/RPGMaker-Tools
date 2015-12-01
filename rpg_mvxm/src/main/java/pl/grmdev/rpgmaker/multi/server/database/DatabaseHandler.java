/**
 *
 */
package pl.grmdev.rpgmaker.multi.server.database;

import pl.grmdev.rpgmaker.multi.server.rest.Actor;
import pl.grmdev.rpgmaker.multi.server.rest.Inventory;
import pl.grmdev.rpgmaker.multi.server.rest.MultiplayerData;
import pl.grmdev.rpgmaker.multi.server.rest.Player;
import pl.grmdev.rpgmaker.multi.server.rest.Position;
import pl.grmdev.rpgmaker.multi.server.rest.Switches;
import pl.grmdev.rpgmaker.multi.server.rest.Token;
import pl.grmdev.rpgmaker.multi.server.rest.User;
import pl.grmdev.rpgmaker.multi.server.rest.Variables;

import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 * @author Levvy055
 *
 */
public final class DatabaseHandler {

    private static volatile boolean configured;

    private DatabaseHandler() {

    }

    public static void initConnection() {
        if (configured) {
            return;
        }

        createSessionFactory();
    }

    private static synchronized void createSessionFactory() {
        if (configured) {
            return;
        }

        HibernateSessionFactory.Builder
                .configureFromDefaultHibernateCfgXml()
                .annotatedClasses(User.class, Token.class, Player.class, Position.class,
                        Variables.class, Switches.class, Inventory.class, MultiplayerData.class,
                        Actor.class).createSessionFactory();

        configured = true;
    }

}
