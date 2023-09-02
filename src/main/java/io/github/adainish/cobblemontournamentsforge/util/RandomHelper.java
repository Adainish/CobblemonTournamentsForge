package io.github.adainish.cobblemontournamentsforge.util;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomHelper
{
    public static <T> @Nullable T getRandomElementFromCollection(Collection<T> collection) {
        Random rand = new Random();
        if (collection.isEmpty()) {
            return null;
        } else {
            int index = rand.nextInt(collection.size());
            Iterator<T> collectionIterator = collection.iterator();

            IntStream.range(0, index).forEachOrdered(i -> collectionIterator.next());

            return collectionIterator.next();
        }
    }
}
