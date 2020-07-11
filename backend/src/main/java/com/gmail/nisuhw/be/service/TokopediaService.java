package com.gmail.nisuhw.be.service;

import com.gmail.nisuhw.model.ProductResult;
import com.gmail.nisuhw.model.ShopResult;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface TokopediaService {
    List<String> getSuggestion(String term) throws IOException, InterruptedException;

    Collection<ShopResult> getShopSearchResult(String query) throws IOException, InterruptedException;

    Collection<ProductResult> getProductSearchResult(long store, String query) throws IOException, InterruptedException;
}
