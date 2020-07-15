package com.everon.sessioninfo.functionalinterface;

@FunctionalInterface
public interface TriPredicate {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param a the first input argument
     * @param b the second input argument
     * @param c the third input argument
     * @return {@code true} if the input arguments match the predicate,
     * otherwise {@code false}
     */
    boolean test(int a, int b, int c);

}
