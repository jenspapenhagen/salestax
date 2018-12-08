/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.salestax.util;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utils
 *
 * @author Jens Papenhagen
 */
public class Utils {

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElseGet(Stream::empty);
    }

    @SafeVarargs
    public static <T> Optional<T> firstPresent(Supplier<Optional<T>>... optionals) {
        return Arrays.stream(optionals)
                .map(Supplier::get)
                .flatMap(Utils::stream)
                .findFirst();
    }

    public static String toBase64(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }
}
