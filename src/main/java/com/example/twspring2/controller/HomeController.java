package com.example.twspring2.controller;

import com.example.twspring2.database.albums.model.AlbumEntity;
import com.example.twspring2.database.albums.model.ImageEntity;
import com.example.twspring2.database.users.model.UserEntity;
import com.example.twspring2.database.users.repository.UserRepository;
import com.example.twspring2.security.AuthenticatedUser;
import com.example.twspring2.service.albums.AlbumService;
import com.example.twspring2.service.albums.ImageService;
import com.example.twspring2.service.users.RoleService;
import com.example.twspring2.service.users.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/home")
public class HomeController {
    UserService userService;
    ImageService imageService;
    AlbumService albumService;
    RoleService roleService;
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    public HomeController(UserService userService, ImageService imageService, AlbumService albumService, RoleService roleService) {
        this.userService = userService;
        this.imageService = imageService;
        this.albumService = albumService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String open(Model model, Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            Object principalObject = authenticationToken.getPrincipal();

            if (principalObject instanceof User userDetails) {
                model.addAttribute("username", userDetails.getUsername());
            }
        } else if (principal instanceof OAuth2AuthenticationToken authenticationToken) {
            Object principalObject = authenticationToken.getPrincipal();

            if (principalObject instanceof AuthenticatedUser user) {
                model.addAttribute("username", user.getUsername());
            }
        }
        else {
            model.addAttribute("username", "Guest");
        }

        List<AlbumEntity> albums = albumService.findAll();
        Map<Long, String> albumCovers = new HashMap<>();
        for (AlbumEntity album : albums) {
            if (album.getCoverImage() != null) {
                byte[] imageData = album.getCoverImage().getImageData();
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                albumCovers.put(album.getId(), base64Image);
            }
        }
        model.addAttribute("albums", albums);
        model.addAttribute("albumCovers", albumCovers);

        return "user/home";
    }

    @GetMapping("/albums/create")
    public String getCreateAlbum(Model model) {
        model.addAttribute("album", new AlbumEntity());
        return "user/create-album";
    }

    @Transactional
    @PostMapping("/albums/create")
    public String postCreateAlbum(@ModelAttribute("album") AlbumEntity album,
                                  @RequestParam("image")List<MultipartFile> files,
                                  RedirectAttributes redirectAttributes, Principal principal) throws IOException {
        try {
            albumService.save(album);
            roleService.createAlbumRoles(album.getTitle(), principal.getName());
            for (MultipartFile file : files) {
                imageService.uploadImage(file, album);
            }
        } catch (IOException e) {
            logger.info("Error uploading image");
            return "redirect:/home/albums/create";
        }
        try {
            UserEntity updatedUser = userService.findByUsername(principal.getName());
            List<GrantedAuthority> authorities = new ArrayList<>(updatedUser.getAuthorities());
            for (GrantedAuthority authority : updatedUser.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getAuthority()));
            }
            UserDetails userDetails = new User(updatedUser.getUsername(), updatedUser.getPassword(), authorities);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, updatedUser.getPassword(), authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/home";
        } catch (Exception e) {
            logger.info("Error updating user");
            return "redirect:/home/albums/create";
        }
        //return "redirect:/home";
    }

    @GetMapping("/album/{title}")
    public String getAlbum(@PathVariable("title") String title, @PathVariable("id") String id, Model model) {
        AlbumEntity album = albumService.findById(Long.valueOf(id));
        model.addAttribute("albumTitle", title);
        List<ImageEntity> images = imageService.getAlbumImages(album.getId());
        Map<Long, String> imagesData = new HashMap<>();
        for (ImageEntity image : images) {
            if (image != null) {
                byte[] imageData = image.getImageData();
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                imagesData.put(image.getId(), base64Image);
            }
        }
        model.addAttribute("imagesData", imagesData);
        model.addAttribute("imageEntities", images);
        return "user/album";
    }

    @PostMapping("/album/{title}/delete")
    public String deleteAlbum(@PathVariable("title") String title, @PathVariable("id") String id) {
        AlbumEntity album = albumService.findById(Long.valueOf(id));
        albumService.delete(album);
        return "redirect:/home";
    }

    @PostMapping("/album/{title}/delete/{imageId}")
    public String deleteImage(@PathVariable("title") String title, @PathVariable("imageId") Long imageId) {
        ImageEntity image = imageService.findById(imageId);
        imageService.delete(image);
        return "redirect:/home/album/" + title;
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        if (query == null || query.isEmpty()) {
            List<AlbumEntity> albums = albumService.findAll();
            Map<Long, String> albumCovers = new HashMap<>();
            for (AlbumEntity album : albums) {
                if (album.getCoverImage() != null) {
                    byte[] imageData = album.getCoverImage().getImageData();
                    String base64Image = Base64.getEncoder().encodeToString(imageData);
                    albumCovers.put(album.getId(), base64Image);
                }
            }
            model.addAttribute("albums", albums);
            model.addAttribute("albumCovers", albumCovers);
            return "user/home";
        }
        List<AlbumEntity> albums = albumService.search(query);
        if (albums.isEmpty()) {
            model.addAttribute("albums", new ArrayList<>());
            model.addAttribute("message", "No results found");
        } else {
            Map<Long, String> albumCovers = new HashMap<>();
            for (AlbumEntity album : albums) {
                if (album.getCoverImage() != null) {
                    byte[] imageData = album.getCoverImage().getImageData();
                    String base64Image = Base64.getEncoder().encodeToString(imageData);
                    albumCovers.put(album.getId(), base64Image);
                }
            }
            model.addAttribute("albums", albums);
            model.addAttribute("albumCovers", albumCovers);
            logger.info("Albums found: " + albums.size());
            logger.info("Covers: " + albumCovers.size());
        }
        return "user/home";
    }
}
