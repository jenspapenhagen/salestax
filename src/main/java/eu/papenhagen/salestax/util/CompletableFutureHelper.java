/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.salestax.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Function;
import javax.inject.Named;

/**
 *
 * @author jens Papenhagen
 */
@Named
public class CompletableFutureHelper {

    /**
     * transforms Future<T> to CompletableFuture<T>
     *
     * @param <T>
     * @param future
     * @param executor
     * @return the CompletableFuture
     */
    public <T> CompletableFuture<T> toCompletable(Future<T> future, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }, executor);
    }

    /**
     * generic wrapper method for Function With Exception
     *
     * @param <T>
     * @param <R>
     * @param <E>
     * @param fe
     * @return
     */
    public <T, R, E extends Exception> Function<T, R> wrapper(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

}
