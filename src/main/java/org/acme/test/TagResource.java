package org.acme.test;

import javax.inject.Inject;
import javax.transaction.*;
import javax.transaction.NotSupportedException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Path("/api")
public class TagResource {

    @Inject
    TransactionManager tm;
    @GET()
    @Path("/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getAllTags() {
      return Tag.listAll();
    }

    @GET
    @Path("/tutorials/{tutorialId}/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTagsByTutorialId(@PathParam(value = "tutorialId") Long tutorialId) {
        Optional<Tutorial> tutorial = Tutorial.findByIdOptional(tutorialId);
        if (tutorial.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Not found Tutorial with id = " + tutorialId).build();
        }
        return Response.ok(tutorial.get().tags).build();
    }

    @GET
    @Path("/tags/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagsById(@PathParam(value = "id") Long id) {
        Optional<Tag> tag = Tag.findByIdOptional(id);

        if (tag.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Not found Tag with id = " + id).build();
        }
        return Response.ok(tag.get()).build();
    }

    @GET
    @Path("/tags/{tagId}/tutorials")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTutorialsByTagId(@PathParam(value = "tagId") Long tagId) {
        Optional<Tag> tag = Tag.findByIdOptional(tagId);
        if (tag.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Set<Tutorial> tutorials = tag.get().tutorials;
        return Response.ok(tutorials).build();
    }

    @POST
    @Path("/tutorials/{tutorialId}/tags")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTag(@PathParam(value = "tutorialId") Long tutorialId, Tag tagRequest) {
        Optional<Tutorial> tutorial = Tutorial.findByIdOptional(tutorialId);
        if (tutorial.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Not found Tutorial with id = " + tutorialId).build();
        }
        long tagId = tagRequest.id == null ? 0L : tagRequest.id;
        // tag is existed
        if (tagId != 0L) {
            Optional<Tag> _tag = Tag.findByIdOptional(tagId);
            if (_tag.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Not found Tag with id = " + tagId).build();
            }
            tutorial.get().addTag(_tag.get());
//            tutorial.persist();
           return Response.ok(_tag).build();
        }
        // add and create new Tag
        tutorial.get().addTag(tagRequest);
        tagRequest.persist();
        return Response.ok(tagRequest).build();
    }

    @PUT
    @Path("/tags/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTag(@PathParam("id") long id, Tag tagRequest) {
        Optional<Tag> tag = Tag.findByIdOptional(id);
        if (tag.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).entity("TagId " + id + "not found").build();
        }

        tag.get().name = tagRequest.name;
        return Response.ok(tag).build();
    }

    @DELETE
    @Path("/tutorials/{tutorialId}/tags/{tagId}")
    @Transactional
    @Produces
    public Response deleteTagFromTutorial(@PathParam(value = "tutorialId") Long tutorialId, @PathParam(value = "tagId") Long tagId) {
        Optional<Tutorial> tutorial = Tutorial.findByIdOptional(tutorialId);
        if (tutorial.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Not found Tutorial with id = " + tutorialId).build();
        }

        tutorial.get().removeTag(tagId);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/tags/{id}")
    @Transactional
    @Produces
    public Response deleteTag(@PathParam("id") Long id) {
        Tag.deleteById(id);
        return Response.noContent().build();
    }
}
