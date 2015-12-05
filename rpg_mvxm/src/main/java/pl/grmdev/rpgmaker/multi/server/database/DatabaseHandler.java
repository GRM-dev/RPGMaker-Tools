/**
 *
 */
package pl.grmdev.rpgmaker.multi.server.database;

import com.github.fluent.hibernate.factory.HibernateSessionFactory;

import pl.grmdev.rpgmaker.multi.server.rest.*;
import pl.grmdev.rpgmaker.multi.server.rest.Character;
import pl.grmdev.rpgmaker.multi.server.rest.inv.*;

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
                .annotatedClasses(User.class, Token.class, Character.class, Position.class,
 Variables.class,
						Switches.class, Inventory.class, Actor.class, Armor.class, Item.class, Weapon.class)
				.createSessionFactory();

        configured = true;
    }

}
