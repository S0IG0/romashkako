package com.soigo.romashkako.utils;

import org.springframework.data.domain.Sort;

public class SortUtil {
    public static Sort createSort(String sortBy, Boolean reverse) {
        Sort sort = (sortBy != null) ? Sort.by(sortBy) : Sort.unsorted();
        if (reverse) {
            sort = sort.reverse();
        }
        return sort;
    }
}
