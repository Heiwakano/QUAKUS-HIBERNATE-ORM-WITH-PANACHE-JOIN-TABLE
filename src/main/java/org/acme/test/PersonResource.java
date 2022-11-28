package org.acme.test;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/api")
public class PersonResource {

    @Inject
    TransactionManager tm;

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

    @GET
    @Path("/persons/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getName(@PathParam("name") String name) {
        return Person.findByName(name);
    }

    @GET
    @Path("/persons")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<PanacheEntityBase> getAll() {
        return Person.findAllPerson();
    }

    @POST
    @Path("/persons")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Person create(Person person) throws SystemException {
        person.persist();
        if (!person.isPersistent()) {
            tm.setRollbackOnly();
        }
        return person;
    }
}