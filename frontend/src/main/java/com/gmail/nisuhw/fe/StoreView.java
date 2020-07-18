package com.gmail.nisuhw.fe;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


public class StoreView extends VerticalLayout {
    private static final Logger log = LoggerFactory.getLogger(StoreView.class);
    private final VerticalLayout products;
    private final long storeId;
    private final BackendService backendService;

    public StoreView(long storeId, String storeName, String storeUrl, Set<String> terms, @Autowired BackendService backendService) {
        this.storeId = storeId;
        this.backendService = backendService;
        Anchor name = new Anchor(storeUrl, storeName);
        products = new VerticalLayout();
        add(name, products);
        getStyle().set("border-style", "solid");
        getStyle().set("border-width", "1px");
        setPadding(false);
        setSpacing(false);
        terms.parallelStream().forEach(term -> {
            HorizontalLayout product = new HorizontalLayout();
            backendService.searchPerStore(storeId, term)
                    .parallelStream()
                    .map(result -> new ProductView(result.getImage(), result.getPrice(), result.getName(), result.getUrl()))
                    .forEach(product::add);
            products.add(new VerticalLayout(new Label(term), product));
        });
    }

}
