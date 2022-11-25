package org.acme.test;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;

@Path("/api")
public class ProductResource {

    @GET
    @Path("/persons/{personId}/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProductsByPersonId(@PathParam("personId") Integer personId) {
        if (!Person.findByIdOptional(personId).isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Set<PanacheEntityBase> products = Product.findByPersonId(personId);
        return Response.ok(products).build();
    }

    @POST
    @Path("/persons/{personId}/products")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createProduct(@PathParam("personId") Integer personId, Product productRequest) {
        Optional<Person> person = Person.findByIdOptional(personId);
        if (person.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productRequest.person = person.get();
        productRequest.persistAndFlush();
        return Response.ok(productRequest).build();
    }
}
