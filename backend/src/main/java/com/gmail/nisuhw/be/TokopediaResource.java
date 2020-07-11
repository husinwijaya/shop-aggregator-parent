package com.gmail.nisuhw.be;

import com.gmail.nisuhw.be.service.TokopediaService;
import com.gmail.nisuhw.model.ProductResult;
import com.gmail.nisuhw.model.ShopResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Path("/tokopedia")
public class TokopediaResource {
    private static final Logger log = LoggerFactory.getLogger(TokopediaResource.class);
    @Inject
    TokopediaService tokopediaService;

    @GET
    @Path("/suggest/{term}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> suggest(@PathParam("term") String term) throws IOException, InterruptedException {
        log.debug("suggest: {}", term);
        return tokopediaService.getSuggestion(term);
    }

    @GET
    @Path("/search/{term}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ShopResult> search(@PathParam("term") String term) throws IOException, InterruptedException {
        log.debug("search: {}", term);
        return tokopediaService.getShopSearchResult(term);
    }

    @GET
    @Path("/search/{store}/{term}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ProductResult> searchPerStore(@PathParam("store") long store, @PathParam("term") String term) throws IOException, InterruptedException {
        log.debug("search: {}", term);
        return tokopediaService.getProductSearchResult(store, term);
    }
}