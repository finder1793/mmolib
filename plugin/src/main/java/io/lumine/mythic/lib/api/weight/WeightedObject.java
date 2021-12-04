package io.lumine.mythic.lib.api.weight;

public class WeightedObject<T> {
    private final T object;
    private final double weight;

    protected WeightedObject(T object, double w) {
        this.object = object;
        this.weight = w;
    }

    public T getObject() {
        return object;
    }

    public double getWeight() {
        return weight;
    }
}
