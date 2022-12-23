package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.news.AddNewNewsDTO;
import source.restaurant_web_project.services.NewsService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("news")
public class NewsController {
private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping()
    public ResponseEntity<?>getActiveNews(){
      return ResponseEntity.ok(newsService.getActiveNews());
    }

    @GetMapping("details/{id}")
    public ResponseEntity<?>getNewDetails(@PathVariable long id){
        return ResponseEntity.ok(newsService.getNew(id));
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PostMapping("add")
    public ResponseEntity<?>addNew(AddNewNewsDTO addNewNewsDTO){
        return ResponseEntity.created(linkTo(methodOn(this.getClass())
                .getNewDetails(newsService.addNews(addNewNewsDTO)))
                .toUri())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PostMapping("delete/{id}")
    public ResponseEntity<?>deleteNew(@PathVariable long id){
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }
}
