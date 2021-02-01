package io.lumine.mythic.lib.api.weight;

import io.lumine.mythic.lib.exception.EmptyWeightListException;
import io.lumine.mythic.lib.exception.InvalidWeightException;
import io.lumine.utils.random.WeightedObject;

import java.util.*;

/**
 * A container class that keeps a list of objects with different weights.
 * {@link #select()} can then be called to get a random object from the container
 * based on the weighted sum of all contained objects.
 * @param <T> The object type of each weighted entry.
 */
public class WeightedContainer<T> implements Iterable<WeightedObject<T>> {
    private final Random rnd = new Random();
    private final List<WeightedObject<T>> options = new ArrayList<>();

    /**
     * Selected a random object based on a weighted sum.
     * @return The selected object
     * @throws EmptyWeightListException if this WeightedContainer is empty.
     */
    public T select() {
        if(options.isEmpty()) throw new EmptyWeightListException();
        options.sort(Comparator.comparingDouble(WeightedObject<T>::getWeight));
        double sum = 0;
        for(WeightedObject<T> option : options)
            sum += option.getWeight();
        double weightedSum = rnd.nextDouble() * sum;
        WeightedObject<T> selected = options.get(0);
        for(WeightedObject<T> o : options) {
            selected = o; weightedSum -= o.getWeight();
            if(weightedSum <= 0) break;
        }
        return selected.getObject();
    }

    /**
     * Selects a random object based on a weighted sum.
     * Keeps doing this until the capacity is out, then
     * it stops.
     * @return A list of selected objects
     * @throws EmptyWeightListException if this WeightedContainer is empty.
     */
    public Collection<T> select(double capacity) {
        if(options.isEmpty()) throw new EmptyWeightListException();
        options.sort(Comparator.comparingDouble(WeightedObject<T>::getWeight));
        double sum = 0;
        for(WeightedObject<T> option : options)
            sum += option.getWeight();
        double weightedSum = rnd.nextDouble() * sum;
        List<T> list = new ArrayList<>();
        for(WeightedObject<T> o : options) {
            list.add(o.getObject());
            weightedSum -= o.getWeight();
            capacity -= o.getWeight();
            if(weightedSum <= 0 || capacity <= 0) break;
        }
        return list;
    }

    /**
     * Add a new element to this WeightedContainer
     * @param weight The weight of the object to add to this container.
     * @param object The object to add to this container.
     * @throws InvalidWeightException throws if weight is lower than or equal to zero
     */
    public void add(double weight, T object) {
        if(weight <= 0) throw new InvalidWeightException();
        options.add(new WeightedObject<>(object, weight));
    }

    /**
     * @return Whether or not this WeightedContainer contains any elements
     */
    public boolean isValid() {
        return !options.isEmpty();
    }

    @Override
    public Iterator<WeightedObject<T>> iterator() {
        return options.iterator();
    }
}