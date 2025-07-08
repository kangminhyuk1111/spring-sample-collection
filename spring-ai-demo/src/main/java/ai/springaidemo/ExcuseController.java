package ai.springaidemo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/excuse")
public class ExcuseController {

  private final ExcuseService excuseService;

  public ExcuseController(final ExcuseService excuseService) {
    this.excuseService = excuseService;
  }

  @PostMapping
  public ExcuseResponse generateExcuse(@RequestBody final ExcuseRequest excuseRequest) {
    return excuseService.generate(excuseRequest);
  }
}
