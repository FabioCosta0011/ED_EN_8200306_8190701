package org.example.Game.Entities.Interfaces;

import org.example.Game.Entities.ToCruz;

/**
 * Interface representing the contract for Enemy entities.
 */
public interface IEnemy extends ICharacter {

    /**
     * Attacks the ToCruz entity.
     *
     * @param toCruz the ToCruz entity to attack.
     */
    void attackToCruz(ToCruz toCruz);

}
