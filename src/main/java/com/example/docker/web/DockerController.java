package com.example.docker.web;

import com.example.docker.domain.Status;
import com.example.docker.service.DockerService;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/docker")
public class DockerController {

  private final DockerService dockerService;

  public DockerController(DockerService dockerService) {
    this.dockerService = dockerService;
  }

  @GetMapping({"", "/", "images"})
  public List<String> images() {
    return dockerService.images();
  }

  @GetMapping("containers")
  public List<String> containers() {
    return dockerService.containers();
  }

  @GetMapping("containers/{id}")
  public Container container(@PathVariable String id) {
    return dockerService.container(id);
  }

  @GetMapping("/create")
  public InspectContainerResponse create(
      @RequestParam String id,
      @RequestParam(required = false) boolean start,
      @RequestParam(required = false, defaultValue = "") List<String> env,
      @RequestParam(required = false, defaultValue = "") List<String> portBindings) {
    return dockerService.createContainer(id, start, env, portBindings);
  }

  @GetMapping("/restart")
  public Status restart(@RequestParam String id) {
    dockerService.restartContainer(id);
    return new Status("OK");
  }

  @GetMapping("/stop")
  public Status stop(@RequestParam String id) {
    dockerService.stopContainer(id);
    return new Status("OK");
  }

  @GetMapping("/kill")
  public Status kill(@RequestParam String id) {
    dockerService.killContainer(id);
    return new Status("OK");
  }

  @GetMapping("ping")
  public Status dockerPing() {
    boolean ping = dockerService.ping();

    if (ping) {
      return new Status("OK");
    }

    return new Status("NOT_OK");
  }
}
