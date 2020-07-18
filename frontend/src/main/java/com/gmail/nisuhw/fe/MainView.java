package com.gmail.nisuhw.fe;

import com.gmail.nisuhw.model.ShopResult;
import com.google.common.collect.Sets;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.vaadin.flow.component.notification.Notification.Position.MIDDLE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/multiselect-combo-box-input-styles.css", themeFor = "multiselect-combo-box-input")
public class MainView extends VerticalLayout {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Logger log = LoggerFactory.getLogger(MainView.class);

    public MainView(@Autowired BackendService backendService) {
        MultiselectComboBox<String> search = new MultiselectComboBox<>();
        search.setLabel("Search something");
        var dataProvider = SuggestionProvider.create(backendService);
        search.setDataProvider(dataProvider);
        search.setAllowCustomValues(true);
        search.addCustomValuesSetListener(event -> {
            dataProvider.cancelCurrent();
            LinkedHashSet<String> newValue = Sets.newLinkedHashSet(search.getValue());
            newValue.add(event.getDetail());
            search.setValue(newValue);
        });
        search.setWidthFull();

        Button submitBtn = new Button("Submit");
        submitBtn.getStyle().set("align-self", "center");

        submitBtn.addClickListener(event -> {
            UI ui = event.getSource().getUI().orElse(UI.getCurrent());
            Set<String> terms = search.getValue();
            clearSearch();
            log.info("prepare search {}", terms);
            Iterator<Set<ShopResult>> storeIterator = terms.parallelStream().map(backendService::search).collect(toList()).iterator();
            log.info("got result from backend");
            Set<ShopResult> stores = null;
            while (storeIterator.hasNext()) {
                if (stores == null) stores = storeIterator.next();
                else {
                    stores = Sets.intersection(stores, storeIterator.next());
                }
            }
            if (CollectionUtils.isEmpty(stores)) {
                log.warn("not found {}", terms);
                Notification.show("No result found", 3000, MIDDLE);
                return;
            }
            log.info("rendering the result");
            stores.parallelStream().limit(10)
                    .map(store -> new StoreView(store.getId(), store.getName(), store.getUrl(), terms, backendService))
                    .collect(toUnmodifiableList())
                    .forEach(this::add);
        });

        addClassName("centered-content");
        add(search, submitBtn);
    }

    public void clearSearch() {
        getChildren().filter(StoreView.class::isInstance).forEach(this::remove);
    }

}
