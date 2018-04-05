package com.akistar.hslslider;

/**
 * Created by NERV on 22.12.2017.
 */

/**
 * Интерфейс для копируемых объектов
 * @param <Type> - тип копируемого объекта
 */
public interface Copyable<Type> {
    Type copy();
}
