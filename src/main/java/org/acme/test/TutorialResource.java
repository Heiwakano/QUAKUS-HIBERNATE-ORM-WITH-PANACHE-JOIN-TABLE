package org.acme.test;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/api")
public class TutorialResource {

    @GET
    @Path("/tutorials")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTutorials(@QueryParam("title") String title) {
        if (title == null) {
            return Response.ok(Tutorial.listAll()).build();
        } else {
            return Response.ok(Tutorial.list("title", title)).build();
        }
    }

    @GET
    @Path("/tutorials/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTutorialById(@PathParam("id") Long id) {
        Optional<Tutorial> tutorial = Tutorial.findByIdOptional(id);
        if (tutorial.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tutorial.get()).build();
    }

    @POST
    @Path("/tutorials")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createTutorial(Tutorial tutorial) {
        tutorial.persist();
        return Response.ok("created").build();
    }

    @PUT
    @Path("/tutorials/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateTutorial(@PathParam("id") Long id, Tutorial tutorial) {
        Tutorial _tutorial = (Tutorial) Tutorial.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Not found Tutorial with id = " + id));

        _tutorial.title = tutorial.title;
        _tutorial.description = tutorial.description;
        _tutorial.published = tutorial.published;

        return Response.ok(_tutorial).build();
    }

    @DELETE
    @Path("/tutorials/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response deleteTutorial(@PathParam("id") Long id) {
        return Response.ok(Tutorial.deleteById(id)).build();
    }

    @DELETE
    @Path("/tutorials")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAllTutorials() {
        return Response.ok(Tutorial.deleteAll()).build();
    }

    @GET
    @Path("/tutorials/published")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByPublished() {
        List<Tutorial> tutorials = Tutorial.list("published", true);

        if (tutorials.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(tutorials).build();
    }
}
