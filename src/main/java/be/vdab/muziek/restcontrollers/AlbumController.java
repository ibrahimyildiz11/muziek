package be.vdab.muziek.restcontrollers;


import be.vdab.muziek.domain.Album;
import be.vdab.muziek.domain.Track;
import be.vdab.muziek.exceptions.AlbumNietGevondenException;
import be.vdab.muziek.services.AlbumService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.TypedEntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("albums")
@ExposesResourceFor(Album.class)
class AlbumController {
    private final AlbumService albumService;
    private final TypedEntityLinks.ExtendedTypedEntityLinks<Album> links;

    AlbumController(AlbumService albumService, EntityLinks links) {
        this.albumService = albumService;
        this.links = links.forType(Album.class, Album::getId);
    }

    @GetMapping("{id}")
    EntityModel<AlbumArtiest> getAlbum(@PathVariable long id) {
        return albumService.findById(id).map(album ->
                EntityModel.of(new AlbumArtiest(album))
                        .add(links.linkToItemResource(album))
                        .add(links.linkForItemResource(album).slash("tracks").withRel("tracks")))
                .orElseThrow(AlbumNietGevondenException::new);
    }

    @GetMapping("{id}/tracks")
    CollectionModel<Track> getTracks(@PathVariable long id) {
        return albumService.findById(id).map(album ->
                CollectionModel.of(album.getTracks())
                        .add(links.linkForItemResource(album).slash("tracks").withRel("self"))
                        .add(links.linkToItemResource(album).withRel("album")))
                .orElseThrow(AlbumNietGevondenException::new);
    }
    @ExceptionHandler(AlbumNietGevondenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void albumNietGevonden() {

    }

    private static class AlbumArtiest {
        private final String album;
        private final String artiest;

        AlbumArtiest(Album album) {
            this.album = album.getNaam();
            this.artiest = album.getArtiest().getNaam();
        }

        public String getAlbum() {
            return album;
        }

        public String getArtiest() {
            return artiest;
        }
    }
}
